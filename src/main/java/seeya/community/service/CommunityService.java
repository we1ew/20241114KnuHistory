package seeya.community.service;

import seeya.common.util.Common;
import seeya.common.util.FileUtils;
import seeya.community.service.dao.CommunityDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @className : CommunityService
 * @description : 게시판 - 게시판 - 공유게시판
 *
 * @author 정보보호 이승환
 * @since 2017.09.30
 * @version 1.0
 * @see
 *
 * - Copyright (C) by seeya All right reserved.
 */
@Service("communityService")
public class CommunityService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource
    private CommunityDAO communityDAO;

    @Resource(name = "fileUtils")
    private FileUtils fileUtils;

    /**
     *
     * 게시판 목록
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getListBoard(Map<String, Object> map) throws Exception {
        
        return communityDAO.getListBoard(map);
    }

    /**
     *
     * 게시판 개수
     * @param map
     * @return
     * @throws Exception
     */
    public String getListBoardCnt(Map<String, Object> map) throws Exception {
        String cnt = communityDAO.getListBoardCnt(map);
        return cnt;
    }


    /**
     *
     * 새글 등록
     * @param map
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public Map<String, Object> regBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", "SUCCESS");
        String title = (String) map.get("title");        // 제목
        String tx_content = (String) map.get("tx_content");        // 내용

        try {

            if (Common.isEmpty(title) || Common.isEmpty(tx_content)) {
                result.put("errCode", "E410");

            } else {
                String regDate = Common.getDate();
                String regTime = Common.getTime();

                map.put("regDate", regDate);
                map.put("regTime", regTime);

                /////////////////////////////////////////////////////////////////
                // attach 가 0보다 큰 경우 첨부 파일이 있다. isFile는 Y, 아니며 N
                String attach = (String) map.get("attach");
                String isFile = "N";
                String isImage = "N";

                if (!Common.isEmpty(attach)) {
                    if (Integer.parseInt(attach) > 0) {
                        isFile = "Y";
                    }
                }

                communityDAO.regBoard(map);
                result.put("SEQ", map.get("SEQ"));

                if (isFile.equals("Y")) {
                    isFile = "N";
                    isImage = "N";
                    List<Map<String, Object>> list = fileUtils.saveFileUpload(map, fileUtils.boardPath);

                    for (int i = 0, size = list.size(); i < size; i++) {

                        communityDAO.insertFile(list.get(i));

                        if (("I").equals(list.get(i).get("FileType"))) {
                            isImage = "Y";
                        } else {
                            isFile = "Y";
                        }
                    } // for
                } // if isFile

                map.put("isFile", isFile);
                map.put("isImage", isImage);
                communityDAO.updateFileType(map);

            } // if isSave
        } catch (Exception e) {
            log.error("ERROR");
        } finally {
            return result;
        }
    }

    /**
     *
     * 게시판 글 읽기
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> getBoardDetail(Map<String, Object> map) throws Exception {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();  // 개체 생성

        /////////////////////
        // 본문
        Map<String, Object> boardDetail = communityDAO.getBoardDetail(map);         // 상세정보를 가졍와서

        // 본문 html 태그 동작을 위해서 <, > 로 변경
        String TEXT = (String) boardDetail.get("BOARD_DESC");
//        TEXT = TEXT.replace("&lt;", "<");
//        TEXT = TEXT.replace("&gt;", ">");
//        TEXT = TEXT.replace("&amp;", "&");
        TEXT = Common.removeXSS(TEXT , true);     // 보안 필터 2017.10.30 이병덕

        String TITLE = (String) boardDetail.get("TITLE");
        TITLE = Common.removeXSS(TITLE , true);     // 보안 필터 2017.10.30 이병덕

        boardDetail.put("TEXT", TEXT);
        boardDetail.put("TITLE", TITLE);

        if (!Common.isEmpty(boardDetail)) {
            communityDAO.updateReadCount(map);                                          // 조회수 증가

            resultMap.put("boardDetail", boardDetail);                                  // 생성된 개체에 넣고  - SampleController에서 사용될 키가 map이다

            /////////////////////
            // 첨부파일
            List<Map<String, Object>> attachList = communityDAO.getFileList(map);       // 첨부파일 목록을 가져와서
            resultMap.put("attachList", attachList);                                    // 첨부파일 개체
            resultMap.put("attachCount", attachList.size());                            // 첨부파일 갯수

            // 수정모드 /////////// 이고 첨부파일이 있을 때 첨부파일을 임시폴더로 복사
            if (map.get("boardMode").equals("E") && !Common.isEmpty(attachList)) {
                map.put("strm", "Board");
                fileUtils.copyToTempFolder(attachList, map);
            }
        }

        /////////////////////
        // 댓글
        //map.put("MIDX", map.get("IDX"));
        List<Map<String, Object>> commentView = getComment(map);
        resultMap.put("commentView", commentView);


        return resultMap;
    }

    /**
     *
     * 게시물 작성자 아이디
     * @param map
     * @return
     * @throws Exception
     */
    public String getRegID(Map<String, Object> map) throws Exception {
        
        return communityDAO.getRegID(map);
    }

    /**
     *
     * 게시물 수정
     * @param map
     * @param request
     * @return
     * @throws Exception
     */
    public String editBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
        String result = "SUCCESS";

        try {
            boolean isSave = true;

            String title = (String) map.get("title");        // 제목
            String tx_content = (String) map.get("tx_content");   // 내용
            String SEQ = (String) map.get("SEQ");          // 수정할 게시물번호
            String REG_ID = getRegID(map);           // 글 작성자
            String editDate = Common.getDate();
            String editTime = Common.getTime();
            String attach = (String) map.get("attach");
            String isFile = "N";
            String isImage = "N";

            boolean isModify = (Boolean) map.get("isModify");

            ////////////////////
            // 첨부 파일 있는지 확인
            // attach 가 0보다 큰 경우 첨부 파일이 있다. isFile는 Y, 아니며 N
            if (!Common.isEmpty(attach)) {
                if (Integer.parseInt(attach) > 0) {
                    isFile = "Y";
                }
            }

            map.put("isFile", isFile);

            ////////////////////
            // 오류 및 권한 체크
            // 제목, 내용, IDX 체크(null 글 등록 불가)
            if (Common.isEmpty(title) || Common.isEmpty(tx_content) || Common.isEmpty(SEQ)) {
                result = "E410";    // 잘못된 정보가 있습니다.
                isSave = false;
            } else {
                // 수정권한이 없을 때 작성자 체크 (작성자 아이디가 있으면 수정 가능)
                if (!isModify && Common.isEmpty(REG_ID)) {
                    result = "E400"; // 잘못된 경로로 오셨습니다.
                    isSave = false;
                }
            }

            if (isSave) {
                // 첨부파일 확인 (기존 첨부파일 존재한 상태에서 수정 시 첨부파일 삭제 시 확인용도)
                Map<String, Object> boardDetail = communityDAO.getBoardDetail(map);

                map.put("editDate", editDate);
                map.put("editTime", editTime);

                ////////////////////
                // 글 내용 저장
                communityDAO.editBoard(map);   // T_BOARD_MASTER 수정

                ////////////////////
                // 첨부파일 저장
                if (("Y").equals(isFile) || ("Y").equals(boardDetail.get("IS_FILE")) || ("Y").equals(boardDetail.get("IS_IMAGE"))) {
                    isFile = "N";
                    isImage = "N";
                    communityDAO.deleteFile(map);                   //    2. 파일테이블 삭제 (MIDX = idx}

                    List<Map<String, Object>> list = fileUtils.saveFileUpload(map, fileUtils.boardPath);

                    // Mybatis에서 제공하는 Batch기능을 이용하여 한번에 처장하게 하기. 현재는 임시방편으로 하나씩 추가..
                    for (int i = 0, size = list.size(); i < size; i++) {
                        communityDAO.insertFile(list.get(i));

                        // 첨부파일의 FileType 체크
                        if (("I").equals(list.get(i).get("FileType"))) {
                            isImage = "Y";
                        } else {
                            isFile = "Y";
                        }
                    }
                }

                ////////////////////
                // 게시글의 첨부파일 타입 업데이트
                map.put("isFile", isFile);
                map.put("isImage", isImage);
                communityDAO.updateFileType(map);

            }// if isSave
            //communityDAO.editBoard(map);
        } catch (Exception e) {
            result = "E200";
        }
        return result;
    }

    /**
     *
     * 게시물 삭제
     * @param map
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public String deleteBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
        String result = "SUCCESS";

        try {

            boolean isSave = true;
            boolean isDelete = (Boolean) map.get("isDelete");

            String REG_ID = getRegID(map);      // 글 작성자
            String SEQ = (String) map.get("SEQ");     // 게시물 번호


            ////////////////////
            // 오류 및 권한 체크
            if (Common.isEmpty(SEQ)) {
                result = "E410";
                isSave = false;
            } else {
                // 삭제권한이 없을 때 작성자 체크 (작성자 아이디가 있으면 삭제 가능)
                if (!isDelete && Common.isEmpty(REG_ID)) {
                    result = "E400";
                    isSave = false;
                }
            }

            if (isSave) {
                String liveDate = Common.getDate();
                String liveTime = Common.getTime();

                map.put("liveDate", liveDate);
                map.put("liveTime", liveTime);

                ////////////////////
                // 글 삭제(LIVE_ 업데이트)
                communityDAO.deleteBoard(map);
            }
        } catch (Exception e) {
            result = "E200";
        } finally {
            return result;
        }
    }

    /**
     *
     * 댓글 등록
     * @param map
     * @return
     */
    @SuppressWarnings("finally")
    public Object regComment(Map<String, Object> map) {
        String result = "SUCCESS";
        String tx_content = (String) map.get("text");        // 내용
        String QSTATE = (String) map.get("QSTATE");        // 진행상태

        if (tx_content.equals("<p><br></p>")) {
            tx_content = "";
        }

        try {
            if (Common.isEmpty(tx_content) && Common.isEmpty(QSTATE)) {
                result = "E410";
            } else {
                // 내용은 없으나 상태가 있으면 마스터에 상태만 업데이트
                if (Common.isEmpty(tx_content) && !Common.isEmpty(QSTATE)) {
                    communityDAO.updateMasterState(map);
                }else {
                    String regDate = Common.getDate();
                    String regTime = Common.getTime();

                    map.put("regDate", regDate);
                    map.put("regTime", regTime);

                    ////////////////////
                    // 댓글 등록
                    communityDAO.regComment(map);

                    ////////////////////
                    // 댓글 그룹 업데이트
                    if (!Common.isEmpty(map.get("groupNum")) && ("Y").equals(map.get("isReply"))) {
                        communityDAO.updateCommentGroup(map);
                    } else {
                        map.put("groupNum", map.get("SEQ"));
                        communityDAO.updateCommentGroup(map);
                    }

                    // 상태값이 있으면 마스터 업데이트
                    if (!Common.isEmpty(QSTATE)) {
                        communityDAO.updateMasterState(map);
                    }
                }
            } // if isSave
        } catch (Exception e) {
            result = "E200";
        } finally {
            return result;
        }
    }

    /**
     *
     * 댓글 읽기
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getComment(Map<String, Object> map) throws Exception {
        
        List<Map<String, Object>> resultMap = communityDAO.getComment(map);         // 댓글 상세정보
        return resultMap;
    }

    /**
     *
     * 댓글 수정
     * @param map
     * @return
     */
    @SuppressWarnings("finally")
    public Object editComment(Map<String, Object> map) {
        String result = "SUCCESS";

        try {
            boolean isSave = true;
            boolean isModify = (Boolean) map.get("isModify");

            String REG_ID = getCommentRegID(map);      // 글 작성자
            String tx_content = (String) map.get("text");        // 내용
            String QSTATE = (String) map.get("QSTATE");        // 진행상태

            if (tx_content.equals("<p><br></p>")) {
                tx_content = "";
            }

            ////////////////////
            // 오류 및 권한 체크
            if (Common.isEmpty(tx_content) && Common.isEmpty(QSTATE)) {
                result = "E410";
                isSave = false;
            } else {
                // 수정권한이 없을 때 작성자 체크 (작성자 아이디가 있으면 수정 가능)
                if (!isModify && Common.isEmpty(REG_ID)) {
                    result = "E400";
                    isSave = false;
                }
            }

            if (isSave) {
                // 내용은 없으나 상태가 있으면 마스터에 상태만 업데이트
                if (Common.isEmpty(tx_content) && !Common.isEmpty(QSTATE)) {
                    communityDAO.updateMasterState(map);
                }else {
                    String regDate = Common.getDate();
                    String regTime = Common.getTime();

                    map.put("editDate", regDate);
                    map.put("editTime", regTime);

                    communityDAO.editComment(map);

                    // 상태값이 있으면 마스터 업데이트
                    if (!Common.isEmpty(QSTATE)) {
                        communityDAO.updateMasterState(map);
                    }
                }
            } // if isSave
        } catch (Exception e) {
            result = "E200";
        } finally {
            return result;
        }
    }

    /**
     *
     * 댓글 삭제
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public String deleteComment(Map<String, Object> map) throws Exception {
        String result = "SUCCESS";

        try {

            boolean isSave = true;
            boolean isDelete = (Boolean) map.get("isDelete");

            String REG_ID = getCommentRegID(map);      // 글 작성자
            String SEQ = (String) map.get("SEQ");     // 댓글 번호

            ////////////////////
            // 오류 및 권한 체크
            if (Common.isEmpty(SEQ)) {
                result = "E410";
                isSave = false;
            } else {
                // 삭제 권한이 없을 때 작성자 체크 (작성자 아이디가 있으면 삭제 가능)
                if (!isDelete && Common.isEmpty(REG_ID)) {
                    result = "E400";
                    isSave = false;
                }
            }

            if (isSave) {
                String liveDate = Common.getDate();
                String liveTime = Common.getTime();

                map.put("liveDate", liveDate);
                map.put("liveTime", liveTime);


                // 댓글의 답글 체크 관련(댓글 그룹 체크)
                map.put("getType", "SEQ");
                List<Map<String, Object>> commentView = communityDAO.getComment(map);   // IDX로 해당 댓글 조회
                String groupNum = String.valueOf(commentView.get(0).get("GROUP_NUM"));  // IDX에 해당하는 댓글그룹 저장

                map.put("groupNum", groupNum);
                map.put("getType", "groupNum");
                List<Map<String, Object>> groupList = communityDAO.getComment(map);     // 댓글그룹 리스트 조회

                // 댓글의 답글이 있는지 체크 (댓글의 답글 삭제 불가)
                if (groupList.size() == 1 && ("N").equals(String.valueOf(commentView.get(0).get("REPLY_YN")))) {
                    communityDAO.deleteComment(map);
                } else if (("Y").equals(String.valueOf(commentView.get(0).get("REPLY_YN")))) {
                    communityDAO.deleteComment(map);
                } else if (groupList.size() > 1 && ("N").equals(String.valueOf(commentView.get(0).get("REPLY_YN")))) {
                    result = "NOT";
                } else {
                    result = "ERROR";
                }
            }
        } catch (Exception e) {
            result = "ERROR";
        } finally {
            return result;
        }
    }

    /**
     *
     * 댓글 작성자 아이디 조회
     * @param map
     * @return
     * @throws Exception
     */
    public String getCommentRegID(Map<String, Object> map) throws Exception {
        
        return communityDAO.getCommentRegID(map);
    }
}


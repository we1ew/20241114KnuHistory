package seeya.community.web;

import seeya.common.common.CommandMap;
import seeya.common.logger.LoggerAspect;
import seeya.common.service.CommonService;
import seeya.common.util.Common;
import seeya.community.service.CommunityService;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 *
 * @className : CommunityController
 * @description : TODO 게시판 - 게시판 - 공유게시판
 *
 * @author 정보보호 이승환
 * @since 2017.09.30
 * @version 1.0
 * @see
 *
 * - Copyright (C) by seeya All right reserved.
 */
@Controller
public class CommunityController {
    protected Log log = LogFactory.getLog(LoggerAspect.class);

    @Resource(name="commonService")
    private CommonService commonService;

    @Resource(name="communityService")
    private CommunityService communityService;

    /**
     * 게시판
     *      boardMode : 목록(L), 상세페이지(V), 쓰기(W), 수정(E)
     * @param commandMap
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Community/Board.do")
    public ModelAndView openBoard(CommandMap commandMap, HttpServletRequest request, HttpSession session) throws Exception{

        ModelAndView mv = null;

        String error = (String) commonService.checkPagePerm(request); // 로그인 여부, 권한 체크
        String boardCode = request.getParameter("boardCode");   // T_MENU에 저장된 기본 주소를 체크하기 위한 값.


        if (Common.isEmpty(boardCode)) {
            error = "E100";
        }

        if (!error.equals("SUCCESS")) {
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("perm"));

            return mv;
        }

        // 사이드메뉴 가져오기
        List<Map<String, Object>> smList = commonService.getSideMenu(request);

        commandMap.getMap().get("boardCode");

        // 페이지 모드 : 기본은 리스트 페이지
        String boardMode = request.getParameter("boardMode");
        boolean isContinue = true;      // 페이지 모드에 권한이 없으면 false로 변경. false이면 오류 페이지로 이동
        boolean isError    = false;     // isContinue ==true 진행 중 오류가 발생하면.

        if (Common.isEmpty(boardMode)) {
            boardMode = "L";
        }

        // 페이지모드에 따른 권한 체크 s ///////////////////////////////////////////////////////////////////
        // 글쓰기이면 쓰기 권한 체크
        if (boardMode.equals("W")) {
            isContinue = Common.isWrite;
//            log.debug("============================ 권한체크(글 쓰기 페이지) s =======================================");
//            log.debug("Common.isRead : "+ Common.isRead);
//            log.debug("Common.isWrite : "+ Common.isWrite);
//            log.debug("Common.isModify : "+ Common.isModify);
//            log.debug("Common.isDelete : "+ Common.isDelete);
//            log.debug("Common.isComment : "+ Common.isComment);
//            log.debug("Common.isManager : "+ Common.isManager);
//            log.debug("============================ 권한체크(글 쓰기 페이지) e =======================================");
        }

        // 읽기 모드
        if (boardMode.equals("V")) {
            isContinue = Common.isRead;
        }

        // 사용자 수정이면 수정권한 체크
        if (boardMode.equals("E")) {
            isContinue = Common.isModify;

            String SEQ    = request.getParameter("SEQ"); // 수정할 게시물번호

            if (Common.isEmpty(SEQ)) {
                isContinue = false;                     // 게시물번호가 없으면 오류
            }else{
                // 수정권한
                // 1. 수정권한이 있으면 누구나 수정 가능   => isContinue = commonController.isModify;
                // 2. 수정권한이 없으나 본인이 작성한 글이면 수정가능 : IDX, REG_ID == strMyID

                if (!isContinue) { // 수정권한이 없을 때 작성자 체크
                    String REG_ID = (String) communityService.getRegID(commandMap.getMap());

                    isContinue = !Common.isEmpty(REG_ID); // 작성자 아이디가 있으면 수정 가능
                }
            }
        }
        // 페이지모드에 따른 권한 체크 e ///////////////////////////////////////////////////////////////////

        isError = !isContinue;   // isContinue가 false이면 오류이다.

        if (isContinue) {
            // 게시판 설정값 가져오기 : 조건 = boardCode
            Map<String,Object> mapBoardSetting = commonService.getBoardSetting(boardCode);

            // 설정값이 없을 때 = 삭제된 게시판 인 경우, 등록되지 않은 경우
            if (Common.isEmpty(mapBoardSetting)) {
                isError = true;
            }else{
                // 이동할 페이지
                mv = new ModelAndView("/Community/Board");

                ////////////////////////////////////////////////////////////////////////////////
                // 읽기 & 수정 모드 //////////////////////////////////////////////////////////////
                if (boardMode.equals("V") || boardMode.equals("E")) {
                    // 읽기 : 해당 글 정보 가져오기
                    Map<String,Object> boardView = communityService.getBoardDetail(commandMap.getMap());


                    if (Common.isEmpty(boardView)) {
                        isError = true;
                    }else{

                        mv.addObject("boardView" , boardView.get("boardDetail"));       // 게시물 상세내용
                        mv.addObject("attachList" , boardView.get("attachList"));       // 첨부파일 목록
                        mv.addObject("attachCount" , boardView.get("attachCount"));       // 첨부파일 갯수
                        mv.addObject("isComment" , boardView.get("isComment"));       // 첨부파일 갯수
                    }
                }

                if (!isError) {
                    mv.addObject("htmltag" , commonService.getConfigureValue("htmltag"));   // 허용된 html tag

                    if (boardMode.equals("E") || boardMode.equals("W")) {
                        String extension = (String) commonService.getConfigureValue("attach");  // 첨부파일 확장자

                        mv.addObject("extension" , extension);
                        mv.addObject("extText" , extension.replace("||" , ","));
                    }
                    mv.addObject("boardConfig", mapBoardSetting);    // 게시판 설정값

                    mv.addObject("sideMenu" , smList);
                    mv.addObject("perm", Common.getPerm());
                    mv.addObject("boardCode" , boardCode);
                    mv.addObject("boardMode" , boardMode);
                    mv.addObject("pageTitle" , commonService.getPageTitle(request));

                    ////////////////////
                    // 페이징 관련
                    String page = Common.getCheckString((String) commandMap.getMap().get("page") , "1");
                    String orderBy = Common.getCheckString((String) commandMap.getMap().get("orderBy") , "REG_DATE");
                    String orderSort = Common.getCheckString((String) commandMap.getMap().get("orderSort") , "DESC");
                    String searchField = Common.getCheckString((String) commandMap.getMap().get("searchField") , "Title");

                    // 이하 검색, 페이징 관련
                    mv.addObject("orderBy" , orderBy);
                    mv.addObject("orderSort" , orderSort);
                    mv.addObject("searchField" , searchField);
                    mv.addObject("searchValue" , commandMap.getMap().get("searchValue"));
                    mv.addObject("category" , commandMap.getMap().get("category"));
                    mv.addObject("page" , page);
                }
            }
        }

        if (isError){
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("E400"));
        }
          return mv;
    }


    /**
     *
     * @param commandMap
     * @param request
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value="/ajax/getListBoard.do")
    public ModelAndView getListBoard(CommandMap commandMap , HttpServletRequest request) throws Exception {

        ModelAndView mv = null;

        if (Common.isRead) {
            mv = new ModelAndView("jsonView");

            List<Map<String,Object>> list = communityService.getListBoard(commandMap.getMap());

            mv.addObject("list" , list);
            mv.addObject("page" , commandMap.getMap().get("page"));
            mv.addObject("listCount" , commandMap.getMap().get("listCount"));
            mv.addObject("category" , commandMap.getMap().get("category"));
            mv.addObject("searchField" , commandMap.getMap().get("searchField"));
            mv.addObject("searchValue" , commandMap.getMap().get("searchValue"));
            mv.addObject("orderBy" , commandMap.getMap().get("orderBy"));
            mv.addObject("orderSort" , commandMap.getMap().get("orderSort"));

            // 총 게시글 갯수
            if (list.size() > 0) {
                mv.addObject("TOTAL" ,  communityService.getListBoardCnt(commandMap.getMap()));
            }else{
                mv.addObject("TOTAL" , 0);
            }
        }else{
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("perm"));
        }// if Common.isRead

        return mv;
    }


    /**
     *
     * TODO 게시판 새글 등록
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/RegBoard.do")
    public ModelAndView RegBoard(CommandMap commandMap , HttpServletRequest request) throws Exception {

        ModelAndView mv = null;

        mv = new ModelAndView("jsonView");
        if (Common.isWrite) {

            Map<String, Object> result = (Map<String, Object>) communityService.regBoard(commandMap.getMap() , request);   // 게시물 등록

            if (result.get("errCode").equals("SUCCESS")) {
                mv.addObject("errCode" , result.get("errCode"));
                mv.addObject("IDX" , result.get("IDX"));    // 게시물 등록 후 해당 게시물 상세보기로 이동하기 위한 IDX
            }else{
                mv.addObject("errCode", "ERROR");
                mv.addObject("errMessage", commonService.getErrMessage((String)result.get("errCode"))); //
            }
        }else{
//            mv = new ModelAndView("/err_page");
//            mv.addObject("err", commonService.getErrorPage("perm"));
            log.debug("============================ 권한체크(글 저장) s =======================================");
            log.debug("Common.isRead : "+ Common.isRead);
            log.debug("Common.isWrite : "+ Common.isWrite);
            log.debug("Common.isModify : "+ Common.isModify);
            log.debug("Common.isDelete : "+ Common.isDelete);
            log.debug("Common.isComment : "+ Common.isComment);
            log.debug("Common.isManager : "+ Common.isManager);
            log.debug("============================ 권한체크(글 저장) e =======================================");
            mv.addObject("errCode", "ERROR");
            mv.addObject("errMessage", commonService.getErrMessage("perm"));
        }// if Common.isWrite

        return mv;
    }


    /**
     *
     * TODO 게시물 수정
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/EditBoard.do")
    public ModelAndView EditBoard(CommandMap commandMap , HttpServletRequest request) throws Exception {
        boolean isModify = Common.isModify;

        ModelAndView mv = new ModelAndView("jsonView");

        commandMap.getMap().put("isModify", isModify);

        String result = (String) communityService.editBoard(commandMap.getMap() , request);   // 게시물 등록

        if (result.equals("SUCCESS")) {
            mv.addObject("errCode" , result);
            mv.addObject("SEQ" , commandMap.get("SEQ"));
        }else{
            mv.addObject("errCode", "ERROR");
            mv.addObject("errMessage", commonService.getErrMessage(result)); // 비번, 비번확인 불일치
        }

        return mv;
    }


    /**
     *
     * TODO 글 삭제하기
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/DeleteBoard.do")
    public ModelAndView deleteBoard(CommandMap commandMap , HttpServletRequest request) throws Exception {
        boolean isDelete = Common.isDelete;      // 권한체크

        ModelAndView mv = new ModelAndView("jsonView");

        commandMap.getMap().put("isDelete", isDelete);

        String result = (String) communityService.deleteBoard(commandMap.getMap() , request);   // 저장

        if (result.equals("SUCCESS")) {
            mv.addObject("errCode" , result);
        }else{
            mv.addObject("errCode", "ERROR");
            mv.addObject("errMessage", commonService.getErrMessage(result)); // 비번, 비번확인 불일치
        }

        return mv;
    }

    /**
     * 댓글 등록하기
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/board/popup/regComment.do")
    public ModelAndView regComment(CommandMap commandMap , HttpServletRequest request) throws Exception {

        ModelAndView mv = new ModelAndView("jsonView");

        if (Common.isComment || Common.isWrite) {

            String result = (String) communityService.regComment(commandMap.getMap());   // 등록

            if (result.equals("SUCCESS")) {
                mv.addObject( "errCode", result);
            } else {
                mv.addObject("errCode", "ERROR");
                mv.addObject("errMessage", commonService.getErrMessage(result));
            }
        } else {
            mv.addObject("errCode", "ERROR");
            mv.addObject("errMessage", commonService.getErrMessage("perm"));
        }// if Common.isWrite

        return mv;
    }

    /**
     *
     * TODO 댓글 조회
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/board/popup/viewComment.do")
    public ModelAndView viewComment(CommandMap commandMap , HttpServletRequest request) throws Exception {

        ModelAndView mv = null;

        if (Common.isRead) {
            mv = new ModelAndView("jsonView");

            List<Map<String, Object>>  commentList = communityService.getComment(commandMap.getMap());   // 조회
            mv.addObject("commentList", commentList);
        } else {
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("perm"));
        }// if Common.isWrite

        return mv;
    }

    /**
     *
     * TODO 댓글 수정하기
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/board/popup/editComment.do")
    public ModelAndView editComment(CommandMap commandMap , HttpServletRequest request) throws Exception {
        boolean isModify = Common.isModify;

        ModelAndView mv = new ModelAndView("jsonView");

        commandMap.getMap().put("isModify", isModify);

        String result = (String) communityService.editComment(commandMap.getMap());   // 수정

        if (result.equals("SUCCESS")) {
            mv.addObject("errCode", result);
        } else {
            mv.addObject("errCode", "ERROR");
            mv.addObject("errMessage", commonService.getErrMessage(result));
        }

        return mv;
    }

    /**
     *
     * TODO 댓글 삭제하기
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/board/popup/deleteComment.do")
    public ModelAndView deleteComment(CommandMap commandMap , HttpServletRequest request) throws Exception {
        boolean isDelete = Common.isDelete;      // 권한체크

        ModelAndView mv = new ModelAndView("jsonView");

        commandMap.getMap().put("isDelete", isDelete);

        String result = (String) communityService.deleteComment(commandMap.getMap());   // 삭제

        if (result.equals("SUCCESS")) {
            mv.addObject("errCode", result);
        }else{
            mv.addObject("errCode", result);
            mv.addObject("errMessage", commonService.getErrMessage(result));
        }

        return mv;
    }
}

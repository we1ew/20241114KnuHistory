package seeya.community.service.dao;

import seeya.core.dao.AbstractDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @className : CommunityDAO
 * @description : TODO 게시판 - 게시판 - 공유게시판
 *
 * @author 정보보호 이승환
 * @since 2017.09.30
 * @version 1.0
 * @see
 *
 * - Copyright (C) by seeya All right reserved.
 */
@Repository("communityDAO")
public class CommunityDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

//    @Resource(name="sqlSessionHira")
//    SqlSessionFactory defaultSqlSession;

    /**
     *
     * TODO 게시판 목록
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getListBoard(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        return (List<Map<String,Object>>) selectPagingList("community.getListBoard" , map);
    }

    /**
     *
     * TODO 게시판 목록 개수
     * @param map
     * @return
     * @throws Exception
     */
    public String getListBoardCnt(Map<String, Object> map) throws Exception {

        return (String) selectOne("community.getListBoardCnt" , map);
    }



    /**
     *
     * TODO 새글 등록
     * @param map
     * @throws Exception
     */
    public void regBoard(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        insert("community.regBoard" , map);
    }

    /**
     *
     * TODO 새글 등록 시 파일타입 업데이트
     * @param map
     * @throws Exception
     */
    public void updateFileType(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        insert("community.updateFileType" , map);
    }

    /**
     *
     * TODO 게시판 글 읽기
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBoardDetail(Map<String, Object> map) throws Exception {
//        super.setSqlSessionFactory(defaultSqlSession);
        return (Map<String, Object>) selectOne("community.getBoardDetail" , map);
    }

    /**
     *
     * TODO 게시물 삭제
     * @param map
     * @throws Exception
     */
    public void deleteBoard(Map<String, Object> map) throws Exception {
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.deleteBoard" , map);
    }

    /**
     *
     * TODO 게시물 작성자 아이디
     * @param map
     * @return
     * @throws Exception
     */
    public String getRegID(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        return (String) selectOne("community.getRegID" , map);
    }

    /**
     *
     * TODO 게시물 수정
     * @param map
     * @throws Exception
     */
    public void editBoard(Map<String, Object> map) throws Exception {
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.editBoard" , map);
    }

    /**
     *
     * TODO 게시물 등록시 파일 정보 저장
     * @param map
     * @throws Exception
     */
    public void insertFile(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        insert("community.insertFile", map);
    }

    /**
     *
     * TODO 조회수 증가
     * @param map
     * @throws Exception
     */
    public void updateReadCount(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.updateReadCount", map);
    }

    /**
     *
     * TODO 첨부파일 목록
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFileList(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        return (List<Map<String,Object>>) list("community.getFileList", map);
    }

    /**
     *
     * TODO 첨부파일 삭제
     * @param map
     * @throws Exception
     */
    public void deleteFile(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        delete("community.deleteFile" , map);
    }

    /**
     *
     * TODO 댓글 등록
     * @param map
     * @throws Exception
     */
    public void regComment(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        insert("community.regComment" , map);
    }

    /**
     *
     * TODO 댓글 그룹 업데이트
     * @param map
     * @throws Exception
     */
    public void updateCommentGroup(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.updateCommentGroup" , map);
    }

    /**
     *
     * TODO 댓글 수정
     * @param map
     * @throws Exception
     */
    public void editComment(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.editComment" , map);
    }

    /**
     *
     * TODO 댓글 읽기
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getComment(Map<String, Object> map) throws Exception {
//        super.setSqlSessionFactory(defaultSqlSession);
        return (List<Map<String,Object>>) list("community.getComment" , map);
    }

    /**
     *
     * TODO 댓글 삭제
     * @param map
     * @throws Exception
     */
    public void deleteComment(Map<String, Object> map) throws Exception {
//        super.setSqlSessionFactory(defaultSqlSession);
        update("community.deleteComment" , map);
    }

    /**
     *
     * TODO 댓글 작성자 아이디
     * @param map
     * @return
     * @throws Exception
     */
    public String getCommentRegID(Map<String, Object> map) throws Exception{
//        super.setSqlSessionFactory(defaultSqlSession);
        return (String) selectOne("community.getCommentRegID" , map);
    }

    /**
     * 마스터 상태 업데이트 - 개발게시판에만 적용 중
     * @param map
     * @throws Exception
     */
    public void updateMasterState(Map<String, Object> map) throws Exception{
        update("community.updateMasterState" , map);
    }
}
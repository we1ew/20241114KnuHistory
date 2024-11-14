package seeya.common.service.dao;


import seeya.core.dao.AbstractDAO;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
*
* @className : SampleDAO
* @description : TODO Description 명시한다.
*
* @modification : 2017. 8. 25.(손인수) 최초생성
*
* @author NDS 손인수
* @since 2017. 8. 25.
* @version 1.0
* @see
*
* -Copyright (C) by seeya All right reserved.
*/
@Repository("commonDAO")
public class CommonDAO extends AbstractDAO {

    /*
     * 에러메시지
     */
    public String getErrMessage(Map<String, Object> map) throws Exception{
        return (String) selectOne("common.getErrMessage" , map);
    }


    /*
     * 로그인용 로그 메시지
     */
    public String getLoginLogMessage(String errCode) throws Exception{
        return (String) selectOne("common.getLoginLogMessage" , errCode);
    }
    
    /*
     * 권한설정값 가져오기
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPerm(Map<String, Object> map) throws Exception{
        return (Map<String, Object>) selectOne("common.getPerm" , map);
    }

    /*
     * URI로 메뉴코드 가져오기
     */
    public String getMenuCode(String menuURL) {
        return (String) selectOne("common.getMenuCode" , menuURL);
    }

    /**
     * getSideMenu                      왼쪽 메뉴 가져오기
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSideMenu(Map<String, Object> map) throws Exception {
        // TODO Auto-generated method stub
        return (List<Map<String, Object>>) list("common.getSideMenu" , map);
    }

    /**
     * getBoardSetting                  게시판 설정값 가져오기
     * @param boardCode
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Object getBoardSetting(String boardCode) throws Exception{
        return (Map<String, Object>) selectOne("common.getBoardSetting" , boardCode);
    }

    /**
     * getFileInfo                      다운로드할 파일 정보
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFileInfo(Map<String, Object> map) throws Exception{
        return (Map<String, Object>)selectOne("common.getFileInfo", map);
    }

    /**
     * getCodeList                      코드목록 : 각 페이지에서 사용
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCodeList(Map<String, Object> map) throws Exception{
        return (List<Map<String, Object>>) list("common.getCodeList", map);
    }

    /**
     * getPageTitle             페이지 타이틀
     * @param menuCode
     * @return
     */
    public String getPageTitle(String menuCode) {
        return (String) selectOne("common.getPageTitle" , menuCode);
    }

    /**
     *                      환경설정 가져오기
     * @param resultMap 
     * @return
     * @throws Exception
     */
    public String getConfigureValue(Map<String, Object> resultMap) throws Exception{
        return (String) selectOne("common.getConfigureValue" , resultMap);
    }

    /**
     *                      로그 기록
     * @param map
     * @throws Exception
     */
    public void regLog(Map<String, Object> map) throws Exception{
        insert("common.regLog" , map);
    }

    /**
     *                      코드리스트의 코드그룹명
     * @param map
     * @return 
     * @throws Exception
     */
    public String getCodeGroupName(Map<String, Object> map) throws Exception{
        return (String) selectOne("common.getCodeGroupName" , map);
    }

    /**
     *                      확장자와 mime-type 체크
     * @param map
     * @return
     * @throws Exception
     */
    public String checkMimeType(Map<String, Object> map) throws Exception{
        return (String) selectOne("common.checkMimeType" , map);
    }

    /**
     * 증빙자료 등록을 위한 기본 설정값 : 파일 크기 합계, 등록가능한 파일 수, 파일 확장자
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked" })
    public Map<String, Object> getConfigureProof() throws Exception {
        return (Map<String, Object>) selectOne("common.getConfigureProof", null);
    }

    /**
     * 다운로드할 증빙자료 양식파일 정보
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> getProofFormInfo(Map<String, Object> map) throws Exception{
        return (Map<String, Object>)selectOne("common.getProofFormInfo", map);
    }

    /**
     * 다운로드할 증빙자료 정보
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> getProofFileInfo(Map<String, Object> map) {
        return (Map<String, Object>)selectOne("common.getProofFileInfo", map);
    }

    /**
     * 다운로드할 신청서 첨부파일 정보
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> getAFormFileInfo(Map<String, Object> map) {
        return (Map<String, Object>)selectOne("common.getAFormFileInfo", map);
    }

    /**
     * 부서 목록
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getTeamList() throws Exception{
        return (List<Map<String, Object>>) list("common.getTeamList", null);
    }

    /**
     * 신청서코드 목록 조회 : 뎁스1 : 셀렉트 박스
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getAFormCodelistSelectbox1() throws Exception{
        return (List<Map<String, Object>>) list("common.getAFormCodelistSelectbox1", null);
    }

    /**
     * 신청서코드 목록 조회 : 뎁스2 : 셀렉트 박스
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getAFormCodelistSelectbox2(Map<String, Object> map) throws Exception{
        return (List<Map<String, Object>>) list("common.getAFormCodelistSelectbox2", map);
    }

    /**
     * 결재해야 할 건수 조회 : 컴플라이언스, 신청서 : 왼쪽 메뉴에 표시
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> getApprovalCount(Map<String, Object> map) {
        return (Map<String, Object>)selectOne("common.getApprovalCount", map);
    }

    /**
     * 컴플라이언스 선택 년도 조회 : 컴플라이언스 선택 셀렉트 박스 왼쪽
     * @return
     */
    public List<Map<String, Object>> getYearList() {
        return (List<Map<String, Object>>) list("common.getYearList");
    }


    /**
     * 분석결과 파일 공유 : 파일 정보 가져오기
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAnalysisFileInfo(Map<String, Object> map) throws Exception{
        return (Map<String, Object>)selectOne("common.getAnalysisFileInfo", map);
    }

    /**
     * 특정페이지  권한에따른   MANAGE_YN  특정페이지 관리여부
     * 사용처예) : 민감데이터 페이지 관리자가 로그인시 관리여부 확인 하여 그관리있는사람만 민감데이터 알림여부 하기위함
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> checkManageYn(Map<String, Object> map) {
        return (Map<String, Object>)selectOne("common.checkManageYn", map);
    }

    /**
     * @methodName  :  getAlertCard
     * @description : 알림카드 설정관련 값 가져오기
     *                설정 - 알림카드 설정에서 셋팅한 값들
     * @Modification
     * @ 수정일      수정자     수정내용
     * @ ----------  -------    --------
     * @ 2021.08.20  이병덕     최초생성
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getAlertCard() throws Exception{
        return (List<Map<String, Object>>) list("common.getAlertCard");
    }
    
    /**
     * @methodName  :  selectSidoList
     * @description : 시도 목록 조회
     * @Modification
     * @ 수정일      수정자     수정내용
     * @ ----------  -------    --------
     * @ 2021.12.08  오근호     최초생성
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> selectSidoList() throws Exception{
        return (List<Map<String, Object>>) list("common.selectSidoList");
    }
    
}

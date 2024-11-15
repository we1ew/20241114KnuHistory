package seeya.common.service;

import seeya.common.service.dao.CommonDAO;
import seeya.common.util.Common;
import seeya.common.util.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// import seeya.Statistic.service.dao.StatisticDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
*
* @className : SampleService
* @description : Description 명시한다.
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
@Service("commonService")
public class CommonService {
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name="commonDAO")
    private CommonDAO commonDAO;
    
    @Resource(name="fileUtils")
    private FileUtils fileUtils;

    // @Resource(name="statisticDAO")
    // private StatisticDAO statisticDAO;

    /*
     * 오류 메시지
     * @see isac.common.service.CommonService#getErrMessage(java.lang.String)
     */
    public String getErrMessage(String errCode) throws Exception {
        
        Map<String, Object> map = new HashMap<String, Object>();  // 개체 생성

        map.put("errCode", errCode);        // 로그 코드 - 코드등록
        
        String errMsg = (String) commonDAO.getErrMessage(map);
        
        if (errMsg == "" || errMsg == null) {
            errMsg = "Exception Error";
        }
        
        return errMsg;
    }


    /*
     * 로그인용 로그 메시지
     * @see isac.common.service.CommonService#getErrMessage(java.lang.String)
     */
    public String getLoginLogMessage(String errCode) throws Exception {
        
        String errMsg = (String) commonDAO.getLoginLogMessage(errCode);
        
        return errMsg;
    }
    
    
    
    /*
     * 권한설정값 가져오기
     * menuCode , permCode
     * @see isac.common.service.CommonService#getPerm(java.util.Map)
     */
    public Map<String, Object> getPerm(Map<String, Object> map) throws Exception {
        
        return commonDAO.getPerm(map);
    }
    
    /*
     * URI로 메뉴코드 가져오기
     * @see isac.common.service.CommonService#getMenuCode(java.lang.String)
     */
    public String getMenuCode(String menuURL) throws Exception {
        return commonDAO.getMenuCode(menuURL);
    }

    /**
     * 
     * 사이드 메뉴 가져오기
     * @param request
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSideMenu(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        
        String permCode = (String) session.getAttribute("strMyPerm");
        
        List<Map<String, Object>> list = null;

        if (permCode != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            
            map.put("permCode", permCode);
            list = commonDAO.getSideMenu(map);
        }
        
        return list; //commonDAO.getSideMenu(map);
    }

    /**
     * getBoardSetting 게시판 설정값 가져오기
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBoardSetting(String boardCode) throws Exception {
        return (Map<String, Object>) commonDAO.getBoardSetting(boardCode);
    }

    /**
     * 
     * 임시폴더로 파일업로드
     * @param request
     * @return
     * @throws Exception
     */
    public String tempFileUpload(HttpServletRequest request) throws Exception {
        // Auto-generated method stub

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        
        String result = "ERROR";

        MultipartFile multipartFile  = null;
        
        String FileExtension = "";
        String FileName = "";
        String FileMime = "";
        
        while (iterator.hasNext()) {
            multipartFile = multipartHttpServletRequest.getFile(iterator.next());
            
            if (multipartFile.isEmpty()) {
                result = "NOT_FILE";
            }else{
                FileMime      = multipartFile.getContentType(); 
                FileName      = multipartFile.getOriginalFilename();
                FileExtension = FileName.substring(FileName.lastIndexOf("."));   // 원본 파일의 확장자 추출
                FileExtension = FileExtension.toLowerCase();
                
                if (this.checkMimeType(FileMime, FileExtension)) {
//                    log.debug("------------------------------- CommonService.tempFileUpload s ------------------------");
                    result = fileUtils.tempFileUpload(request);
//                    log.debug("------------------------------- CommonService.tempFileUpload e ------------------------");
                }else{
                    result = "OVER_MIME";
                }
            }
        }

        return result;
    }

    /**
     * getFileList 임시폴더 내 파일 목록 가져오기
     */
    @SuppressWarnings("static-access")
    public List<Map<String, Object>> getFileList(HttpServletRequest request) throws Exception {
        // Auto-generated method stub
        String serviceType = request.getParameter("strm");

        String filePath = fileUtils.baseDir + fileUtils.basePath + fileUtils.tempPath;
        
        filePath = filePath + request.getSession().getAttribute("strMyID") + "/" + serviceType + "/";
        //filePath = request.getSession().getServletContext().getRealPath(filePath);
        
        return (List<Map<String, Object>>) fileUtils.getFileList(filePath);
    }

    /**
     * tempDeleteFolder 임시폴더 초기화
     */
    public String tempDeleteFolder(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        return (String) fileUtils.tempDeleteFolder(map);
    }

    /**
     * tempDeleteFile 임시폴더 내 1개 파일 삭제
     */
    public String tempDeleteFile(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        return (String) fileUtils.tempDeleteFile(map);
    }

    /**
     * getFileInfo 다운로드할 파일 정보
     */
    public Map<String, Object> getFileInfo(Map<String, Object> map) throws Exception {
        Map<String, Object> resultMap = null;

        switch ((String)map.get("strm")) {
            case "Board": // 게시판 첨부파일
                resultMap = commonDAO.getFileInfo(map); break;
            case "Analysis": // 분석결과 파일 공유
                resultMap = commonDAO.getAnalysisFileInfo(map); break;
        }

        return resultMap;
    }

    /**
     * getCodeList 코드목록 : 각 페이지에서 사용
     */
    public List<Map<String, Object>> getCodeList(String Code) throws Exception {
        // Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("Code", Code);
        return commonDAO.getCodeList(map);
    }

    /**
     * getPageTitle 페이지 타이틀
     */
    public String getPageTitle(HttpServletRequest request) {
        // Auto-generated method stubHttpSession session = request.getSession();
        HttpSession session = request.getSession();
        
        String menuCode = (String) session.getAttribute("menuCode");
        
        return commonDAO.getPageTitle(menuCode);
    }

    /**
     * 엑셀파일 저장 경로
     */
    @SuppressWarnings("static-access")
    public String getExcelDir() {
        // Auto-generated method stub
        return (String) fileUtils.baseDir + fileUtils.basePath + fileUtils.excelPath;
    }

    /**
     * 환경설정 가져오기
     */
    public String getConfigureValue(String Code) throws Exception {
        // Auto-generated method stub
        
        Map<String, Object> resultMap = new HashMap<String, Object>();  // 개체 생성

        resultMap.put("Code", Code);
        
        String result = (String) commonDAO.getConfigureValue(resultMap);
        
        if (Code.equals("attach")) {
            result = result.replaceAll(",", "||");
        }

        return result;
    }

    /**
     * 
     * 사용자 로그 기록
     * @param map
     * @throws Exception
     */
    public void regLog(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        //commonDAO.regLog(map);
    }

    /**
     * 
     * 코드리스트의 코드그룹명
     * @param code
     * @return
     * @throws Exception
     */
    public String getCodeGroupName(String code) throws Exception {
        // Auto-generated method stub

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("code", code);

        return (String) commonDAO.getCodeGroupName(map);
    }
    

    /**
     * 해당 페이지 권한 체크
     */
    public String checkPagePerm(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        
        String result = "SUCCESS";
        
        String menuURL   = request.getRequestURI();             // 경로
        String permCode  = (String) session.getAttribute("strMyPerm");   // 현재 권한
        String boardCode = request.getParameter("boardCode");   // T_MENU에 저장된 기본 주소를 체크하기 위한 값.
        String Code      = request.getParameter("Code");   // T_MENU에 저장된 기본 주소를 체크하기 위한 값.

        if (!Common.isEmpty(boardCode)) {
            menuURL = menuURL + "?boardCode="+ boardCode;
        }
        if (!Common.isEmpty(Code)) {
            menuURL = menuURL + "?Code="+ Code;
        }

        // URI로 메뉴코드 가져오기
        String menuCode = getMenuCode(menuURL);
        
        if (Common.isEmpty(menuCode)) {
            result = "E100";
        }else{
            // 권한설정값 세팅
            Map<String, Object> listMap = new HashMap<String, Object>();

            listMap.put("permCode", permCode);
            listMap.put("menuCode", menuCode);

            Map<String,Object> map = getPerm(listMap); 
            
            if (Common.isEmpty(map)) {
                Common.permInit();
            }else{
                Common.setPerm(map);
            }
    
            // 읽기 권한이 없으면 오류 페이지로 이동
            if (!Common.isRead) {
                result = "perm";
            }else{
                session.setAttribute("menuCode", menuCode);
                session.setAttribute("boardCode" , boardCode);
            }
        }
        
        return result;
    }

    /**
     *                  에러페이지 내용
     */
    public Map<String, Object> getErrorPage(String errCode) throws Exception {
        // Auto-generated method stub
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("errCode", "ERROR");
        map.put("errMessage", getErrMessage(errCode));
        
        return map;
    }

    /**
     *                  확장자와 mime-type 체크
     */
    public boolean checkMimeType(String fileMime, String fileExtension) throws Exception {
        // Auto-generated method stub

        Map<String, Object> map = new HashMap<String, Object>();  // 개체 생성
        
        map.put("fileMime", fileMime);
        map.put("fileExtension", fileExtension);

        String mimeCheck = commonDAO.checkMimeType(map);

        return mimeCheck.equals("Y");
    }

    /**
     * 파일을 저장할 기본 경로
     * @return
     */
    @SuppressWarnings("static-access")
    public String getBaseDir() {
        return (String) fileUtils.baseDir;
    }

    /**
     * 파일존재여부 체크
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean checkFile(String filePath, String fileName) throws Exception {
        return fileUtils.isCheckFile(filePath , fileName);
    }

    /**
     * 
     * 증빙자료 등록을 위한 기본 설정값 : 파일 크기 합계, 등록가능한 파일 수, 파일 확장자
     * @return
     */
    public Map<String, Object> getConfigureProof() throws Exception {
        return commonDAO.getConfigureProof();
    }

    /**
     * 다운로드할 등록된 증빙자료 정보
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getTeamList() throws Exception {
        return commonDAO.getTeamList();
    }

    /**
     * 파일 다운로드 : 공통
     * @param map
     * @param response
     * @return
     * @throws Exception
     */
    public String downloadFile(Map<String,Object> map , HttpServletResponse response) throws Exception {

        String result = "SUCCESS";

        String FileName         = (String) map.get("FileName");
        String originalFileName = (String) map.get("originalFileName");
        String FileDir          = (String) map.get("FileDir");

        // 파일 존재 여부 체크
        if (checkFile(FileDir, FileName)) {

            byte[] fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(new File(FileDir + FileName));

            response.setContentType("application/octet-stream");
            response.setContentLength(fileByte.length);

            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.getOutputStream().write(fileByte);

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }else{
            result = "FAIL";
        }

        return result;
    }

    /**
     * 신청서코드 목록 조회 : 뎁스1 : 셀렉트 박스
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getAFormCodelistSelectbox1() throws Exception {
        return commonDAO.getAFormCodelistSelectbox1();
    }

    /**
     * 신청서코드 목록 조회 : 뎁스2 : 셀렉트 박스
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getAFormCodelistSelectbox2(Map<String, Object> map) throws Exception {
        return commonDAO.getAFormCodelistSelectbox2(map);
    }

    /**
     * 신청서 서버, IP 정보 파싱
     * @param str
     * @return
     * @throws Exception
     */
    public Map<String,Object> parseServerToDB(String str) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();  // 개체 생성

        //str = 0.0.0.0^^서버명^$^0.0.0.0^^서버명^$^0.0.0.0^^서버명
        String[] arrIP = str.split("^$^");

        String parseStr = "";
        String saveIP = "";
        String saveName = "";

        for (int ii = 0; ii < arrIP.length; ii++) {
            parseStr = arrIP[ii]; // 0.0.0.0^^서버명

            String[] arr = parseStr.split("^^");

            if (!Common.isEmpty(saveIP)) {
                saveIP   = saveIP   + "\r\n";
                saveName = saveName + "\r\n";
            }

            saveIP   = saveIP   + arr[0];
            saveName = saveName + arr[1];
        }

        map.put("IP" , saveIP);
        map.put("SERVER" , saveName);

        return map;
    }

    /**
     * 신청서 서버명 / IP 정보 파싱
     * @param str
     * @return
     * @throws Exception
     */
    public String parseServerExcelToDB(String str) throws Exception {
        String[] arr = null;

        String result = "";

        arr = str.split("\n");

        for (int ii=0; ii<arr.length; ii++) {
            if (Common.isEmpty(result)) {
                result = arr[ii];
            }else{
                result += "^^" + arr[ii];
            }
        }
        return result;
    }
//    public String parseServerExcelToDB(String strIP , String strServer) throws Exception {
//        String[] arrIP = null;
//        String[] arrSERVER = null;
//
//        String result = "";
//
//        arrIP = strIP.split("\n");
//        arrSERVER = strServer.split("\n");
//
//        for (int ii=0; ii<arrIP.length; ii++) {
//            result += arrIP[ii] + "^^" + arrSERVER[ii];
//
//            if (ii < (arrIP.length - 1)) {
//                result += "^$^";
//            }
//        }
//
//        return result;
//    }

    /**
     * 신청서 정책 코드
     * @param str
     * @param aformCodeUp
     * @return
     * @throws Exception
     */
    public String getPolicyCode(String str, String aformCodeUp) throws Exception{
        final String aformCode_FW      = "FW";        // 방화벽
        // final String aformCode_VPN     = "VPN";       // 원격근무
        final String aformCode_DOMAIN  = "DOMAIN";    // 도메인 신청서
        final String aformCode_LAN     = "LAN";       // LAN 설치요청서
        final String aformCode_SITE    = "SITE";      // 유해사이트 허용 및 차단
        // final String aformCode_NW      = "NW";        // 네트워크 작업의뢰서
        final String aformCode_SERVER  = "SERVER";    // 서버 사용자계정 신청서
        // final String aformCode_BACKUP  = "BACKUP";    // 서버 백업 의뢰서
        // final String aformCode_RESTORE = "RESTORE";   // 서버 복원 의뢰서
        final String aformCode_DESKTOP = "DESKTOP";   // 데스크탑 가상화
        final String aformCode_IP      = "IP";        // IP주소 신청서

        String result = "";

        switch (aformCodeUp) {
            case aformCode_FW      : result = str.equals("허용") ? "Y" : "N";  break;
            case aformCode_DOMAIN  : result = str.equals("신규") ? "Y" : "N";  break;
            case aformCode_LAN     : result = str.equals("신규") ? "Y" : "N";  break;
            case aformCode_SITE    : result = str.equals("허용") ? "Y" : "N";  break;
            case aformCode_SERVER  : if (str.equals("신규")) {
                                         result = "1";
                                     }else if (str.equals("신규생성")) {
                                         result = "2";
                                     }else{
                                         result = "3";
                                     }
                                     break;
            case aformCode_DESKTOP : result = str.equals("신규") ? "Y" : "N";  break;
            case aformCode_IP      : result = str.equals("신규") ? "Y" : "N";  break;
        }

        return result;
    }

    /**
     * 결재해야 할 건수 조회 : 컴플라이언스, 신청서 : 왼쪽 메뉴에 표시
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> getApprovalCount(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        return commonDAO.getApprovalCount(map);
    }

    /**
     * 컴플라이언스 선택 년도 조회 : 컴플라이언스 선택 셀렉트 박스 왼쪽
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getYearList() throws Exception {

        return commonDAO.getYearList();
    }

    /**
     * 특정페이지  권한에따른   MANAGE_YN  특정페이지 관리여부
     * 사용처 : 민감데이터 페이지 관리자가 로그인시 관리여부 확인
     * @param map
     * @return
     * @throws Exception
     *
     */
    public Map<String, Object> checkManageYn(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        return commonDAO.checkManageYn(map);
    }

    /**
     * @methodName  : ExcelDownload
     * @description : 엑셀 다운로드
     * @Modification
     * @ 수정일      수정자    수정내용
     * @ ----------  -------   --------
     * @ 2021.09.07  전형상    최초생성
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> ExcelDownload(Map<String, Object> map, HttpServletRequest request) throws Exception {

        String excelName = (String) Common.getDate() + Common.getTime() + "_"+ map.get("strMyID") + ".xlsx";
        String excelPath = fileUtils.baseDir + fileUtils.basePath + fileUtils.excelPath;
        String ExcelServerCode = (String) map.get("ExcelServerCode");
        String title = (String) map.get("title");

        Map<String, Object> resultMap = new HashMap<String, Object>();  // 개체 생성

        resultMap.put("excelName", excelName);                                  // 생성된 개체에 넣고  - SampleController에서 사용될 키가 map이다
        resultMap.put("excelPath", excelPath);                                  // 생성된 개체에 넣고  - SampleController에서 사용될 키가 map이다
        resultMap.put("ExcelServerCode", ExcelServerCode);
        resultMap.put("title", title);
        List<Map<String, Object>> list = null;

        /*
        switch (ExcelServerCode){
            case "WEEK"  : list = statisticDAO.ExcelDownloadWeek(map);   break;           // SQL - 요일별 엑셀 다운로드
            case "MONTH" : list = statisticDAO.ExcelDownloadMonth(map);  break;           // SQL - 월별 엑셀 다운로드
        }
        */
        resultMap.put("list", list);
        return resultMap;
    }

    /**
     * @methodName  : getAlertCard
     * @description : 알림카드 설정관련 값 가져오기
     *                설정 - 알림카드 설정에서 셋팅한 값들
     * @Modification
     * @ 수정일      수정자     수정내용
     * @ ----------  -------    --------
     * @ 2021-09-28  이세희     최초생성
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAlertCard() throws Exception {
        return commonDAO.getAlertCard();
    }
    
    /**
     * @methodName  : selectSidoList
     * @description : 시도 목록 조회
     * @Modification
     * @ 수정일      수정자     수정내용
     * @ ----------  -------    --------
     * @ 2021-12-08  오근호     최초생성
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectSidoList() throws Exception {
        return commonDAO.selectSidoList();
    }
    
}

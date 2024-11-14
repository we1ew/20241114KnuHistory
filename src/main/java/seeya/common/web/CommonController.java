package seeya.common.web;

import seeya.common.common.CommandMap;
import seeya.common.service.CommonService;
import seeya.common.util.Common;
import seeya.excel.util.ExcelUtil;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @className : CommonController
 * @description : 파일 업로드, 다운로드, 생성된 엑셀파일 다운로드
 *
 *      - tempFileUpload        임시폴더로 파일업로드
 *      - getFileList           파일 목록 가져오기
 *      - tempDeleteFolder      임시폴더 초기화 : 임시폴더 삭제
 *      - tempDeleteFile        임시폴더 초기화 : 임시폴더 내의 파일 삭제\
 *      - checkAttachment       게시판 첨부파일 존재여부 체크
 *      - downloadFile          첨부파일 다운로드
 *      - goDownloadNameDir     파일명, 경로로 다운로드
 *      - downloadExcelFile     생성된 엑셀파일 다운로드
 *
 * @author 팀명 이병덕
 * @since 2017. 9. 14.
 * @version 1.0
 * @see
 *
 * - Copyright (C) by seeya All right reserved.
 */

@Controller
public class CommonController {

    @Resource(name="commonService")
    private CommonService commonService;

    @Resource(name="excelUtil")
    private ExcelUtil excelUtil;
    
    /**
     * tempFileUpload 임시폴더로 파일업로드
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/tempFileUpload.do")
    public ModelAndView tempFileUpload(HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        mv.addObject("errCode" , commonService.tempFileUpload(request));

        return mv;
    }

    /**
     * 파일 목록 가져오기
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/getFileList.do")
    public ModelAndView getFileList(HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        List<Map<String,Object>> list = commonService.getFileList(request);

        mv.addObject("fileList" , list);
        return mv;
    }

    /**
     * 임시폴더 초기화 : 임시폴더 삭제
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/tempDeleteFolder.do")
    public ModelAndView tempDeleteFolder(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        mv.addObject("errCode" , commonService.tempDeleteFolder(commandMap.getMap()));

        return mv;
    }

    /**
     * 임시폴더 초기화 : 임시폴더 내의 파일 삭제
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/tempDeleteFile.do")
    public ModelAndView tempDeleteFile(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        mv.addObject("errCode" , commonService.tempDeleteFile(commandMap.getMap()));

        return mv;
    }

    /**
     * 게시판 첨부파일 존재여부 체크
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajax/common/checkAttachment.do")
    public ModelAndView checkAttachment(CommandMap commandMap , HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        boolean error = false;
        String strm = (String) commandMap.getMap().get("strm");

        Map<String, Object> map = null;

        if ("Board".equals(strm) || "ProofForm".equals(strm) || "ProofFile".equals(strm) || "AForm".equals(strm)) {
            map = commonService.getFileInfo(commandMap.getMap());
        } else {
            error = true;
        }

        if (map == null) error = true;

        if(!error) {
            String FileName = (String) map.get("FILE_NM");
            String FileDir  = (String) map.get("FILE_DIR");
            String baseDir  = (String) commonService.getBaseDir();

            FileDir = baseDir + FileDir;

            if (commonService.checkFile(FileDir, FileName)) {
                mv.addObject("errCode" , "SUCCESS");
            }else{
                mv.addObject("errCode" , "FAIL");
            }
        } else {
            mv.addObject("errCode" , "FAIL");
        }

        return mv;
    }

    /**
     * 첨부파일 다운로드
     * @param commandMap
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/common/goDownloadFile.do")
    public ModelAndView downloadFile(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

        String result = "";

        // 다운로드 할 파일 정보 가져오기
        Map<String,Object> map = commonService.getFileInfo(commandMap.getMap());

        String FileName         = (String) map.get("FILE_NM");
        String originalFileName = (String) map.get("ORIGINAL_NAME");
        String FileDir          = (String) map.get("FILE_DIR");
        String baseDir          = (String) commonService.getBaseDir();

        FileDir = baseDir + FileDir;

        // 다운로드할 파일 정보를 담을 객체
        Map<String, Object> downloadMap = new HashMap<String, Object>();  // 개체 생성

        downloadMap.put("FileName"         , FileName);
        downloadMap.put("originalFileName" , originalFileName);
        downloadMap.put("FileDir"          , FileDir);

        // 파일 존재여부 체크 후 다운로드. 결과 텍스트 리턴
        result = commonService.downloadFile(downloadMap , response);

        if (result.equals("SUCCESS")) {
            return null;
        }else{
            ModelAndView mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("E782"));  // 파일다운로드에실패하였습니다.

            return mv;
        }
    }

    /**
     * 파일명, 경로로 다운로드
     * @param commandMap
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/common/goDownloadNameDir.do")
    public ModelAndView goDownloadNameDir(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

        String result = "";

        String FileName         = (String) commandMap.getMap().get("fileName");
        String FileDir          = (String) commandMap.getMap().get("fileDir");
        String baseDir          = (String) commonService.getBaseDir();

        FileDir = baseDir + FileDir;

        // 다운로드할 파일 정보를 담을 객체
        Map<String, Object> downloadMap = new HashMap<String, Object>();  // 개체 생성

        downloadMap.put("FileName"         , FileName);
        downloadMap.put("originalFileName" , FileName);
        downloadMap.put("FileDir"          , FileDir);

        // 파일 존재여부 체크 후 다운로드. 결과 텍스트 리턴
        result = commonService.downloadFile(downloadMap , response);

        if (result.equals("SUCCESS")) {
            return null;
        }else{
            ModelAndView mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("E782")); // 파일다운로드에실패하였습니다.

            return mv;
        }
    }

    /**
     * 생성된 엑셀파일 다운로드
     * @param commandMap
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/common/downloadExcelFile.do")
    public void downloadExcelFile(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

        String FileName         = (String) commandMap.getMap().get("excelName");
        String originalFileName = (String) commandMap.getMap().get("category") + "_" + FileName;
        String FileDir          = (String) commonService.getExcelDir();

        //FileDir = request.getSession().getServletContext().getRealPath(FileDir);

        byte fileByte[] = FileUtils.readFileToByteArray(new File(FileDir + FileName));

        response.setContentType("application/octet-stream");
        response.setContentLength(fileByte.length);

        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileByte);

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }



    /**
     * @methodName  : ExcelDownload
     * @description : 엑셀 다운로드
     * @Modification
     * @ 수정일      수정자    수정내용
     * @ ----------  -------   --------
     * @ 2021.09.07  전형상    최초생성
     *
     * @param request
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value="/common/ajax/ExcelDownload.do")
    public ModelAndView ExcelDownload(CommandMap commandMap ,HttpServletRequest request) throws Exception {

        ModelAndView mv = null;
        
        if (Common.isRead) {
            mv = new ModelAndView("jsonView");

            // 리스트 불러오기
            Map<String, Object> map = commonService.ExcelDownload(commandMap.getMap(), request);

            String excelName = excelUtil.ExcelDownload(map);
            mv.addObject("excelName" , excelName);    // 엑셀 명

        }else{
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("perm"));
        }// if Common.isRead

        return mv;
    }
    
    /**
     * @methodName  : selectSidoList
     * @description : 시도 목록 조회
     * @Modification
     * @ 수정일      수정자    수정내용
     * @ ----------  -------   --------
     * @ 2021.09.07  전형상    최초생성
     *
     * @param request
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value="/common/ajax/selectSidoList.do")
    public ModelAndView selectSidoList(CommandMap commandMap ,HttpServletRequest request) throws Exception {

        ModelAndView mv = null;

        if (Common.isRead) {
            mv = new ModelAndView("jsonView");

            // 시도 목록 조회
            List<Map<String, Object>> list = commonService.selectSidoList();

            mv.addObject("result" , list);    //시도 목록
        }else{
            mv = new ModelAndView("/err_page");
            mv.addObject("err", commonService.getErrorPage("perm"));
        }// if Common.isRead

        return mv;
    }
}

package seeya.common.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;

@Component("fileUtils")  // @Componet 어노테이션을 이용하여 이 객체 관리를 스프링이 담당하도록 한다
public class FileUtils {
    Logger log = Logger.getLogger(this.getClass());

    public static final String baseDir        = "/home/seeya/project/2021/FireProject";   // 운영,개발서버
    public static final String historyURL     = "http://localhost:8780";      // 개발서버

    public final String basePath       = "/_wwwPDS/";                  // 최상위 경로
    public final String tempPath       = "_temp/";            // 임시파일 저장될 위치

    public final String boardPath       = "Board/";            // 게시판 첨부파일 저장위치
    public final String excelPath       = "_excel/";           // 엑셀 다운로드용 파일 생성 폴더
    public final String thumbnailPath   = "Thumbnail/";        // 게시판 이미지 썸네일 저장위치
    public final String AnalysisPath    = "Analysis/";             // 분석결과파일 저장위치

    /**
     * tempFileUpload           임시폴더로 파일 저장
     * @param request
     * @return
     * @throws Exception
     */
    public String tempFileUpload (HttpServletRequest request) throws Exception{    
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        String serviceType = multipartHttpServletRequest.getParameter("strm");
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

        MultipartFile multipartFile  = null;
        
        String result = "SUCCESS";
        String filePath = baseDir + basePath + tempPath + request.getSession().getAttribute("strMyID") + "/" + serviceType + "/";
        //filePath = request.getSession().getServletContext().getRealPath(filePath);
        
        try {

            long folderSize = getFolderSize(filePath);   // 임시폴더 크기

            long limit = Long.parseLong(request.getParameter("limit"));

            if (Common.isEmpty(limit) || limit < 0) {
                throw new Exception();
            }

            if (limit == 0) {
                limit = 5;
            }
            limit = limit * 1024 * 1024;

            File file = new File(filePath);

            // 저장경로가 없으면 폴더 생성
            if (file.exists() == false) {
                file.mkdirs();
            }
            
            if (file.canWrite() == false){
                file.setWritable(true);
            }
            
            while (iterator.hasNext()) {
                multipartFile = multipartHttpServletRequest.getFile(iterator.next());
                
                if (multipartFile.isEmpty() == false) {
                    folderSize += multipartFile.getSize();  // 현재 임시폴더의 크기 + 등록되는 파일의 크기
                    
                    if (folderSize >= limit) {
                        result = "OVER_LIMIT";
                    }else{
                        String FileName = multipartFile.getOriginalFilename();
                        file = new File(filePath + FileName);
                        multipartFile.transferTo(file);             // 파일 저장
                    }
                }
            }
        }catch(Exception e){
            result = "ERROR";
        }

        return result;
    }
    
    /**
     * 폴더 초기화 : 임시폴더(/_wwwPDS/_temp/ 안에 있는 현재 로그인 아이디 폴더) 삭제 후 재 생성
     * @param map
     * @throws Exception
     */
    public String tempDeleteFolder(Map<String, Object> map) throws Exception{
        String filePath = baseDir + basePath + tempPath + map.get("strMyID") + "/" + map.get("strm") + "/";
        return deleteFolder(filePath);
    }
    
    /**
     * 폴더 삭제 - 하위 폴더가 있으면 하위 폴더까지
     * @param filePath
     * @throws Exception
     */
    public String deleteFolder(String filePath) throws Exception{
        File dir = new File(filePath);
        
        // 폴더가 있으면...
        if (dir.exists()) {
            File[] fileList = dir.listFiles(); // 폴더내 파일 목록

            for (int i=0; i<fileList.length; i++) {
                if (fileList[i].isFile()) {
                    fileList[i].delete();
                }else{
                    deleteFolder(fileList[i].getPath()); // 재귀함수
                }

                fileList[i].delete();
            }

            if (dir.delete()) {
                return "SUCCESS";
            }else{
                return "FAIL";
            }
        }else{
            return "SUCCESS";
        }
    }

    /**
     *                      폴더 생성
     * @param filePath
     * @throws Exception
     */
    public String makeFolder(String filePath , Boolean isDelete) throws Exception{
        try {
            File dir = new File(filePath);

            // 경로가 없으면 폴더 생성
            if (!dir.exists()) {
                dir.mkdirs();
            } else {

                if (isDelete) {
                    // 폴더가 있으면 폴더 삭제 후 재생성
                    deleteFolder(filePath);
                    dir.mkdirs();
                }
            }

            // 쓰기권한 부여
            if (!dir.canWrite()) {
                dir.setWritable(true);
            }
        }catch (Exception e){
            //e.printStackTrace();
            log.error("ERROR : 폴더생성 ERROR");
            return "ERROR";
        }
        return "SUCCESS";
    }

    /**
     *                      폴더 존재 여부
     * @param filePath
     * @throws Exception
     */
    public Boolean checkFolder(String filePath) throws Exception{
        File dir = new File(filePath);

        return dir.exists();
    }

    /**
     *                      임시폴더 내 1개 파일 삭제
     * @param map
     * @throws Exception
     */
    public String tempDeleteFile(Map<String, Object> map) throws Exception{

        String filePath = baseDir + basePath + tempPath + map.get("strMyID") + "/" + map.get("strm") + "/";
        //filePath = request.getSession().getServletContext().getRealPath(filePath);
        
        String result = "FAIL";
        String deleteFile = (String)map.get("Fname");

        if (deleteFile == null || deleteFile == "") {
            result = "FILE_ERROR";
        }else{
            File dir = new File(filePath);

            // 폴더가 있으면...
            if (dir.exists()) {
                File []fileList = dir.listFiles();

                for(File tempFile : fileList) {
                    if(tempFile.isFile()) {
                        if (tempFile.getName().equals(deleteFile)) {
                            if (tempFile.delete()) {
                                result = "SUCCESS";
                            }
                        } // equals
                    } // tempFile.isFile
                } // for
            }// if dir.exists  , 폴더가 없으면 오류이다. 잘못된 경로.
        }
        
        return result;
    }

    /**
     * 폴더 내 1개 파일 삭제
     * @param filePath
     * @param deleteFileName
     * @return
     * @throws Exception
     */
    public String deleteFile(String filePath , String deleteFileName ) throws Exception{

        //String filePath = baseDir + basePath + tempPath + request.getSession().getAttribute("strMyID") + "/";

        String result = "FAIL";
        System.out.print("-------------------------- deleteFile - filePath: "+ filePath +"\n");
        System.out.print("-------------------------- deleteFile - deleteFileName: "+ deleteFileName +"\n");

        if (deleteFileName == null || deleteFileName == "") {
            result = "FILE_ERROR";
        }else{
            File dir = new File(filePath);

            // 폴더가 있으면...
            if (dir.exists()) {
                File []fileList = dir.listFiles();

                for(File tempFile : fileList) {
                    if(tempFile.isFile()) {
                        if (tempFile.getName().equals(deleteFileName)) {
                            if (tempFile.delete()) {
                                result = "SUCCESS";
                            }
                        } // equals
                    } // tempFile.isFile
                } // for
            }// if dir.exists  , 폴더가 없으면 오류이다. 잘못된 경로.
        }

        return result;
    }
    
    
    /**
     *                      폴더 내 파일 목록
     * @param filePath
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getFileList (String filePath) throws Exception{
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
        Map<String, Object> listMap = null;
        
        File dir = new File(filePath);
        
        // 폴더가 있으면...
        if (dir.exists()) {
            File []fileList = dir.listFiles();
        
            for(File tempFile : fileList) {
                if(tempFile.isFile()) {
                
                    listMap = new HashMap<String, Object>();
                
                    String tempFileName = tempFile.getName();
                
                    listMap.put("FileName" , tempFileName);
                    listMap.put("FileSize", tempFile.length());
                
                    list.add(listMap);
                }
            }
        }
        return list;
    }

    /**
     *                      폴더 내 파일 존재 여부 체크 
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean isCheckFile (String filePath , String fileName) throws Exception{

        File dir = new File(filePath);
        boolean isHave = false;
        
        // 폴더가 있으면...
        if (dir.exists()) {
            File []fileList = dir.listFiles();
        
            for(File tempFile : fileList) {
                if(tempFile.isFile()) {
                
                    String tempFileName = tempFile.getName();
                    
                    if (tempFileName.equals(fileName)) {
                        isHave = true;
                    }
                }
            }
        }
        return isHave;
    }
    
    /**
     *                      폴더 크기 : 폴더 내 파일 크기의 합
     * @param filePath
     * @return
     * @throws Exception
     */
    public long getFolderSize(String filePath) throws Exception{
        File dir = new File(filePath);
        long folderSize = 0;
        
        // 폴더가 있으면...
        if (dir.exists()) {
            File []fileList = dir.listFiles();
                
            for(File tempFile : fileList) {
                if(tempFile.isFile()) {        
                    folderSize += tempFile.length();
                    
                }
            }
        }

        return folderSize;
    }

    /**
     * 임시폴더의 파일을 첨부파일 폴더로 이동 : 게시판 등 idx가 있는 게시물
     * @param map
     * @param savePath
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> saveFileUpload(Map<String, Object> map, String savePath) throws Exception{

        String originalFileName = null;      // 파일 원본명
        String FileExtension    = null;
        String storedFileName   = null;      // 저장될 파일명
        String thumbFileName    = null;      // 섬네일 파일명
        String FileDir          = null;      // DB 저장 경로
        String FileSize         = null;      // DB 저장 파일 사이즈
        String FileType         = null;      // DB 저장 파일 종류 : I = 이미지, F = 파일
        String tempFullPath     = null;      // 임시폴더
        String saveFullPath     = null;      // 실 파일 저장 디렉토리
        String thumbFullPath    = null;      // 썸네일 디렉토리

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // 클라이언트에서 전송된 파일 정보를 담아 반환할 객체
        Map<String, Object> listMap = null;

        String SEQ;
        String codePath;

        // 게시물 SEQ
        SEQ = map.get("SEQ").toString();
        codePath = "";

        // 실제 저장될 폴더 경로
        saveFullPath = baseDir + basePath + savePath + codePath + SEQ + "/";

        // 썸네일 저장될 폴더 경로
        thumbFullPath = baseDir + basePath + thumbnailPath + savePath + codePath + SEQ + "/";

        // 임시파일 폴더 경로
        tempFullPath = baseDir + basePath + tempPath + map.get("strMyID") + "/" + savePath;

        FileDir = saveFullPath.replace(baseDir , "");

        File dir = new File(tempFullPath);
        File saveDir = new File(saveFullPath);

        // 폴더가 있으면...
        if (dir.exists()) {
            File []fileList = dir.listFiles();

            // 저장할 폴더가 있으면 수정상태이므로 저장 폴더 삭제
            if (saveDir.exists()) {
                deleteFolder(saveFullPath);
            }
            saveDir.mkdirs();

            if (!saveDir.canWrite()) {
                saveDir.setWritable(true);
            }

//            Integer num = 1;
            int fileIndex = 0;
            for(File tempFile : Objects.requireNonNull(fileList)) {
                if(tempFile.isFile()) {

                    listMap = new HashMap<String, Object>();

                    originalFileName = tempFile.getName();
                    FileExtension    = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();   // 원본 파일의 확장자 추출

                    // DB에 저장될 파일명 생성
                    storedFileName   = Common.getRandomString() + FileExtension;                        // 새로 생성된 파일명
                    thumbFileName    = Common.getRandomString() + FileExtension;                        // 새로 생성된 파일명
                    FileSize         = Long.toString(tempFile.length());                                // 파일 크기

                    // 이미지 파일 체크
                    if (checkImage(tempFile)) {
                        FileType = "I";
                        File thumbFolder = new File(thumbFullPath);
                        // int thumbnail_width = 220;      // 썸네일1 가로사이즈
                        // int thumbnail_height = 100;     // 썸네일1 세로사이즈
                        // int thumbnail2_rate = 80;       // 썸네일2 비율


                        // 저장할 폴더가 있으면 수정상태이므로 저장 폴더 삭제 후 재생성
                        if (fileIndex == 0) {
                            if (thumbFolder.exists()) {
                                deleteFolder(thumbFullPath);
                            }
                            thumbFolder.mkdirs();

                            if (!thumbFolder.canWrite()) {
                                thumbFolder.setWritable(true);
                            }
                        }
                        /*// 상세페이지 첨부파일에 표시되는 썸네일 (2021.10.25 이세희 제거)
                        createThumbnail(tempFullPath + originalFileName, thumbFullPath + "thumb_" + thumbFileName
                                            , thumbnail_width, thumbnail_height);
                        // 상세페이지 첨부파일에 표시되는 썸네일 이미지 클릭시 보이는 썸네일
                        createThumbnail(tempFullPath + originalFileName, thumbFullPath + "thumb2_" + thumbFileName
                                , thumbnail2_rate);

                         */
                    }else{
                        FileType = "F";
                    }

                    // 파일 이동
                    fileMove(tempFullPath + originalFileName , saveFullPath + storedFileName);

                    listMap.put("SEQ"              , SEQ);
                    listMap.put("originalFileName" , originalFileName);
                    listMap.put("FileName"         , storedFileName);
                    listMap.put("thumbFileName"    , thumbFileName);
                    listMap.put("FileSize"         , FileSize);
                    listMap.put("FileDir"          , FileDir);
                    listMap.put("FileType"         , FileType);

                    list.add(listMap);

//                    num++;
                }
                fileIndex++;
            }
        }
        return list;
    }

    /**
     * 임시폴더의 파일을 첨부파일 폴더로 이동 - 증빙자료양식 업로드
     * @param map
     * @param request
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> saveProofFormUpload(Map<String, Object> map, HttpServletRequest request , String saveDir) throws Exception{

        String originalFileName = null;
        String FileExtension    = null;
        String storedFileName   = null;
        String filePath         = tempPath;   // 임시폴더
        String FileDir          = null;
        String FileSize         = null;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // 클라이언트에서 전송된 파일 정보를 담아 반환할 객체
        Map<String, Object> listMap = null;

        // 게시물 idx
        String proofCode = (String) map.get("PROOF_CODE");  // 증빙자료 양식 코드 : 증빙자료 양식을 업로할 때.

        // 저장될 폴더 경로
        saveDir = baseDir + basePath + saveDir;

        // 임시파일 경로
        filePath = baseDir + basePath + filePath + request.getSession().getAttribute("strMyID") + "/";

        // DB에 저장될 경로명
        FileDir = saveDir.replace(baseDir , "");

        File dir = new File(filePath);
        File saveFolder = new File(saveDir);

        // 폴더가 있으면...
        if (dir.exists()) {
            File []fileList = dir.listFiles();

            // 저장경로가 없으면 폴더 생성
            if (saveFolder.exists() == false) {
                saveFolder.mkdirs();
            }

            // 저장경로에 쓰기 권한 부여
            if (saveFolder.canWrite() == false) {
                saveFolder.setWritable(true);
            }

            Integer num = 1;

            for(File tempFile : fileList) {
                if(tempFile.isFile()) {

                    listMap = new HashMap<String, Object>();

                    originalFileName = tempFile.getName();
                    FileExtension    = originalFileName.substring(originalFileName.lastIndexOf("."));   // 원본 파일의 확장자 추출
                    FileExtension    = FileExtension.toLowerCase();

                    // 증빙자료 양식 파일명 생성
                    storedFileName   = proofCode + "_" + Common.changeIntToStr(num, 3) + FileExtension;
                    FileSize         = Long.toString(tempFile.length());                                // 파일 크기

                    // 파일 이동 : 임시폴더 -> 저장폴더
                    fileMove(filePath + originalFileName , saveDir + storedFileName);

                    listMap.put("originalFileName" , originalFileName);
                    listMap.put("FileName", storedFileName);
                    listMap.put("FileSize", FileSize);
                    listMap.put("FileDir", FileDir);
                    listMap.put("proofCode", proofCode);

                    list.add(listMap);

                    num++;
                }
            }
        }
        return list;
    }
    
    /**
     * checkImage                   이미지 파일 여부 체크
     * @param files
     * @return
     * @throws Exception
     */
    private boolean checkImage(File files) throws Exception{
        try {
            String checkType = "";
            checkType = (String)new MimetypesFileTypeMap().getContentType(files).toLowerCase();
            //  jpg                                      jpg                               png                               png : windows에서 화면캡쳐 했을 때의 파일 속성
            //if (("image/jpeg").equals(checkType) || ("image/pjpeg").equals(checkType) || ("image/png").equals(checkType) || ("application/octet-stream").equals(checkType)) {
                                                            // 상단에있는 png : windows에서 화면캡쳐 했을 때의 파일 속성 은 한글문서나 다른문서가들어와도 똑같은 속성이라 제거.
            if (("image/jpeg").equals(checkType) || ("image/pjpeg").equals(checkType) || ("image/png").equals(checkType)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    /**
     * isPermittedMimeType          허용된 마임타입
     * @param mimeType
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean isPermittedMimeType(String mimeType) {

        String[] validMimeTypes = {"image" };

        for (String validMimeType : validMimeTypes) {
            if (StringUtils.startsWithIgnoreCase(mimeType, validMimeType)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * copyToTempFolder                     임시폴더로 파일복사 : 게시물 수정시 첨부파일 관리
     * @param attachList
     * @param map
     */
    @SuppressWarnings("rawtypes")
    public void copyToTempFolder(List<Map<String, Object>> attachList, Map<String, Object> map) {
        HashMap getHm = new HashMap();
        
        String originalName = null;     // 원본파일명
        String fileName     = null;     // 저장된 파일명
        String fileDIR      = null;     // 저장경로
        String tempDir     = baseDir + basePath + tempPath + map.get("strMyID") + "/" + map.get("strm") + "/";  // 임시폴더

        File file = new File(tempDir);

        try {
            // 임시폴더 삭제 후 재 생성
            if (file.exists()) {
                deleteFolder(tempDir);
            }

            file.mkdirs();

            if (file.canWrite() == false){
                file.setWritable(true);
            }

            for ( int i=0; i<attachList.size(); i++ ){
                getHm        = (HashMap)attachList.get(i);
                originalName = (String)getHm.get("ORI_FILE_NM");
                fileName     = (String)getHm.get("FILE_NM");
                fileDIR      = (String)getHm.get("FILE_DIR");
                fileDIR      = baseDir + fileDIR;

                //fileDIR  = request.getSession().getServletContext().getRealPath(fileDIR);   // 파일이 저장된 풀경로
                //System.out.println("----------------------------------- "+ (i+1) +". 원본파일명 : "+ originalName);
                //System.out.println("-----------------------------------    파일명 : "+ fileName);
                //System.out.println("-----------------------------------    경로 : "+ fileDIR );
                //System.out.println("-----------------------------------    임시폴더 : "+ tempDir );

                fileCopy(fileDIR + fileName , tempDir + originalName);
           }
        }catch(Exception e){
            log.error("ERROR");
        }
    }
    
    /**
     * fileCopy                 파일 복사
     * @param inFileName        원본파일
     *
     * @param outFileName       새로 저장될 파일
     */
    public void fileCopy(String inFileName, String outFileName) {
        FileInputStream inputStream   = null;
        FileOutputStream outputStream = null;
        try {
            inputStream   = new FileInputStream(inFileName);
            outputStream = new FileOutputStream(outFileName);

            FileChannel fcin  = inputStream.getChannel();
            FileChannel fcout = outputStream.getChannel();

            long size = fcin.size();
            fcin.transferTo(0, size, fcout);
              
            fcout.close();
            fcin.close();
              
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            //e.printStackTrace();
            log.error("ERROR : FILE COPY 에러");
        }finally {
             if (!Common.isEmpty(inputStream)) {
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     log.error("ERROR");
                 }
            }
             if (!Common.isEmpty(outputStream)) {
                 try {
                     outputStream.close();
                 } catch (IOException e) {
                     log.error("ERROR");
                 }
            }
        }
    }

    /**
     * fileMove                     파일 이동
     * @param inFileName
     * @param outFileName
     */
    public void fileMove(String inFileName, String outFileName) throws Exception {
            File file = new File(inFileName);
            // File file2 = new File(outFileName);// 이동

            if (file.exists()) {
                // boolean iii = file.renameTo(file2); // 변경
            }
    }
}
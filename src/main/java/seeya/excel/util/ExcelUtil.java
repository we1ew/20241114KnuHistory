package seeya.excel.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import seeya.common.util.Common;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;


@Component("excelUtil")  // @Componet 어노테이션을 이용하여 이 객체 관리를 스프링이 담당하도록 한다 
public class ExcelUtil {
    protected Log log = LogFactory.getLog(this.getClass());

    /**
     * 엑셀 파일 읽기
     * @param excelReadOption
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> readExcel(ExcelReadOption excelReadOption) throws Exception {
        // 엑셀파일을 읽어온다.
        Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getExcelFilePath());

        // 엑셀 파일에서 첫번째 시트를 가지고 온다.
        Sheet sheet = wb.getSheetAt(0);

        // sheet에서 유효한 행의 갯수를 가져온다.
        int numOfRows  = sheet.getPhysicalNumberOfRows();
        int numOfCells = 0;

        Row row = null;
        Cell cell = null;
        String cellName = "";

        // 각 row마다 값을 저장할 맵 객체.
        Map<String, Object> map = null;

        // 각 Row를 리스트에 담는다.
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

        // Row 만큼 반복
        for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
            // 워크북에서 가져온 시으테서 rowIndex에 해당하는 Row를 가지고 온다.
            row = sheet.getRow(rowIndex);

            // 맵초기화
            map = new HashMap<String,Object>();

            if (!Common.isEmpty(row)) {
                // 가져온 row에 있는 Cell 갯수 파악
                numOfCells = row.getLastCellNum();

                // cell 수 만큼 반복
                for (int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
                    cell = row.getCell(cellIndex);                                  // row에서 cellIndex에 해당하는 Cell
                    cellName = ExcelCellRef.getName(cell, cellIndex);               // cell의 이름을 가져온다.

                    // map 객체의 Cell이름을 Key로 하여 데이터를 담는다.
                    map.put(cellName, ExcelCellRef.getValue(cell));
                }// for Cell

                result.add(map);
            } // if
        } // for Row

        return result;
    }

    /**
     * 로그 엑셀 저장
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String saveExcelLog(Map<String, Object> map) throws Exception {
        Log log = LogFactory.getLog(ExcelUtil.class);
        // Workbook 생성
        Workbook xlsxWb = new XSSFWorkbook(); // Excel 2007 이상

        // *** Style--------------------------------------------------
        // Cell 스타일 생성
        CellStyle cellStyle = xlsxWb.createCellStyle();

        // 줄 바꿈
        cellStyle.setWrapText(true);

        Row row = null;
        Cell cell = null;
        //----------------------------------------------------------

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);                     //스타일인스턴스의 속성 ?V팅
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);        //셀에 색깔 채우기
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);              //테두리 설정
        cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyleCenter = xlsxWb.createCellStyle();
        cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle0 = xlsxWb.createCellStyle();
        cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        //얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle1 = xlsxWb.createCellStyle();
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        // *** Sheet-------------------------------------------------
        // Sheet 생성
        Sheet sheet1 = xlsxWb.createSheet("LOG");

        Integer rowNum = 0;
        String[] arrHead  = {"Pattern"  , "State" , "Target Name" , "Target ID" , "Writer"   , "Writer ID" , "Writer IP" , "Created" };
        String[] arrField = {"LOG_TEXT" , "STATE" , "TAR_NAME"    , "TARGET_ID" , "REG_NAME" , "REG_ID"    , "CON_IP"    , "REG_DATE" };
        int[] arrColSize  = {15         , 15      , 25            , 25          , 25         , 25          , 15          , 25 };

        // 문서 타이틀
        row = sheet1.createRow(rowNum);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));    // 가로병합
        cell = row.createCell(0);
        cell.setCellValue("Audit Log");
        cell.setCellStyle(cellStyleCenter);

        Font font = xlsxWb.createFont();                                    //폰트 조정 인스턴스 생성
        font.setBoldweight((short)700);
        cellStyle.setFont(font);

        rowNum++;

        // 문서 타이틀 : 엑셀파일 생성일자
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createDate = dateFormat.format(d);

        row = sheet1.createRow(rowNum);
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, arrHead.length - 1));    // 가로병합(
        cell = row.createCell(0);
        cell.setCellValue(createDate);
        cell.setCellStyle(cellStyleCenter);

        rowNum++;

        // 헤더 라인 생성
        row = sheet1.createRow(rowNum);

        // 셀별 폭 정하기
        for (int i=0; i<arrColSize.length; i++){
            sheet1.setColumnWidth(i, arrColSize[i]*256);
        }

        // 헤더 그리기
        for ( int i=0; i<arrHead.length; i++ ) {
            cell = row.createCell(i);
            cell.setCellValue(arrHead[i]);
            cell.setCellStyle(cellStyle);
        }

        rowNum++;

        List<Map<String,Object>> list = (List) map.get("list");

        // 데이터 출력
        for ( int i=0; i<list.size(); i++ ) {

            row = sheet1.createRow(i + rowNum);

            for (int c=0; c<arrField.length; c++) {
                cell = row.createCell(c);

                cell.setCellValue(String.valueOf(list.get(i).get(arrField[c])));
                cell.setCellStyle(cellStyleCenter);
            }
        }

        String excelName = (String) map.get("excelName");
        String excelPath = (String) map.get("excelPath");
        FileOutputStream fileOut = null;
        // excel 파일 저장
        try {

            File file = new File(excelPath);

            // 저장경로가 없으면 폴더 생성
            if (file.exists() == false) {
                file.mkdirs();
            }

            File xlsxFile = new File(excelPath + excelName);
            fileOut = new FileOutputStream(xlsxFile);

            xlsxWb.write(fileOut);
            xlsxWb.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            log.error("ERROR :  로그 엑셀 저장 실패");
            excelName = "ERROR";
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("ERROR :  로그 엑셀 저장 실패");
            excelName = "ERROR";
        }finally {
            if (!Common.isEmpty(fileOut)) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    log.error("ERROR");
                }
            }
        }
        return excelName; // 저장된 파일명 리턴
    }


    /**
     *  신청서 내역 엑셀저장 - 보안서비스 - 보안서비스현황 - 보안신청서 관리대장
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String saveExcelAFormManager(Map<String, Object> map) throws Exception {
        Log log = LogFactory.getLog(ExcelUtil.class);
        // Workbook 생성
        Workbook xlsxWb = new XSSFWorkbook(); // Excel 2007 이상

        // *** Style--------------------------------------------------
        // Cell 스타일 생성
        CellStyle cellStyle = xlsxWb.createCellStyle();

        // 줄 바꿈
        cellStyle.setWrapText(true);

        Row row = null;
        Cell cell = null;
        //----------------------------------------------------------

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);                     //스타일인스턴스의 속성 ?V팅
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);        //셀에 색깔 채우기
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);              //테두리 설정
        cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyleCenter = xlsxWb.createCellStyle();
        cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle0 = xlsxWb.createCellStyle();
        cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        //얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle1 = xlsxWb.createCellStyle();
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        String[] arrHead;
        String[] arrField;
        String[] arrColSize;

        String aformCodeM;
        String aformCodeD;
        String aformName;
        String strHead;
        String strField;
        String strSize;

        Integer rowNum = 0;

        List<Map<String,Object>> headList = (List) map.get("headList");
        for (int i=0, aformLen = headList.size(); i < aformLen; i++) {
            strHead  = (String) headList.get(i).get("HEAD");
            strField = (String) headList.get(i).get("FIELD");
            strSize  = (String) headList.get(i).get("COLSIZE");

            aformCodeM = (String) headList.get(i).get("AFORM_CODE_KEY");
            aformName  = (String) headList.get(i).get("AFORM_NAME");

            aformName = aformName.replaceAll("/" , " ");

            arrHead    = strHead.split(",");
            arrField   = strField.split(",");
            arrColSize = strSize.split(",");

            // *** Sheet-------------------------------------------------
            // Sheet 생성
            Sheet sheet1 = xlsxWb.createSheet(aformName);

            rowNum = 0;

            // 문서 타이틀
            row = sheet1.createRow(rowNum);
            sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));    // 가로병합
            cell = row.createCell(0);
            cell.setCellValue(aformName);
            cell.setCellStyle(cellStyleCenter);

            Font font = xlsxWb.createFont();                                    //폰트 조정 인스턴스 생성
            font.setBoldweight((short)700);
            cellStyle.setFont(font);

            rowNum++;

            // 문서 타이틀 : 엑셀파일 생성일자
            Date d = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String createDate = dateFormat.format(d);

            row = sheet1.createRow(rowNum);
            sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, arrHead.length - 1));    // 가로병합(
            cell = row.createCell(0);
            cell.setCellValue(createDate);
            cell.setCellStyle(cellStyleCenter);

            rowNum++;

            // 헤더 라인 생성
            row = sheet1.createRow(rowNum);

            // 셀별 폭 정하기
            for (int ii=0; ii<arrColSize.length; ii++){
                sheet1.setColumnWidth(ii, Integer.parseInt(arrColSize[ii])*256);
            }

            // 헤더 그리기
            for ( int ii=0; ii<arrHead.length; ii++ ) {
                cell = row.createCell(ii);
                cell.setCellValue(arrHead[ii]);
                cell.setCellStyle(cellStyle);
            }

            rowNum++;

            List<Map<String,Object>> dataList = (List) map.get("dataList");

            // 데이터 출력
            for ( int ii=0, size=dataList.size(); ii<size; ii++ ) {

                aformCodeD = (String) dataList.get(ii).get("AFORM_CODE");

                if (aformCodeD.equals(aformCodeM)) {
                    row = sheet1.createRow(rowNum);

                    for (int c = 0; c < arrField.length; c++) {
                        cell = row.createCell(c);

                        cell.setCellValue(Common.getCheckString(String.valueOf(dataList.get(ii).get(arrField[c])) , "-"));
                    }

                    rowNum++;
                } // if
            } // for dataList
        } // for headList
        // ********************************************************************************************************

        String excelName = (String) map.get("excelName");
        String excelPath = (String) map.get("excelPath");
        FileOutputStream fileOut = null;

        // excel 파일 저장
        try {

            File file = new File(excelPath);

            // 저장경로가 없으면 폴더 생성
            if (file.exists() == false) {
                file.mkdirs();
            }

            File xlsxFile = new File(excelPath + excelName);
            fileOut = new FileOutputStream(xlsxFile);
            xlsxWb.write(fileOut);
            xlsxWb.close();
            
        } catch (FileNotFoundException e) {
            log.error("ERROR : 엑셀파일 다운로드 에러");
            //e.printStackTrace();
            excelName = "ERROR";
        } catch (IOException e) {
            log.error("ERROR : 엑셀파일 다운로드 에러");
            //e.printStackTrace();
            excelName = "ERROR";
        }finally {
            if (!Common.isEmpty(fileOut)) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    log.error("ERROR");
                }
            }
        }

        return excelName; // 저장된 파일명 리턴
    }



    /**
     * @methodName : ExcelDownload
     * @description : 요일별 결과 엑셀 다운로드
     * @Modification
     * @ 수정일      수정자    수정내용
     * @ ----------  -------   --------
     * @ 2021.09.07  전형상    최초생성
     *
     * @return ModelAndView
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String ExcelDownload(Map<String, Object> map) throws Exception {
        Log log = LogFactory.getLog(ExcelUtil.class);
        // Workbook 생성
        Workbook xlsxWb = new XSSFWorkbook(); // Excel 2007 이상

        // *** Style--------------------------------------------------
        // Cell 스타일 생성
        CellStyle cellStyle = xlsxWb.createCellStyle();

        // 줄 바꿈
        cellStyle.setWrapText(true);
        Row row = null;
        Cell cell = null;
        //----------------------------------------------------------

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);                           //스타일인스턴스의 속성 ?V팅
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);        //셀에 색깔 채우기
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);                        //테두리 설정
        cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyleCenter = xlsxWb.createCellStyle();
        cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        CellStyle cellStyleLeft = xlsxWb.createCellStyle();
        cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        CellStyle cellStyleRight = xlsxWb.createCellStyle();
        cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        //가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle0 = xlsxWb.createCellStyle();
        cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        //얇은 테두리를 위한 스타일 인스턴스 생성
        CellStyle cellStyle1 = xlsxWb.createCellStyle();
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        // *** Sheet-------------------------------------------------
        // Sheet 생성
        String ExcelServerCode = (String) map.get("ExcelServerCode");
        // Sheet 명 설정
        Sheet sheet1 = xlsxWb.createSheet(ExcelServerCode);
        Integer rowNum = 0;

        // 데이터가 담긴 리스트 : 맵에서 리스트 가져오기(쿼리 결과)
        List<Map<String,Object>> list = (List) map.get("list");

        /*
          1. list.get(0)을 맵에 담는다
          2. 맵에서 키값만 추출한다.
          3. 키값에서 헤드, 정렬, 크기를 구분한다.
         */
        // 1. 쿼리결과 목록의 첫번째 행만 추출한다. : 키값이 필드명, 키는 타이틀, 정렬, 셀 크기로 구성되어 있음. 구분자 :
        Map<String,Object> headMap = new LinkedHashMap<>();
        headMap = list.get(0);

        int size = headMap.size();            // 맵의 요소 개수 : 추출된 첫번째 행의 필드 개수
        int x    = 0;                         // arrHead, arrColSize에서 사용. 헤더, 크기 배열의 키로 사용

        String[] arrHead  = new String[size]; // 타이틀이 담길 배열
        String[] arrAlign = new String[size]; // 정렬이 담길 배열
        int[] arrColSize  = new int[size];    // 셀 크기가 담길 배열

        // 2. 맵에서 키값 가져오기
        for(String key : headMap.keySet()){
            String[] arrValue = key.split(":");      // arrValue = {타이틀, 정렬, 셀크기}
            arrHead[x]    = arrValue[0];                   // 타이틀
            arrAlign[x]   = arrValue[1];                   // 정렬
            arrColSize[x] = Integer.parseInt(arrValue[2]); // 셀 크기
            x++;
        }

        // 문서 타이틀
        row = sheet1.createRow(rowNum);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));    // 가로병합
        cell = row.createCell(0);
        cell.setCellValue((String)map.get("title"));
        cell.setCellStyle(cellStyleCenter);

        Font font = xlsxWb.createFont();                                    //폰트 조정 인스턴스 생성
        font.setBoldweight((short)700);
        cellStyle.setFont(font);

        rowNum++;

        // 문서 타이틀 : 엑셀파일 생성일자
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createDate = dateFormat.format(d);

        row = sheet1.createRow(rowNum);
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, arrHead.length - 1));    // 가로병합(
        cell = row.createCell(0);
        cell.setCellValue(createDate);
        cell.setCellStyle(cellStyleCenter);

        rowNum++;

        // 헤더 라인 생성
        row = sheet1.createRow(rowNum);

        // 셀별 폭 정하기
        for (int i=0; i<arrColSize.length; i++){
            sheet1.setColumnWidth(i, arrColSize[i]*256);
        }

        // 헤더 그리기
        for ( int i=0; i<arrHead.length; i++ ) {
            cell = row.createCell(i);
            cell.setCellValue(arrHead[i]);
            cell.setCellStyle(cellStyle);
        }

        rowNum++;

        // 데이터 출력
        for ( int i=0; i<list.size(); i++ ) {
            Map<String,Object> dataMap = new LinkedHashMap<>();

            dataMap = list.get(i);
            row = sheet1.createRow(i + rowNum);

            List<Object> dataList = new ArrayList<Object>(dataMap.values());

            int ii = 0;
            // 필드 수 만큼 반복
            for(Object data : dataList){
                cell = row.createCell(ii);

                // 셀에 값 입력
                cell.setCellValue(String.valueOf(data));

                // 정렬
                if ("C".equals(arrAlign[ii])) {
                    cell.setCellStyle(cellStyleCenter);
                }else if ("L".equals(arrAlign[ii])) {
                    cell.setCellStyle(cellStyleLeft);
                }else if ("R".equals(arrAlign[ii])) {
                    cell.setCellStyle(cellStyleRight);
                }else{
                    cell.setCellStyle(cellStyleCenter);
                }

                ii++;
            }
        }

        String excelName = (String) map.get("excelName");
        String excelPath = (String) map.get("excelPath");
        FileOutputStream fileOut = null;
        // excel 파일 저장
        try {

            File file = new File(excelPath);

            // 저장경로가 없으면 폴더 생성
            if (file.exists() == false) {
                file.mkdirs();
            }

            File xlsxFile = new File(excelPath + excelName);
            fileOut = new FileOutputStream(xlsxFile);
            xlsxWb.write(fileOut);
            xlsxWb.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            log.error("ERROR : 엑셀파일 다운로드 에러");
            excelName = "ERROR";
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("ERROR : 엑셀파일 다운로드 에러");
            excelName = "ERROR";
        }finally {
            if (!Common.isEmpty(fileOut)) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    log.error("ERROR");
                }
            }
        }
        return excelName; // 저장된 파일명 리턴
    }
}

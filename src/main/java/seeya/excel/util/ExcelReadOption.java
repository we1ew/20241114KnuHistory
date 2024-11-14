package seeya.excel.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @className : ExcelReadOption
 * @description : ExcelReadOption 클래스
 * 
 * @author 정보보호 이병덕
 * @since 2017.10.24
 * @version 1.0
 * @see
 * 
 * - Copyright (C) by HIRA All right reserved.
 */
public class ExcelReadOption {
    private String excelFilePath;       // 읽을 엑셀파일 경로
    private List<String> outputColumns; // 추출할 LAST_LGN_DTIME
    private int startRow;               // 추출을 시작할 행 번호

    public String getExcelFilePath() {
        return excelFilePath;
    }

    public void setExcelFilePath(String filePath) {
        this.excelFilePath = filePath;
    }

    public List<String> getOutputColumns(){
        List<String> temp = new ArrayList<String>();
        temp.addAll(outputColumns);

        return temp;
    }

    public void setOutputColumns(List<String> outputColumns) {
        List<String> temp = new ArrayList<String>();
        temp.addAll(outputColumns);

        this.outputColumns = temp;
    }

    public int getStartRow(){
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}

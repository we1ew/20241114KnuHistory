package seeya.excel.util;

import seeya.common.util.Common;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

/**
 * @className : ExcelCellRef
 * @description : ExcelCellRef 클래스
 * 
 * @author 정보보호 이병덕
 * @since 2017.10.24
 * @version 1.0
 * @see
 * 
 * -Copyright (C) by HIRA All right reserved.
 */
public class ExcelCellRef {
    /**
     * Cell에 해당하는 Column Name 가져온다.
     * null이면 cellIndex의 값으로 컬럼명을 가져온다.
     * @param cell
     * @param cellIndex
     * @return
     */
    public static String getName(Cell cell, int cellIndex) {
        int cellNum = 0;
        if (Common.isEmpty(cell)) {
            cellNum = cellIndex;
        }else{
            cellNum = cell.getColumnIndex();
        }

        return CellReference.convertNumToColString(cellNum);
    }

    /**
     * cell의 값을 가져온다
     * @param cell
     * @return
     */
    public static String getValue(Cell cell) {
        String value = "";

        if (Common.isEmpty(cell)) {
            value = "";
        }else{
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA : value = cell.getCellFormula();                    break;
                case Cell.CELL_TYPE_NUMERIC : value = (int) cell.getNumericCellValue() + "";    break;
                case Cell.CELL_TYPE_STRING  : value = cell.getStringCellValue();                break;
                case Cell.CELL_TYPE_BOOLEAN : value = cell.getBooleanCellValue() + "";          break;
                case Cell.CELL_TYPE_ERROR   : value = cell.getErrorCellValue() + "";            break;
                case Cell.CELL_TYPE_BLANK   : value = "";                                       break;
                default                     : value = cell.getStringCellValue();                break;
            }
        }

        return value;
    }
}

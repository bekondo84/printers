package cm.pak.canon.services;

import cm.pak.canon.beans.PrintUsageData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelService  <T extends Object>{

     /**
      *
      * @param headers
      * @param headerStyle
      * @param datas
      */
     public void pullExcellRow(String[] headers, final Sheet sheet, CellStyle headerStyle, List<PrintUsageData> datas) throws NoSuchFieldException, IllegalAccessException;
     /**
      *
      * @param header
      * @param data
      * @return
      */
    public default Workbook createExcel(final String[] header, final List<T> data) {
          final Workbook workbook = new XSSFWorkbook();
          final Sheet sheet = workbook.createSheet();
          createHeader(header, sheet, workbook);
          return workbook;
     }

     /**
      *
      * @param headers
      * @param sheet
      * @param workbook
      */
      default void createHeader(final String[] headers, final Sheet sheet, final Workbook workbook) {
          CellStyle style = workbook.createCellStyle();
          style.setWrapText(true);
          Row row = sheet.createRow(headers.length);

          for(int i=0; i<headers.length; i++){
               Cell cell = row.createCell(i);
               cell.setCellValue(headers[i]);
               cell.setCellStyle(style);
          }
     }

     /**
      *
      * @param headers
      * @param workbook
      */
     default void createBody(String[] headers, final Sheet sheet, final Workbook workbook, final List<PrintUsageData> datas) throws NoSuchFieldException, IllegalAccessException {
          sheet.setColumnWidth(0, 6000);
          sheet.setColumnWidth(1, 4000);

          final Row header =  sheet.createRow(0);
          CellStyle headerStyle = workbook.createCellStyle();
          headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
          headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

          XSSFFont font = ((XSSFWorkbook) workbook).createFont();
          font.setFontName("Arial");
          font.setFontHeightInPoints((short) 16);
          font.setBold(true);
          headerStyle.setFont(font);

          pullExcellRow(headers, sheet, headerStyle, datas);
     }

}

package cm.pak.canon.services;

import cm.pak.canon.beans.PrintUsageData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelService  {

     /**
      *
      * @param header
      * @param data
      * @return
      */
    public default Workbook createExcel(final String[] header, final List data, final ExcelRowService rowService) throws NoSuchFieldException, IllegalAccessException {
          final Workbook workbook = new XSSFWorkbook();
          final Sheet sheet = workbook.createSheet();
          createHeader(header, sheet, workbook);
          createBody(header, sheet, workbook, data, rowService);
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
          XSSFFont font = ((XSSFWorkbook) workbook).createFont();
          font.setFontName("Arial");
          font.setFontHeightInPoints((short) 12);
          font.setBold(true);
          style.setFont(font);
          style.setWrapText(true);
          Row row = sheet.createRow(0);
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
     default void createBody(String[] headers, final Sheet sheet, final Workbook workbook, final List datas, final ExcelRowService rowService) throws NoSuchFieldException, IllegalAccessException {
          sheet.setColumnWidth(0, 6000);
          sheet.setColumnWidth(1, 4000);

          CellStyle headerStyle = workbook.createCellStyle();
          headerStyle.setWrapText(true);
          //headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
          //headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

          XSSFFont font = ((XSSFWorkbook) workbook).createFont();
          font.setFontName("Arial");
          font.setFontHeightInPoints((short) 12);
          font.setBold(false);
          headerStyle.setFont(font);

          rowService.createRows(headers, sheet, headerStyle, datas);
     }

}

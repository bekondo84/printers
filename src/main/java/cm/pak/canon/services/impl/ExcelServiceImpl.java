package cm.pak.canon.services.impl;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.services.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ExcelServiceImpl implements ExcelService<PrintUsageData> {

    @Override
    public void pullExcellRow(String[] headers, Sheet sheet, CellStyle headerStyle, List<PrintUsageData> datas) throws NoSuchFieldException, IllegalAccessException {
        int rowIndex = 1 ;
        final  Row row = sheet.getRow(rowIndex);
        for (PrintUsageData data : datas) {
             for (int col=0; col < headers.length; col++) {
                final Cell cell = row.createCell(col);
               final Field field = data.getClass().getDeclaredField(headers[col]);
               field.setAccessible(true);
               final Object value = field.get(headers[0]);

               if (value instanceof String) {

               }
            }
        }
    }
}

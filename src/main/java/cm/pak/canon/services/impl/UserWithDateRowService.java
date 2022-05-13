package cm.pak.canon.services.impl;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.services.ExcelRowService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserWithDateRowService implements ExcelRowService {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Override
    public void createRows(String[] headers, Sheet sheet, CellStyle headerStyle, List datas) {
        int rowIndex = 1 ;
        for (Object data: datas) {
            final Row row = sheet.createRow(rowIndex);
            final PrintUsageData printData = (PrintUsageData) data;
           //Col ID
            Cell cell = row.createCell(0);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(printData.getUser().getId());
            //Nom
            cell = row.createCell(1);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(printData.getUser().getName());
            //Date
            cell = row.createCell(2);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(printData.getDate());
            //QTY
            cell = row.createCell(3);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(printData.getOutput());
            rowIndex++;
        }
    }
}

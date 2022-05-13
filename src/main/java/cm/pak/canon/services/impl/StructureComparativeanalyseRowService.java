package cm.pak.canon.services.impl;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.services.ExcelRowService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StructureComparativeanalyseRowService implements ExcelRowService {
    @Override
    public void createRows(String[] headers, Sheet sheet, CellStyle headerStyle, List datas) {
           int rowIndex = 1 ;

           for (Object value : datas) {
               final Row row = sheet.createRow(rowIndex);
               final AnalyseComparativeData data = (AnalyseComparativeData) value;
               //Col ID
               Cell cell = row.createCell(0);
               cell.setCellStyle(headerStyle);
               cell.setCellValue(data.getStructure().getCode());

               for (int cellIndex = 0; cellIndex < data.getLignes().size(); cellIndex++){
                   cell = row.createCell(cellIndex + 1);
                   cell.setCellStyle(headerStyle);
                   cell.setCellValue(data.getLignes().get(cellIndex).getQuantity());
               }
               rowIndex++;
           }
    }
}

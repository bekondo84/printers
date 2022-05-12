package cm.pak.canon.services;

import cm.pak.canon.beans.PrintUsageData;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelRowService {

    void createRows(String[] headers, final Sheet sheet, CellStyle headerStyle, List datas);
}

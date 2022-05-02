package cm.pak.canon.services;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.models.PrintUsage;

import java.text.ParseException;
import java.util.List;

public interface PrintUsageService {

    List<PrintUsage> getPrinterForUsers(String from, String to) throws ParseException;

    List<PrintUsage> getPrinterForPrinters(String from, String to) throws ParseException;
}

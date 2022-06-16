package cm.pak.canon.facades;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.beans.SearchBean;
import cm.pak.canon.models.PrintUsage;

import java.text.ParseException;
import java.util.List;

public interface PrintUsageFacade {

    List<PrintUsageData> getPrinterForUsers(String from, String to) throws ParseException;

    List<PrintUsageData> getPrinterForPrinters(String from, String to) throws ParseException;

    List<PrintUsageData> printGroupbyAffectation(final String from, final String to) throws ParseException;

    List<PrintUsageData> printGroupbyStructure(final String from, final String to) throws ParseException;

    List<PrintUsageData> getPrintUsageForStructureForDates(final SearchBean search) throws ParseException;

    List<PrintUsageData> getUserPrintUsageResume(final SearchBean search) throws ParseException;
}

package cm.pak.canon.facades;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.models.PrintUsage;

import java.text.ParseException;
import java.util.List;

public interface PrintUsageFacade {

    List<PrintUsageData> getPrinterForUsers(String from, String to) throws ParseException;

    List<PrintUsageData> getPrinterForPrinters(String from, String to) throws ParseException;

    List<PrintUsageData> printGroupbyAffectation(final String from, final String to) throws ParseException;

    List<PrintUsageData> printGroupbyStructure(final String from, final String to) throws ParseException;

    List<PrintUsageData> getPrintUsageForStructureForDates(final String from, final String to, final String structure) throws ParseException;

    List<PrintUsageData> getUserPrintUsageResume(final String from, final String to, final String user) throws ParseException;
}
package cm.pak.canon.services;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.models.PrintUsage;
import org.springframework.data.jpa.repository.Query;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface PrintUsageService {

    List<PrintUsage> getPrinterForUsers(String from, String to) throws ParseException;

    List<PrintUsage> getPrinterForPrinters(String from, String to) throws ParseException;

    List<PrintUsage> getPrintUsageForStructureForDates(final String from, final String to, final String structure) throws ParseException;

    List<PrintUsage> getUserPrintUsageResume(final String from, final String to, final String user) throws ParseException;
}

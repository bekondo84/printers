package cm.pak.canon.services;

import cm.pak.canon.beans.ResultBean;
import cm.pak.canon.models.PrintUsage;

import java.text.ParseException;
import java.util.List;

public interface PrintUsageService {

    List<ResultBean> getPrinterForUsers(String from, String to) throws ParseException;

    List<ResultBean> getPrinterForPrinters(String from, String to) throws ParseException;
}

package cm.pak.canon.services.impl;

import cm.pak.canon.beans.ResultBean;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.repertories.PrintUsageRepository;
import cm.pak.canon.services.PrintUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrintUsageServiceImpl implements PrintUsageService {

    @Autowired
    private PrintUsageRepository repository;
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<ResultBean> getPrinterForUsers(String from, String to) throws ParseException {

        final List<PrintUsage> datas = new ArrayList<>() ;

        if (from == null || from.trim().isEmpty()
        || to == null || to.trim().isEmpty()) {
            repository.findAll()
                    .forEach(d -> datas.add(d));
        } else {
            final Date fromD = SDF.parse(from);
            final Date toD = SDF.parse(to);
            datas.addAll(repository.getPrinterUsageForDates(fromD, toD));
        }
        final List<ResultBean> values = new ArrayList<>();
        Map<String, Integer> resumes = datas.stream()
                .filter(d -> Objects.nonNull(d.getOutputPages()))
                .collect(Collectors.groupingBy(PrintUsage::getUserName, Collectors.summingInt(PrintUsage::getOutputPages)));
        resumes.keySet().forEach(key -> {
            ResultBean bean = new ResultBean();
            bean.setOwner(key);bean.setOutput(resumes.get(key));
            values.add(bean);
        });
        return values;
    }

    @Override
    public List<ResultBean> getPrinterForPrinters(String from, String to) throws ParseException {
        final List<PrintUsage> datas = new ArrayList<>() ;

        if (from == null || from.trim().isEmpty()
                || to == null || to.trim().isEmpty()) {
            repository.findAll()
                    .forEach(d -> datas.add(d));
        } else {
            final Date fromD = SDF.parse(from);
            final Date toD = SDF.parse(to);
            datas.addAll(repository.getPrinterUsageForDates(fromD, toD));
        }
        final List<ResultBean> values = new ArrayList<>();
        Map<Imprimante, Integer> resumes = datas.stream()
                .filter(d -> Objects.nonNull(d.getOutputPages()))
                .collect(Collectors.groupingBy(PrintUsage::getPrinter, Collectors.summingInt(PrintUsage::getOutputPages)));
        resumes.keySet().forEach(key -> {
            ResultBean bean = new ResultBean();
            bean.setOwner(key.getIpAdress());bean.setOutput(resumes.get(key));
            values.add(bean);
        });
        return values;
    }
}

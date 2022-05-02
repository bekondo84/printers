package cm.pak.canon.services.impl;

import cm.pak.canon.beans.PrintUsageData;
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

    public static final String FIRST_DAY_TIME = " 00:00:01";
    public static final String LAST_DAY_TIME = " 23:59:59";
    @Autowired
    private PrintUsageRepository repository;
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public List<PrintUsage> getPrinterForUsers(String from, String to) throws ParseException {

        final List<PrintUsage> datas = new ArrayList<>() ;

        if (from == null || from.trim().isEmpty()
        || to == null || to.trim().isEmpty()) {
            repository.findAll()
                    .forEach(d -> datas.add(d));
        } else {
            final Date fromD = SDF.parse(from.concat(FIRST_DAY_TIME));
            final Date toD = SDF.parse(to.concat(LAST_DAY_TIME));
            datas.addAll(repository.getPrinterUsageForDates(fromD, toD));
        }
        return datas;
    }

    @Override
    public List<PrintUsage> getPrinterForPrinters(String from, String to) throws ParseException {
        final List<PrintUsage> datas = new ArrayList<>() ;

        if (from == null || from.trim().isEmpty()
                || to == null || to.trim().isEmpty()) {
            repository.findAll()
                    .forEach(d -> datas.add(d));
        } else {
            final Date fromD = SDF.parse(from.concat(FIRST_DAY_TIME));
            final Date toD = SDF.parse(to.concat(LAST_DAY_TIME));
            datas.addAll(repository.getPrinterUsageForDates(fromD, toD));
        }
        return datas;
    }

    private List<PrintUsageData> getPrintUsageData(List<PrintUsage> datas) {
        final List<PrintUsageData> values = new ArrayList<>();
        Map<Imprimante, Integer> resumes = datas.stream()
                .filter(d -> Objects.nonNull(d.getOutputPages()))
                .collect(Collectors.groupingBy(PrintUsage::getPrinter, Collectors.summingInt(PrintUsage::getOutputPages)));
        resumes.keySet().forEach(key -> {
            PrintUsageData bean = new PrintUsageData();
            //bean.setOwner(key.getIpAdress());bean.setOutput(resumes.get(key));
            values.add(bean);
        });
        values.sort(new Comparator<PrintUsageData>() {
            @Override
            public int compare(PrintUsageData o1, PrintUsageData o2) {
                return o2.getOutput()==o1.getOutput() ? 0 : (o2.getOutput() > o1.getOutput() ? 1 : -1);
            }
        });
        return values;
    }
}

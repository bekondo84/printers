package cm.pak.canon.facades.impl;

import cm.pak.canon.DateUtils;
import cm.pak.canon.beans.*;
import cm.pak.canon.facades.PrintUsageFacade;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.populator.impl.PrintUsagePopulator;
import cm.pak.canon.services.PrintUsageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PrintUsageFacadeImpl implements PrintUsageFacade {

    private static final Logger LOG = LoggerFactory.getLogger(PrintUsageFacadeImpl.class);

    @Autowired
    private PrintUsageService printUsageService;

    @Autowired
    private PrintUsagePopulator populator ;

    @Override
    public List<PrintUsageData> getPrintUsageForStructureForDates(final SearchBean search) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        final Map<Object, Integer> userGroup = printUsageService.getPrintUsageForStructureForDates(search.getFrom(), search.getTo(), search.getCodeStructure())
                .stream().map(data ->populator.populate(data))
                .collect(Collectors.groupingBy(pr ->{
                    if (search.getGroupBy() == 1) {
                        return pr.getUser();
                    }
                    return pr.getService();
                }, Collectors.summingInt(PrintUsageData::getOutput)));
        userGroup.keySet()
                .forEach(key ->{
                    final PrintUsageData data = new PrintUsageData();

                    if (key instanceof UserData) {
                        data.setUser((UserData) key);
                    } else if (key instanceof StructureData) {
                        data.setService((StructureData) key);
                    }
                    data.setOutput(userGroup.get(key));
                    datas.add(data);
                });
        sortDatas(datas);
        return datas;
    }

    @Override
    public List<PrintUsageData> getUserPrintUsageResume(final SearchBean search) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final List<PrintUsageData> datas = new ArrayList<>();
        Map<String, List<PrintUsageData>> groupingByDays = new HashMap<>();
        final List<PrintUsage> printUsages = printUsageService.getUserPrintUsageResume(search.getFrom(), search.getTo(), search.getUserId());
        if (search.getCycle().equalsIgnoreCase("1")) {
            groupingByDays = printUsages
                    .stream()
                    .filter(pr -> Objects.nonNull(pr.getStartTime()))
                    .map(pr -> populator.populate(pr))
                    .collect(Collectors.groupingBy(p -> p.getDate()));
        } else if (search.getCycle().equalsIgnoreCase("3")) {
            groupingByDays = printUsages
                    .stream()
                    .filter(pr -> Objects.nonNull(pr.getStartTime()))
                    .map(pr -> populator.populate(pr))
                    .collect(Collectors.groupingBy(p -> {
                       return p.getDate().substring(3, p.getDate().length()-1);
                    }));
        } else if(search.getCycle().equalsIgnoreCase("4")) {
            groupingByDays = printUsages
                    .stream()
                    .filter(pr -> Objects.nonNull(pr.getStartTime()))
                    .map(pr -> populator.populate(pr))
                    .collect(Collectors.groupingBy(p -> {
                        //LOG.info(p.getDate().substring(6, p.getDate().length()));
                        return p.getDate().substring(6, p.getDate().length());
                    }));
        } else {
            final List<Week> weeks = DateUtils.getWeeksBetween(search.getFrom(), search.getTo());
            groupingByDays = printUsages
                    .stream()
                    .filter(printUsage -> Objects.nonNull(printUsage.getStartTime()))
                    .map(pr -> populator.populate(pr))
                    .collect(Collectors.groupingBy(p ->{
                        final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            final Date date = sdf2.parse(p.getDate()) ;
                            for (Week week : weeks) {
                                if (week.getFrom().compareTo(date)*week.getTo().compareTo(date) <=0) {
                                    return week.getCode();
                                }
                            }
                        } catch (ParseException e) {
                            // e.printStackTrace();
                           // LOG.info(String.format("Date : %s : %s", p.getDate(), weeks));
                            return null;
                        }
                       // LOG.info(String.format("Date : %s : %s", p.getDate(), weeks));
                        return null;
                    }));

        }

        for (String key : groupingByDays.keySet()) {
            final PrintUsageData printUsageData = groupingByDays.get(key).get(0);
            printUsageData.setDate(key);
            printUsageData.setOutput(groupingByDays.get(key).stream().collect(Collectors.summingInt(PrintUsageData::getOutput)));
            datas.add(printUsageData);
        }
        sortDatas(datas);
        return datas;
    }

    @Override
    public List<PrintUsageData> getPrinterForUsers(String from, String to) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        final Map<UserData, Integer> userGroup = printUsageService.getPrinterForUsers(from, to)
                .stream().map(data ->populator.populate(data))
                .collect(Collectors.groupingBy(PrintUsageData::getUser, Collectors.summingInt(PrintUsageData::getOutput)));
        userGroup.keySet()
                .forEach(key ->{
                    final PrintUsageData data = new PrintUsageData();
                    data.setUser(key);
                    data.setOutput(userGroup.get(key));
                    datas.add(data);
                });
        sortDatas(datas);
        return datas;
    }

    private void sortDatas(List<PrintUsageData> datas) {
        datas.sort(new Comparator<PrintUsageData>() {
            @Override
            public int compare(PrintUsageData o1, PrintUsageData o2) {
                return o2.getOutput()==o1.getOutput() ? 0 : (o2.getOutput() > o1.getOutput() ? 1 : -1);
            }
        });
    }

    @Override
    public List<PrintUsageData> getPrinterForPrinters(String from, String to) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        final Map<ImprimanteData, Integer> printerGroup = printUsageService.getPrinterForPrinters(from, to)
                .stream().map(data -> populator.populate(data))
                .collect(Collectors.groupingBy(PrintUsageData::getPrinter, Collectors.summingInt(PrintUsageData::getOutput)));
        printerGroup.keySet()
                .forEach(key ->{
                    final PrintUsageData data = new PrintUsageData();
                    data.setPrinter(key);
                    data.setOutput(printerGroup.get(key));
                    datas.add(data);
                });
        sortDatas(datas);
        return datas;
    }

    @Override
    public List<PrintUsageData> printGroupbyAffectation(String from, String to) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        final Map<StructureData, Integer> group = printUsageService.getPrinterForUsers(from, to)
                .stream().map(printUsage -> populator.populate(printUsage))
                .collect(Collectors.groupingBy(pu -> new StructureData(pu.getUser().getCodeService(), pu.getUser().getNameService()), Collectors.summingInt(PrintUsageData::getOutput)));
        group.keySet()
                .forEach(key -> {
                    final PrintUsageData data = new PrintUsageData();
                    data.setStructure(key);
                    data.setOutput(group.get(key));
                    datas.add(data);
                });
        sortDatas(datas);
        return datas;
    }

    @Override
    public List<PrintUsageData> printGroupbyStructure(String from, String to) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        final Map<StructureData, Integer> group = printUsageService.getPrinterForUsers(from, to)
                .stream().map(printUsage -> populator.populate(printUsage))
                .collect(Collectors.groupingBy(pu -> new StructureData(pu.getUser().getCodeStructure(), pu.getUser().getNameStructure()), Collectors.summingInt(PrintUsageData::getOutput)));
        group.keySet()
                .forEach(key -> {
                    final PrintUsageData data = new PrintUsageData();
                    data.setStructure(key);
                    data.setOutput(group.get(key));
                    datas.add(data);
                });
        sortDatas(datas);
        return datas;
    }
}

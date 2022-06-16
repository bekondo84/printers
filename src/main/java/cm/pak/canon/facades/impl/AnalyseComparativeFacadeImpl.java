package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.beans.AnalyseComparativeDetailData;
import cm.pak.canon.beans.SearchBean;
import cm.pak.canon.beans.Week;
import cm.pak.canon.facades.AnalyseComparativeFacade;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.models.Structure;
import cm.pak.canon.models.User;
import cm.pak.canon.populator.impl.ImprimantePopulator;
import cm.pak.canon.populator.impl.StructurePopulator;
import cm.pak.canon.populator.impl.UserPopulator;
import cm.pak.canon.services.PrintUsageService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cm.pak.canon.DateUtils.*;

@Component
public class AnalyseComparativeFacadeImpl implements AnalyseComparativeFacade {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyseComparativeFacadeImpl.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private PrintUsageService printUsageService;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private StructurePopulator structurePopulator;

    @Autowired
    private ImprimantePopulator imprimantePopulator;

    @Override
    public List<String> headers(String cycle, String start, String end) throws ParseException {
        if (cycle.equalsIgnoreCase("1")) {
            return getDatesBetween(start, end);
        } else if (cycle.equalsIgnoreCase("3")) {
            return getMonthsBetween(start, end);
        } else if (cycle.equalsIgnoreCase("4")) {
            return getYearsBetween(start, end);
        } else if (cycle.equalsIgnoreCase("2")) {
            return CollectionUtils.emptyIfNull(getWeeksBetween(start, end)).stream()
                    .map(week -> week.getCode())
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List analyseComparative(final SearchBean search) throws ParseException {
        TypeEnum type = getType(Integer.toString(search.getGroupBy()));
        final List<AnalyseComparativeData> result = new ArrayList<>();

        if (search.getCycle().equalsIgnoreCase("1")) {
            result.addAll(analyseComparativePerDays(type, search.getFrom(), search.getTo()));
        }else if(search.getCycle().equalsIgnoreCase("3")) {
            result.addAll(analyseComparativePerMonths(type, search.getFrom(), search.getTo()));
        } else if(search.getCycle().equalsIgnoreCase("4")) {
            result.addAll(analyseComparativePerYears(type, search.getFrom(), search.getTo()));
        } else if (search.getCycle().equalsIgnoreCase("2")) {
            result.addAll(analyseComparativePerWeeks(type, search.getFrom(), search.getTo()));
        }

        if (search.getVueType() == 1) {
            return result;
        }
        return convertAnalyticsToReportData(result);
    }

    private TypeEnum getType(String groupBy) {
        if (groupBy.equalsIgnoreCase("2"))
            return TypeEnum.PRINTER;
        else if (groupBy.equalsIgnoreCase("3"))
            return TypeEnum.AFFECTATION;
        else if( groupBy.equalsIgnoreCase("4"))
            return TypeEnum.STRUCTURE;
        else return TypeEnum.USER ;
    }

    protected List<AnalyseComparativeData> analyseComparativePerWeeks(final TypeEnum type, final String start, final String end) throws ParseException {
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<Week> weeks = getWeeksBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            final Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                setObjectByType(result, row, type, user);

                Map<Week, Integer> printsperDays = CollectionUtils.emptyIfNull(userMap.get(user))
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(printUsage -> {
                             return CollectionUtils.emptyIfNull(weeks).stream().filter(week ->week.getFrom().compareTo(printUsage.getStartTime())*week.getTo().compareTo(printUsage.getStartTime())<=0)
                                     .filter(Objects::nonNull).findAny().orElse(new Week());
                            }, Collectors.summingInt(PrintUsage::getOutputPages)));
                CollectionUtils.emptyIfNull(weeks)
                        .forEach(week -> {
                            final AnalyseComparativeDetailData col = new AnalyseComparativeDetailData();
                            int value = Objects.nonNull(printsperDays.get(week)) ? printsperDays.get(week):0 ;
                            col.setValue(week.getCode()); col.setQuantity(value);
                            row.getLignes().add(col);
                        });
            }
        }
        return result;
    }

    protected List<AnalyseComparativeData> analyseComparativePerYears(final TypeEnum type, final String start, final String end) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<String> months = getYearsBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            final Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                setObjectByType(result, row, type, user);

                Map<String, Integer> printsperDays = CollectionUtils.emptyIfNull(userMap.get(user))
                        .stream()
                        .collect(Collectors.groupingBy(printUsage -> sdf.format(printUsage.getStartTime()), Collectors.summingInt(PrintUsage::getOutputPages)));
                CollectionUtils.emptyIfNull(months)
                        .forEach(day -> {
                            final AnalyseComparativeDetailData col = new AnalyseComparativeDetailData();
                            int value = Objects.nonNull(printsperDays.get(day)) ? printsperDays.get(day) : 0 ;
                            col.setValue(day); col.setQuantity(value);
                            row.getLignes().add(col);
                        });
            }

        }
        return result;
    }

    private void setObjectByType(List<AnalyseComparativeData> result, AnalyseComparativeData row, TypeEnum type, Object user) {
        result.add(row);
        if (TypeEnum.PRINTER.equals(type)) {
            row.setImprimante(imprimantePopulator.populate((Imprimante) user));
        } else if (TypeEnum.STRUCTURE.equals(type)
                || TypeEnum.AFFECTATION.equals(type)) {
            row.setStructure(structurePopulator.populate((Structure) user));
        } else  {
            row.setUser(userPopulator.populate((User) user));
        }
    }

    protected List<AnalyseComparativeData> analyseComparativePerMonths(final TypeEnum type, final String start, final String end) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<String> months = getMonthsBetween(start, end);
        //LOG.info(String.format("TYPE ENUM : %s", type));
        if (CollectionUtils.isNotEmpty(datas)) {
            final Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                setObjectByType(result, row, type, user);

                Map<String, Integer> printsperDays = CollectionUtils.emptyIfNull(userMap.get(user))
                        .stream()
                        .collect(Collectors.groupingBy(printUsage -> sdf.format(printUsage.getStartTime()), Collectors.summingInt(PrintUsage::getOutputPages)));
                CollectionUtils.emptyIfNull(months)
                        .forEach(day -> {
                            final AnalyseComparativeDetailData col = new AnalyseComparativeDetailData();
                            int value = Objects.nonNull(printsperDays.get(day)) ? printsperDays.get(day) : 0 ;
                            col.setValue(day); col.setQuantity(value);
                            row.getLignes().add(col);
                        });
            }

        }
        return result;
    }

    protected  List<AnalyseComparativeData> analyseComparativePerDays(final TypeEnum type, final String start, final String end) throws ParseException {
        //LOG.info(String.format("TYPE ENUM IS : %s", type));
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<String> days = getDatesBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                setObjectByType(result, row, type, user);

                Map<String, Integer> printsperDays = CollectionUtils.emptyIfNull(userMap.get(user))
                        .stream()
                        .collect(Collectors.groupingBy(printUsage -> SDF.format(printUsage.getStartTime()), Collectors.summingInt(PrintUsage::getOutputPages)));
                CollectionUtils.emptyIfNull(days)
                        .forEach(day -> {
                            final AnalyseComparativeDetailData col = new AnalyseComparativeDetailData();
                            int value = Objects.nonNull(printsperDays.get(day)) ? printsperDays.get(day) : 0 ;
                            col.setValue(day); col.setQuantity(value);
                            row.getLignes().add(col);
                        });
            }

        }
        return result;
    }

    protected List<List<Object>> convertAnalyticsToReportData(final List<AnalyseComparativeData> datas) {
        final List<List<Object>> result = new ArrayList<>();
        final List<Object> header = datas.get(0).getLignes()
                     .stream().map(l -> l.getValue())
                     .collect(Collectors.toList());
        header.add(0, "Source");
        result.add(header);

        for (AnalyseComparativeData data : datas) {
            final List values = new ArrayList<>();
            values.add(Objects.nonNull(data.getStructure()) ? data.getStructure().getCode(): Objects.nonNull(data.getImprimante()) ? data.getImprimante().getName():data.getUser().getId() );
            values.addAll(data.getLignes()
                    .stream().map(l -> l.getQuantity())
                    .collect(Collectors.toList()));
            result.add(values);
        }

        return result;
    }
    /**
     *
     * @param type
     * @param datas
     * @return
     */
    private Map<Object, List<PrintUsage>> groupByType(TypeEnum type, List<PrintUsage> datas) {
        List<PrintUsage> result = datas ;

        if (TypeEnum.STRUCTURE.equals(type) ){
            result = result.stream().filter(printUsage -> Objects.nonNull(printUsage.getUser().getStructure()))
                    .collect(Collectors.toList());
        }
        if(TypeEnum.AFFECTATION.equals(type)) {
            result = result.stream().filter(printUsage -> Objects.nonNull(printUsage.getUser().getAffectation()))
                    .collect(Collectors.toList());
        }
        return result
                .stream()
                .collect(Collectors.groupingBy(printUsage -> {
                    if (TypeEnum.PRINTER.equals(type)) {
                        return printUsage.getPrinter();
                    }
                    if (TypeEnum.AFFECTATION.equals(type)) {
                        return printUsage.getUser().getAffectation();
                    }
                    if (TypeEnum.STRUCTURE.equals(type)) {
                        return printUsage.getUser().getStructure();
                    }
                    return printUsage.getUser();
                }));
    }



    public void setPrintUsageService(PrintUsageService printUsageService) {
        this.printUsageService = printUsageService;
    }

    public void setUserPopulator(UserPopulator userPopulator) {
        this.userPopulator = userPopulator;
    }
    enum TypeEnum {
        USER, PRINTER, AFFECTATION, STRUCTURE
    }

}

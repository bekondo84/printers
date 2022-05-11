package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.beans.AnalyseComparativeDetailData;
import cm.pak.canon.facades.AnalyseComparativeFacade;
import cm.pak.canon.models.CycleEnum;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.models.Structure;
import cm.pak.canon.models.User;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Override
    public List<String> headers(String cycle, String start, String end) throws ParseException {
        if (cycle.equalsIgnoreCase("1")) {
            return getDatesBetween(start, end);
        } else if (cycle.equalsIgnoreCase("3")) {
            return getMonthsBetween(start, end);
        }
        return null;
    }

    @Override
    public List<AnalyseComparativeData> analyseComparative(String start, String end, String groupBy, String cycle) throws ParseException {
        TypeEnum type = getType(groupBy);
        if (cycle.equalsIgnoreCase("1")) {
            return analyseComparativePerDays(type, start, end);
        }else if(cycle.equalsIgnoreCase("3")) {
            return analyseComparativePerMonths(type, start, end);
        }

       return null;
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
        final List<String> days = getWeeksBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            final Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
        }
        return result;
    }
    protected List<AnalyseComparativeData> analyseComparativePerMonths(final TypeEnum type, final String start, final String end) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<String> months = getMonthsBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            final Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                result.add(row);
                if (TypeEnum.PRINTER.equals(type)) {
                    row.setUser(userPopulator.populate((User) user));
                }
                if (TypeEnum.STRUCTURE.equals(type)
                        || TypeEnum.AFFECTATION.equals(type)) {
                    row.setStructure(structurePopulator.populate((Structure) user));
                } else  {
                    row.setUser(userPopulator.populate((User) user));
                }

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
        final List<PrintUsage> datas = printUsageService.getPrinterForUsers(start, end);
        final List<AnalyseComparativeData> result = new ArrayList<>();
        final List<String> days = getDatesBetween(start, end);

        if (CollectionUtils.isNotEmpty(datas)) {
            Map<Object, List<PrintUsage>> userMap = groupByType(type, datas);
            for (Object user : userMap.keySet()) {
                final AnalyseComparativeData row = new AnalyseComparativeData();
                result.add(row);
                if (TypeEnum.PRINTER.equals(type)) {
                    row.setUser(userPopulator.populate((User) user));
                }
                if (TypeEnum.STRUCTURE.equals(type)
                        || TypeEnum.AFFECTATION.equals(type)) {
                    row.setStructure(structurePopulator.populate((Structure) user));
                } else  {
                    row.setUser(userPopulator.populate((User) user));
                }

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

    /**
     *
     * @param type
     * @param datas
     * @return
     */
    private Map<Object, List<PrintUsage>> groupByType(TypeEnum type, List<PrintUsage> datas) {
        return datas
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


    private List<String> getDatesBetween(final String to, final String from) throws ParseException {
         final LocalDate toLocal = getToLocal(to);
         final LocalDate fromLocal = getToLocal(from);
         long numOfDaysBetween = ChronoUnit.DAYS.between(toLocal, fromLocal);
         return IntStream.iterate(0, i -> i+1)
                 .limit(numOfDaysBetween + 1)
                 .mapToObj(i -> SDF.format(Date.from(toLocal.plusDays(i).atStartOfDay()
                         .atZone(ZoneId.systemDefault()).toInstant())))
                 .collect(Collectors.toList());
    }

    private List<String> getWeeksBetween(final String to, final String from) throws ParseException {
        final LocalDate toLocal = getToLocal(to);
        final LocalDate fromLocal = getToLocal(from);
        long numOfDaysBetween = ChronoUnit.WEEKS.between(toLocal, fromLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> String.format("Week %s", i+1))
                .collect(Collectors.toList());
    }

    private List<String> getMonthsBetween(final String to, final String from) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        final LocalDate toLocal = getToLocal(to);
        final LocalDate fromLocal = getToLocal(from);
        long numOfDaysBetween = ChronoUnit.MONTHS.between(toLocal, fromLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> sdf.format(Date.from(toLocal.plusMonths(i)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())))
                .collect(Collectors.toList());
    }

    private LocalDate getToLocal(String to) throws ParseException {
        return SDF.parse(to)
                .toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
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

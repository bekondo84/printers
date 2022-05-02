package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.ImprimanteData;
import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.beans.StructureData;
import cm.pak.canon.beans.UserData;
import cm.pak.canon.facades.PrintUsageFacade;
import cm.pak.canon.models.Structure;
import cm.pak.canon.populator.impl.PrintUsagePopulator;
import cm.pak.canon.services.PrintUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PrintUsageFacadeImpl implements PrintUsageFacade {

    @Autowired
    private PrintUsageService printUsageService;

    @Autowired
    private PrintUsagePopulator populator ;

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

package cm.pak.canon.tasks;

import cm.pak.canon.models.Configuration;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.models.TaskHistory;
import cm.pak.canon.services.*;
import cm.pak.canon.services.HttpUrlConnectionService.Type;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class CanonDataCollectorTask implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(CanonDataCollectorTask.class);
    @Autowired
    private HttpUrlConnectionService connectionService;
    @Autowired
    private ConfigurationService configService ;
    @Autowired
    private ImprimanteService imprimanteService;
    @Autowired
    private CSVService csvService ;
    @Autowired
    private TaskHistoryService taskHistoryService;

    public CanonDataCollectorTask() {

    }

    @Override
    public void run() {
        final Configuration config = configService.getConfiguration().orElse(null);
        if (Objects.isNull(config)) {
            return;
        }
        List<Imprimante> printers = imprimanteService.getAllPrinters();

        if (CollectionUtils.isNotEmpty(printers)) {
            printers.forEach(printer -> {
                //Process Print Log data
                processPrintDataCollect(config, printer, Type.PRINT);
                //Process CopyLog data
                processPrintDataCollect(config, printer, Type.COPY);
            });
        }

    }

    private void processPrintDataCollect(Configuration config, Imprimante printer, Type type) {
        TaskHistory printerTaskHistory = new TaskHistory();
        try {
            printerTaskHistory.setDescription(String.format("Collected of %s data for printer %s", type.name(), printer.getName()));
            printerTaskHistory.setTaskname(printer.getName());
            printerTaskHistory.setStartAt(new Date());
            printerTaskHistory.setStatus("success");
            //Process printLog data
            csvService.processCsv(printer, connectionService.downloadFileFromPrinter(printer.getIpAdress(), config.getUsername(), config.getPassword(), type));
        } catch (Exception e) {
            printerTaskHistory.setStatus("error");
            printerTaskHistory.setErrormessage(e.getMessage());
        } finally {
           printerTaskHistory.setEndAt(new Date());
           taskHistoryService.save(printerTaskHistory);
        }
    }


}

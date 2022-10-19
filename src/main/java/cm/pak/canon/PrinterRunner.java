package cm.pak.canon;

import cm.pak.canon.services.AccountService;
import cm.pak.canon.tasks.CanonDataCollectorTask;
import cm.pak.canon.tasks.TaskHistoriesCleannerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class PrinterRunner implements ApplicationRunner {

    @Value("${collector.task.delai}")
    private int rate ;
    @Value("${cleaner.delai}")
    private int cleannercycle ;
    @Autowired
    private AccountService accountService ;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private CanonDataCollectorTask collectorTask;
    @Autowired
    private TaskHistoriesCleannerTask cleannerTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
         accountService.initAdminUser();
         scheduler.scheduleAtFixedRate(collectorTask, rate*60*60*1000);
         scheduler.scheduleAtFixedRate(cleannerTask, cleannercycle*60*60*1000);
    }
}

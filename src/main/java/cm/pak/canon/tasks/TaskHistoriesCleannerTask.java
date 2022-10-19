package cm.pak.canon.tasks;

import cm.pak.canon.services.TaskHistoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskHistoriesCleannerTask implements Runnable{

    @Value("${cleaner.oldthan}")
    private int oldthan;
    @Autowired
    private TaskHistoryService historyService;

    @Override
    public void run() {
        historyService.remove(CollectionUtils.emptyIfNull(historyService.getHistoriesOldThan(oldthan)));
    }
}

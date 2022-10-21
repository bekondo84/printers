package cm.pak.canon.services.impl;

import cm.pak.canon.models.TaskHistory;
import cm.pak.canon.repertories.TaskHistoryRepository;
import cm.pak.canon.services.TaskHistoryService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskhistoryServiceImpl implements TaskHistoryService {

    @Autowired
    private TaskHistoryRepository historyRepository;

    @Override
    public void save(TaskHistory history) {
         historyRepository.save(history);
    }

    @Override
    public List<TaskHistory> getHistory() {
        final List<TaskHistory> histories = new ArrayList<>();
        historyRepository.findAll().forEach(task -> histories.add(task));
        return  histories;
    }

    @Override
    public void remove(TaskHistory history) {
        historyRepository.delete(history);
    }

    @Override
    public void remove(Iterable<TaskHistory> histories) {
        historyRepository.deleteAll(histories);
    }

    @Override
    public List<TaskHistory> getHistoriesOldThan(int days) {
        final Date lessDate = DateUtils.addDays(new Date(), -1*days);
        return historyRepository.getHistoriesOldThan(lessDate);
    }
}

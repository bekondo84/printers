package cm.pak.canon.services;

import cm.pak.canon.models.TaskHistory;

import java.util.List;

public interface TaskHistoryService {

    void save(final TaskHistory history);
    List<TaskHistory> getHistory();
    void remove(final TaskHistory history);
    void remove(final Iterable<TaskHistory> histories);
    List<TaskHistory> getHistoriesOldThan(int days);
}

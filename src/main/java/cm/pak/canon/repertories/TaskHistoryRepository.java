package cm.pak.canon.repertories;

import cm.pak.canon.models.TaskHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskHistoryRepository extends CrudRepository<TaskHistory, Long> {

    @Query("SELECT h FROM TaskHistory AS h WHERE h.startAt < ?1")
    List<TaskHistory> getHistoriesOldThan(Date day);
}

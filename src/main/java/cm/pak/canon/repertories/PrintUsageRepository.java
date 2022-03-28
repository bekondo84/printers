package cm.pak.canon.repertories;

import cm.pak.canon.models.PrintUsage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrintUsageRepository extends CrudRepository<PrintUsage, String> {

    @Query("SELECT f FROM PrintUsage f WHERE f.printer.id = :id")
    List<PrintUsage> getPrintUsageForPrinter(@Param("id") final Long id) ;

    @Query("SELECT f FROM PrintUsage f WHERE f.userName = :username")
    List<PrintUsage> getPrinterUsageForUser(final String username);

    @Query("SELECT f FROM PrintUsage f WHERE f.startTime >= :from AND f.endTime <= :to")
    List<PrintUsage> getPrinterUsageForDates(final Date from, final Date to);
}

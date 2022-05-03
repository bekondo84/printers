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

    @Query("SELECT f FROM PrintUsage f WHERE f.startTime >= :from AND f.endTime <= :to AND (f.user.affectation.code = :structure OR f.user.structure.code = :structure)")
    List<PrintUsage> getPrintUsageForStructureForDates(final Date from, final Date to, final String structure);

    @Query("SELECT f FROM PrintUsage f WHERE f.user.affectation.code = :structure OR f.user.structure.code = :structure")
    List<PrintUsage> getPrintUsageForStructureForDates(final String structure);

    @Query("SELECT f FROM PrintUsage f WHERE f.startTime >= :from AND f.endTime <= :to AND f.user.userId = :user")
    List<PrintUsage> getUserPrintUsageResume(final Date from, final Date to, final String user);

    @Query("SELECT f FROM PrintUsage f WHERE f.user.userId = :user")
    List<PrintUsage> getUserPrintUsageResume(final String user);
}

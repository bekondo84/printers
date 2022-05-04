package cm.pak.canon.repertories;

import cm.pak.canon.models.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Query("SELECT u FROM Account u WHERE u.username = :username")
    Account getAccountByUsername(@Param("username")String username);
}

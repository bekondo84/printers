package cm.pak.canon.services;

import cm.pak.canon.models.Account;

public interface AccountService {
     void createOrUpdateAccount(final Account account);

     void initAdminUser() ;
}

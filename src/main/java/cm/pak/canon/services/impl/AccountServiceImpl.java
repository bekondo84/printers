package cm.pak.canon.services.impl;

import cm.pak.canon.models.Account;
import cm.pak.canon.repertories.AccountRepository;
import cm.pak.canon.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    BCryptPasswordEncoder encoder ;


    @Override
    public void createOrUpdateAccount(Account account) {
        assert Objects.nonNull(account): "Account can't not be null";
        accountRepository.save(account);
    }

    @Override
    public void initAdminUser() {
        final Account account = new Account();
        account.setUsername("admin");
        account.setEnabled(true);
        account.setRole("admin");
        account.setPassword(encoder.encode("nimda"));
        accountRepository.save(account);
    }
}

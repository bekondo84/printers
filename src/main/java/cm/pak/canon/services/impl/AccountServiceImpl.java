package cm.pak.canon.services.impl;

import cm.pak.canon.models.Account;
import cm.pak.canon.repertories.AccountRepository;
import cm.pak.canon.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;



    @Override
    public void createOrUpdateAccount(Account account) {
        assert Objects.nonNull(account): "Account can't not be null";
        accountRepository.save(account);
    }
}

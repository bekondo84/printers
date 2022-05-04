package cm.pak.canon.security;

import cm.pak.canon.models.Account;
import cm.pak.canon.repertories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

public class AccountDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountRepository.getAccountByUsername(username);

        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException(String.format("Could not found account with userna"));
        }
        return new AccountDetails(account);
    }
}

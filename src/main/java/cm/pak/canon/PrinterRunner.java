package cm.pak.canon;

import cm.pak.canon.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PrinterRunner implements ApplicationRunner {

    @Autowired
    private AccountService accountService ;

    @Override
    public void run(ApplicationArguments args) throws Exception {
         accountService.initAdminUser();
    }
}

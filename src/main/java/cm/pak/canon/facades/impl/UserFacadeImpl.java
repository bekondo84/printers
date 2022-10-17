package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.UserData;
import cm.pak.canon.beans.UsersBean;
import cm.pak.canon.facades.UserFacade;
import cm.pak.canon.models.Account;
import cm.pak.canon.models.User;
import cm.pak.canon.populator.impl.UserPopulator;
import cm.pak.canon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserFacadeImpl implements UserFacade {
    private static final Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

    @Autowired
    private UserPopulator populator;
    @Autowired
    private UserService userService;

    @Override
    public List<UserData> getUsers(int start, int max) {
        List<UserData> datas = userService.getUsers(start, max)
                .stream().map(user -> populator.populate(user))
                .collect(Collectors.toList());
        return datas;
    }

    @Override
    public void importUsers(UsersBean bean) throws IOException {
        final MultipartFile mpf = CollectionUtils.isEmpty(bean.getFiles()) ? null : bean.getFiles().get(0)  ;

        if (Objects.nonNull(mpf)) {
            userService.importUsers(mpf);
        }
    }

}

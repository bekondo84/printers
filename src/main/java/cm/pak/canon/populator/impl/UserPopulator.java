package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.UserData;
import cm.pak.canon.models.User;
import cm.pak.canon.populator.Populator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserPopulator implements Populator<User, UserData> {
    @Override
    public UserData populate(User data) {
        final UserData result = new UserData();
        result.setId(data.getUserId());
        result.setName(data.getName());
        result.setCodeStructure(Objects.nonNull(data.getStructure()) ? data.getStructure().getCode() : "");
        result.setCodeService(Objects.nonNull(data.getAffectation()) ? data.getAffectation().getCode() : "");
        return result;
    }
}

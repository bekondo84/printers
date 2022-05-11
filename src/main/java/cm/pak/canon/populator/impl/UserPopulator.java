package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.UserData;
import cm.pak.canon.models.User;
import cm.pak.canon.populator.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserPopulator implements Populator<User, UserData> {

    @Autowired
    private StructurePopulator populator ;

    @Override
    public UserData populate(User data) {
        final UserData result = new UserData();
        result.setId(data.getUserId());
        result.setName(data.getName());
        setAffectation(data, result);
        setStructure(data, result);
        return result;
    }

    private void setStructure(User data, UserData result) {
        result.setCodeStructure(Objects.nonNull(data.getStructure()) ? data.getStructure().getCode() : "");
        result.setNameStructure(Objects.nonNull(data.getStructure()) ? data.getStructure().getIntitule() : "");
    }

    private void setAffectation(User data, UserData result) {
        result.setCodeService(Objects.nonNull(data.getAffectation()) ? data.getAffectation().getCode() : "");
        result.setNameService(Objects.nonNull(data.getAffectation()) ? data.getAffectation().getIntitule() : "");
    }
}

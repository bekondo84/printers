package cm.pak.canon.facades;

import cm.pak.canon.beans.UserData;
import cm.pak.canon.beans.UsersBean;

import java.io.IOException;
import java.util.List;

public interface UserFacade {

    List<UserData> getUsers(int start, int max) ;

    /**
     * Import users data from CSV file
     * @param bean
     */
    void importUsers(final UsersBean bean) throws IOException;

}

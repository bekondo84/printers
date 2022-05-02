package cm.pak.canon.services;

import cm.pak.canon.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    /**
     * Return
     * @param start
     * @param max
     * @return
     */
    List<User> getUsers(int start, int max);

    void importUsers(final MultipartFile mpf) throws IOException;
}

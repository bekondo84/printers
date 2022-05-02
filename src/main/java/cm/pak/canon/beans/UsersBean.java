package cm.pak.canon.beans;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class UsersBean implements Serializable {
    private List<MultipartFile> files;

    public UsersBean() {
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "UsersBean{" +
                "files=" + files +
                '}';
    }
}

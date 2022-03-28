package cm.pak.canon.beans;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class ImportBean implements Serializable {
    private Long printer;
    private List<MultipartFile> files;

    public ImportBean() {
    }

    public void setPrinter(Long printer) {
        this.printer = printer;
    }

    public Long getPrinter() {
        return printer;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ImportBean{" +
                "printer=" + printer +
                ", files=" + files +
                '}';
    }
}

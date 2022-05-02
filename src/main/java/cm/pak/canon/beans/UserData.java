package cm.pak.canon.beans;

import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {
    private String id ;

    private String name ;

    private String codeService;

    private String nameService;

    private String codeStructure;

    private String nameStructure;

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeService() {
        return codeService;
    }

    public void setCodeService(String codeService) {
        this.codeService = codeService;
    }

    public String getCodeStructure() {
        return codeStructure;
    }

    public void setCodeStructure(String codeStructure) {
        this.codeStructure = codeStructure;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public String getNameStructure() {
        return nameStructure;
    }

    public void setNameStructure(String nameStructure) {
        this.nameStructure = nameStructure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(id, userData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", affectation='" + codeService + '\'' +
                ", structure='" + codeStructure + '\'' +
                '}';
    }
}

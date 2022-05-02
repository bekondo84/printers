package cm.pak.canon.beans;

import java.io.Serializable;
import java.util.Objects;

public class StructureData implements Serializable {
   private String code ;

   private String intitule ;

    public StructureData() {
    }

    public StructureData(String codeService, String nameService) {
        this.code = codeService;
        this.intitule = nameService;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructureData that = (StructureData) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

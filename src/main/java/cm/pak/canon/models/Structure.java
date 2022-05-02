package cm.pak.canon.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
public class Structure implements Serializable {

    @Id
    private String code ;

    private String intitule ;

    public Structure() {
    }

    public Structure(String code, String intitule) {
        this.code = code;
        this.intitule = intitule;
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
}

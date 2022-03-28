package cm.pak.canon.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "printer")
public class Imprimante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    private String model;
    private String localisation ;
    private String ipAdress ;

    public Imprimante() {
    }

    public Imprimante(String name, String model, String localiation, String ipAdress) {
        this.name = name;
        this.model = model;
        this.localisation = localiation;
        this.ipAdress = ipAdress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localiation) {
        this.localisation = localiation;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    @Override
    public String toString() {
        return "Imprimante{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", localisation='" + localisation + '\'' +
                ", ipAdress='" + ipAdress + '\'' +
                '}';
    }
}

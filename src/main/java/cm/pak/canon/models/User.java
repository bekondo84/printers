package cm.pak.canon.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class User implements Serializable {

    @Id
    private String userId ;

    private String name ;

    private String firstName ;

    @ManyToOne
    @JoinColumn(name = "aff_id")
    private Structure affectation ;

    @ManyToOne
    @JoinColumn(name = "struct_id")
    private Structure structure ;

    public Structure getAffectation() {
        return affectation;
    }

    public User() {
    }

    public User(String userId, String name, String firstName, Structure affectation, Structure structure) {
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.affectation = affectation;
        this.structure = structure;
    }

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAffectation(Structure affectation) {
        this.affectation = affectation;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", affectation=" + affectation +
                ", structure=" + structure +
                '}';
    }
}

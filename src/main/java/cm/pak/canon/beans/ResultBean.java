package cm.pak.canon.beans;

import java.io.Serializable;

public class ResultBean implements Serializable {
    private String owner ;

    private int original ;

    private int output ;

    public ResultBean() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}

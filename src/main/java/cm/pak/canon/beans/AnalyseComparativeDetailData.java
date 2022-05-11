package cm.pak.canon.beans;

import java.io.Serializable;

public class AnalyseComparativeDetailData implements Serializable {
    private int quantity;
    private String value;

    public AnalyseComparativeDetailData() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package cm.pak.canon.beans;

import java.io.Serializable;

public class ConfigurationData implements Serializable {
   private long id ;
   private int pageSize ;

    public ConfigurationData() {
    }

    public ConfigurationData(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

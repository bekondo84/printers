package cm.pak.canon.beans;

import java.io.Serializable;

public class PrintUsageData implements Serializable {
    private UserData user ;

    private ImprimanteData printer;

    private StructureData structure;

    private StructureData service ;

    private String date ;

    private String filename ;

    private int original ;

    private int output ;

    public PrintUsageData() {
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public ImprimanteData getPrinter() {
        return printer;
    }

    public void setPrinter(ImprimanteData printer) {
        this.printer = printer;
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

    public StructureData getStructure() {
        return structure;
    }

    public void setStructure(StructureData structure) {
        this.structure = structure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public StructureData getService() {
        return service;
    }

    public void setService(StructureData service) {
        this.service = service;
    }
}

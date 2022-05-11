package cm.pak.canon.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnalyseComparativeData implements Serializable {

    private ImprimanteData imprimante ;
    private StructureData structure ;
    private UserData user ;

    private List<AnalyseComparativeDetailData> lignes ;

    public AnalyseComparativeData() {
        this.lignes = new ArrayList<>();
    }

    public ImprimanteData getImprimante() {
        return imprimante;
    }

    public void setImprimante(ImprimanteData imprimante) {
        this.imprimante = imprimante;
    }

    public StructureData getStructure() {
        return structure;
    }

    public void setStructure(StructureData structure) {
        this.structure = structure;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public List<AnalyseComparativeDetailData> getLignes() {
        return lignes;
    }

    public void setLignes(List<AnalyseComparativeDetailData> lignes) {
        this.lignes = lignes;
    }
}

package cm.pak.canon.services;

import cm.pak.canon.models.Imprimante;

import java.util.List;

public interface ImprimanteService {

    public void save(final Imprimante printer) ;

    public void  delete(final Long id) ;

    public Imprimante getPrinterById(final Long id) ;

    public List<Imprimante> getAllPrinters() ;

}

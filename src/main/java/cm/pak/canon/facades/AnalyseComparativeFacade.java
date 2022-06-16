package cm.pak.canon.facades;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.beans.SearchBean;
import cm.pak.canon.models.CycleEnum;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface AnalyseComparativeFacade {

    List<String> headers(final String cycle, final String start, final String end) throws ParseException;
    /**
     *
     * @return
     */
    List analyseComparative(final SearchBean search) throws ParseException;
}

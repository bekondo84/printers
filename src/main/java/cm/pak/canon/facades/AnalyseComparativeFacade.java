package cm.pak.canon.facades;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.models.CycleEnum;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface AnalyseComparativeFacade {

    List<String> headers(final String cycle, final String start, final String end) throws ParseException;
    /**
     *
     * @param start
     * @param end
     * @param groupBy
     * @param cycle
     * @return
     */
    List<AnalyseComparativeData> analyseComparative(final String start, final String end, final String groupBy, final String cycle) throws ParseException;
}

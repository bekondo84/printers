package cm.pak.canon.facades.impl;


import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.beans.Week;
import cm.pak.canon.facades.AnalyseComparativeFacade;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.models.User;
import cm.pak.canon.populator.impl.UserPopulator;
import cm.pak.canon.services.PrintUsageService;
import cm.pak.canon.services.impl.PrintUsageServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class AnalyseComparativeFacadeImplTest extends AnalyseComparativeFacadeImpl{

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    @Mock
    private PrintUsageService printUsageService;
    private AnalyseComparativeFacade analyseComparativeFacade;
    private final String START = "2020-02-20";
    private final String END = "2021-02-20";

    @BeforeEach
    void setUp() {
        printUsageService = Mockito.mock(PrintUsageServiceImpl.class);
       setPrintUsageService(printUsageService);
       setUserPopulator(new UserPopulator());
    }

    @Test
    void shouldRetournEmptyistWhenThereIsNotData() throws ParseException {
        assertTrue(analyseComparativePerDays(TypeEnum.USER, START, END).isEmpty());
    }

    @Test
    void shouldReturnDataOrganizeByUserPerDays() throws ParseException {
        String from = "2022-05-09";
        String to = "2022-05-11";
        Mockito.when(printUsageService.getPrinterForUsers(from, to))
                .thenReturn(getPrintDateForUsers());
        final List<AnalyseComparativeData> result = analyseComparativePerDays(TypeEnum.USER, from, to);
        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertNotNull(result.get(0).getUser());
        assertEquals(result.get(0).getUser().getId(), "user1.name");
        assertEquals(result.get(0).getLignes().size(), 3);
        assertEquals(result.get(0).getLignes().get(0).getQuantity(), 3);
        assertEquals(result.get(0).getLignes().get(1).getQuantity(), 3);
        assertEquals(result.get(0).getLignes().get(2).getQuantity(), 0);
    }

    @Test
    void shouldReturnWeekWtihCorrectDate() throws ParseException {
        final String from = "2022-04-01";
        final String to ="2022-05-12";
        final List<Week> weeks = getWeeksBetween(from, to);
        assertNotNull(weeks);
        assertTrue(CollectionUtils.isNotEmpty(weeks));
        assertEquals(SDF.format(weeks.get(0).getFrom()), "28/03/2022");
        assertEquals(SDF.format(weeks.get(0).getTo()), "03/04/2022");
        assertEquals(SDF.format(weeks.get(1).getFrom()), "04/04/2022");
        assertEquals(SDF.format(weeks.get(1).getTo()), "10/04/2022");
        assertEquals(SDF.format(weeks.get(4).getFrom()), "25/04/2022");
        assertEquals(SDF.format(weeks.get(4).getTo()), "01/05/2022");
    }

    private List<PrintUsage> getPrintDateForUsers() throws ParseException {
        final List<PrintUsage> result = new ArrayList<>();
        final User user1 = new User("user1.name", "BEKO", null, null, null);
        final User user2 = new User("user2.name", "DADA", null, null, null);
        final User user3 = new User("user3.name", "MAMA", null, null, null);
        result.add(new PrintUsage("00001",1234L, "file1", user1,SDF.parse("09/05/2022"), SDF.parse("09/05/2022"), 2, 2));
        result.add(new PrintUsage("00002",1234L, "file2", user1,SDF.parse("10/05/2022"), SDF.parse("10/05/2022"), 3, 3));
        result.add(new PrintUsage("00003",1234L, "file3", user1,SDF.parse("09/05/2022"), SDF.parse("09/05/2022"), 1, 1));
        result.add(new PrintUsage("00002",1234L, "file2", user2,SDF.parse("09/05/2022"), SDF.parse("09/05/2022"), 5, 5));
        result.add(new PrintUsage("00002",1234L, "file2", user3,SDF.parse("10/05/2022"), SDF.parse("10/05/2022"), 4, 4));
        result.add(new PrintUsage("00002",1234L, "file2", user3,SDF.parse("11/05/2022"), SDF.parse("11/05/2022"), 1, 1));
        result.add(new PrintUsage("00002",1234L, "file2", user2,SDF.parse("11/05/2022"), SDF.parse("11/05/2022"), 2, 2));
        return result;
    }


}
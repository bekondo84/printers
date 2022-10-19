package cm.pak.canon.services.impl;

import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.models.User;
import cm.pak.canon.repertories.PrintUsageRepository;
import cm.pak.canon.repertories.UserRepository;
import cm.pak.canon.services.CSVService;
import cm.pak.canon.services.ImprimanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVServiceImpl implements CSVService {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM yyyy hh:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(CSVServiceImpl.class);
    @Autowired
   private ImprimanteService printerService ;

    @Autowired
    private PrintUsageRepository repository ;

    @Autowired
    private UserRepository userRepository ;

    @Override
    public <T> List<T> parseCsv(InputStream is, final String[] headers, T type) throws UnsupportedEncodingException {
      assert Objects.nonNull(headers): "Headers can't be null";

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final List<T> datas = new ArrayList<>();

        reader.lines().skip(1L)
                .forEach(line ->{
                    try {
                        populateObject(headers, type, line)
                                .ifPresent(instance ->datas.add(instance));
                    } catch (Exception e) {
                        LOG.error(String.format("Unable to process line %s with error [%s]", line, e.getMessage()));
                        //e.printStackTrace();
                    }

                });
        return datas ;
    }

    @Override
    public <T> List<T> parceCsv(List<String> data, String[] headers, T type) {
        final List<T> datas = new ArrayList<>();
        data.stream()
                .skip(1)
                .forEach(line -> {
                    try {
                        populateObject(headers, type, line)
                                .ifPresent(instance ->datas.add(instance));
                    } catch (Exception e) {
                        LOG.error(String.format("Unable to process line %s with error [%s]", line, e.getMessage()));
                        //e.printStackTrace();
                    }
                });
        return datas;
    }

    private <T> Optional<T> populateObject(String[] headers, T type, String line) throws Exception {
        final String[] cols = line.split(";");

        if (cols.length != headers.length) {
            return  Optional.empty();
        }

        final T instance = (T) type.getClass().newInstance();

        for(int i = 0; i < headers.length; i++ ) {
            populateObjectField(headers, type, cols, instance, i);
        }
        return Optional.ofNullable(instance);
    }

    private <T> void populateObjectField(String[] headers, T type, String[] cols, T instance, int i) throws NoSuchFieldException, IllegalAccessException {
        final Field field = type.getClass().getDeclaredField(headers[i]);
        field.setAccessible(Boolean.TRUE);
        field.set(instance, cols[i]);
    }

    @Override
    public void processCsv(final Imprimante printer, List<String> data) {
        data.stream().skip(1).forEach(line -> {
            processLine(printer, FORMATTER, line);
        });
    }

    @Override
    public void processCsv(ImportBean data) throws IOException {
       final Imprimante printer = printerService.getPrinterById(data.getPrinter());
        if (CollectionUtils.isEmpty(data.getFiles())) {
            return;
        }

        for (MultipartFile mpf : data.getFiles()) {
            processMultipartFile(printer, FORMATTER, mpf);
        }
    }



    /**
     *
     * @param printer
     * @param formatter
     * @param mpf
     * @throws IOException
     */
    private void processMultipartFile(Imprimante printer, SimpleDateFormat formatter, MultipartFile mpf) throws IOException {
        if (Objects.isNull(mpf)) {
            return;
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(mpf.getInputStream(), "UTF-8"));

        reader.lines().skip(1).forEach(line -> {
            processLine(printer, formatter, line);
        });
    }

    private void processLine(Imprimante printer, SimpleDateFormat formatter, String line) {
        String[] cols = line.split(",");

        if (cols.length == 10) {
            final PrintUsage usage = new PrintUsage();
            usage.setPrinter(printer);
            usage.setPrinterId(printer.getId());
            usage.setJobId(cols[0]);
            usage.setResult(clean(cols[1]));
            try {
                usage.setStartTime(formatter.parse(clean(cols[2])));
                usage.setEndTime(formatter.parse(clean(cols[3])));
            } catch (ParseException e) {
                ;
            }
            usage.setDepartmentId(clean(cols[4]));
            usage.setUserName(clean(cols[5]));
            setUser(usage);
            usage.setOriginalPages(Integer.valueOf(clean(cols[6])));
            usage.setOutputPages(Integer.valueOf(clean(cols[7])));
            usage.setSheetCopies(clean(cols[8]));
            usage.setEndCode(clean(cols[9]));
            //System.out.println("Usage before save : "+usage+" output : "+clean(cols[9])+" value ::: "+Integer.valueOf(clean(cols[9])));
            try{
                repository.save(usage);
            }catch (Exception ex) {
                LOG.error(String.format("Erreur pendant la sauvegarde de l'item : %s", usage));
            }
        }

        if (cols.length == 14) {

            final PrintUsage usage = new PrintUsage();
            usage.setPrinter(printer);
            usage.setPrinterId(printer.getId());
            usage.setJobId(cols[0]);
            usage.setResult(clean(cols[1]));
            try {
                usage.setStartTime(formatter.parse(clean(cols[2])));
                usage.setEndTime(formatter.parse(clean(cols[3])));
            } catch (ParseException e) {
               ;
            }
            usage.setDepartmentId(clean(cols[4]));
            usage.setJobType(clean(cols[5]));
            usage.setFileName(clean(cols[6]));
            usage.setUserName(clean(cols[7]));
            usage.setOriginalPages(Integer.valueOf(clean(cols[8])));
            usage.setOutputPages(Integer.valueOf(clean(cols[9])));
            usage.setSheetCopies(clean(cols[10]));
            usage.setEndCode(clean(cols[11]));
            usage.setJobType(clean(cols[12]));
            usage.setDetails(clean(cols[13]));
            setUser(usage);
            //System.out.println("Usage before save : "+usage+" output : "+clean(cols[9])+" value ::: "+Integer.valueOf(clean(cols[9])));
            try{
                repository.save(usage);
            }catch (Exception ex) {
                LOG.error(String.format("Erreur pendant la sauvegarde de l'item : %s", usage));
            }
        }
    }

    private void setUser(PrintUsage usage) {
        User user = userRepository.findById(usage.getUserName())
                .orElse(null);
        if (Objects.isNull(user)) {
            user = new User(usage.getUserName());
            user = userRepository.save(user);
        }
       usage.setUser(user);
    }

    private String clean(final String value) {
        return  value. replaceAll("^\"|\"$", "");
    }
}

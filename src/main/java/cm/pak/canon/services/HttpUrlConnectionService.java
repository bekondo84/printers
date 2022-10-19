package cm.pak.canon.services;

import java.io.IOException;
import java.util.List;

public interface HttpUrlConnectionService {

      List<String> downloadFileFromPrinter(final String url, final String username, final String password, Type type) throws IOException;

     enum Type {
           PRINT ("Print"),
           COPY ("Copy");

           String name ;
         Type(String name) {
               this.name = name;
         }
     }
}

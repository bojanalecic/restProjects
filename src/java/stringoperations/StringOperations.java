/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stringoperations;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boban
 */
public class StringOperations {

    private static StringOperations instance;

    public static StringOperations getInstance() {
        if (instance == null) {
            instance = new stringoperations.StringOperations();
        }
        return instance;
    }

    public static String returnBaseAddress(String address) {
        int numberOfSlashes = 0;

        for (int i = 0; i < address.length(); i++) {

            char key = address.charAt(i);
            if (key == '/') {
                numberOfSlashes++;
            }
            if (numberOfSlashes == 3) {
                address = address.substring(0, i + 1);
            }

        }

        return address.substring(0, address.length() - 1);
    }

    public URI vratiPraviLink(URI link) {
        try {
           
            HttpURLConnection con = (HttpURLConnection) (new URL(link.toString()).openConnection());
            con.setInstanceFollowRedirects(false);
            con.connect();
            int responseCode = con.getResponseCode();
            
            String location = con.getHeaderField("Location");
           
            if (location == null) {
                return link;
            } else {
                return new URI(location);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(StringOperations.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(StringOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return link;
    }
}

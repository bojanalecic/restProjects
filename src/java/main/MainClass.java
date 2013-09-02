/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controler.Kontroler;
import java.io.IOException;
import persistance.RDFPersistance;

/**
 *
 * @author Boban
 */
public class MainClass {

    public static void main(String[] args) {
        try {
            //scrape data from all pages   
            //Kontroler.getInstance().scrapeAll();

            //scrape data from selected page
            //Kontroler.getInstance().scrapePage(7);

            //scrape data from first page
//            Kontroler.getInstance().scrapeFirst();

           Kontroler.getInstance().scrapeSourceForge("http://sourceforge.net/directory/os:windows/freshness:recently-updated/");
            
            RDFPersistance.getInstance().flush();

//            serialize data to disk, filename: database.rdf    
//           RDFPersistance.getInstance().serializeToFile();
//            System.out.println("Model saved to: " + RDFPersistance.getInstance().MODEL_FILENAME);

        } catch (IOException ex) {
            //Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            // Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }

    }
}

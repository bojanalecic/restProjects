/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import com.hp.hpl.jena.rdf.model.Model;
import domain.Person;
import domain.Project;
import domain.Version;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import persistance.RDFPersistance;
import thewebsemantic.Bean2RDF;
import util.URIGenerator;
import webscraping.freecode.Scraper;
import webscraping.sourceforge.ScraperSF;

/**
 *
 * @author Boban
 */
public class Kontroler {

    private List<Project> articleList;
    private List<Project> sourceForgeProjects;
    private static Kontroler instance;

    private Kontroler() {
        articleList = new ArrayList<Project>();
        sourceForgeProjects = new ArrayList<Project>();
    }

    public static Kontroler getInstance() {

        if (instance == null) {
            instance = new Kontroler();
        }

        return instance;

    }

    public void scrape(String address) throws IOException, Exception {
        Scraper sc = new Scraper(address);
        articleList = sc.returnArticleList();

        toRDF(articleList);

    }

    public List<Project> getArticleList() {
        if (articleList == null) {
            throw new NullPointerException();
        }
        return articleList;
    }

    private void toRDF(List<Project> articleList) throws Exception {

        if (articleList.isEmpty()) {
            throw new Exception("No more pages.");
        }
        for (Project pr : articleList) {
            pr.setUri(URIGenerator.generateSFUri(pr));
            RDFPersistance.getInstance().getWriter().save(pr);
        }


    }

    public void scrapeFirst() throws Exception {
        Kontroler.getInstance().scrape("http://freecode.com/");
    }

    public void scrapePage(int page) throws Exception {

        System.out.println("*************************************");
        System.out.println("Scraping page " + page + ":");
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Kontroler.getInstance().scrape("http://freecode.com/?page=" + page);
    }

    public void scrapeAll() throws Exception {

        for (int i = 1; i > 0; i++) {
            try {
                System.out.println("*************************************");
                System.out.println("Scraping page " + i + ":");
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                Kontroler.getInstance().scrape("http://freecode.com/?page=" + i);
                RDFPersistance.getInstance().flush();
                RDFPersistance.getInstance().serializeToFile();
                System.out.println("Model saved to: " + RDFPersistance.MODEL_FILENAME);
            } catch (IOException ex) {

                System.out.println("Error 404");
                continue;
            }
        }
    }
    
    public void scrapeSourceForge(String address) throws Exception {
        try{
        ScraperSF scraper = new ScraperSF(address);
        sourceForgeProjects = scraper.returnArticleList();
        if (sourceForgeProjects.isEmpty()) {
            throw new Exception("No more pages.");
        }
        for (Project pr : sourceForgeProjects) {
            pr.setUri(URIGenerator.generateSFUri(pr));
            Bean2RDF writer = RDFPersistance.getInstance().getWriter();
            writer.save(pr);
        }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

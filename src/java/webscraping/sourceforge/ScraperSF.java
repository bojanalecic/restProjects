/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.sourceforge;
import domain.Person;
import domain.Project;
import domain.Version;
import domain.Category;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import stringoperations.StringOperations;
import util.URIGenerator;
/**
 *
 * @author Lecic
 */
public class ScraperSF {
    
    String address = new String();
    Document doc;

    public ScraperSF(String address) throws IOException {
        this.address = address;
        Connection con = Jsoup.connect(address);
                con.timeout(30000);
        doc = con.get();
    }
    /**
     * scrapes categories of projects
     * @return list of categories
     */
    public List<Category> returnCategoryList(){
        List<Category> categories = new ArrayList<Category>();
        
        Elements categ  = doc.select(".facets li a");
        for (Element elem : categ) {
            categories.add(new Category(elem.attr("href").substring(11, elem.attr("href").length())));
        }
       return categories;
    }
    
    /*
     * scrapes projects for each category 
     * @return list of projects
     */
    public List<Project> returnArticleList() throws Exception {

        List<Project> articleList = new ArrayList<Project>();
        List<Category> categories = returnCategoryList();
        String categoryPage = "";
        for (int j = 7; j < categories.size(); j++) {
            Category categ = categories.get(j);
            System.out.println(categ.getName());
            categoryPage = "http://sourceforge.net/directory/" + categ.getName();
            Elements headlines = getHeadlines(categoryPage);
            for (int i = 0; i < headlines.size(); i+=2) {
                String title = headlines.get(i).attr("title").substring(20, headlines.get(i).attr("title").length());
            System.out.println("Scraping: " + title);
            String temp = headlines.get(i).attr("href").substring(10);
            String SFName = temp.substring(0, temp.indexOf('/'));
            ArrayList<String> category = new ArrayList<String>();
            category.add(categ.getName());
            Project a = new Project();
            a.setCategory(category);
            ArrayList<Version> verzije = getRealeases(SFName);
            if(verzije!=null){
            a.setRelease(verzije);
            }
            setDetails(a, SFName);
            articleList.add(a);
        }
            System.out.println("zavrsio kategoriju: "+categ);
            return articleList;
        }
        System.out.println("zavrsio sourceforge");
        
        return articleList;
    }

    /**
     * scrapes headlines
     * @param address
     * @return list of headlines for specified category
     * @throws IOException 
     */
    private Elements getHeadlines(String address) throws IOException {
        Connection con = Jsoup.connect(address);
                con.timeout(10000);
        doc = con.get();
        Elements headlines = doc.select(".projects li a");

        return headlines;
    }
/**
 *  sets details for each project
 * @param a project
 * @param SFName specific name of project for json file
 */
    private void setDetails(Project a, String SFName) {
        try {
            String jsonPage = "http://sourceforge.net/api/project/name/" + SFName +"/json";
            BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(jsonPage).openStream(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            
            JsonScraperSF js = new JsonScraperSF(json);
            js.setDetails(a);
        } catch (Exception ex) {
            Logger.getLogger(ScraperSF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }
/**
 * 
 * @param SFName specific name of project
 * @return list of realeases
 * @throws IOException
 * @throws URISyntaxException 
 */
    private ArrayList<Version> getRealeases(String SFName) throws IOException, URISyntaxException {
        String adresa = "http://sourceforge.net/projects/" + SFName;
        Connection con = Jsoup.connect(adresa);
        con.timeout(10000);
        doc = con.get();
        Elements sidebar = doc.select("#project-activity");
        if (sidebar.size() > 0){
        Elements realeasesEl = sidebar.get(0).getElementsByTag("li");
        ArrayList<Version> realeases = new ArrayList<Version>();
        for (Element e : realeasesEl) {
            String releaseAddress = "";

            String day = e.getElementsByClass("day").text();
            String monthStr = e.getElementsByClass("monthname").text();
            int month = returnNumberOfMonth(monthStr);
             DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dateD = day +"/"+month+"/2013";
            Date date = new Date();
            try {
               date = df.parse(dateD);
            } catch (ParseException ex) {
                Logger.getLogger(ScraperSF.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Parse date error.");
            }
            Elements versionName = e.getElementsByTag("a");
            String trueVersionName;
            if (versionName.size() > 0){
            String relAddress = versionName.get(0).attr("href");
            releaseAddress =  relAddress;
            trueVersionName = versionName.get(0).text();
            if(trueVersionName.indexOf('/') > -1){
            trueVersionName = trueVersionName.substring(trueVersionName.indexOf('/')+1);
            if(trueVersionName.indexOf('/') > -1){
            trueVersionName = trueVersionName.substring(0, trueVersionName.indexOf('/'));
            }
            }
            }else
                trueVersionName = "";
            Version version = new Version(trueVersionName, date, "No description...", new URI(releaseAddress));
            version.setUri(URIGenerator.generateSFUri(version));
            realeases.add(version);
        }
        

        return realeases;
        }else
            return null;
    }
/**
 * for 
 * @param month
 * @return 
 */
    private int returnNumberOfMonth(String month) {
       if (month.equals("Januar")) return 1;
           if (month.equals("Februar")) return 2;
           if (month.equals("Mart")) return 3;
           if (month.equals("April")) return 4;
           if (month.equals("Maj")) return 5;
           if (month.equals("Jun")) return 6;
           if (month.equals("Jul")) return 7;
           if (month.equals("Avgust")) return 8;
           if (month.equals("Septembar")) return 9;
           if (month.equals("Oktobar")) return 10;
           if (month.equals("Novembar")) return 11;
           if (month.equals("Decembar")) return 12;
           return 0;
    }

}

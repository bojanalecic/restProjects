/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.freecode;

import domain.Person;
import domain.Project;
import domain.Version;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import stringoperations.StringOperations;
import util.URIGenerator;

/**
 *
 * @author Boban
 */
public class Scraper {

    String address = new String();
    Document doc;

    public Scraper(String address) throws IOException {
        this.address = address;
        doc = Jsoup.connect(address).get();
    }

    public Elements getHeadlines() throws Exception {


        Elements possibleHeadlines = doc.select(".release-head a");

        Elements trueHeadlines = new Elements();

        for (Element element : possibleHeadlines) {

            String hrefval = element.attr("href");

            if (hrefval.substring(0, 3).equals("/pr")) {
                trueHeadlines.add(element);
            }

        }

        return trueHeadlines;

    }

    public Elements getDescription() throws Exception {


        Elements possibleDescriptions = doc.select(".description");
        //final HtmlDivision div = page.getHtmlElementById("globalContainer");
        Elements trueDescriptions = new Elements();
        for (Element element : possibleDescriptions) {
            trueDescriptions.add(element);
        }
        return trueDescriptions;

    }

    public Elements getChanges() throws Exception {


        Elements possibleChanges = doc.select(".changes");
        //final HtmlDivision div = page.getHtmlElementById("globalContainer");
        Elements trueChanges = new Elements();
        for (Element element : possibleChanges) {
            trueChanges.add(element);
        }
        return trueChanges;
    }

    public String getArticleAddress(Project a) {
        Elements possibleHeadlines = doc.select(".release-head a");



        for (Element element : possibleHeadlines) {

            String hrefval = element.attr("href");

            if (hrefval.substring(0, 3).equals("/pr")) {
                if (element.text().equals(a.getName())) {
                    //trebalo bi da se normalizuje adresa pre nego sto se poveze sa relativnom
                    String articleAddress = StringOperations.returnBaseAddress(address) + hrefval;
                    return articleAddress;
                }

            }

        }
        return null;
    }

//        public String returnData() throws Exception 
//        {
//            Elements headlines = getHeadlines();
//            Elements descriptions = getDescription();
//            Elements changes = getChanges();
//            
//            String returnString="";
//            
//            for (int i = 0; i < headlines.size(); i++) {
//                
//                returnString = returnString+headlines.get(i).text()+"\r\n\r\n";
//                returnString = returnString+descriptions.get(i).text()+"\r\n\r\n";
//                returnString = returnString+changes.get(i).text()+"\r\n\r\n";
//                
//               returnString=returnString+"************************************************\r\n";
//                
//            }
//            
//            return returnString;
//            
//        }
    public List<Project> returnArticleList() throws Exception {

        List<Project> articleList = new ArrayList<Project>();
        Elements headlines = getHeadlines();
        Elements descriptions = getDescription();
        Elements changes = getChanges();


        for (int i = 0; i < headlines.size(); i++) {
            System.out.println("Scraping: " + headlines.get(i).text());
            Project a = new Project();
            a.setName(headlines.get(i).text());
            a.setDescription(descriptions.get(i).text());


            a.setSeeAlso(new URI(getArticleAddress(a)));
            setArticleDetails(a);

            articleList.add(a);

        }

        return articleList;
    }

    private void setArticleDetails(Project a) throws IOException, Exception {

        DetailsScraper ds = new DetailsScraper(a.getSeeAlso().toString());


        Elements tagElements = ds.getTags();
        if (tagElements != null) {
            a.setCategory(returnElementCategoryList(tagElements));
        }


        Elements licensesElements = ds.getLicenses();
        if (licensesElements != null) {
            a.setLicense(returnElementLicenseList(licensesElements));
        }


        Elements operatingSystemElements = ds.getOperatingSystems();
        if (operatingSystemElements != null) {
            a.setOs(returnElementOSList(operatingSystemElements));
        }


        Elements implementationElements = ds.getImplementations();
        if (implementationElements != null) {
            a.setProgramminglanguages(returnElementImplementationList(implementationElements));
        }

        Element downloadLink = ds.getDownloadLink();
        if (downloadLink != null) {

            a.setDownloadpage(StringOperations.getInstance().vratiPraviLink(new URI(StringOperations.returnBaseAddress(address) + downloadLink.attr("href"))));
        }

        Element siteLink = ds.getSiteLink();
        if (siteLink != null) {
            a.setHomepage(StringOperations.getInstance().vratiPraviLink(new URI(StringOperations.returnBaseAddress(address) + siteLink.attr("href"))));
        }

        Elements releases = ds.getReleases();
        if (releases != null) {
            a.setRelease(returnVersionList(releases));
        }
        Element maintainer = ds.getMaintainer();
        if (maintainer != null) {
            Person p = new Person(maintainer.text(), StringOperations.getInstance().vratiPraviLink(new URI(StringOperations.returnBaseAddress(address) + maintainer.attr("href"))));
            p.setUri(URIGenerator.generateUri(p));
            a.setMaintainer(p);

        }

    }

    private List<Version> returnVersionList(Elements releases) throws Exception {

        ArrayList<Version> releaseList = new ArrayList<Version>();

        for (Element element : releases) {

            String version = "";
            String releaseAddress = "";
            String date = "";
            String releaseNote = "";

            try {
                Elements versionElement = element.select(".release a");
                version = versionElement.get(0).text();

                releaseAddress = StringOperations.returnBaseAddress(address) + versionElement.get(0).attr("href");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Element dateElement = element.child(0).child(1).child(1);
                date = dateElement.text();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Elements releaseNotesElement = element.select(".changes");
                releaseNote = releaseNotesElement.get(0).text().substring(15);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:ss");
            Date dateD = null;

            try {
                dateD = df.parse(date.substring(1));
            } catch (ParseException ex) {
                Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Parse date error.");
            }


            Version r = new Version(version, dateD, releaseNote, StringOperations.getInstance().vratiPraviLink(new URI(releaseAddress)));
            r.setUri(URIGenerator.generateUri(r));
            releaseList.add(r);

        }

        return releaseList;
    }

    private List<String> returnElementCategoryList(Elements someElements) throws Exception {

        ArrayList<String> elementList = new ArrayList<String>();

        for (Element element : someElements) {

            String a = element.text();

            elementList.add(a);

        }
        return elementList;
    }

//    private List<URI> returnElementLicenseList(Elements licensesElements) throws Exception {
//        ArrayList<URI> elementList = new ArrayList<URI>();
//
//        for (Element element : licensesElements) {
//            URI uri = StringOperations.getInstance().vratiPraviLink(new URI(StringOperations.returnBaseAddress(address) + element.attr("href")));
//           
//            elementList.add(uri);
//
//        }
//        return elementList;
//    }
    private List<String> returnElementLicenseList(Elements licensesElements) throws Exception {
        ArrayList<String> elementList = new ArrayList<String>();

        for (Element element : licensesElements) {
            //URI uri = StringOperations.getInstance().vratiPraviLink(new URI(StringOperations.returnBaseAddress(address) + element.attr("href")));
            String s = element.text();
            elementList.add(s);

        }
        return elementList;
    }

    private List<String> returnElementOSList(Elements operatingSystemElements) throws Exception {
        ArrayList<String> elementList = new ArrayList<String>();

        for (Element element : operatingSystemElements) {
            String os = element.text();

            elementList.add(os);

        }
        return elementList;
    }

    private List<String> returnElementImplementationList(Elements implementationElements) throws Exception {
        ArrayList<String> elementList = new ArrayList<String>();

        for (Element element : implementationElements) {
            String pr = element.text();

            elementList.add(pr);
        }
        return elementList;
    }
}

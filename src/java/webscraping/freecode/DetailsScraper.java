/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.freecode;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Boban
 */
public class DetailsScraper {

    String address = null;
    Document doc;

    public DetailsScraper(String address) throws IOException {
        this.address = address;
        doc = Jsoup.connect(address).get();
    }

    public Elements getTags() throws Exception {

        return getElements("Tags");


    }

    public Elements getLicenses() throws Exception {

        return getElements("Licenses");

    }

    public Elements getOperatingSystems() throws Exception {
        return getElements("Operating Systems");
    }

    public Elements getImplementations() throws Exception {
        return getElements("Implementation");
    }

    private Elements getElements(String string) {

        Elements possibleElements = doc.select("#project-tag-cloud");
        Elements foundedElements = new Elements();

        Element elementsTable = null;
        try {
            elementsTable = possibleElements.get(0).child(0);
            Elements tableRows = elementsTable.children();

            for (Element element : tableRows) {

                if (element.child(0).child(0).text().equals(string)) {

                    foundedElements = element.select("a");
                    return foundedElements;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No tags");
            return null;
        }
        return null;

    }

    public Element getDownloadLink() {
        try {
            Element downloadLink = doc.select(".downloadlink a").get(0);
            return downloadLink;


        } catch (Exception e) {
            System.out.println("No download link.");
            return null;
        }


    }

    public Element getSiteLink() {
        try {
            Element siteLink = doc.select(".moreinfolink a").get(0);
            return siteLink;


        } catch (Exception e) {
            System.out.println("No site link.");
            return null;
        }


    }

    public Elements getReleases() {

        Elements confirmedReleases = new Elements();

        try {
            Elements possibleReleases = doc.select(".release");
            int i = 0;
            for (Element object : possibleReleases) {
                if (object.attr("id").matches("release_(.*)")) {
                    confirmedReleases.add(object);
                } else {
                    continue;
                }

            }
        } catch (Exception e) {
            System.out.println("Bad release.");
            return null;
        }
        return confirmedReleases;
    }

    public Element getMaintainer() {
        try {
            Element maintainer = doc.select(".submitter a").get(1);

            return maintainer;


        } catch (Exception e) {
            System.out.println("No maintainer.");
            return null;
        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.sourceforge;

import domain.Person;
import domain.Project;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import stringoperations.StringOperations;

/**
 *
 * @author Lecic
 */
public class JsonScraperSF {
    JSONObject first;
    JSONObject datas;
    Project project;

    public JsonScraperSF(JSONObject first) {
        try {
            this.first = first;
            this.datas = first.getJSONObject("Project");
        } catch (JSONException ex) {
            Logger.getLogger(JsonScraperSF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setDetails(Project project) throws JSONException, URISyntaxException{
        project.setName(datas.getString("name"));
        project.setDescription(datas.getString("description"));
        project.setDownloadpage(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("download-page"))));
        project.setHomepage(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("homepage"))));
        project.setMaintainer(getMaintainer());
        project.setLicense(getLicenses());
        project.setOs(getOS());
        project.setProgramminglanguages(getProgrammingLanguages());
        project.setSeeAlso(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("homepage"))));
        project.setCategory(getCategories());
    }

    private Person getMaintainer() throws JSONException, URISyntaxException {
        JSONArray maintainersJson = datas.getJSONArray("maintainers");
        String name = maintainersJson.getJSONObject(0).getString("name");
        URI page = StringOperations.getInstance().vratiPraviLink(new URI(maintainersJson.getJSONObject(0).getString("homepage")));
        Person maintainer = new Person(name, page);
        return maintainer;
    }

    private Collection<String> getLicenses() throws JSONException {
        JSONArray licensesJson = datas.getJSONArray("licenses");
        ArrayList<String> licenses = new ArrayList<String>();
        for (int i = 0; i < licensesJson.length(); i++) {
            String name = licensesJson.getJSONObject(0).getString("name");
            licenses.add(name);
        }
        return licenses;
    }

    private Collection<String> getOS() throws JSONException {
        JSONArray osJson = datas.getJSONArray("os");
        ArrayList<String> os = new ArrayList<String>();
        for (int i = 0; i < osJson.length(); i++) {
            String name = osJson.get(i).toString();
            os.add(name);
        }
        return os;
    }

    private Collection<String> getProgrammingLanguages() throws JSONException {
        JSONArray plJson = datas.getJSONArray("programming-languages");
        ArrayList<String> pl = new ArrayList<String>();
        for (int i = 0; i < plJson.length(); i++) {
            String name = plJson.get(i).toString();
            pl.add(name);
        }
        return pl;
    }

    private Collection<String> getCategories() throws JSONException {
        JSONArray categoriesJson = datas.getJSONArray("categories");
        ArrayList<String> categories = new ArrayList<String>();
        for (int i = 0; i < categoriesJson.length(); i++) {
            String name = categoriesJson.get(i).toString();
            categories.add(name);
        }
        return categories;
    }
    
}

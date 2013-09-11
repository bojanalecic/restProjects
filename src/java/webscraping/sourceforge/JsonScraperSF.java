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
import util.URIGenerator;

/**
 *
 * @author Lecic
 */
public class JsonScraperSF {
    JSONObject first;
    JSONObject datas;
    /**
     * current project
     */
    Project project;

    public JsonScraperSF(JSONObject first) {
        try {
            this.first = first;
            this.datas = first.getJSONObject("Project");
        } catch (JSONException ex) {
            Logger.getLogger(JsonScraperSF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * sets all details for certain project
     * @param project
     * @throws JSONException
     * @throws URISyntaxException 
     */
    public void setDetails(Project project) throws JSONException, URISyntaxException{
        project.setName(datas.getString("name"));
        project.setDescription(datas.getString("description"));
        project.setDownloadpage(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("download-page"))));
        project.setHomepage(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("homepage"))));
        Person maintainer = getMaintainer();
        if (maintainer!=null){
        project.setMaintainer(getMaintainer());
        }
        ArrayList<String> licenses = getLicenses();
        if(licenses!=null){
        project.setLicense(getLicenses());
        }
        ArrayList<String> os = getOS();
        if(os!=null){
        project.setOs(getOS());
        }
        ArrayList<String> pl = getProgrammingLanguages();
        if(pl!=null){
        project.setProgramminglanguages(getProgrammingLanguages());
        }
        project.setSeeAlso(StringOperations.getInstance().vratiPraviLink(new URI(datas.getString("homepage"))));
    }
/**
 * 
 * @return maintainer for current project
 * @throws JSONException
 * @throws URISyntaxException 
 */
    private Person getMaintainer() throws JSONException, URISyntaxException {
         try{
            JSONArray maintainersJson = datas.getJSONArray("maintainers");
            String name = maintainersJson.getJSONObject(0).getString("name");
            URI page = StringOperations.getInstance().vratiPraviLink(new URI(maintainersJson.getJSONObject(0).getString("homepage")));
            Person maintainer = new Person(name, page);
            maintainer.setUri(URIGenerator.generateSFUri(maintainer));
            return maintainer;
        }catch(Exception ex){
            System.out.println("Greska pri ucitavanju maintainera...");
            return null;
        }
    }
/**
 * 
 * @return list of licenses for current project
 * @throws JSONException 
 */
     private ArrayList<String> getLicenses() throws JSONException {
        try{
            JSONArray licensesJson = datas.getJSONArray("licenses");
            ArrayList<String> licenses = new ArrayList<String>();
            for (int i = 0; i < licensesJson.length(); i++) {
                String name = licensesJson.getJSONObject(0).getString("name");
                licenses.add(name);
        }
        return licenses;
        }catch(Exception ex){
            System.out.println("Greska pri ucitavanju licenci...");
            return null;
       }
    }
     /**
      * 
      * @returnreturns list of operating systems for current project
      * @throws JSONException 
      */
    private ArrayList<String> getOS() throws JSONException {
        try{
            JSONArray osJson = datas.getJSONArray("os");
            ArrayList<String> os = new ArrayList<String>();
            for (int i = 0; i < osJson.length(); i++) {
                String name = osJson.get(i).toString();
                os.add(name);
            }
            return os;
        }catch(Exception ex){
            System.out.println("Greska pri ucitavanju operativnih sistema...");
            return null;
        }
    }
/**
 * 
 * @return list of programming languages for current project
 * @throws JSONException 
 */
    private ArrayList<String> getProgrammingLanguages() throws JSONException {
        try{
            JSONArray plJson = datas.getJSONArray("programming-languages");
            ArrayList<String> pl = new ArrayList<String>();
            for (int i = 0; i < plJson.length(); i++) {
                String name = plJson.get(i).toString();
                pl.add(name);
            }
            return pl;
        }catch(Exception ex){
            System.out.println("Greska pri ucitavanju programskih jezika...");
            return null;
        }
    }

}

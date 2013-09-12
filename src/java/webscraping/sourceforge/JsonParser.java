/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.sourceforge;

import domain.Project;
import domain.Version;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Lecic
 */
public class JsonParser {

    public static JSONObject serialize(Project project) {
        try {
            JSONObject projectJson = new JSONObject();

            JSONObject maintainer = new JSONObject();
            maintainer.put("name", project.getMaintainer().getName());
            maintainer.put("seeAlso", project.getMaintainer().getSeeAlso().toString());
            projectJson.put("maintainer", maintainer);
            projectJson.put("name", project.getName());
            projectJson.put("seeAlso", project.getSeeAlso().toString());
            projectJson.put("description", project.getDescription());
            projectJson.put("downloadpage", project.getDownloadpage().toString());
            projectJson.put("homepage", project.getHomepage().toString());
            String uri = project.getUri().toString();
            projectJson.put("id", uri.substring(uri.lastIndexOf("/") + 1, uri.length()));
            projectJson.put("uri", uri);
            projectJson.put("category", project.getCategory());
            projectJson.put("license", project.getLicense());
            projectJson.put("os", project.getOs());
            JSONArray releases = new JSONArray();
            for (Version v : project.getRelease()) {
                JSONObject release = new JSONObject();
                release.put("revision", v.getRevision());
                release.put("description", v.getDescription());
                release.put("seeAlso", v.getSeeAlso());
                release.put("created", v.getDate());
                releases.put(release);
            }
            projectJson.put("release", releases);
            projectJson.put("os", project.getOs());
            projectJson.put("programminglanguage", project.getProgramminglanguage());

            return projectJson;
        } catch (JSONException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

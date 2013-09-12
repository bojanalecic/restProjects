package service;

import domain.Project;
import domain.SearchString;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collection;
import javax.ejb.Stateless;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.query.QueryStore;
import webscraping.sourceforge.JsonParser;

/**
 * REST Web Service
 *
 * @author boban
 */
@Stateless
@Path("/projects")
public class Projects {

    private QueryStore queryStore = new QueryStore();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getXml() {
        return "{}";
    }

    @Path("/languages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAllProgrammingLanguages() {

        return queryStore.getAllProgrammingLanguages();
    }

    @Path("/oss")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAllOperatingSystems() {

        return queryStore.getAllOperatingSystems();
    }

    @Path("/licenses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAllLicenses() {

        return queryStore.getAllLicenses();
    }

    @Path("/tags")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAllTags() {

        return queryStore.getAllTags();
    }

    @Path("/projsearch")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public String findProjects(SearchString ss) throws URISyntaxException, ParseException, com.hp.hpl.jena.n3.turtle.parser.ParseException {
        Collection<Project> projects = queryStore.returnProjectsList(ss);
        if (projects != null && !projects.isEmpty()) {

            JSONArray projectsArray = new JSONArray();
            for (Project project : projects) {
                JSONObject projectJson = JsonParser.serialize(project);
                projectsArray.put(projectJson);
            }
            return projectsArray.toString();
        }
        return null;
    }
//    sluzila za testiranje izlaza - json-a
//    @Path("/rrr")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Collection<Project> getAllP() throws URISyntaxException, ParseException {
//          Collection<Project> projectList = queryService.returnProjectsList(new SearchString());
//           return projectList;
//        }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.query;

import com.hp.hpl.jena.n3.turtle.parser.ParseException;
import domain.Person;
import domain.Project;
import domain.SearchString;
import domain.Version;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import persistance.RDFPersistance;
import persistence.query.results.QueryResult;
import persistence.query.results.ResultsList;
import util.Constants;

/**
 *
 * @author Cirkovic
 */
public class QueryStore {

    QueryExecutor queryExecutor = new QueryExecutor();

    public Collection<Project> returnProjectsList(SearchString ss) throws URISyntaxException, ParseException, java.text.ParseException {

        String queryString =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT DISTINCT ?proj ?name ?see ?desc ?maint ?maintname ?maintsee ?down ?home \n"
                + "WHERE { { ?proj a doap:Project. \n"
                + "?proj doap:name ?name. \n"
       //         + "?proj doap:resource ?resource. \n"
                + "?proj rdfs:seeAlso ?see. \n"
                + " ?proj dc:description ?desc. \n"
                + "?proj doap:maintainer ?maint. \n"
                + "?maint foaf:name ?maintname. \n"
                + "?maint rdfs:seeAlso ?maintsee. \n "
                + "OPTIONAL {?proj doap:download-page ?down. }\n"
                + "OPTIONAL {?proj doap:homepage ?home.}\n ";

        if (ss.getOperatingSystem() != null) {
            queryString = queryString + "?proj doap:os ?os. \n";
        }
        if (ss.getLicense() != null) {
            queryString = queryString + "?proj doap:license ?lic. \n";
        }
        if (ss.getProgrlang() != null) {
            queryString = queryString + "?proj doap:programming-language ?prog. \n";
        }
        if (ss.getTag() != null) {
            queryString = queryString + "?proj doap:category ?tag. \n";
        }
        if (ss.getOperatingSystem() != null) {
            queryString = queryString + "FILTER (?os=\"" + ss.getOperatingSystem() + "\"). \n";
        }
        if (ss.getLicense() != null) {
            queryString = queryString + "FILTER (?lic=\"" + ss.getLicense() + "\"). \n";
        }
        if (ss.getProgrlang() != null) {
            queryString = queryString + "FILTER (?prog=\"" + ss.getProgrlang() + "\"). \n";
        }
        if (ss.getTag() != null) {
            queryString = queryString + "FILTER (?tag=\"" + ss.getTag() + "\"). \n";
        }
        queryString = queryString + " }.";
        if (ss.getKeyword() != null) {
            queryString = queryString + "{ { ?proj doap:name ?name. \n "
                    + "FILTER regex(?name, \"" + ss.getKeyword() + "\", \"i\") }\n"
                    + " UNION { ?proj dc:description ?desc. \n "
                    + "FILTER regex(?desc, \"" + ss.getKeyword() + "\", \"i\") } }.";
        }

        if (ss.getPage() <= 1) {
            queryString = queryString + " } ORDER BY ?name LIMIT 10";
        } else {
            int page = (ss.getPage() - 1) * 10;
            queryString = queryString + " } ORDER BY ?name LIMIT 10 \n OFFSET " + Integer.toString(page);
        }

        ResultsList projectList = queryExecutor.executeSelectSparqlQuery(queryString, RDFPersistance.getInstance().getDataModel());
        Collection<Project> listProject = new LinkedList<Project>();

        LinkedList<QueryResult> listaRez = (LinkedList<QueryResult>) projectList.getResultRecords();

        for (QueryResult queryResult : listaRez) {
            String name = queryResult.getQueryResult().get("name");
            String seeAlso = queryResult.getQueryResult().get("see");
            String description = queryResult.getQueryResult().get("desc");
            String download = queryResult.getQueryResult().get("down");
            String home = queryResult.getQueryResult().get("home");

            String maintname = queryResult.getQueryResult().get("maintname");
            String maintsee = queryResult.getQueryResult().get("maintsee");
            String perUri = queryResult.getQueryResult().get("maint");
            Person per;
            if (maintname != null && maintsee != null && perUri != null) {
                per = new Person(maintname.substring(0, maintname.indexOf("^^")), new URI(maintsee.replace("<", "").replace(">", "")));
                per.setUri(new URI(perUri.replace("<", "").replace(">", "")));
            } else {
                per = new Person("Unknown", new URI("http://www.google.com/"));
            }

            Project pr = new Project();
            pr.setMaintainer(per);
            pr.setName(name.substring(0, name.indexOf("^^")));


            pr.setSeeAlso(new URI(seeAlso.replace("<", "").replace(">", "")));
            if (description != null) {
                pr.setDescription(description.substring(0, description.indexOf("^^")));
            } else {
                pr.setDescription("Without description");
            }
            if (download != null) {
                pr.setDownloadpage(new URI(download.replace("<", "").replace(">", "")));
            } else {
                pr.setDownloadpage(new URI("http://www.google.com/"));
            }
            if (home != null) {
                pr.setHomepage(new URI(home.replace("<", "").replace(">", "")));
            } else {
                pr.setHomepage(new URI("http://www.google.com/"));
            }

            String proj = queryResult.getQueryResult().get("proj");
            pr.setUri(new URI(proj.replace("<", "").replace(">", "")));
            pr.setCategory(getProjectCategories(proj));
            pr.setLicense(getProjectLicenses(proj));
            pr.setOs(getProjectOperatingSystems(proj));
            pr.setProgramminglanguages(getProgrammingLanguages(proj));
            pr.setRelease(getProjectVersionList(proj));

            listProject.add(pr);
        }
        return listProject;
    }

    private Collection<String> getProjectCategories(String proj) {

        Collection<String> nameList = new LinkedList<String>();

        String queryString1 =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT ?tag \n"
                + "WHERE { ?proj a doap:Project. \n"
                + "?proj doap:category ?tag. \n"
                + "FILTER(?proj=<" + proj + ">) \n"
                + "}";

        Collection<String> list = queryExecutor.executeOneVariableSelectSparqlQuery(queryString1, "tag",
                RDFPersistance.getInstance().getDataModel());
        return list;
    }

    private Collection<String> getProjectLicenses(String proj) {
        Collection<String> nameList = new LinkedList<String>();

        String queryString1 =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT ?lic \n"
                + "WHERE { ?proj a doap:Project. \n"
                + "?proj doap:license ?lic. \n"
                + "FILTER(?proj=<" + proj + ">) \n"
                + "}";

        Collection<String> list = queryExecutor.executeOneVariableSelectSparqlQuery(queryString1, "lic",
                RDFPersistance.getInstance().getDataModel());
        return list;
    }

    private Collection<String> getProjectOperatingSystems(String proj) {

        Collection<String> nameList = new LinkedList<String>();

        String queryString1 =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT ?os \n"
                + "WHERE { ?proj a doap:Project. \n"
                + "?proj doap:os ?os. \n"
                + "FILTER(?proj=<" + proj + ">) \n"
                + "}";

        Collection<String> list = queryExecutor.executeOneVariableSelectSparqlQuery(queryString1, "os",
                RDFPersistance.getInstance().getDataModel());
        return list;
    }

    private Collection<String> getProgrammingLanguages(String proj) {
        Collection<String> nameList = new LinkedList<String>();

        String queryString1 =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT ?pl \n"
                + "WHERE { ?proj a doap:Project. \n"
                + "?proj doap:programming-language ?pl. \n"
                + "FILTER(?proj=<" + proj + ">) \n"
                + "}";

        Collection<String> list = queryExecutor.executeOneVariableSelectSparqlQuery(queryString1, "pl",
                RDFPersistance.getInstance().getDataModel());
        return list;
    }

    private Collection<Version> getProjectVersionList(String proj) throws URISyntaxException, ParseException, java.text.ParseException {
        String queryString1 =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT ?proj ?vers ?rev ?created ?desc ?see \n"
                + "WHERE { ?proj a doap:Project. \n"
                + "?proj doap:release ?vers. \n"
                + "?vers a doap:Version. \n"
                + "?vers doap:revision ?rev. \n"
                + "?vers dc:created ?created. \n"
                + "?vers dc:description ?desc. \n"
                + "?vers rdfs:seeAlso ?see. \n"
                + "FILTER(?proj=<" + proj + ">) \n"
                + "}";

        ResultsList versionList = queryExecutor.executeSelectSparqlQuery(queryString1, RDFPersistance.getInstance().getDataModel());
        Collection<Version> listVersion = new LinkedList<Version>();

        LinkedList<QueryResult> listaRez = (LinkedList<QueryResult>) versionList.getResultRecords();

        for (QueryResult queryResult : listaRez) {
            String revision = queryResult.getQueryResult().get("rev");
            String created = queryResult.getQueryResult().get("created");
            String desc = queryResult.getQueryResult().get("desc");
            String see = queryResult.getQueryResult().get("see");
            String vers = queryResult.getQueryResult().get("vers");

            String date = created.substring(0, created.indexOf("T"));
            String time = created.substring(created.indexOf("T") + 1, created.indexOf("Z"));

            DateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date realDate = format.parse(date + " " + time);

            Version ver = new Version();
            ver.setDate(realDate);
            ver.setRevision(revision.substring(0, revision.indexOf("^^")));
            // ver.setDate(new Date(created.substring(0, created.indexOf("^^"))));
            ver.setSeeAlso(new URI(see.replace("<", "").replace(">", "")));
            ver.setDescription(desc.substring(0, desc.indexOf("^^")));
            ver.setUri(new URI(vers.replace("<", "").replace(">", "")));

            listVersion.add(ver);
        }
        return listVersion;
    }

    public Collection<String> getAllProgrammingLanguages() {
        String queryString =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT DISTINCT ?lang \n"
                + "WHERE { ?x doap:programming-language ?lang }"
                + "ORDER BY ?lang";

        return queryExecutor.executeOneVariableSelectSparqlQuery(queryString, "lang",
                RDFPersistance.getInstance().getDataModel());
    }

    public Collection<String> getAllTags() {
        String queryString =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT DISTINCT ?tag \n"
                + "WHERE { ?x doap:category ?tag }"
                + "ORDER BY ?tag";

        return queryExecutor.executeOneVariableSelectSparqlQuery(queryString, "tag",
                RDFPersistance.getInstance().getDataModel());
    }

    public Collection<String> getAllLicenses() {
        String queryString =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT DISTINCT ?lic \n"
                + "WHERE { ?x doap:license ?lic }"
                + "ORDER BY ?lic";

        return queryExecutor.executeOneVariableSelectSparqlQuery(queryString, "lic",
                RDFPersistance.getInstance().getDataModel());
    }

    public Collection<String> getAllOperatingSystems() {
        String queryString =
                "PREFIX rdfs: <" + Constants.RDF_NS + "> \n"
                + "PREFIX doap: <" + Constants.DOAP_NS + "> \n"
                + "PREFIX foaf: <" + Constants.FOAF_NS + "> \n"
                + "PREFIX dc: <" + Constants.DC_NS + "> \n"
                + "PREFIX rep: <" + Constants.F_NS + "> \n"
                + "SELECT DISTINCT ?os \n"
                + "WHERE { ?x doap:os ?os }"
                + "ORDER BY ?os";

        return queryExecutor.executeOneVariableSelectSparqlQuery(queryString, "os",
                RDFPersistance.getInstance().getDataModel());
    }
}
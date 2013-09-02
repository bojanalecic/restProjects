/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;
import util.Constants;

/**
 *
 * @author Boban
 */
@Namespace(Constants.DOAP_NS)
@RdfType("Project")
public class Project extends Thing {

    @RdfProperty(Constants.DOAP_NS + "name")
    private String name;
    @RdfProperty(Constants.DC_NS + "description")
    private String description;
    @RdfProperty(Constants.RDF_NS + "seeAlso")
    private URI seeAlso;
    @RdfProperty(Constants.DOAP_NS + "download-page")
    private URI downloadpage;
    @RdfProperty(Constants.DOAP_NS + "homepage")
    private URI homepage;
    @RdfProperty(Constants.DOAP_NS + "category")
    private Collection<String> category;
    @RdfProperty(Constants.DOAP_NS + "license")
    private Collection<String> license;
    @RdfProperty(Constants.DOAP_NS + "programming-language")
    private Collection<String> programminglanguage;
    @RdfProperty(Constants.DOAP_NS + "os")
    private Collection<String> os;
    @RdfProperty(Constants.DOAP_NS + "release")
    private Collection<Version> release;
    @RdfProperty(Constants.DOAP_NS + "maintainer")
    private Person maintainer;

    

    public Project() {
    }

    public Project(String name, String description, URI adress, URI downloadLink, URI homePage, List<String> tags, List<String> licenses, List<String> programmingLanguages, List<String> operatingSystems, List<Version> releaseList) {
        this.name = name;
        this.description = description;

        this.seeAlso = adress;
        this.downloadpage = downloadLink;
        this.homepage = homePage;
        this.category = tags;
        this.license = licenses;
        this.programminglanguage = programmingLanguages;
        this.os = operatingSystems;
        this.release = releaseList;
    }
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the adress
     */
    public URI getSeeAlso() {
        return seeAlso;
    }

    /**
     * @param adress the adress to set
     */
    public void setSeeAlso(URI adress) {
        this.seeAlso = adress;
    }

    /**
     * @return the downloadLink
     */
    public URI getDownloadpage() {
        return downloadpage;
    }

    /**
     * @param downloadpage the downloadLink to set
     */
    public void setDownloadpage(URI downloadLink) {
        this.downloadpage = downloadLink;
    }

    /**
     * @return the homePage
     */
    public URI getHomepage() {
        return homepage;
    }

    /**
     * @param homePage the homePage to set
     */
    public void setHomepage(URI homePage) {
        this.homepage = homePage;
    }

    /**
     * @return the tags
     */
    public Collection<String> getCategory() {
        return category;
    }

    /**
     * @param tags the tags to set
     */
    public void setCategory(Collection<String> tags) {
        this.category = tags;
    }

    /**
     * @return the licenses
     */
    public Collection<String> getLicense() {
        return license;
    }

    /**
     * @param licenses the licenses to set
     */
    public void setLicense(Collection<String> licenses) {
        this.license = licenses;
    }

    /**
     * @return the programmingLanguages
     */
    public Collection<String> getProgramminglanguage() {
        return programminglanguage;
    }

    /**
     * @param programmingLanguages the programmingLanguages to set
     */
    public void setProgramminglanguages(Collection<String> programmingLanguages) {
        this.programminglanguage = programmingLanguages;
    }

    /**
     * @return the operatingSystems
     */
    public Collection<String> getOs() {
        return os;
    }

    /**
     * @param operatingSystems the operatingSystems to set
     */
    public void setOs(Collection<String> operatingSystems) {
        this.os = operatingSystems;
    }

    /**
     * @return the release
     */
    public Collection<Version> getRelease() {
        return release;
    }

    /**
     * @param release the release to set
     */
    public void setRelease(Collection<Version> releaseList) {
        this.release = releaseList;
    }

    /**
     * @return the maintainer
     */
    public Person getMaintainer() {
        return maintainer;
    }

    /**
     * @param maintainer the maintainer to set
     */
    public void setMaintainer(Person maintainer) {
        this.maintainer = maintainer;
    }
}

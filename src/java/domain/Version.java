/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.net.URI;
import java.util.Date;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;
import util.Constants;

/**
 *
 * @author Boban
 */
@Namespace(Constants.DOAP_NS)
@RdfType("Version")
public class Version extends Thing {

    @RdfProperty(Constants.DOAP_NS + "revision")
    private String revision;
    @RdfProperty(Constants.DC_NS + "created")
    private Date created;
    @RdfProperty(Constants.DC_NS + "description")
    private String description;
    @RdfProperty(Constants.RDF_NS + "seeAlso")
    private URI link;

    public Version() {
    }

    public Version(String version, Date date, String releaseNote, URI releaseAddress) {
        this.revision = version;
        this.created = date;
        this.description = releaseNote;
        this.link = releaseAddress;
    }

    /**
     * @return the name
     */
    public String getRevision() {
        return revision;
    }

    /**
     * @param name the name to set
     */
    public void setRevision(String version) {
        this.revision = version;
    }

    /**
     * @return the created
     */
    public Date getDate() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setDate(Date date) {
        this.created = date;
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
    public void setDescription(String releaseNote) {
        this.description = releaseNote;
    }

    /**
     * @return the link
     */
    public URI getSeeAlso() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setSeeAlso(URI releaseAddress) {
        this.link = releaseAddress;
    }

    @Override
    public String toString() {
        return this.getRevision();
    }
}

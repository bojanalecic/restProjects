/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.net.URI;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;
import util.Constants;

/**
 *
 * @author Boban
 */
@Namespace(Constants.FOAF_NS)
@RdfType("Person")
public class Person extends Thing {

    @RdfProperty(Constants.FOAF_NS + "name")
    private String name;
    @RdfProperty(Constants.RDF_NS + "seeAlso")
    private URI seeAlso;

    public Person() {
    }

    public Person(String name, URI address) {
        this.name = name;
        this.seeAlso = address;
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
     * @return the address
     */
    public URI getSeeAlso() {
        return seeAlso;
    }

    /**
     * @param address the address to set
     */
    public void setSeeAlso(URI address) {
        this.seeAlso = address;
    }
}

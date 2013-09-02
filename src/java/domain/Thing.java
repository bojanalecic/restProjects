/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.net.URI;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfType;

/**
 *
 * @author Boban
 */
@Namespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
@RdfType("Thing")
public class Thing {

    @Id
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}

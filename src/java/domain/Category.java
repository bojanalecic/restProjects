/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import thewebsemantic.RdfProperty;
import util.Constants;

/**
 *
 * @author Bojana
 */
public class Category extends Thing{
    
    @RdfProperty(Constants.DOAP_NS + "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }
        
}

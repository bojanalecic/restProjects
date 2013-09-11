package util;

import java.net.URI;
import java.util.UUID;

import domain.Thing;

public class URIGenerator {
        //makes URI for freecode projects
    public static URI generateUri(Thing resource) {
        String namespace = Constants.F_NS;
       String uriString = namespace + resource.getClass().getSimpleName() + "/" + UUID.randomUUID();
        return URI.create(uriString);
    }
    //makes URI for sourceforge projects
    public static URI generateSFUri(Thing resource) {
        String namespace = Constants.SF_NS;
       String uriString = namespace + resource.getClass().getSimpleName() + "/" + UUID.randomUUID();
        return URI.create(uriString);
    }
}

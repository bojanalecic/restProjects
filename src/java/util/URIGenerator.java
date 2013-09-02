package util;

import java.net.URI;
import java.util.UUID;

import domain.Thing;

public class URIGenerator {

    public static URI generateUri(Thing resource) {
        String namespace = Constants.F_NS;
//            if (resource.getClass().getSimpleName().equals("Project") || resource.getClass().getSimpleName().equals("Version"))
//                namespace = Constants.DOAP_NS;
        String uriString = namespace + resource.getClass().getSimpleName() + "/" + UUID.randomUUID();
        return URI.create(uriString);
    }
    
    public static URI generateSFUri(Thing resource) {
        String namespace = Constants.SF_NS;
//            if (resource.getClass().getSimpleName().equals("Project") || resource.getClass().getSimpleName().equals("Version"))
//                namespace = Constants.DOAP_NS;
        String uriString = namespace + resource.getClass().getSimpleName() + "/" + UUID.randomUUID();
        return URI.create(uriString);
    }
}

package persistance;

import thewebsemantic.Bean2RDF;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import util.Constants;

public class RDFPersistance {

    public static String MODEL_FILENAME = "database.rdf";
    private SDBDataProvider dataProvider;
    private Model model;
    private RDF2Bean reader;
    private Bean2RDF writer;
    private static RDFPersistance INSTANCE;

    private static class RDFPersistanceHolder {

        private static final RDFPersistance INSTANCE = new RDFPersistance();
    }

    public static RDFPersistance getInstance() {
        return RDFPersistanceHolder.INSTANCE;
    }

    protected RDFPersistance() {
    }

    public SDBDataProvider getDataProvider() {
        if (null == dataProvider) {
            dataProvider = new SDBDataProvider();
        }
        return dataProvider;
    }
//creates data model
    public Model getDataModel() {
        if (null == model) {
            System.out.println("Retrieving data model...");
            model = getDataProvider().getDataModel();
            model.setNsPrefix("rep", Constants.F_NS);
            model.setNsPrefix("doap", Constants.DOAP_NS);
            model.setNsPrefix("foaf", Constants.FOAF_NS);
            model.setNsPrefix("dc", Constants.DC_NS);
            model.setNsPrefix("rdfs", Constants.RDF_NS);

        }
        return model;
    }
    

    public RDF2Bean getReader() {
        if (reader == null) {
            reader = new RDF2Bean(getDataModel());
            reader.bindAll("domain");
        }
        return reader;
    }

    public Bean2RDF getWriter() {
        if (writer == null) {
            writer = new Bean2RDF(getDataModel());
        }
        return writer;
    }

    public void flush() {
        getDataProvider().flushDataModel(model);
    }
//stores data in file
    public void serializeToFile() {
        try {

            model.write(new FileOutputStream(MODEL_FILENAME), "TURTLE");
        } catch (FileNotFoundException e) {
            System.out.println("File not saved!");
        }


    }
    
}

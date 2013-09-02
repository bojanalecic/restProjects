package persistence.query.results;

import java.util.Collection;
import java.util.LinkedList;

public class ResultsList {

    /**
     * collection of all the results of a SELECT SPARQL query
     */
    private Collection<QueryResult> resultRecords;

    public ResultsList() {
        resultRecords = new LinkedList<QueryResult>();
    }

    public Collection<QueryResult> getResultRecords() {
        return resultRecords;
    }

    public void addQueryResult(QueryResult result) {
        resultRecords.add(result);
    }
}

package persistence.query.results;

import java.util.HashMap;
import java.util.Map;

public class QueryResult {

    /**
     * The map keeps one result of a SELECT SPARQL query; each map's entry
     * represents one variable-value pair: - key keeps the name of the variable;
     * - value stores the actual value of the variable
     */
    private Map<String, String> queryResult;

    public QueryResult() {
        queryResult = new HashMap<String, String>();
    }

    public Map<String, String> getQueryResult() {
        return queryResult;
    }

    public void addVariableValuePair(String var, String value) {
        getQueryResult().put(var, value);
    }
}

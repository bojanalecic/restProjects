package persistence.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import persistence.query.results.QueryResult;
import persistence.query.results.ResultsList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class QueryExecutor {

    public Collection<String> executeOneVariableSelectSparqlQuery(String query,
            String variable, Model model) {

        List<String> results = new LinkedList<String>();

        Query q = QueryFactory.create(query);
        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(q, model);
        ResultSet resultSet = qe.execSelect();

        while (resultSet.hasNext()) {
            QuerySolution solution = resultSet.nextSolution();
            RDFNode value = solution.get(variable);

            if (value.isLiteral()) {
                results.add(((Literal) value).getLexicalForm());
            } else {
                results.add(((Resource) value).getURI());
            }
        }

        qe.close();

        return results;
    }

    public ResultsList executeSelectSparqlQuery(String query, Model model) {

        ResultsList resCollection = new ResultsList();

        Query q = QueryFactory.create(query);
        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(q, model);
        ResultSet results = qe.execSelect();

        for (; results.hasNext();) {
            QuerySolution solution = results.nextSolution();
            Iterator<String> variables = solution.varNames();
            QueryResult queryResult = new QueryResult();

            while (variables.hasNext()) {
                String var = (String) variables.next();
                RDFNode valueNode = solution.get(var);
                if (valueNode != null) {
                    queryResult.addVariableValuePair(var, valueNode.toString());
                }
            }
            resCollection.addQueryResult(queryResult);
        }
        qe.close();

        return resCollection;
    }

    public Model executeDescribeSparqlQuery(String queryString,
            Model model) {

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        Model resultModel = qe.execDescribe();

        // Important - free up resources used running the query
        qe.close();

        return resultModel;
    }
}

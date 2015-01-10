package org.deri.grefine.reconcile.rdf.executors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONWriter;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

/**
 * @author fadmaa
 * query a remote SPARQL endpoint
 */
public class RemoteQueryExecutor implements QueryExecutor{
	protected String sparqlEndpointUrl;
	protected String defaultGraphUri;
	
	public RemoteQueryExecutor(String sparqlEndpointUrl,String defaultGraphUri) {
		this.sparqlEndpointUrl = sparqlEndpointUrl;
		this.defaultGraphUri = defaultGraphUri;
	}

	@Override
	public TupleQueryResult sparql(String sparql) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		Repository repo = new SPARQLRepository(sparqlEndpointUrl);
		repo.initialize();
		RepositoryConnection con = repo.getConnection();
		if(defaultGraphUri!=null){
			throw new RuntimeException("I removed the support for named graphs and forgot to add it back! shame!");
		}
		TupleQuery tupleQuery =  con.prepareTupleQuery(QueryLanguage.SPARQL, sparql);
		return tupleQuery.evaluate();
	}

	@Override
	public void save(String serviceId, FileOutputStream baseDir) throws IOException{
		//nothing to save... all data is external
	}
	
	@Override
	public void write(JSONWriter writer) throws JSONException {
		writer.object();
		writer.key("type"); writer.value("remote");
		writer.key("sparql-url"); writer.value(sparqlEndpointUrl);
		if(defaultGraphUri!=null){
			writer.key("default-graph-uri"); writer.value(defaultGraphUri);
		}
		writer.endObject();
	}

	@Override
	public void initialize(FileInputStream in) {
		//nothing to initialize
		
	}
}

package org.deri.grefine.reconcile.rdf.executors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONWriter;
import org.openrdf.query.TupleQueryResult;

/**
 * @author fadmaa
 * this interface takes care of executing SPARQL queries. the RDF to query might be a dump or a remote SPARQL endpoint. 
 * different implementation should take care of this. 
 */
public interface QueryExecutor {

	public TupleQueryResult sparql(String sparql) throws Exception;
	
	public void save(String serviceId, FileOutputStream out) throws IOException;
	public void write(JSONWriter writer)throws JSONException;
	public void initialize(FileInputStream in);
}

package org.deri.grefine.reconcile.rdf.factories;

import java.util.List;

import org.deri.grefine.reconcile.model.ReconciliationCandidate;
import org.deri.grefine.reconcile.model.ReconciliationRequest;
import org.deri.grefine.reconcile.model.SearchResultItem;
import org.json.JSONException;
import org.json.JSONWriter;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

/**
 * @author fadmaa
 * this interface provides SPARQL queries needed for reconciliation. These queries cannot be fully compliant with the the standard SPARQL 1.1 as
 * full-text search is needed and it is not part of the standard. so different implementations are needed to handle this peculiarity.  
 */
public interface SparqlQueryFactory {

	/**
	 * @param request
	 * @param searchPropertyUris
	 */
	public String getReconciliationSparqlQuery(ReconciliationRequest request, ImmutableList<String> searchPropertyUris);
	
	/**
	 * convert ResultSet into GRefineReconciliationResponse. the conversion depends on knowing how the query was phrased which is mainly affected by the method
	 * {@link #buildSelectClause(ImmutableList)}.  
	 * @param resultSet
	 * @param searchPropertyUris <i>ordered</i> list of properties used for fulltext search and for picking resource labels i.e. display name
	 * @param limit number of result items to wrap as the resultset might contain more
	 * @param matchThreshold minimum score to consider a candidate as a match
	 * @return list of candidates <em>ordered according to the score descendingly</em>
	 * @throws QueryEvaluationException 
	 */
	public List<ReconciliationCandidate> wrapReconciliationResultset(TupleQueryResult resultSet, String queryString, ImmutableList<String> searchPropertyUris, int limit, double matchThreshold) throws QueryEvaluationException;
	
	
	/**
	 * @param prefix
	 * @param limit
	 * @return sparql query for type autocomplete i.e. given this prefix give me a query to retrieve relevant classes 
	 */
	public String getTypeSuggestSparqlQuery(String prefix, int limit);
	public ImmutableList<SearchResultItem> wrapTypeSuggestResultSet(TupleQueryResult resultSet, String prefix, int limit) throws QueryEvaluationException; 
	
	public String getTypesOfEntitiesQuery(ImmutableList<String> entityUris);
	public Multimap<String, String> wrapTypesOfEntities(TupleQueryResult resultSet) throws QueryEvaluationException;
	
	public String getResourcePropertiesMapSparqlQuery(String resourceId, int limit);
	public Multimap<String, String> wrapResourcePropertiesMapResultSet(TupleQueryResult resultSet, String resourceId, int limit) throws QueryEvaluationException;
	public String getResourcePropertiesMapSparqlQuery(PreviewResourceCannedQuery cannedQuery, String resourceId);
	public Multimap<String, String> wrapResourcePropertiesMapResultSet(PreviewResourceCannedQuery cannedQuery, TupleQueryResult resultset) throws QueryEvaluationException;
	
	public String getSampleInstancesSparqlQuery(String typeId, ImmutableList<String> searchPropertyUris, int limit);
	public ImmutableList<SearchResultItem> wrapSampleInstancesResultSet(TupleQueryResult resultSet, String typeId, ImmutableList<String> searchPropertyUris, int limit) throws QueryEvaluationException; 
	
	public String getSampleValuesOfPropertySparqlQuery(String propertyUri, int limit);
	/**
	 * @param resultSet
	 * @param propertyUri
	 * @param limit
	 * @return List of array of strings. each array is of length 2... subject then object
	 * @throws QueryEvaluationException 
	 */
	public ImmutableList<String[]> wrapSampleValuesOfPropertyResultSet(TupleQueryResult resultSet, String propertyUri, int limit) throws QueryEvaluationException;
	
	public String getPropertySuggestSparqlQuery(String prefix, String typeUri, int limit);
	public String getPropertySuggestSparqlQuery(String prefix, int limit);
	public ImmutableList<SearchResultItem> wrapPropertySuggestResultSet(TupleQueryResult resultSet, String prefix, int limit) throws QueryEvaluationException;
	
	public String getEntitySearchSparqlQuery(String prefix ,ImmutableList<String> searchPropertyUris, int limit);
	public ImmutableList<SearchResultItem> wrapEntitySearchResultSet(TupleQueryResult resultSet, int limit) throws QueryEvaluationException;
	
	public void write(JSONWriter writer)throws JSONException;
}

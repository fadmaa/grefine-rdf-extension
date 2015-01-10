package org.deri.grefine.reconcile.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.grefine.reconcile.model.SearchResultItem;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;

public class ResultSetWrappingUtil {

	public static ImmutableList<SearchResultItem> resultSetToSearchResultList(TupleQueryResult resultSet) throws QueryEvaluationException{
		List<String> varNames = resultSet.getBindingNames();
		String idVar = varNames.get(0);
		String labelVar = varNames.get(1);
		String scoreVar = null;
		if(varNames.size()>2){
			scoreVar = varNames.get(2);
		}
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(resultSet.hasNext()){
			BindingSet sol = resultSet.next();
			Value nameLiteral = sol.getValue(labelVar);
			String name = nameLiteral==null?"":nameLiteral.stringValue();
			String id = sol.getValue(idVar).stringValue();
			double score = 0;
			if(scoreVar!=null){
				score = ((org.openrdf.model.Literal)sol.getValue(scoreVar)).doubleValue();
			}
			
			results.add(new SearchResultItem(id, name, score));
		}
		return ImmutableList.copyOf(results);
	}
	
	public static ImmutableList<SearchResultItem> resultSetToSearchResultListFilterDuplicates(TupleQueryResult resultSet, int limit) throws QueryEvaluationException{
		List<String> varNames = resultSet.getBindingNames();
		String idVar = varNames.get(0);
		String labelVar = varNames.get(1);
		String scoreVar = null;
		if(varNames.size()>2){
			scoreVar = varNames.get(2);
		}
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		Set<String> seen = new HashSet<String>();
		while(resultSet.hasNext()){
			BindingSet sol = resultSet.next();
			String id = sol.getValue(idVar).stringValue();
			if(seen.contains(id)){
				continue;
			}
			seen.add(id);
			Value nameLiteral = sol.getValue(labelVar);
			String name = nameLiteral==null?"":nameLiteral.stringValue();
			double score = 0;
			if(scoreVar!=null){
				score = ((org.openrdf.model.Literal)sol.getValue(scoreVar)).doubleValue();
			}
			
			results.add(new SearchResultItem(id, name, score));
			if(results.size()==limit){
				//got enough
				break;
			}
		}
		return ImmutableList.copyOf(results);
	}
	
	/**
	 * @param resultSet
	 * @param limit number of unique keys to include in the result
	 * @return LinkedHashMultimap keeps the order of key input, so order in the result set is retained
	 * the first variable in resultSet solutions is the key to the map ,the second is the value
	 * @throws QueryEvaluationException 
	 */
	public static LinkedHashMultimap<String, String> resultSetToMultimap(TupleQueryResult resultSet) throws QueryEvaluationException{
		LinkedHashMultimap<String,String> map = LinkedHashMultimap.create();
		List<String> varNames = resultSet.getBindingNames();
		if(varNames.size()!=2){
			throw new RuntimeException("resultSetToMultimap only accepts a resultset with exactly two variables in the solution");
		}
		String keyVar = varNames.get(0);
		String valVar = varNames.get(1);
		while(resultSet.hasNext()){
			BindingSet sol = resultSet.next();
			
			String key = sol.getValue(keyVar).stringValue();
			String val = sol.getValue(valVar).stringValue();
			map.put(key, val);
		}
		return map;
	}
	
	public static List<String> resultSetToList(TupleQueryResult resultset, String varName) throws QueryEvaluationException{
		List<String> result = new ArrayList<String>();
		while(resultset.hasNext()){
			BindingSet sol = resultset.next();
			String uri = sol.getValue(varName).stringValue();
			result.add(uri);
		}
		return result;
	}
	
	public static ImmutableList<String[]> resultSetToListOfPairs(TupleQueryResult resultset) throws QueryEvaluationException{
		List<String> varNames = resultset.getBindingNames();
		String var1 = varNames.get(0);
		String var2 = varNames.get(1);
		List<String[]> result = new ArrayList<String[]>();
		while(resultset.hasNext()){
			BindingSet sol = resultset.next();
			String s = sol.getValue(var1).stringValue();
			String o = sol.getValue(var2).stringValue();
			result.add(new String[]{s,o});
		}
		return ImmutableList.copyOf(result);
	}
}

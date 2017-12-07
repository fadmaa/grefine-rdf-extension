package org.deri.orefine.rdf.vocab;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.deri.orefine.rdf.commands.vocab.PrefixExistsException;

public class ProjectPrefixes {
	private Map<String, Vocabulary> prefixesMap = new HashMap<String, Vocabulary>();

	public void loadPredefinedVocabs(String projectId) throws IOException {
		this.prefixesMap = PredefinedVocabularies.getPredefinedVocabulariesAsMap();
		// index these vocabularies
		for(Vocabulary v : this.prefixesMap.values()) {
			VocabularyImporter.importAndIndexVocabulary(v, VocabularyIndexer.singleton, projectId);
		}
	}
	
	public Map<String, Vocabulary> getPrefixesMap() {
		return prefixesMap;
	}
	
	public void setPrefixesMap(Map<String, Vocabulary> prefixesMap) {
		this.prefixesMap = prefixesMap;
	}

	public void addPrefix(String name, String uri, String fetchUrl) throws PrefixExistsException{
    	synchronized(prefixesMap){
    		if(this.prefixesMap.containsKey(name)){
    			throw new PrefixExistsException(name + " already defined");
    		}
    		this.prefixesMap.put(name, new Vocabulary(name, uri, fetchUrl));
    	}
    }
    
    public void removePrefix(String name){
    	this.prefixesMap.remove(name);
    }
    
}

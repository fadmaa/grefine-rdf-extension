package org.deri.orefine.rdf.vocab;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.deri.orefine.rdf.commands.vocab.PrefixExistsException;

public class ProjectPrefixes {
	private Map<String, Vocabulary> prefixesMap = new HashMap<String, Vocabulary>();

	public void loadPredefinedVocabs(String projectId) throws IOException {
		this.prefixesMap = PredefinedVocabularies.singleton.prefixesMap;
		// add the index of these vocabularies to this project
		// these vocabs have been loaded in the global project with id "g" so just copy the index
		VocabularyIndexer.singleton.addPredefinedVocabulariesToProject(projectId);
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

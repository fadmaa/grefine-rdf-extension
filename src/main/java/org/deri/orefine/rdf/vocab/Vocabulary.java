package org.deri.orefine.rdf.vocab;

import org.json.JSONException;
import org.json.JSONWriter;

public class Vocabulary {
	private final String name;
	private final String uri;
	private final String fetchUrl;

    public Vocabulary(String name, String uri, String fetchUrl){
    	this.name = name;
    	this.uri = uri;
    	this.fetchUrl = fetchUrl;
    }
    

	public String getName() {
		return name;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getFetchUrl() {
		return fetchUrl;
	}
	
    public void write(JSONWriter writer)throws JSONException {
        writer.object();
        writer.key("name"); writer.value(name);
        writer.key("uri"); writer.value(uri);
        writer.key("fetchUrl"); writer.value(fetchUrl);
        writer.endObject();
    }

	@Override
	public int hashCode() {
		return name.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		if(obj.getClass().equals(this.getClass())){
			Vocabulary v2 = (Vocabulary) obj;
			return name.equals(v2.getName());
		}
		return false;
	}
    
    

}
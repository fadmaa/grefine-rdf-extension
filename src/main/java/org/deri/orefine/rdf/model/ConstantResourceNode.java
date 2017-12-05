package org.deri.orefine.rdf.model;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.deri.orefine.rdf.Utils;
import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.model.Project;
import com.google.refine.model.Row;

public class ConstantResourceNode extends ResourceNode{

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ConstantResourceNode(String uri){
        this.uri = uri;
    }

	@Override
	protected void writeNode(JSONWriter writer) throws JSONException {
		writer.key("nodeType"); writer.value("resource");
        writer.key("value"); writer.value(uri);	
	}

	@Override
    public Resource[] createResource(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks) {
		if(this.uri == null || this.uri.isEmpty()){
			return null;
		} else {
			return new Resource[] { model.createResource(Utils.resolveUri(baseUri, this.uri)) };
		}
	}
}

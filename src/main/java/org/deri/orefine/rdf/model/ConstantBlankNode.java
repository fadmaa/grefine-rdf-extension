package org.deri.orefine.rdf.model;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.model.Project;
import com.google.refine.model.Row;

public class ConstantBlankNode extends ResourceNode{

    private int id;
    
    public ConstantBlankNode(int id){
        this.id = id;
    }

	@Override
	protected void writeNode(JSONWriter writer) throws JSONException {
		writer.key("nodeType"); writer.value("blank");
	}

	@Override
    public Resource[] createResource(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks) {
		// we don't create a new node because this is a constant bnode, just retrieve the node by its position in the pre-created constant blanks
		return new Resource[]{ blanks[this.id] };
	}
}

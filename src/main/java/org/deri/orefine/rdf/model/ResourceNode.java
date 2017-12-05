package org.deri.orefine.rdf.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.deri.orefine.rdf.Utils;
import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.model.Project;
import com.google.refine.model.Row;

abstract public class ResourceNode implements Node {

    private List<Link> links = new ArrayList<Link>();
    
    public List<Link> getLinks() {
		return links;
	}

	private List<RdfType> rdfTypes = new ArrayList<RdfType>();
    
    public void addLink(Link link) {
        this.links.add(link);
    }

    public void addType(RdfType type) {
        this.rdfTypes.add(type);
    }

    public Link getLink(int index) {
        return this.links.get(index);
    }

    public int getLinkCount() {
        return this.links.size();
    }

    public List<RdfType> getTypes() {
        return this.rdfTypes;
    }

    protected abstract void writeNode(JSONWriter writer) throws JSONException;
    @Override
    public void write(JSONWriter writer, Properties options)throws JSONException{
        writer.object();
        //writer node
        writeNode(writer);
        //write types
        writer.key("rdfTypes");
        writer.array();
        for(RdfType type:this.getTypes()){
            writer.object();
            writer.key("uri");writer.value(type.uri);
            writer.key("curie");writer.value(type.curie);
            writer.endObject();
        }
        writer.endArray();
        //write links
        writer.key("links");
        writer.array();
        for(int i=0;i<getLinkCount();i++){
            Link l = getLink(i);
            l.write(writer,options);
        }
        writer.endArray();
        
        writer.endObject();
        
    }
    
    public void setTypes(List<RdfType> types) {
        this.rdfTypes = types;
    }
    
    public RDFNode[] create(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks){
    	Resource[] r = createResource(model, baseUri, project, row, rowIndex, blanks);
        if(r == null){
            return null;
        }
       	addTypes(r, model, baseUri);
        return addLinks(r, model, baseUri, project, row, rowIndex, blanks);
    }

    protected void addTypes(Resource[] rs, Model model, URI baseUri) {
    	for(Resource r:rs){
    		for(RdfType type : this.getTypes()){
    			model.add(r, RDF.type, model.createResource(Utils.resolveUri(baseUri, type.uri)));
    		}
    	}
    }
    
    protected Resource[] addLinks(Resource[] rs, Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks) {
   		for(int i=0; i < getLinkCount(); i += 1){
           	Link l = getLink(i);
           	Property p = model.createProperty(Utils.resolveUri(baseUri, l.propertyUri));
           	RDFNode[] os = l.target.create(model, baseUri, project, row, rowIndex, blanks);
           	if(os!=null){
           		for(RDFNode o : os){
           			for(Resource r:rs){
           				model.add(r, p, o);
           			}
           		}
           	}
       	}
        return rs;
    }
    
    public abstract Resource[] createResource(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks) ;
    
    public static class RdfType{
        String uri;
        public String getUri() {
			return uri;
		}
		String curie;
        public RdfType(String uri,String curie){
            this.uri = uri;
            this.curie = curie;
        }
    }
}

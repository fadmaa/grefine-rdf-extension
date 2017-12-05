package org.deri.orefine.rdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import com.google.refine.browsing.Engine;
import com.google.refine.browsing.FilteredRows;
import com.google.refine.browsing.RowVisitor;
import com.google.refine.exporters.WriterExporter;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.deri.orefine.rdf.model.Node;
import org.deri.orefine.rdf.vocab.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdfExporter implements WriterExporter{

    private String format;
    final static Logger logger = LoggerFactory.getLogger("RdfExporter");

	public RdfExporter(String f){
        this.format = f;
    }
	
	private RdfSchema getRdfSchema(Project project) {
		return (RdfSchema) project.overlayModels.get("rdfSchema");
	}
	
	@Override
	public void export(Project project, Properties options, Engine engine,
					   Writer writer) throws IOException {
		RdfSchema schema = getRdfSchema(project);
		if (schema == null) {
			throw new RuntimeException("RDF Schema is not defined");
		}
		
		// create an empty model
	    Model model = ModelFactory.createDefaultModel();
		// register namespaces
		for(Vocabulary v : schema.prefixesMap.values()) {
			model.setNsPrefix(v.getName(), v.getUri());
	    }
		// prepare all constant blank nodes
		Resource[] blanks = new Resource[schema.blanks.size()];
		for (int i = 0; i < blanks.length; i++) {
			blanks[i] = model.createResource();
		}
		
		// export row by row 
		FilteredRows filteredRows = engine.getAllFilteredRows();
		RowVisitor visitor = new RowVisitor() {

			@Override
			public void start(Project project) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean visit(Project project, int rowIndex, Row row) {
				for(Node root : schema.rootNodes){
					root.create(model, schema.baseUri, project, row, rowIndex, blanks);
				}
				return false; // continue visiting, true will stop the tierator
			}

			@Override
			public void end(Project project) {
				model.write(writer);
			}
			
		};
		
        filteredRows.accept(project, visitor);
    }

    @Override
    public String getContentType() {
        if(format.equals("ttl")){
            return "text/turtle";
        }else{
            return "application/rdf+xml";
        }
    }
}

package org.deri.orefine.rdf.commands;

import com.google.refine.browsing.Engine;
import com.google.refine.browsing.FilteredRows;
import com.google.refine.browsing.RowVisitor;
import com.google.refine.commands.Command;
import com.google.refine.model.Project;
import com.google.refine.model.Row;
import com.google.refine.util.ParsingUtilities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.deri.orefine.rdf.RdfSchema;
import org.deri.orefine.rdf.model.Node;
import org.deri.orefine.rdf.vocab.Vocabulary;
import org.json.JSONObject;
import org.json.JSONWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

public class PreviewRdfCommand extends Command {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Project project = getProject(request);
            Engine engine = getEngine(request, project);

            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");

            String jsonString = request.getParameter("schema");
            JSONObject json = ParsingUtilities.evaluateJsonStringToObject(jsonString);
            final RdfSchema schema = RdfSchema.reconstruct(json);

	        StringWriter sw = new StringWriter();
	     // create an empty model
		    Model model = ModelFactory.createDefaultModel();
			// register namespaces
			for(Vocabulary v : schema.getPrefixes().getPrefixesMap().values()) {
				model.setNsPrefix(v.getName(), v.getUri());
		    }
			// prepare all constant blank nodes
			Resource[] blanks = new Resource[schema.getBlanks().size()];
			for (int i = 0; i < blanks.length; i++) {
				blanks[i] = model.createResource();
			}
			
			// export row by row 
			FilteredRows filteredRows = engine.getAllFilteredRows();
			RowVisitor visitor = new RowVisitor() {
				private int count = 0;
				@Override
				public void start(Project project) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean visit(Project project, int rowIndex, Row row) {
					for(Node root : schema.getRootNodes()){
						root.create(model, schema.getBaseUri(), project, row, rowIndex, blanks);
					}
					count += 1;
					return count > 10; // continue visiting until we have seen 10 rows
				}

				@Override
				public void end(Project project) {
					model.write(sw, "TURTLE");
				}
				
			};
			
	        filteredRows.accept(project, visitor);
            JSONWriter writer = new JSONWriter(response.getWriter());
            writer.object();
            writer.key("v");
            writer.value(sw.getBuffer().toString());
            writer.endObject();
        }catch (Exception e) {
            respondException(response, e);
        }
    }
}

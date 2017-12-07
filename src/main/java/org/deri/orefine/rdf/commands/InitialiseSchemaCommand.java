package org.deri.orefine.rdf.commands;

import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.deri.orefine.rdf.RdfSchema;
import org.deri.orefine.rdf.Utils;
import com.google.refine.commands.Command;
import com.google.refine.model.Project;

public class InitialiseSchemaCommand extends Command {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String base = request.getParameter("baseURI");
	        String projectId = request.getParameter("project");
	        Project project = getProject(request);
			URI baseUri = Utils.buildURI(base);
			RdfSchema schema = new RdfSchema(baseUri);
            project.overlayModels.put("rdfSchema", schema);
			schema.getPrefixes().loadPredefinedVocabs(projectId);;
			respondJSON(response, schema);
		} catch (Exception e) {
			respondException(response, e);
			return;
		}
	}
}

package org.deri.orefine.rdf.commands.vocab;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.deri.orefine.rdf.RdfSchema;
import com.google.refine.commands.Command;
import com.google.refine.model.Project;

public class RemovePrefixCommand extends Command{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name").trim();
        try {
        	Project project = getProject(request);
        	((RdfSchema) project.overlayModels.get("rdfSchema")).removePrefix(name);
        		// TODO remove vocabulary terms from the index  
        	response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
        	respond(response,"{\"code\":\"ok\"}");
        } catch (Exception e) {
        	respondException(response, e);
        }
    }
}

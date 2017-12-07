package org.deri.orefine.rdf.commands.vocab;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deri.orefine.rdf.RdfSchema;
import org.deri.orefine.rdf.vocab.Vocabulary;
import org.deri.orefine.rdf.vocab.VocabularyImporter;
import org.deri.orefine.rdf.vocab.VocabularyIndexer;

import com.google.refine.commands.Command;
import com.google.refine.model.Project;

public class AddPrefixCommand extends Command{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name").trim();
        String uri = request.getParameter("uri").trim();
        String fetchOption = request.getParameter("fetch");
        try {
        	Project project = getProject(request);
        	String fetchUrl = request.getParameter("fetch-url");
    		if(fetchUrl==null || fetchOption.trim().isEmpty()){
    			fetchUrl = uri;
    		}
    		RdfSchema schema = ((RdfSchema) project.overlayModels.get("rdfSchema"));
        	schema.getPrefixes().addPrefix(name, uri, fetchUrl);
        	if(fetchOption.equals("web")){
        		VocabularyImporter.importAndIndexVocabulary(new Vocabulary(name, uri, fetchUrl), VocabularyIndexer.singleton, String.valueOf(project.id));
        	}
        	response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
        	respond(response,"{\"code\":\"ok\"}");
        } catch (PrefixExistsException e) {
            respondException(response, e);
        } catch (Exception e){
        	respondException(response, e);
        }
    }
}

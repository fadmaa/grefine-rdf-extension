package org.deri.orefine.rdf.commands.vocab;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.deri.orefine.rdf.vocab.Vocabulary;
import org.deri.orefine.rdf.vocab.VocabularyImporter;
import org.deri.orefine.rdf.vocab.VocabularyIndexer;
import com.google.refine.commands.Command;

public class RefreshPrefixCommand extends Command{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String uri = request.getParameter("uri");
		String fetchUrl = request.getParameter("fetchUrl");
		String projectId = request.getParameter("project");
		
		// remove vocab terms
		VocabularyIndexer.singleton.deleteTermsOfVocab(name, projectId);
		try{
			VocabularyImporter.importAndIndexVocabulary(new Vocabulary(name, uri, fetchUrl), VocabularyIndexer.singleton, projectId);
			response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
        	respond(response,"{\"code\":\"err\"}");
        } catch (Exception e){
			respondException(response, e);
        }
	}
}

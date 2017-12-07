package org.deri.orefine.rdf.commands.vocab;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.deri.orefine.rdf.vocab.SearchResultItem;
import org.deri.orefine.rdf.vocab.VocabularyIndexer;
import org.json.JSONWriter;
import com.google.refine.commands.Command;

public class SuggestTermCommand extends Command {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// type will hold the project Id. parameters names are defined by the
		// JavaScript library.
		String projectId = request.getParameter("project");
		response.setHeader("Content-Type", "application/json");
		JSONWriter writer = new JSONWriter(response.getWriter());
		String type = request.getParameter("type");
		String query = request.getParameter("prefix");

		try {
			writer.object();

			writer.key("prefix");
			writer.value(query);

			writer.key("result");
			writer.array();
			List<SearchResultItem> nodes;
            if(type!=null && type.trim().equals("property")){
                nodes = VocabularyIndexer.singleton.searchProperties(query, projectId);
            }else{
                nodes = VocabularyIndexer.singleton.searchClasses(query, projectId);
            }
            /*
            if(nodes.size()==0){
            	RdfSchema schema = Util.getProjectSchema(getRdfContext(),getProject(request));
            	nodes = search(schema,query);
            }
            */
            for(SearchResultItem c:nodes){
                c.writeAsSearchResult(writer);
            }
			writer.endArray();
			writer.endObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

}

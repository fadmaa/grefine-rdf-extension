package org.deri.orefine.rdf.commands.vocab;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deri.orefine.rdf.vocab.PrefixesCC;
import org.json.JSONWriter;

import com.google.refine.commands.Command;

public class SuggestPrefixUriCommand extends Command {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String prefix = request.getParameter("prefix");
		String uri = PrefixesCC.singleton.getUri(prefix);
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "application/json");
			JSONWriter writer = new JSONWriter(response.getWriter());
			writer.object();
			writer.key("code");
			writer.value("ok");
			writer.key("uri");
			writer.value(uri);
			writer.endObject();
		} catch (Exception e) {
			respondException(response, e);
		}
	}
}

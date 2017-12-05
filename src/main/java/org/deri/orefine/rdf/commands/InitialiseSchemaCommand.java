package org.deri.orefine.rdf.commands;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deri.orefine.rdf.RdfSchema;
import org.deri.orefine.rdf.Utils;

import com.google.refine.commands.Command;

public class InitialiseSchemaCommand extends Command {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String base = request.getParameter("baseURI");
			URI baseUri = Utils.buildURI(base);
			RdfSchema schema = new RdfSchema(baseUri);
			respondJSON(response, schema);
		} catch (Exception e) {
			respondException(response, e);
			return;
		}
	}
}

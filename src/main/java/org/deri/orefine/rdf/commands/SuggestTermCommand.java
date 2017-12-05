package org.deri.orefine.rdf.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONWriter;

import com.google.refine.commands.Command;

public class SuggestTermCommand extends Command {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// type will hold the project Id. parameters names are defined by the
		// JavaScript library.
		String projectId = request.getParameter("type");

		response.setHeader("Content-Type", "application/json");

		JSONWriter writer = new JSONWriter(response.getWriter());
		String type = request.getParameter("type_strict");

		String query = request.getParameter("prefix");

		try {
			writer.object();

			writer.key("prefix");
			writer.value(query);

			writer.key("result");
			writer.array();
			writer.endArray();
			writer.endObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

}

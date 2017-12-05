package org.deri.orefine.rdf.model;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.refine.Jsonizable;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

public interface Node extends Jsonizable{
	public RDFNode[] create(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks);
}

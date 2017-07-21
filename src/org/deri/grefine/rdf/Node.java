package org.deri.grefine.rdf;

import java.net.URI;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import com.google.refine.Jsonizable;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

public interface Node extends Jsonizable{
    Value[] createNode(URI baseUri,ValueFactory factory,RepositoryConnection con,Project project,Row row,int rowIndex,BNode[] blanks);
}

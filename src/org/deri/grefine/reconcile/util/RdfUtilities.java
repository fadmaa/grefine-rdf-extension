package org.deri.grefine.reconcile.util;

import org.apache.jena.rdf.model.Model;

public interface RdfUtilities {
	public Model dereferenceUri(String uri);
}

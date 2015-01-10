package org.deri.grefine.reconcile.util;

import org.openrdf.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdfUtilitiesImpl implements RdfUtilities{
	final static Logger logger = LoggerFactory.getLogger("RdfUtilities");
	@Override
	public Repository dereferenceUri(String uri) {
		//TODO
		throw new RuntimeException("forgot to add back the support for URI dereferencing");
	}

}

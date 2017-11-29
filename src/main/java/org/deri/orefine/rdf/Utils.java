package org.deri.orefine.rdf;

import java.net.URI;
import java.net.URISyntaxException;

public class Utils {

	public static URI buildURI(String uri) {
		try {
			URI baseUri = new URI(uri);
			return baseUri;
		} catch (URISyntaxException e) {
			throw new RuntimeException("malformed Base URI " + uri, e);
		}
	}
}

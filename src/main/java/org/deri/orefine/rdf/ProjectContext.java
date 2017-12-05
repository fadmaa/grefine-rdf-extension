package org.deri.orefine.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.deri.orefine.rdf.vocab.PrefixManager;

public class ProjectContext {

	private File workingDir;
	private PrefixManager prefixManager;
	
	protected void init(File workingDir) throws IOException, JSONException{
		this.workingDir = workingDir;
		InputStream in = this.getClass().getResourceAsStream("/files/prefixes");
		this.prefixManager = new PrefixManager(in);
		in.close();
	}

	public PrefixManager getPrefixManager() {
		return prefixManager;
	}	
}

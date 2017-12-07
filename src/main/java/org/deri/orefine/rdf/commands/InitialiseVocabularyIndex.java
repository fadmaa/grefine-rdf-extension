package org.deri.orefine.rdf.commands;

import java.io.File;

import org.deri.orefine.rdf.vocab.VocabularyIndexer;

import com.google.refine.RefineServlet;
import com.google.refine.commands.Command;

public class InitialiseVocabularyIndex extends Command {

	@Override
	public void init(RefineServlet servlet) {
		super.init(servlet);
		File workingDir = servlet.getCacheDir("rdfExtension/export");
		try {
			VocabularyIndexer.initialise(workingDir.getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException("RDF Extension failed to initialiase Lucene index", e);
		}
	}

}

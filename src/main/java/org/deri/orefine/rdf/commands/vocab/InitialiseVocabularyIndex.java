package org.deri.orefine.rdf.commands.vocab;

import java.io.File;

import org.deri.orefine.rdf.vocab.PredefinedVocabularies;
import org.deri.orefine.rdf.vocab.Vocabulary;
import org.deri.orefine.rdf.vocab.VocabularyImporter;
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
			// check if pre-defined vocabs have been imported
			// if not, import them
			if( !VocabularyIndexer.singleton.globalVocabsIndexed()) {
				for(Vocabulary v: PredefinedVocabularies.singleton.prefixesMap.values()) {
					VocabularyImporter.importAndIndexVocabulary(v, VocabularyIndexer.singleton, VocabularyIndexer.GLOBAL_VOCABULARY_PLACE_HOLDER);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("RDF Extension failed to initialiase Lucene index", e);
		}
	}

}

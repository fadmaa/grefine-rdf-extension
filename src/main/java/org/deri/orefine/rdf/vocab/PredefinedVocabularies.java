package org.deri.orefine.rdf.vocab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredefinedVocabularies {
	final static Logger logger = LoggerFactory.getLogger("predefined_vocabulary_manager");
	private static final String PREDEFINED_VOCABS_FILE_NAME = "files/predefined_vocabs.tsv";

	public static Map<String, Vocabulary> getPredefinedVocabulariesAsMap() throws IOException {
		Map<String, Vocabulary> map = new HashMap<String, Vocabulary>();
		for(Vocabulary v: getPredefinedVocabularies()){
			map.put(v.getName(), v);
		}
		return map;
	}
	
	public static List<Vocabulary> getPredefinedVocabularies() throws IOException {
		List<Vocabulary> predefinedVocabularies = new ArrayList<Vocabulary>();
		try {
			InputStream in = getPredefinedVocabularyFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringTokenizer tokenizer;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				tokenizer = new StringTokenizer(strLine, "\t");

				String name = tokenizer.nextToken();
				String uri = tokenizer.nextToken();
				String fetchUrl = tokenizer.nextToken();
				predefinedVocabularies.add(new Vocabulary(name, uri, fetchUrl));

			}
			br.close();

		} catch (Exception e) {
			// predefined vocabularies are not defined properly
			// ignore the exception, just log it
			logger.warn("unable to load predefined vocabularies", e);
		}
		return predefinedVocabularies;
	}

	protected static InputStream getPredefinedVocabularyFile() {
		return PredefinedVocabularies.class.getClassLoader().getResourceAsStream(PREDEFINED_VOCABS_FILE_NAME);
	}
	
	public static void main(String[] args) throws Exception {
		getPredefinedVocabularies();
	}
}

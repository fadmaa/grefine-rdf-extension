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

public class PrefixesCC {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String PREFIXES_CC_FILE_NAME = "files/prefixes_cc.tsv";
	public final Map<String, Vocabulary> prefixesMap;
	public static PrefixesCC singleton = new PrefixesCC();

	private PrefixesCC() {
		try {
			prefixesMap = getPredefinedVocabulariesAsMap();
		} catch (IOException e) {
			throw new RuntimeException("RDF Extension failed to intialise predefined prefixes", e);
		}
	}
	
	public String getUri(String prefix) {
		if(prefixesMap.containsKey(prefix)) {
			return prefixesMap.get(prefix).getUri();	
		} else {
			return "";
		}
	}
	
	private Map<String, Vocabulary> getPredefinedVocabulariesAsMap() throws IOException {
		Map<String, Vocabulary> map = new HashMap<String, Vocabulary>();
		for(Vocabulary v: getPredefinedVocabularies()){
			map.put(v.getName(), v);
		}
		return map;
	}
	
	private List<Vocabulary> getPredefinedVocabularies() throws IOException {
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
				String fetchUrl = uri;
				predefinedVocabularies.add(new Vocabulary(name, uri, fetchUrl));

			}
			br.close();

		} catch (Exception e) {
			// predefined vocabularies are not defined properly
			// ignore the exception, just log it
			logger.warn("unable to load predefined prefixes", e);
		}
		return predefinedVocabularies;
	}

	protected static InputStream getPredefinedVocabularyFile() {
		return PredefinedVocabularies.class.getClassLoader().getResourceAsStream(PREFIXES_CC_FILE_NAME);
	}
}

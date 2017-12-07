package org.deri.orefine.rdf.vocab;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.jena.rdf.model.Literal;

public class VocabularyImporter {

	private static final String PREFIXES = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ";
	private static final String CLASSES_QUERY_C1 = PREFIXES
			+ "SELECT ?resource ?label ?en_label ?description ?en_description ?definition ?en_definition " + "WHERE { "
			+ "?resource rdf:type rdfs:Class. " + "OPTIONAL {?resource rdfs:label ?label.} "
			+ "OPTIONAL {?resource rdfs:label ?en_label. FILTER langMatches( lang(?en_label), \"EN\" )  } "
			+ "OPTIONAL {?resource rdfs:comment ?description.} "
			+ "OPTIONAL {?resource rdfs:comment ?en_description. FILTER langMatches( lang(?en_description), \"EN\" )  } "
			+ "OPTIONAL {?resource skos:definition ?definition.} "
			+ "OPTIONAL {?resource skos:definition ?en_definition. FILTER langMatches( lang(?en_definition), \"EN\" )  } "
			+ "FILTER regex(str(?resource), \"^";
	private static final String CLASSES_QUERY_C2 = "\")}";

	private static final String PROPERTIES_QUERY_P1 = PREFIXES
			+ "SELECT ?resource ?label ?en_label ?description ?en_description ?definition ?en_definition " + "WHERE { "
			+ "?resource rdf:type rdf:Property. " + "OPTIONAL {?resource rdfs:label ?label.} "
			+ "OPTIONAL {?resource rdfs:label ?en_label. FILTER langMatches( lang(?en_label), \"EN\" )  } "
			+ "OPTIONAL {?resource rdfs:comment ?description.} "
			+ "OPTIONAL {?resource rdfs:comment ?en_description. FILTER langMatches( lang(?en_description), \"EN\" )  } "
			+ "OPTIONAL {?resource skos:definition ?definition.} "
			+ "OPTIONAL {?resource skos:definition ?en_definition. FILTER langMatches( lang(?en_definition), \"EN\" )  } "
			+ "FILTER regex(str(?resource), \"^";
	private static final String PROPERTIES_QUERY_P2 = "\")}";

	public static void importAndIndexVocabulary(Vocabulary vocabulary, VocabularyIndexer indexer, String projectId)
			throws CorruptIndexException, IOException {
		try {
			// read a model from the URL
			Model model = ModelFactory.createDefaultModel();
			model.read(vocabulary.getFetchUrl());
			// extract classes and properties
			Set<IndexedRDFTerm> classes = extractRDFClasses(model, vocabulary);
			Set<IndexedRDFTerm> properties = extractRDFProperties(model, vocabulary);
			// add to Lucene index
			indexer.indexTerms(vocabulary.getName(), vocabulary.getUri(), projectId, classes, properties);
		} catch (Exception e) {
			// silent
		}
	}

	private static Set<IndexedRDFTerm> extractRDFClasses(Model model, Vocabulary v) {
		return extractRDFTerms(CLASSES_QUERY_C1 + v.getUri() + CLASSES_QUERY_C2, model, v);
	}

	private static Set<IndexedRDFTerm> extractRDFProperties(Model model, Vocabulary v) {
		return extractRDFTerms(PROPERTIES_QUERY_P1 + v.getUri() + PROPERTIES_QUERY_P2, model, v);
	}

	private static Set<IndexedRDFTerm> extractRDFTerms(String queryStr, Model model, Vocabulary v) {
		Set<IndexedRDFTerm> terms = new HashSet<IndexedRDFTerm>();
		Query query = QueryFactory.create(queryStr);
		QueryExecution qExec = QueryExecutionFactory.create(query, model);
		ResultSet result = qExec.execSelect();
		while (result.hasNext()) {
			QuerySolution solution = result.nextSolution();
			String uri = solution.getResource("resource").getURI();
			String label = getFirstNotNull(
					new Literal[] { solution.getLiteral("en_label"), solution.getLiteral("label") });
			String description = getFirstNotNull(
					new Literal[] { solution.getLiteral("en_definition"), solution.getLiteral("definition"),
							solution.getLiteral("en_description"), solution.getLiteral("description") });
			IndexedRDFTerm term = new IndexedRDFTerm(uri, label, description, v.getName(), v.getUri());
			terms.add(term);
		}
		return terms;
	}

	private static String getFirstNotNull(Literal[] values) {
		String s = null;
		for (int i = 0; i < values.length; i++) {
			s = getString(values[i]);
			if (s != null) {
				break;
			}
		}
		return s;
	}

	private static String getString(Literal v) {
		if (v != null) {
			return v.getLexicalForm();
		}
		return null;
	}

}

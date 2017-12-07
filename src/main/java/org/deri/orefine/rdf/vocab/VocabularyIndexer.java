package org.deri.orefine.rdf.vocab;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Field;

public class VocabularyIndexer {

	private static final String CLASS_TYPE = "class";
	private static final String PROPERTY_TYPE = "property";

	private IndexWriter writer;
	private IndexSearcher searcher;
	private IndexReader reader;
	private String workingDir;

	public static VocabularyIndexer singleton = null;
	
	public static void initialise(String workingDir) throws IOException {
		singleton = new VocabularyIndexer(workingDir); 
	}
	
	private VocabularyIndexer(String workingDir) throws IOException {
		this.workingDir = workingDir;
		FSDirectory dir = FSDirectory.open(Paths.get(workingDir));
		IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
		writer = new IndexWriter(dir, config);
		writer.commit();
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}

	public void indexTerms(String name, String uri, String projectId, Set<IndexedRDFTerm> classes,
			Set<IndexedRDFTerm> properties) throws CorruptIndexException, IOException {
		for (IndexedRDFTerm c : classes) {
			indexRdfNode(c, CLASS_TYPE, projectId);
		}
		for (IndexedRDFTerm p : properties) {
			indexRdfNode(p, PROPERTY_TYPE, projectId);
		}

		this.update();
	}

	public List<SearchResultItem> searchClasses(String str, String projectId)
			throws IOException {
		Query query = prepareQuery(str, CLASS_TYPE, projectId);
		TopDocs docs = searcher.search(query, getMaxDoc());		
		return prepareSearchResults(docs);
	}

	public List<SearchResultItem> searchProperties(String str, String projectId)
			throws IOException {
		Query query = prepareQuery(str, PROPERTY_TYPE, projectId);
		TopDocs docs = searcher.search(query, getMaxDoc());
		return prepareSearchResults(docs);
	}
	
	private void indexRdfNode(IndexedRDFTerm node, String type, String projectId)
			throws CorruptIndexException, IOException {
		Document doc = new Document();
		doc.add(new StringField("type", type, Field.Store.YES));
		doc.add(new StringField("prefix", node.getVocabularyPrefix(), Field.Store.YES));
		String l = node.getLabel() == null ? "" : node.getLabel();
		doc.add(new TextField("label", l, Field.Store.YES));
		String d = node.getDescription() == null ? "" : node.getDescription();
		doc.add(new TextField("description", d, Field.Store.YES));
		doc.add(new StoredField("uri", node.getURI()));
		doc.add(new TextField("localPart", node.getLocalPart(), Field.Store.YES));
		doc.add(new StringField("namespace", node.getVocabularyUri(), Field.Store.YES));
		doc.add(new StringField("projectId", String.valueOf(projectId), Field.Store.YES));
		writer.addDocument(doc);
	}

	public void update() throws CorruptIndexException, IOException {
		writer.commit();
		// TODO this shouldn't be required but it is not working without it...
		// check
		reader.close();
		FSDirectory dir = FSDirectory.open(Paths.get(workingDir));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}
	
	private Query prepareQuery(String s, String type, String projectId)
			throws IOException {
	    BooleanQuery.Builder builder1 = new BooleanQuery.Builder();		
		// Term("projectId",GLOBAL_VOCABULARY_PLACE_HOLDER)), Occur.SHOULD);
	    builder1.add(new TermQuery(new Term("projectId", projectId)), Occur.MUST);
	    BooleanQuery.Builder builder2 = new BooleanQuery.Builder();		
		builder2.add(new TermQuery(new Term("type", type)), Occur.MUST);
	    BooleanQuery.Builder builder = new BooleanQuery.Builder();		
		builder.add(builder1.build(), Occur.MUST);
		builder.add(builder2.build(), Occur.MUST);
		if (s != null && s.trim().length() > 0) {
			SimpleAnalyzer analyzer = new SimpleAnalyzer();
			if (s.indexOf(":") == -1) {
				// the query we need:
				// "projectId":projectId AND "type":type AND ("prefix":s* OR
				// "localPart":s* OR "label":s* OR "description":s*)
			    BooleanQuery.Builder builder3 = new BooleanQuery.Builder();		
				builder3.add(new WildcardQuery(new Term("prefix", s + "*")),
						Occur.SHOULD);
				TokenStream stream = analyzer.tokenStream("localPart",
						new StringReader(s));
				// get the TermAttribute from the TokenStream
				CharTermAttribute termAtt = (CharTermAttribute) stream
						.addAttribute(CharTermAttribute.class);
				stream.reset();
				while (stream.incrementToken()) {
					String tmp = termAtt.toString() + "*";
					builder3.add(new WildcardQuery(new Term("localPart", tmp)),
							Occur.SHOULD);
				}
				stream.close();
				stream.end();
				stream = analyzer.tokenStream("description",
						new StringReader(s));
				// get the TermAttribute from the TokenStream
				termAtt = (CharTermAttribute) stream
						.addAttribute(CharTermAttribute.class);
				stream.reset();
				while (stream.incrementToken()) {
					String tmp = termAtt.toString() + "*";
					builder3.add(new WildcardQuery(new Term("description", tmp)),
							Occur.SHOULD);
				}
				stream.close();
				stream.end();
				stream = analyzer.tokenStream("label", new StringReader(s));
				// get the TermAttribute from the TokenStream
				termAtt = (CharTermAttribute) stream
						.addAttribute(CharTermAttribute.class);
				stream.reset();
				while (stream.incrementToken()) {
					String tmp = termAtt.toString() + "*";
					builder3.add(new WildcardQuery(new Term("label", tmp)),
							Occur.SHOULD);
				}
				stream.close();
				stream.end();
				builder.add(builder3.build(), Occur.MUST);
				return builder.build();
			} else {
				// the query we need:
				// "projectId":projectId AND "type":type AND ("prefix":p1 AND
				// "localPart":s*)
				String p1 = s.substring(0, s.indexOf(":"));
				String p2 = s.substring(s.indexOf(":") + 1);
			    BooleanQuery.Builder builder3 = new BooleanQuery.Builder();		// q1.add(new TermQuery(new
				builder3.add(new TermQuery(new Term("prefix", p1)), Occur.SHOULD);
			    BooleanQuery.Builder builder4 = new BooleanQuery.Builder();		// q1.add(new TermQuery(new
				TokenStream stream = analyzer.tokenStream("localPart",
						new StringReader(p2));
				// get the TermAttribute from the TokenStream
				CharTermAttribute termAtt = (CharTermAttribute) stream
						.addAttribute(CharTermAttribute.class);
				stream.reset();
				if (!p2.isEmpty()) {
					while (stream.incrementToken()) {
						builder4.add(new WildcardQuery(new Term("localPart", termAtt.toString()
								 + "*")), Occur.SHOULD);
					}
				}
				stream.close();
				stream.end();
				builder.add(builder3.build(), Occur.MUST);
				if (!p2.isEmpty()) {
					builder.add(builder4.build(), Occur.MUST);
				}
				analyzer.close();
				return builder.build();
			}
		} else {
			return builder.build();
		}
	}

	private List<SearchResultItem> prepareSearchResults(TopDocs docs)
			throws CorruptIndexException, IOException {
		List<SearchResultItem> res = new ArrayList<SearchResultItem>();
		for (int i = 0; i < docs.totalHits; i++) {
			Document doc = searcher.doc(docs.scoreDocs[i].doc);
			String uri = doc.get("uri");
			String label = doc.get("label");
			String description = doc.get("description");
			String prefix = doc.get("prefix");
			String lPart = doc.get("localPart");
			SearchResultItem item = new SearchResultItem(uri, prefix, lPart,
					label, description);
			res.add(item);
		}
		return res;
	}
	
	private int getMaxDoc() throws IOException {
		return reader.maxDoc() > 0 ? reader.maxDoc() : 100000;
	}
}

package org.deri.orefine.rdf.vocab;

public class IndexedRDFTerm {
    private String localPart;
    private String description;
    private String URI;
    private String label;
    private String vocabularyPrefix;
    private String vocabularyUri;
    
    public String getVocabularyUri() {
        return vocabularyUri;
    }
    public void setVocabularyUri(String vocabularyUri) {
        this.vocabularyUri = vocabularyUri;
    }
    public String getVocabularyPrefix() {
        return vocabularyPrefix;
    }
    public void setVocabularyPrefix(String vocabularyPrefix) {
        this.vocabularyPrefix = vocabularyPrefix;
    }
    public String getLocalPart() {
        return localPart;
    }
    public void setLocalPart(String l) {
        this.localPart = l;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getURI() {
        return URI;
    }
    public void setURI(String uRI) {
        URI = uRI;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public IndexedRDFTerm(String uRI, String label, String description, 
            String prefix,String vocabularyUri) {
    	this.description = description;
        URI = uRI;
        this.label = label;
        this.vocabularyPrefix = prefix;
        this.vocabularyUri = vocabularyUri;
        this.localPart = extractlocalPart();
    }
    
    private String extractlocalPart(){
        String l;
        if(this.URI==null){
            return null;
        }
        if(this.URI.indexOf("#")!=-1){
            l = this.URI.substring(this.URI.indexOf("#")+1);
        }else{
            l = this.URI.substring(this.URI.lastIndexOf("/")+1);
        }
        return l;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IndexedRDFTerm)) return false;
        IndexedRDFTerm n = (IndexedRDFTerm) obj;
        if(n.getURI()==null || this.URI==null){
            return false;
        }
        return this.URI.equals(n.getURI());
    }
    @Override
    public int hashCode() {
        return this.URI.hashCode();
    }
  
    @Override
    public String toString() {
    	return URI;
    }
}
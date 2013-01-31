package org.deri.grefine.rdf.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.deri.grefine.rdf.app.ApplicationContext;
import org.deri.grefine.rdf.vocab.VocabularyImporter;
import org.json.JSONException;
import org.json.JSONWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;


public class AddPrefixFromFileCommand extends RdfCommand{

    public AddPrefixFromFileCommand(ApplicationContext ctxt) {
		super(ctxt);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
   
            Writer w = response.getWriter();
            JSONWriter writer = new JSONWriter(w);
		
		try {
			
			System.out.println("in doPost...");
			
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			String uri = null, prefix = null, format = null, projectId = null, filename="";
			InputStream in = null;
			
			
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
			
			for(FileItem item:items){
				if(item.getFieldName().equals("vocab-prefix")){
					prefix = item.getString(); 
				}else if(item.getFieldName().equals("vocab-uri")){
					uri = item.getString();
				}else if(item.getFieldName().equals("file_format")){
					format = item.getString();
				}else if(item.getFieldName().equals("project")){
					projectId = item.getString();
				}else{
					filename = item.getName();
					in = item.getInputStream();
				}
			}
			
			Repository repository = new SailRepository(
					new ForwardChainingRDFSInferencer(new MemoryStore()));
			repository.initialize();
			RepositoryConnection con = repository.getConnection();
			RDFFormat rdfFromat;
			if(format.equals("auto-detect")){
				rdfFromat = guessFormat(filename);
			}else if(format.equals("TTL")){
				rdfFromat = RDFFormat.TURTLE;
			}else if(format.equals("N3")){
				rdfFromat = RDFFormat.N3;
			}else if(format.equals("NTRIPLE")) {
				rdfFromat = RDFFormat.NTRIPLES;
			}else{
				rdfFromat = RDFFormat.RDFXML;
			}
			con.add(in, "", rdfFromat);
			con.close();
			
			System.out.println("Getting RDF schema...");
			getRdfSchemaForUpload(request, projectId).addPrefix(prefix, uri);
			getRdfContext().getVocabularySearcher().importAndIndexVocabulary(prefix, uri, repository, projectId, new VocabularyImporter());
        	        //success

                        //response.setHeader("Content-Type", "application/json");

                        writer.object();
                        writer.key("code");
                        writer.value("ok");
                        writer.endObject();
                    	
		} catch (Exception e) {
			
			logger.error("Some error: " + e.getLocalizedMessage());
			try {
			
				logger.info("Generating response for error....");
				writer.object();
                	        writer.key("code");
                	        writer.value("error");
                	        writer.endObject();
                	            
                	        writer.object();
                	        writer.key("message");
                	        writer.value(e.getMessage());
                	        writer.endObject();
			}
			catch(JSONException e1) {
			    System.out.println("What now??????");
			}
				
			
		}
		finally {
		    //response.setStatus(HttpServletResponse.SC_OK);
		    System.out.println("Made it to here....");
		    System.out.println(response.getContentType());
                    w.flush();
                    w.close();

		}
	}
    
	private RDFFormat guessFormat(String filename){
		if(filename.lastIndexOf('.')!=-1){
			String extension = filename.substring(filename.lastIndexOf('.')).toLowerCase();
			if(extension.equals(".ttl")){
				return RDFFormat.TURTLE;
			}else if(extension.equals(".rdf")){
				return RDFFormat.RDFXML;
			}else if(extension.equals(".owl")){
				return RDFFormat.RDFXML;
			}else if(extension.equals(".nt")){
				return RDFFormat.NTRIPLES;
			}else if(extension.equals(".n3")){
				return RDFFormat.N3;
			}
		}
		return RDFFormat.RDFXML;
	}

}

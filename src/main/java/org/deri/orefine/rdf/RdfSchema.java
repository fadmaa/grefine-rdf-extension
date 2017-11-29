package org.deri.orefine.rdf;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.model.OverlayModel;
import com.google.refine.model.Project;

public class RdfSchema implements OverlayModel {

	final static Logger logger = LoggerFactory.getLogger("RdfSchema");
	
    @Override
    public void onBeforeSave(Project project) {
    }
    
    @Override
    public void onAfterSave(Project project) {
    }
    
   @Override
    public void dispose(Project project) {
	   /*try {
			ApplicationContext.instance().getVocabularySearcher().deleteProjectVocabularies(String.valueOf(project.id));
		} catch (ParseException e) {
			//log
			logger.error("Unable to delete index for project " + project.id, e);
		} catch (IOException e) {
			//log
			logger.error("Unable to delete index for project " + project.id, e);
		}*/
    }

    @Override
    public void write(JSONWriter writer, Properties options)
            throws JSONException {
        writer.object();
        
       	writer.endArray();
        writer.endObject();
    }
}

package org.deri.orefine.rdf.model;

import java.lang.reflect.Array;
import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.deri.orefine.rdf.Utils;
import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.expr.EvalError;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

public class CellBlankNode extends ResourceNode implements CellNode{

    final private String columnName;
    final boolean isRowNumberCell;
    final private String expression;
    
    public CellBlankNode(String columnName, String exp, boolean isRowNumberCell){
        this.columnName = columnName;
        this.isRowNumberCell = isRowNumberCell;
        this.expression = exp;
    }
    
    @Override
    public void writeNode(JSONWriter writer) throws JSONException {
        writer.key("nodeType"); writer.value("cell-as-blank");
        writer.key("isRowNumberCell"); writer.value(isRowNumberCell);
        if(columnName!=null){
        	writer.key("columnName");writer.value(columnName);
        }
    }

	@Override
	public boolean isRowNumberCellNode() {
		return isRowNumberCell;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public Resource[] createResource(Model model, URI baseUri, Project project, Row row, int rowIndex,
			Resource[] blanks) {
		try{
    		Object result = Utils.evaluateExpression(project, expression, columnName, row, rowIndex);
    		if (result.getClass() == EvalError.class){
    			return null;
    		}
    		if (result.getClass().isArray()){
    			int lngth = Array.getLength(result);
    			Resource[] bs = new Resource[lngth];
    			for(int i=0;i<lngth;i++){
    				bs[i] = model.createResource();
    			}
    			return bs;
    		}
    		return new Resource[]{ model.createResource() }; // Resource with no URI set is a bnode in Jena
    	} catch (Exception e) {
    		return null;
    	}
	}
}

package org.deri.orefine.rdf.model;

import java.net.URI;
import java.util.Properties;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.model.Project;
import com.google.refine.model.Row;

public class ConstantLiteralNode implements Node {

	private String valueType;
	private String lang;
	private String value;

	public ConstantLiteralNode(String val, String type, String l) {
		this.value = val;
		this.valueType = type;
		this.lang = l;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void write(JSONWriter writer, Properties options) throws JSONException {
		writer.object();
		writer.key("nodeType");
		writer.value("literal");
		writer.key("value");
		writer.value(value);
		if (valueType != null) {
			writer.key("valueType");
			writer.value(valueType);
		}
		if (lang != null) {
			writer.key("lang");
			writer.value(lang);
		}
		writer.endObject();
	}

	@Override
	public RDFNode[] create(Model model, URI baseUri, Project project, Row row, int rowIndex, Resource[] blanks) {
		if (this.value == null || this.value.isEmpty()) {
			return null;
		} else {
			Literal l;
			if (this.valueType != null) {
				// TODO handle exception when valueType is not a valid URI
				l = model.createTypedLiteral(this.value, valueType);
			} else {
				if (this.lang != null) {
					l = model.createLiteral(this.value, lang);
				} else {
					l = model.createLiteral(this.value);
				}
			}
			return new Literal[] { l };
		}
	}
}

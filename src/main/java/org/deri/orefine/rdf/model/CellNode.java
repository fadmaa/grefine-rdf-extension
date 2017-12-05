package org.deri.orefine.rdf.model;

public interface CellNode extends Node{
	boolean isRowNumberCellNode();
	String getColumnName();
}

package org.deri.orefine.rdf.commands.vocab;

public class PrefixExistsException extends Exception {
    private static final long serialVersionUID = 1916094460059608851L;
    public PrefixExistsException(String msg){
        super(msg);
    }
}

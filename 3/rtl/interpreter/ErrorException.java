package rtl.interpreter;

import rtl.Ident;

public class ErrorException extends RuntimeException {
    public ErrorException(String message) { this(message,null); }
    public ErrorException(String message, Ident id) {
    		super(((id==null)||(id.debug==null))?message:id.debug.toString()+", "+message);
    }
}

package br.com.db1.util.exception;

public class ErroSistema extends Exception{

	private static final long serialVersionUID = 1L;

	public ErroSistema(String message) {
        super(message);
    }
    
    public ErroSistema(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ErroSistema(Throwable cause) {
    	super(cause);
    }
}
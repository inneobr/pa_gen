package br.coop.integrada.api.pa.aplication.exceptions;

public class ObjectDefaultException extends RuntimeException {

    public ObjectDefaultException(String msg ) {
        super( msg );
    }

    public ObjectDefaultException(String msg, Throwable ceuse ) {
        super( msg, ceuse );
    }
}

package br.coop.integrada.api.pa.aplication.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException( String msg ) {
        super( msg );
    }

    public ObjectNotFoundException( String msg, Throwable ceuse ) {
        super( msg, ceuse );
    }
}

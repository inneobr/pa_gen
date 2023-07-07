package br.coop.integrada.api.pa.aplication.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ObjectFieldErrorsException extends RuntimeException {
    private List<FieldErrorItem> fieldErrors = new ArrayList<>();

    public ObjectFieldErrorsException(String msg, List<FieldErrorItem> fieldErrors) {
        super( msg );
        this.fieldErrors = fieldErrors;
    }

    public ObjectFieldErrorsException(String msg, List<FieldErrorItem> fieldErrors, Throwable ceuse ) {
        super( msg, ceuse );
        this.fieldErrors = fieldErrors;
    }
}

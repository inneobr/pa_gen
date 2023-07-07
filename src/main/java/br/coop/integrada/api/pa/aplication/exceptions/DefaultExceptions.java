package br.coop.integrada.api.pa.aplication.exceptions;

import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Data
public class DefaultExceptions {
	private int code;
	private String message;
	private String exception;
	private List<FieldErrorItem> fieldErrors = new ArrayList<>();
	
	public static DefaultExceptions construir(int code, String message) {
		DefaultExceptions exception = new DefaultExceptions();
		exception.setCode(code);
		exception.setMessage(message);
		return exception;
	}
	
	public static DefaultExceptions construir(int code, String message, String ex) {
		DefaultExceptions exception = new DefaultExceptions();
		exception.setCode(code);
		exception.setMessage(message);
		exception.setException(ex);
		return exception;
	}
	
	public static DefaultExceptions construir(int code, String message, ConstraintViolationException e) {
		DefaultExceptions exception = new DefaultExceptions();
		exception.setCode(code);
		exception.setMessage(message);
		exception.setException(e.getMessage());
		
		for (ConstraintViolation<?> constraint : e.getConstraintViolations()) {
			exception.getFieldErrors().add(FieldErrorItem.construir(
					((PathImpl)constraint.getPropertyPath()).getLeafNode().getName(),
					constraint.getMessage()));
		}
		
		return exception;
	}
	
	public static DefaultExceptions construir(int code, String message, DataIntegrityViolationException ex) {
				
		DefaultExceptions exception = new DefaultExceptions();
		exception.setCode(code);
		exception.setMessage(message);
		exception.setException(ex.getMessage());
		
		
		return exception;
	}

	public static DefaultExceptions construir(int code, String message, ObjectFieldErrorsException ex) {

		DefaultExceptions exception = new DefaultExceptions();
		exception.setCode(code);
		exception.setMessage(message);
		exception.setException(ex.getMessage());
		exception.setFieldErrors(ex.getFieldErrors());

		return exception;
	}
	
}

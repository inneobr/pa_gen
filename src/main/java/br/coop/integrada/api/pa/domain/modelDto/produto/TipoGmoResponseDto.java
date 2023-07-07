package br.coop.integrada.api.pa.domain.modelDto.produto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;



@Data
public class TipoGmoResponseDto extends AbstractResponseDto {
	
	private static final long serialVersionUID = 1L;
	
	private String idUnico;
	
	
	public static TipoGmoResponseDto construir(TipoGmoDto tipoGmoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        
		TipoGmoResponseDto responseDto = new TipoGmoResponseDto();

		responseDto.setIdUnico(tipoGmoDto.getIdUnico());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        
        if(exception != null) {
            responseDto.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                responseDto.getFieldErrors().add(fieldErrorItem);
            }
        }
        
        return responseDto;
    }
	
	
	public static TipoGmoResponseDto construir(TipoGmo tipoGmo, Boolean integrated, String message, ConstraintViolationException exception) {
        
		TipoGmoResponseDto responseDto = new TipoGmoResponseDto();

		responseDto.setIdUnico(tipoGmo.getIdUnico());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        
        if(exception != null) {
            responseDto.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                responseDto.getFieldErrors().add(fieldErrorItem);
            }
        }
        
        return responseDto;
    }


	public static TipoGmoResponseDto construir(TipoGmo tipoGmo, boolean integrated, String message) {
		var tipoGmoResponse = new TipoGmoResponseDto();
		tipoGmoResponse.setIdUnico(tipoGmo.getIdUnico());
		tipoGmoResponse.setIntegrated(integrated);
		tipoGmoResponse.setMessage(message);

        return tipoGmoResponse;
	}


	public static TipoGmoResponseDto construir(TipoGmo tipoGmo, boolean integrated, String message, String messageError) {
		var tipoGmoResponseDto = new TipoGmoResponseDto();
        
		tipoGmoResponseDto.setIdUnico(tipoGmo.getIdUnico());
        tipoGmoResponseDto.setIntegrated(integrated);
        tipoGmoResponseDto.setMessage(message);
        tipoGmoResponseDto.setException(messageError);

        return tipoGmoResponseDto;
	}
	
	
}

package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import javax.validation.ConstraintViolationException;
import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import javax.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;

@Data
public class SituacaoReResponseDto  extends AbstractResponseDto {
	
	private static final long serialVersionUID = 1L;
	
	private Long codigo;
	
//	public static SituacaoReResponseDto construir(SituacaoRe situacaoReRetorno, boolean b, String string) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public static SituacaoReResponseDto construir(@Valid SituacaoRe situacaoRe, boolean b, String string,
//			ConstraintViolationException e) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public static SituacaoReResponseDto construir(@Valid SituacaoRe situacaoRe, boolean b, String string,
//			String message) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public static SituacaoReResponseDto construir(SituacaoReDto situacaoReDto, boolean b, String string,
//			String message) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	public static SituacaoReResponseDto construir(SituacaoReDto situacaoReDto, Boolean integrated, String message, ConstraintViolationException exception) {
        
		SituacaoReResponseDto responseDto = new SituacaoReResponseDto();

		responseDto.setCodigo( situacaoReDto.getCodigo() );
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
	
	
	public static SituacaoReResponseDto construir(SituacaoRe situacaoRe, Boolean integrated, String message, ConstraintViolationException exception) {
        
		SituacaoReResponseDto responseDto = new SituacaoReResponseDto();

		responseDto.setCodigo(situacaoRe.getCodigo());
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


	public static SituacaoReResponseDto construir(SituacaoRe situacaoRe, boolean integrated, String message) {
		var situacaoReResponse = new SituacaoReResponseDto();
		situacaoReResponse.setCodigo(situacaoRe.getCodigo());
		situacaoReResponse.setIntegrated(integrated);
		situacaoReResponse.setMessage(message);

        return situacaoReResponse;
	}


	public static SituacaoReResponseDto construir(SituacaoRe situacaoRe, boolean integrated, String message, String messageError) {
		var situacaoReResponseDto = new SituacaoReResponseDto();
        
		situacaoReResponseDto.setCodigo(situacaoRe.getCodigo());
        situacaoReResponseDto.setIntegrated(integrated);
        situacaoReResponseDto.setMessage(message);
        situacaoReResponseDto.setException(messageError);

        return situacaoReResponseDto;
	}
	
	public static SituacaoReResponseDto construir(SituacaoReDto situacaoRe, boolean integrated, String message, String messageError) {
		var situacaoReResponseDto = new SituacaoReResponseDto();
        
		situacaoReResponseDto.setCodigo(situacaoRe.getCodigo());
        situacaoReResponseDto.setIntegrated(integrated);
        situacaoReResponseDto.setMessage(message);
        situacaoReResponseDto.setException(messageError);

        return situacaoReResponseDto;
	}
	

}

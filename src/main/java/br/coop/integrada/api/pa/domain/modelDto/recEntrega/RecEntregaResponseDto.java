package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.io.Serializable;
import java.util.Date;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class RecEntregaResponseDto  extends AbstractResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codEstabel;
	private Long nrRe;
	private String idRe;

	public static RecEntregaResponseDto construir(RecEntrega recEntrega, Boolean integrated, String message) {
        var objDto = new RecEntregaResponseDto();
        BeanUtils.copyProperties(recEntrega, objDto);
        objDto.setIdRe(recEntrega.getId().toString());
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }
	
	public static RecEntregaResponseDto construir(RecEntregaDto recEntrega, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new RecEntregaResponseDto();
        BeanUtils.copyProperties(recEntrega, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        if(exception instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(
                        ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName(),
                        constraint.getMessage()
                );

                objDto.getFieldErrors().add(fieldErrorItem);
            }
        }

        return objDto;
    }

	public static RecEntregaResponseDto construir(RecEntregaDto recEntregaDto, Boolean integrated, String message, String exception) {
        var objDto = new RecEntregaResponseDto();
        BeanUtils.copyProperties(recEntregaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }
    
    public static RecEntregaResponseDto construir(String message, Exception exception) {
        var objDto = new RecEntregaResponseDto();
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
	
	public static RecEntregaResponseDto construir(RecEntregaDto recEntregaDto, Boolean integrated, String message) {
        var objDto = new RecEntregaResponseDto();
        BeanUtils.copyProperties(recEntregaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }
	
	public static RecEntregaResponseDto construir(RecEntregaDto recEntregaDto, Boolean integrated, String message, Exception exception) {
        var objDto = new RecEntregaResponseDto();
        BeanUtils.copyProperties(recEntregaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
    
}

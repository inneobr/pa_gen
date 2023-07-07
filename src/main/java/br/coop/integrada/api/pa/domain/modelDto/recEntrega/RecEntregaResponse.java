package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class RecEntregaResponse extends AbstractResponseDto{
	private static final long serialVersionUID = 1L;
	private Long id;
	
	public static RecEntregaResponse construir(RecEntrega recEntrega, Boolean integrated, String message) {
        var objDto = new RecEntregaResponse();
        BeanUtils.copyProperties(recEntrega, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        return objDto;
    }

    public static RecEntregaResponse construir(RecEntrega recEntrega, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new RecEntregaResponse();
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

    public static RecEntregaResponse construir(RecEntrega recEntrega, Boolean integrated, String message, Exception exception) {
        var objDto = new RecEntregaResponse();
        BeanUtils.copyProperties(recEntrega, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
}

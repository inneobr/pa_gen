package br.coop.integrada.api.pa.domain.modelDto.rependente;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class RePendenteResponse extends AbstractResponseDto{
	private static final long serialVersionUID = 1L;

	public static RePendenteResponse construir(RePendente rePendente, Boolean integrated, String message) {
        var objDto = new RePendenteResponse();
        BeanUtils.copyProperties(rePendente, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        return objDto;
    }

    public static RePendenteResponse construir(RePendente rePendente, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new RePendenteResponse();
        BeanUtils.copyProperties(rePendente, objDto);

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

    public static RePendenteResponse construir(RePendente rePendente, Boolean integrated, String message, Exception exception) {
        var objDto = new RePendenteResponse();
        BeanUtils.copyProperties(rePendente, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
}

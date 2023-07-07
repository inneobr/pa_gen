package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class BaixaCreditoMovResponseDto extends AbstractResponseDto {
	private static final long serialVersionUID = 1L;

	private String codEstabel;
	private Long nrRe;
	private String idMovtoBxaCred;
	
	public static BaixaCreditoMovResponseDto construir(BaixaCreditoMov baixaCreditoMov, Boolean integrated, String message) {
        var objDto = new BaixaCreditoMovResponseDto();
        BeanUtils.copyProperties(baixaCreditoMov, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

	public static BaixaCreditoMovResponseDto construir(BaixaCreditoMovRequestDto baixaCreditoMov, Boolean integrated, String message, Exception exception) {
        var objDto = new BaixaCreditoMovResponseDto();
        BeanUtils.copyProperties(baixaCreditoMov, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
	
	public static BaixaCreditoMovResponseDto construir(BaixaCreditoMovRequestDto baixaCreditoMov, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new BaixaCreditoMovResponseDto();
        BeanUtils.copyProperties(baixaCreditoMov, objDto);

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
}

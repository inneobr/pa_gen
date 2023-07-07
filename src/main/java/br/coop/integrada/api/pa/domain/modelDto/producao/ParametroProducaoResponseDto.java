package br.coop.integrada.api.pa.domain.modelDto.producao;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Data
public class ParametroProducaoResponseDto extends AbstractResponseDto {

    public static ParametroProducaoResponseDto construir(Boolean integrated, String message) {
        var objDto = new ParametroProducaoResponseDto();
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static ParametroProducaoResponseDto construir(Boolean integrated, String message, Exception exception) {
        var objDto = new ParametroProducaoResponseDto();
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ParametroProducaoResponseDto construir(Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ParametroProducaoResponseDto();
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

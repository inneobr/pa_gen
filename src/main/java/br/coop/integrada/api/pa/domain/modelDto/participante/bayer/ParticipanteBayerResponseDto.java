package br.coop.integrada.api.pa.domain.modelDto.participante.bayer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class ParticipanteBayerResponseDto extends AbstractResponseDto {
    private static final long serialVersionUID = 1L;

    private String cnpj;

    public static ParticipanteBayerResponseDto construir(String cnpj, Boolean integrated, String message) {
        var objDto = new ParticipanteBayerResponseDto();
        objDto.setCnpj(cnpj);
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        return objDto;
    }

    public static ParticipanteBayerResponseDto construir(String cnpj, Boolean integrated, String message, Exception exception) {
        var objDto = new ParticipanteBayerResponseDto();
        objDto.setCnpj(cnpj);
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());
        return objDto;
    }

    public static ParticipanteBayerResponseDto construir(String cnpj, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ParticipanteBayerResponseDto();
        objDto.setCnpj(cnpj);
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

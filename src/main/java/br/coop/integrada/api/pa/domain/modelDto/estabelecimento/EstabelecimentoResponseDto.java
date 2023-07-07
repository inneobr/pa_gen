package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Data
public class EstabelecimentoResponseDto extends AbstractResponseDto {

    private Long id;
    private String codigo;

    public static EstabelecimentoResponseDto construir(Estabelecimento estabelecimento, Boolean integrated, String message) {
        var objDto = new EstabelecimentoResponseDto();
        BeanUtils.copyProperties(estabelecimento, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static EstabelecimentoResponseDto construir(EstabelecimentoDto estabelecimentoDto, Boolean integrated, String message, Exception exception) {
        var objDto = new EstabelecimentoResponseDto();
        BeanUtils.copyProperties(estabelecimentoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static EstabelecimentoResponseDto construir(EstabelecimentoDto estabelecimentoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new EstabelecimentoResponseDto();
        BeanUtils.copyProperties(estabelecimentoDto, objDto);

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

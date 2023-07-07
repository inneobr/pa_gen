package br.coop.integrada.api.pa.domain.modelDto.unidade.federacao;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;

@Data
public class UnidadeFederacaoResponseDto extends AbstractResponseDto implements Serializable {
    private Long id;
    private String estado;
    private String estadoNome;
    private String codigoIbge;

    public static UnidadeFederacaoResponseDto construir(UnidadeFederacao unidadeFederacao, Boolean integrated, String message) {
        var objDto = new UnidadeFederacaoResponseDto();
        BeanUtils.copyProperties(unidadeFederacao, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static UnidadeFederacaoResponseDto construir(UnidadeFederacaoDto unidadeFederacaoDto, Boolean integrated, String message) {
        var objDto = new UnidadeFederacaoResponseDto();
        BeanUtils.copyProperties(unidadeFederacaoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static UnidadeFederacaoResponseDto construir(UnidadeFederacaoDto unidadeFederacaoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new UnidadeFederacaoResponseDto();
        BeanUtils.copyProperties(unidadeFederacaoDto, objDto);

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

    public static UnidadeFederacaoResponseDto construir(UnidadeFederacaoDto unidadeFederacaoDto, Boolean integrated, String message, Exception exception) {
        var objDto = new UnidadeFederacaoResponseDto();
        BeanUtils.copyProperties(unidadeFederacaoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
}

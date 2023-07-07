package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class ParametroEstabelecimentoResponseDto extends AbstractResponseDto {

    private static final long serialVersionUID = 1L;
	private Long id;
    private String codigo;

    public static ParametroEstabelecimentoResponseDto construir(ParametroEstabelecimento parametroEstabelecimento, Boolean integrated, String message) {
        var objDto = new ParametroEstabelecimentoResponseDto();
        BeanUtils.copyProperties(parametroEstabelecimento, objDto);

        if(parametroEstabelecimento.getEstabelecimento() != null) {
            String codigoEstabelecimento = parametroEstabelecimento.getEstabelecimento().getCodigo();
            objDto.setCodigo(codigoEstabelecimento);
        }

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }
    
    public static ParametroEstabelecimentoResponseDto construir(ParametroEstabelecimentoDto parametroEstabelecimento, Boolean integrated, String message) {
        var objDto = new ParametroEstabelecimentoResponseDto();
        BeanUtils.copyProperties(parametroEstabelecimento, objDto);
        objDto.setCodigo(parametroEstabelecimento.getCodigo());       
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static ParametroEstabelecimentoResponseDto construir(ParametroEstabelecimentoDto parametroEstabelecimentoDto, Boolean integrated, String message, Exception exception) {
        var objDto = new ParametroEstabelecimentoResponseDto();
        BeanUtils.copyProperties(parametroEstabelecimentoDto, objDto);

        if(parametroEstabelecimentoDto.getCodigo() != null) {
            objDto.setCodigo(parametroEstabelecimentoDto.getCodigo());
        }

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ParametroEstabelecimentoResponseDto construir(ParametroEstabelecimentoDto parametroEstabelecimentoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ParametroEstabelecimentoResponseDto();
        BeanUtils.copyProperties(parametroEstabelecimentoDto, objDto);

        if(parametroEstabelecimentoDto.getCodigo() != null) {
            objDto.setCodigo(parametroEstabelecimentoDto.getCodigo());
        }

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

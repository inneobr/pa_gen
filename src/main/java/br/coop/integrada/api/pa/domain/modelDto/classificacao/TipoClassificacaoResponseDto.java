package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;


@Data
public class TipoClassificacaoResponseDto extends AbstractResponseDto {
	
	private Long id;
	
	public static TipoClassificacaoResponseDto construir(TipoClassificacao tipoClassificacao, Boolean integrated, String message) {
		var objResponseDto = new TipoClassificacaoResponseDto();
        BeanUtils.copyProperties(tipoClassificacao, objResponseDto);

        objResponseDto.setIntegrated(integrated);
        objResponseDto.setMessage(message);

        return objResponseDto;
	}

	public static TipoClassificacaoResponseDto construir(@Valid TipoClassificacao tipoClassificacao, boolean integrated, String message, ConstraintViolationException exception) {
		var objResponseDto = new TipoClassificacaoResponseDto();
        BeanUtils.copyProperties(tipoClassificacao, objResponseDto);

        objResponseDto.setIntegrated(integrated);
        objResponseDto.setMessage(message);
        
        if(exception instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(
                        ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName(),
                        constraint.getMessage()
                );

                objResponseDto.getFieldErrors().add(fieldErrorItem);
            }
        }

        return objResponseDto;
	}

	public static TipoClassificacaoResponseDto construir(@Valid TipoClassificacao tipoClassificacao, boolean integrated, String message, Exception exception) {
		var objDto = new TipoClassificacaoResponseDto();
        BeanUtils.copyProperties(tipoClassificacao, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
	}
	
}

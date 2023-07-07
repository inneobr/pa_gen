package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class TipoProdutoResponseDto extends AbstractResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private String idUnico;
    private String nome;

    public static TipoProdutoResponseDto construir(TipoProduto tipoProduto, Boolean integrated, String message) {
        var objDto = new TipoProdutoResponseDto();
        BeanUtils.copyProperties(tipoProduto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static TipoProdutoResponseDto construir(TipoProdutoDto tipoProdutoDto, Boolean integrated, String message) {
        var objDto = new TipoProdutoResponseDto();
        BeanUtils.copyProperties(tipoProdutoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static TipoProdutoResponseDto construir(TipoProduto tipoProduto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new TipoProdutoResponseDto();
        BeanUtils.copyProperties(tipoProduto, objDto);

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

    public static TipoProdutoResponseDto construir(TipoProdutoDto tipoProdutoDto, Boolean integrated, String message, Exception exception) {
        var objDto = new TipoProdutoResponseDto();
        BeanUtils.copyProperties(tipoProdutoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
    
    public static TipoProdutoResponseDto construir(TipoProdutoDto tipoProdutoDto, Boolean integrated, String message, String exception) {
        var objDto = new TipoProdutoResponseDto();
        BeanUtils.copyProperties(tipoProdutoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }

	

	
}

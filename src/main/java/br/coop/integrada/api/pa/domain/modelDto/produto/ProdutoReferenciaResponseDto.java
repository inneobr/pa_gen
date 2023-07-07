package br.coop.integrada.api.pa.domain.modelDto.produto;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Data
public class ProdutoReferenciaResponseDto extends AbstractResponseDto {

    private String codRef;

    public static ProdutoReferenciaResponseDto construir(ProdutoReferencia produtoReferencia, Boolean integrated, String message) {
        var objDto = new ProdutoReferenciaResponseDto();
        BeanUtils.copyProperties(produtoReferencia, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static ProdutoReferenciaResponseDto construir(ProdutoReferencia produtoReferencia, Boolean integrated, String message, Exception exception) {
        var objDto = new ProdutoReferenciaResponseDto();
        BeanUtils.copyProperties(produtoReferencia, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ProdutoReferenciaResponseDto construir(ProdutoReferencia produtoReferencia, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ProdutoReferenciaResponseDto();
        BeanUtils.copyProperties(produtoReferencia, objDto);

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

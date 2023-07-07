package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration.GrupoProdutoIntegrationSimplesDto;
import lombok.Data;

@Data
public class GrupoProdutoResponseDto extends AbstractResponseDto {
    private static final long serialVersionUID = 1L;
	private Long id;
    private String fmCodigo;

    public static GrupoProdutoResponseDto construir(GrupoProdutoDto grupoProdutoDto, Boolean integrated, String message, Exception exception) {
        GrupoProduto grupoProduto = new GrupoProduto();
        BeanUtils.copyProperties(grupoProdutoDto, grupoProduto);
        return construir(grupoProduto, integrated, message, exception);
    }

    public static GrupoProdutoResponseDto construir(GrupoProdutoDto grupoProdutoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        GrupoProduto grupoProduto = new GrupoProduto();
        BeanUtils.copyProperties(grupoProdutoDto, grupoProduto);
        return construir(grupoProduto, integrated, message, exception);
    }
    public static GrupoProdutoResponseDto construir(GrupoProdutoIntegrationSimplesDto grupoProdutoIntegrationSimplesDto, Boolean integrated, String message, Exception exception) {
        GrupoProduto grupoProduto = new GrupoProduto();
        BeanUtils.copyProperties(grupoProdutoIntegrationSimplesDto, grupoProduto);
        return construir(grupoProduto, integrated, message, exception);
    }

    public static GrupoProdutoResponseDto construir(GrupoProdutoIntegrationSimplesDto grupoProdutoIntegrationSimplesDto, Boolean integrated, String message, ConstraintViolationException exception) {
        GrupoProduto grupoProduto = new GrupoProduto();
        BeanUtils.copyProperties(grupoProdutoIntegrationSimplesDto, grupoProduto);
        return construir(grupoProduto, integrated, message, exception);
    }

    public static GrupoProdutoResponseDto construir(GrupoProduto grupoProduto, Boolean integrated, String message) {
        var objDto = new GrupoProdutoResponseDto();
        BeanUtils.copyProperties(grupoProduto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }
    
    public static GrupoProdutoResponseDto construir(GrupoProdutoDto grupoProdutoDto, Boolean integrated, String message) {
        var objDto = new GrupoProdutoResponseDto();
        BeanUtils.copyProperties(grupoProdutoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static GrupoProdutoResponseDto construir(GrupoProduto grupoProduto, Boolean integrated, String message, Exception exception) {
        var objDto = new GrupoProdutoResponseDto();
        BeanUtils.copyProperties(grupoProduto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static GrupoProdutoResponseDto construir(GrupoProduto grupoProduto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new GrupoProdutoResponseDto();
        BeanUtils.copyProperties(grupoProduto, objDto);

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
    
    public static GrupoProdutoResponseDto construir(GrupoProdutoDto grupoProdutoDto, Boolean integrated, String message, String exception) {
        var objDto = new GrupoProdutoResponseDto();
        BeanUtils.copyProperties(grupoProdutoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }
}

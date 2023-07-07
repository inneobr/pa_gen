package br.coop.integrada.api.pa.domain.modelDto.preco;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class PrecoResponseDto extends AbstractResponseDto {

	private static final long serialVersionUID = 1L;
	private String idUnico;
	private IntegrationOperacaoEnum operacao;	

    /* PRECRO SAVE DTO */
    public static PrecoResponseDto construir(PrecoSaveDto precoSaveDto, Boolean integrated, String message, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoSaveDto.converterDto(precoSaveDto);
        return construir(preco, integrated, message, operacao);
    }

    public static PrecoResponseDto construir(PrecoSaveDto precoSaveDto, Boolean integrated, String message, Exception exception, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoSaveDto.converterDto(precoSaveDto);
        return construir(preco, integrated, message, exception, operacao);
    }

    public static PrecoResponseDto construir(PrecoSaveDto precoSaveDto, Boolean integrated, String message, ConstraintViolationException exception, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoSaveDto.converterDto(precoSaveDto);
        return construir(preco, integrated, message, exception, operacao);
    }

    /* PRECRO DTO */
    public static PrecoResponseDto construir(PrecoDto precoDto, Boolean integrated, String message, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoDto.converterDto(precoDto);
        return construir(preco, integrated, message, operacao);
    }

    public static PrecoResponseDto construir(PrecoDto precoDto, Boolean integrated, String message, Exception exception, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoDto.converterDto(precoDto);
        return construir(preco, integrated, message, exception, operacao);
    }

    public static PrecoResponseDto construir(PrecoDto precoDto, Boolean integrated, String message, ConstraintViolationException exception, IntegrationOperacaoEnum operacao) {
        Preco preco = PrecoDto.converterDto(precoDto);
        return construir(preco, integrated, message, exception, operacao);
    }

    /* PRECO */
    public static PrecoResponseDto construir(Preco preco, Boolean integrated, String message, IntegrationOperacaoEnum operacao) {
        var objDto = new PrecoResponseDto();
        BeanUtils.copyProperties(preco, objDto);
        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
        objDto.setMessage(message);

        return objDto;
    }

    public static PrecoResponseDto construir(Preco preco, Boolean integrated, String message, Exception exception, IntegrationOperacaoEnum operacao) {
        var objDto = new PrecoResponseDto();
        BeanUtils.copyProperties(preco, objDto);

        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static PrecoResponseDto construir(Preco preco, Boolean integrated, String message, ConstraintViolationException exception, IntegrationOperacaoEnum operacao) {
        var objDto = new PrecoResponseDto();
        BeanUtils.copyProperties(preco, objDto);

        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
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

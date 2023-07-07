package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class NaturezaOperacaoMovimentoResponseDto extends AbstractResponseDto {
    
    private static final long serialVersionUID = 1L;
    private String idUnico;
	private Integer codGrupo;
	private IntegrationOperacaoEnum operacao;
    
    public static NaturezaOperacaoMovimentoResponseDto construir(
    		NaturezaOperacaoMovimentoIntegrationDto dto,
            Boolean integrated,
            String message,
            ConstraintViolationException exception
        ) {
    	
        NaturezaOperacaoMovimentoResponseDto responseDto = new NaturezaOperacaoMovimentoResponseDto();
        responseDto.setIdUnico(dto.getIdUnico());
        responseDto.setCodGrupo(dto.getCodGrupo());
        responseDto.setOperacao(dto.getOperacao());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        
        if(exception != null) {
            responseDto.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                responseDto.getFieldErrors().add(fieldErrorItem);
            }
        }
        return responseDto;
        
    }
    
    public static NaturezaOperacaoMovimentoResponseDto construir(NaturezaOperacaoMovimentoIntegrationDto dto, Boolean integrated, String message, String exception) {
        var responseDto = new NaturezaOperacaoMovimentoResponseDto();
        responseDto.setIdUnico(dto.getIdUnico());
        responseDto.setCodGrupo(dto.getCodGrupo());
        responseDto.setOperacao(dto.getOperacao());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        responseDto.setException(exception);

        return responseDto;
    }

    public static NaturezaOperacaoMovimentoResponseDto construir(NaturezaOperacaoMovimentoIntegrationDto dto, Boolean integrated, String message) {
        var responseDto = new NaturezaOperacaoMovimentoResponseDto();
        responseDto.setIdUnico(dto.getIdUnico());
        responseDto.setCodGrupo(dto.getCodGrupo());
        responseDto.setOperacao(dto.getOperacao());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);

        return responseDto;
    }

}

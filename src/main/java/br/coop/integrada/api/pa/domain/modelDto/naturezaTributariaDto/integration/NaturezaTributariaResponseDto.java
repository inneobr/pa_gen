package br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.integration;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.BeanUtils;
import javax.validation.ConstraintViolation;

import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturezaTributariaDto;

@Getter
@Setter
public class NaturezaTributariaResponseDto extends AbstractResponseDto{	
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;	
    private IntegrationOperacaoEnum operacao;
    
    public static NaturezaTributariaResponseDto construir(NaturezaTributariaDto naturezaDto, Boolean integrated, String message, ConstraintViolationException exception) {
        
		NaturezaTributariaResponseDto responseDto = new NaturezaTributariaResponseDto();
		responseDto.setOperacao(naturezaDto.getOperacao());
		responseDto.setCodigo(naturezaDto.getCodigo());
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
	
	
	public static NaturezaTributariaResponseDto construir(NaturezaTributaria naturezaDto, Boolean integrated, String message, ConstraintViolationException exception) {
        
		NaturezaTributariaResponseDto naturezaResponse = new NaturezaTributariaResponseDto();

		naturezaResponse.setCodigo(naturezaDto.getCodigo());
		naturezaResponse.setIntegrated(integrated);
		naturezaResponse.setMessage(message);
        
        if(exception != null) {
        	naturezaResponse.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                naturezaResponse.getFieldErrors().add(fieldErrorItem);
            }
        }
        
        return naturezaResponse;
    }


	public static NaturezaTributariaResponseDto construir(NaturezaTributaria natureza, boolean integrated, String message) {
		var naturezaResponse = new NaturezaTributariaResponseDto();
		naturezaResponse.setCodigo(natureza.getCodigo());
		naturezaResponse.setIntegrated(integrated);
		naturezaResponse.setMessage(message);

        return naturezaResponse;
	}
	

	public static NaturezaTributariaResponseDto construir(NaturezaTributaria natureza, boolean integrated, String message, String messageError) {
		var naturezaResponseDto = new NaturezaTributariaResponseDto();
        
		naturezaResponseDto.setCodigo(natureza.getCodigo());
        naturezaResponseDto.setIntegrated(integrated);
        naturezaResponseDto.setMessage(message);
        naturezaResponseDto.setException(messageError);

        return naturezaResponseDto;
	}
	
	public static NaturezaTributariaResponseDto construir(NaturezaTributariaDto naturezaDto, boolean integrated, String message) {
		var naturezaResponse = new NaturezaTributariaResponseDto();
		naturezaResponse.setOperacao(naturezaDto.getOperacao());
		naturezaResponse.setCodigo(naturezaDto.getCodigo());
		naturezaResponse.setIntegrated(integrated);
		naturezaResponse.setMessage(message);
        return naturezaResponse;
	}
	

	public static NaturezaTributariaResponseDto construir(NaturezaTributariaDto naturezaDto, boolean integrated, String message, IntegrationOperacaoEnum operacao) {
		var naturezaResponse = new NaturezaTributariaResponseDto();
		naturezaResponse.setOperacao(naturezaDto.getOperacao());
		naturezaResponse.setCodigo(naturezaDto.getCodigo());
		naturezaResponse.setIntegrated(integrated);
		naturezaResponse.setMessage(message);
		naturezaResponse.setOperacao(operacao);
        return naturezaResponse;
	}
	
   public static NaturezaTributariaResponseDto construir(NaturezaTributariaDto naturezaDto, Boolean integrated, String message, Exception exception) {
        var objDto = new NaturezaTributariaResponseDto();
        BeanUtils.copyProperties(naturezaDto, objDto);
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());
        return objDto;
    }

}
  

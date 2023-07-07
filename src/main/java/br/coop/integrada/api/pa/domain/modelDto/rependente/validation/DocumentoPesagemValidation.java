package br.coop.integrada.api.pa.domain.modelDto.rependente.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class DocumentoPesagemValidation {
		
	private String codEstabel;
	private Integer safra;
	private Integer nrDocPes;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean liberado;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mensagem;
}

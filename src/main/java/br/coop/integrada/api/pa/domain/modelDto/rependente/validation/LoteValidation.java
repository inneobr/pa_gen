package br.coop.integrada.api.pa.domain.modelDto.rependente.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class LoteValidation {

	private String codEstabel;
	private String codItem;
	private String lote;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mensagem;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean liberado;
}

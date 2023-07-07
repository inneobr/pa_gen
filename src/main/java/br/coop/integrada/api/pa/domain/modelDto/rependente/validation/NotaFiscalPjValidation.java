package br.coop.integrada.api.pa.domain.modelDto.rependente.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class NotaFiscalPjValidation {
	
	private String nrNotaFiscal;
	private String serie;
	private String natOperacao;
	private String codProdutor;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean existeErp;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mensagem;
}

package br.coop.integrada.api.pa.domain.modelDto.rependente.validation;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class ProdutoPadraoValidation {

	private String codProdutor;
	private Integer safra;
	private String fmCodigo;
	private BigDecimal umidade;
	private BigDecimal impureza;
	private BigDecimal chuvAvar;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean padronizado;
		
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mensagem;
}

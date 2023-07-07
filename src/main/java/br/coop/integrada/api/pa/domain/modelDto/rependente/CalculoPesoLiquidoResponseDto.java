package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoPesoLiquidoResponseDto {
	
	private BigDecimal pesoLiquido;
		
	private String mensagem;
	
	public CalculoPesoLiquidoResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}

}

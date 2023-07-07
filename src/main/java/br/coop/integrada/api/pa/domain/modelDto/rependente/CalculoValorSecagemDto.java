package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoValorSecagemDto {
	/*
	 Peso líquido (quantidade sem desconto de classificação) 
	 Percentual de desconto da impureza
	 Valor da taxa de limpeza obtido na tabela de umidade
	 Preço de Ponto
	 */
	
	private BigDecimal pesoLiquido;
	private BigDecimal percentualDescontoImpureza;
	private BigDecimal valorTaxaLimpeza;
	private BigDecimal precoPonto;
	
	
	
}

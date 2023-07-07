package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoRendaLiquidaDto {
	/*
	 * Calcula Secagem (sim/não)
	 * Peso líquido (quantidade sem desconto de classificação)
	 * %Desc.Impureza
	 * %Desc.Umidade
	 * %Desc.Chuvado/Avariado
	 * %TBM
	 * Quantidade recepção
	 * Quantidade Secagem
	 * Taxa Secagem
	 */
	private Boolean calculaSecagem;
	private BigDecimal pesoLiquido;
	private BigDecimal percDescImpureza;
	private BigDecimal percDescUmidade;
	private BigDecimal percDescChuvadoAvariado;
	private BigDecimal percTBM;
	private BigDecimal quantidadeRecepcao;
	private BigDecimal quantidadeSecagem;
	private BigDecimal taxaSecagem;
	
	public BigDecimal getPesoLiquido() {
		if(pesoLiquido == null) return BigDecimal.ZERO;
		return pesoLiquido;
	}
	
	public BigDecimal getPercDescImpureza() {
		if(percDescImpureza == null) return BigDecimal.ZERO;
		return percDescImpureza;
	}
	
	public BigDecimal getPercDescUmidade() {
		if(percDescUmidade == null) return BigDecimal.ZERO;
		return percDescUmidade;
	}
	
	public BigDecimal getPercDescChuvadoAvariado() {
		if(percDescChuvadoAvariado == null) return BigDecimal.ZERO;
		return percDescChuvadoAvariado;
	}
	
	public BigDecimal getPercTBM() {
		if(percTBM == null) return BigDecimal.ZERO;
		return percTBM;
	}
	
	public BigDecimal getQuantidadeRecepcao() {
		if(quantidadeRecepcao == null) return BigDecimal.ZERO;
		return quantidadeRecepcao;
	}
	
	public BigDecimal getQuantidadeSecagem() {
		if(quantidadeSecagem == null) return BigDecimal.ZERO;
		return quantidadeSecagem;
	}
	
	public BigDecimal getTaxaSecagem() {
		if(taxaSecagem == null) return BigDecimal.ZERO;
		return taxaSecagem;
	}
}

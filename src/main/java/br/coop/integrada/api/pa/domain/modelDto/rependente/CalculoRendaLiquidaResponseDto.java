package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.Data;

@Data
public class CalculoRendaLiquidaResponseDto {
	/*
	 * Parâmetros de OUTPUT:
	 * Qt.Impureza
	 * Qt.Umidade
	 * Qt.Chuvado/Avariado
	 * Qt.Secagem (quando calcula secagem = sim, senão retorna o valor passado no parâmetro)
	 * Qt.TBM
	 * Qt.Renda Líquida
	 */
	
	private BigDecimal qtdImpureza;
	private BigDecimal qtdUmidade;
	private BigDecimal qtdChuvaAvariado;
	private BigDecimal qtdSecagem;
	private BigDecimal qtdTBM;
	private BigDecimal qtdRendaLiquida;
	
	private String mensagem;
	
	public CalculoRendaLiquidaResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
	
	
}

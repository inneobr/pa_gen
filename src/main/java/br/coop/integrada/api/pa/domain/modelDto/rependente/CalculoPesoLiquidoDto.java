package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoPesoLiquidoDto {
	
	private BigDecimal rendaLiquida; //(peso - classificação)
	private BigDecimal percDescImpureza;
	private BigDecimal percDescUmidade;
	private BigDecimal percDescChvadoAvariado;
	private BigDecimal qtdRecepcao;
	private BigDecimal qtdSecagem;
	private BigDecimal qtdTbm;
	private BigDecimal percTbm;
	private Boolean tipoTbm;
}

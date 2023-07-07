package br.coop.integrada.api.pa.domain.modelDto.nfRemessa;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class IndicadorNfRemessaDto {
	
	private BigDecimal aguardandoIntegracao = BigDecimal.ZERO;
	private BigDecimal emProcesso = BigDecimal.ZERO;
	private BigDecimal aguardandoTotvs = BigDecimal.ZERO;
	private BigDecimal nfeGerada = BigDecimal.ZERO;
	private BigDecimal funcaoGera = BigDecimal.ZERO;
	private BigDecimal funcaoEscritura = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;

}

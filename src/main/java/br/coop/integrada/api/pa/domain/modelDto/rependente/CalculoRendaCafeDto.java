package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoRendaCafeDto {
	
	private BigDecimal qtdCafeCoco;
	private BigDecimal qtdCafeBeneficiado;

}

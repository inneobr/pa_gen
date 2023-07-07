package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CalculoRecepcaoDto {
		
	private BigDecimal quantidade;
	private BigDecimal valorPonto;
	private BigDecimal valorRecepcao;
	
}

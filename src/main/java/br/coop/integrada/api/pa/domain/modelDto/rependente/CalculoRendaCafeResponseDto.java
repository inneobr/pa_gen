package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculoRendaCafeResponseDto {
	
	private BigDecimal resultado;
	private String mensagem;

	public CalculoRendaCafeResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
}

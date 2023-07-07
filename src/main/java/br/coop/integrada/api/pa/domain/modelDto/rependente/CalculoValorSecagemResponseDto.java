package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculoValorSecagemResponseDto {
	
	@Digits( integer = 19 , fraction = 9)
	private BigDecimal valorSecagem;
	
	@Digits( integer = 19 , fraction = 9)
	private BigDecimal quantidadeSecagem;
	
	
	private String mensagem;
	
	public CalculoValorSecagemResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
}

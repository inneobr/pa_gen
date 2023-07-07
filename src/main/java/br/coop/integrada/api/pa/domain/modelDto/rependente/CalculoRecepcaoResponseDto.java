package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculoRecepcaoResponseDto {
	
	@Digits( integer = 19 , fraction = 9)
	private BigDecimal valorRecepcao;
	
	@Digits( integer = 19 , fraction = 0)
	private BigDecimal quantidadeRecepcao;
		
	private String mensagem;
	
	public CalculoRecepcaoResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
}

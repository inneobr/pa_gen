package br.coop.integrada.api.pa.domain.modelDto.pesagem;


import java.math.BigDecimal;

import br.coop.integrada.api.pa.domain.enums.OperacaoPesagemEnum;
import lombok.Data;

@Data
public class CriarMovimentoPesagemDto {
	
	public String codEstabel;
	public Long nrRe;
	public OperacaoPesagemEnum operacao;
	public String serie;
	public String nroNota;
	public String natOperacao;
	public Boolean estornado;
	public BigDecimal quantidade;
		
}

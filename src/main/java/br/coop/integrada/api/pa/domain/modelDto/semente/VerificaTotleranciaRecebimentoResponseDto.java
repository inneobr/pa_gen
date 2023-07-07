package br.coop.integrada.api.pa.domain.modelDto.semente;

import lombok.Data;

@Data
public class VerificaTotleranciaRecebimentoResponseDto {
	private String tipoRetorno;
	private String mensagem;
	
	public VerificaTotleranciaRecebimentoResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
	
}

package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import lombok.Data;

@Data
public class ValidarPesagemResponse {
	private Boolean status;
	private String mensagem;
	private String horaSaída;
}

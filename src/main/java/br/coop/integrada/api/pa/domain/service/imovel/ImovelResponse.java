package br.coop.integrada.api.pa.domain.service.imovel;

import lombok.Data;

@Data
public class ImovelResponse {
	private String cadPro;
	private boolean bloqueado;
	private String mensagem;
}

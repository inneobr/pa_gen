package br.coop.integrada.api.pa.domain.enums.integration;

public enum TipoIntegracaoLogEnum {
	REQUEST("Requisição"),
	RESPONSE("Retorno");
	
	private String descricao;

	TipoIntegracaoLogEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

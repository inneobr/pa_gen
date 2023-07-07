package br.coop.integrada.api.pa.domain.enums.integration;

public enum SituacaoIntegracaoLogEnum {
	SUCESSO("Sucesso"),
	FALHA("Falha");
	
	private String descricao;

	SituacaoIntegracaoLogEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

package br.coop.integrada.api.pa.domain.enums.integration;

public enum TipoAuthenticationEnum {
	BEARER("Bearer"), BASIC("Basic");
	
	private String descricao;

	TipoAuthenticationEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

}

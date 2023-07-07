package br.coop.integrada.api.pa.domain.enums.integration;

public enum SituacaoFuncionalidadePaginaEnum {
	ATIVO("Ativo"),
	INATIVO("Inativo");
	
	private final String descricao;
    
    private SituacaoFuncionalidadePaginaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

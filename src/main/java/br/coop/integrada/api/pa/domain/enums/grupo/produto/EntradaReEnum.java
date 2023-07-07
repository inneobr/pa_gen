package br.coop.integrada.api.pa.domain.enums.grupo.produto;

public enum EntradaReEnum {
	PERMITE ("Permite"),
	NAO_PERMITE ("Não Permite"),
	CONDICAO("Condição");
	
	private String descricao;
	
	EntradaReEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
		
}

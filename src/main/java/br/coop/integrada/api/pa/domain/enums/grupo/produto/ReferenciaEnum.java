package br.coop.integrada.api.pa.domain.enums.grupo.produto;

public enum ReferenciaEnum {
	TIPO ("Tipo"),
	NENHUM ("Nenhum");
	
	private String descricao;
	
	ReferenciaEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}

package br.coop.integrada.api.pa.domain.enums.re.pendente;

public enum UnidadeDesmembramento {
	PERCENTUAL ("Percentual"),
	QUILOS ("Quilos"),
	SACAS ("Sacas");

	private String descricao;
	
	UnidadeDesmembramento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}

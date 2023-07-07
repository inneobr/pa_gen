package br.coop.integrada.api.pa.domain.enums.gerarRe;

public enum TipoEntradaEnum {
	ENTRADA("Entrada"),
	DEVOLUCAO("Devolução"),
	TRANSFERENCIA("Transferência");
	
	private String descricao;
	
	TipoEntradaEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}

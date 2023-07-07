package br.coop.integrada.api.pa.domain.enums;

public enum TipoPesagemBalancaEnum {
	MANUAL("Manual", false), AUTOMATICA("Automática", true);
	
	private String descricao;
	private boolean pesoAutomatico;
	
	private TipoPesagemBalancaEnum(String descricao, boolean pesoAutomatico) {
		this.descricao = descricao;
		this.pesoAutomatico = pesoAutomatico;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public boolean isPesoAutomatico() {
		return pesoAutomatico;
	}

	
}

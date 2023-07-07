package br.coop.integrada.api.pa.domain.enums;

public enum ParametroEstabelecimentoSituacaoEnum {
	BLOQUEADO ("Bloqueado"),
	LIBERADO_TOTAL ("Liberado Total");
	
	private String descricao;
	
	private ParametroEstabelecimentoSituacaoEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}

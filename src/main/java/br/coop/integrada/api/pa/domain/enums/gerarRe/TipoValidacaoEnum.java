package br.coop.integrada.api.pa.domain.enums.gerarRe;

public enum TipoValidacaoEnum {
	NFPJ("NF PJ"),
	DOCPESAGEM("Documento Pesagem"),
	LOTE("Lote RE"),
	PRODPADRONIZADO("Produto Padronizado");
	
	private String descricao;
	
	TipoValidacaoEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
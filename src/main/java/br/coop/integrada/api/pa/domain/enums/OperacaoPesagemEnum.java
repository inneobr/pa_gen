package br.coop.integrada.api.pa.domain.enums;

public enum OperacaoPesagemEnum {
	REMESSA_DEPOSITO("Remessa Depósito", 1, "ENT"),
	RETORNO_DEP_DEVOLUCAO_ENT("Retorno Depósito Dev.Entrada", 2, "SAI" ),
	REMESSA_DEP_DEVOLUCAO_ENT("Remessa Depósito Dev.Entrada", 3, "ENT" ),
	RETORNO_DEP_FIXACAO("Retorno Depósito Fixação", 4, "SAI"),
	FIXACAO("Fixação", 5, "ENT"),
	RETORNO_DEP_TRANSF("Retorno Depósito Mov.Prod", 6, "SAI"),
	REMESSA_DEP_TRANSF("Remessa Depósito Mov.Prod", 7, "ENT"),
	RETORNO_DEP_RETIRADA("Retorno Depósito Retirada", 8, "SAI"),
	DEVOLUCAO_FIXACAO("Dev.Fixação", 9, "SAI"),
	REMESSA_DEP_DEV_FIXACAO("Remessa Depósito Dev.Fixação", 10, "ENT");
		
	private final String descricao;
	//TRUE para quando for ENT FALSE para quando SAI
	private final String EntSai;
	private final Integer codDataSul;
		
	
	OperacaoPesagemEnum(String descricao, Integer codDataSul, String EntSai){
		this.descricao = descricao;
		this.EntSai = EntSai;
		this.codDataSul = codDataSul;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getEntSai() {
		return this.EntSai;
	}
	
	public Integer getCodDataSul() {
		return this.codDataSul;
	}
	
	
}

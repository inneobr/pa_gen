package br.coop.integrada.api.pa.domain.enums;

public enum StatusNotaFiscalEnum {
	
	AGUARDANDO_INTEGRACAO("Aguardando Integração"), 
	EM_PROCESSAMENTO("Em Processamento"),
	AGUARDANDO_TOTVS("Aguardando TOTVS"),
	NFE_GERADA("NFe Gerada");
	
	private final String descricao;
    
	StatusNotaFiscalEnum(String descricao) {
        this.descricao = descricao;
    }

	public String getDescricao() {
		return descricao;
	} 
}

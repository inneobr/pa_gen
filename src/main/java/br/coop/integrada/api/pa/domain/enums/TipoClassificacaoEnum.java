package br.coop.integrada.api.pa.domain.enums;

public enum TipoClassificacaoEnum {
	
	IMPUREZA("Impureza"),
	UMIDADE("Umidade"),
	CHUVA_AVARIADO("Chuvado e Avariado"),
	PH("PH");	
	
	private final String descricao;
	
	TipoClassificacaoEnum(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	} 
	
}

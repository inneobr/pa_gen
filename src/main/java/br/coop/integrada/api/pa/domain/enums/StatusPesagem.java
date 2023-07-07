package br.coop.integrada.api.pa.domain.enums;

public enum StatusPesagem {
	AGUARDANDO_RE("Aguard. RE"), 
	GERANDO_RE("Gerando RE"), 
	CONCLUIDO("Concluído");
	
	private final String descricao;
    
    StatusPesagem(String descricao) {
        this.descricao = descricao;
    }

	public String getDescricao() {
		return descricao;
	} 
}

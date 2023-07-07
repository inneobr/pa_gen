package br.coop.integrada.api.pa.domain.enums;

public enum StatusIntegracao {
	INTEGRAR("Integrar", false), 
	INTEGRADO("Integrado", true), 
	PROCESSANDO("Processando", false);
	
	private final String descricao;
    private final Boolean integrado;
    
    StatusIntegracao(String descricao, Boolean integrado) {
        this.descricao = descricao;
        this.integrado = integrado;
    }

	public Boolean getIntegrado() {
		return integrado;
	}

	public String getDescricao() {
		return descricao;
	} 
}

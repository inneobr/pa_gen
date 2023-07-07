package br.coop.integrada.api.pa.domain.enums.integration;

public enum TipoIntegracaoEnum {
	AUTOMATICA("Automatica"), MANUAL("Manual");
    
    private final String descricao;
    
    private TipoIntegracaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

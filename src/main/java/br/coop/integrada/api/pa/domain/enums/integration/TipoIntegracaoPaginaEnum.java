package br.coop.integrada.api.pa.domain.enums.integration;

public enum TipoIntegracaoPaginaEnum {
    
    AUTOMATICA("Automática"), MANUAL("Manual");
    
    private final String descricao;
    
    private TipoIntegracaoPaginaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}

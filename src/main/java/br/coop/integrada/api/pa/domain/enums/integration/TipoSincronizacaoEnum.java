package br.coop.integrada.api.pa.domain.enums.integration;

public enum TipoSincronizacaoEnum {

    SINCRONA("Sincrona"), ASSINCRONA("Assíncrona");
    
    private final String descricao;
    
    private TipoSincronizacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

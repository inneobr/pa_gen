package br.coop.integrada.api.pa.domain.enums.pedidoIntegracao;

public enum TipoIntegracaoEnum {
	CAMPOS ("Campos"),
    TABELACOMPLETA ("Tabela Completa");
    
    

    private String descricao;

    TipoIntegracaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}

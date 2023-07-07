package br.coop.integrada.api.pa.domain.enums.integration;

public enum OrigemInputEnum {
	GENESIS("Genesis"), ERP("Erp"), DATASUL("Datasul");
    
    private final String descricao;
    
    private OrigemInputEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

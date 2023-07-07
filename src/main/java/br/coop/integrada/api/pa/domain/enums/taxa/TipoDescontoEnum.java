package br.coop.integrada.api.pa.domain.enums.taxa;

public enum TipoDescontoEnum {
    NENHUM ("Nenhum"),
    VALOR ("valor"),
    PRODUTO ("Produto");


    private String descricao;
    TipoDescontoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

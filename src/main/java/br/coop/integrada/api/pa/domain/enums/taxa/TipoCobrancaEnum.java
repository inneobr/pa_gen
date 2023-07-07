package br.coop.integrada.api.pa.domain.enums.taxa;

public enum TipoCobrancaEnum {
    NENHUM ("Nenhum"),
    ENTRADA ("Entrada"),
    FIXACAO ("Fixação");

    private String descricao;
    TipoCobrancaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

package br.coop.integrada.api.pa.domain.enums;

public enum Operacao {
    FIXACAO_TRIBUTADA ("Finação Tributada"),
    ENTRADA ("Entrada"),
    DEV_ENTRADA ("Dev Entrada"),
    FIXACAO ("Fixação"),
    FIXACAO_SEMENTE ("Fixação Semente"),
    DEV_SEMENTE ("Dev Semente");

    private String descricao;

    Operacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}

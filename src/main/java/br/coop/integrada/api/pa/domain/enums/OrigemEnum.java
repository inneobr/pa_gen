package br.coop.integrada.api.pa.domain.enums;

public enum OrigemEnum {
    MANUAL ("Manual"),
    IMPORTACAO ("Importação");

    private String descricao;

    OrigemEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

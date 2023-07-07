package br.coop.integrada.api.pa.domain.enums;

public enum TipoSafraEnum {
    INVERNO ("Inverno"),
    VERAO ("Verão");

    private String descricao;

    TipoSafraEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

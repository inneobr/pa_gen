package br.coop.integrada.api.pa.domain.enums;

public enum ItemAvariadoValidacaoEnum {
    CHUVADO_AVARIADO("Chuvado Avariado"),
	PRODUTO("Produto"),
    PH("PH");

    private final String descricao;

    ItemAvariadoValidacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

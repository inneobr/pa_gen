package br.coop.integrada.api.pa.domain.enums;

public enum OperacaoProdutoEnum {
	FIXACAO ("Fixação"),
    AFIXAR("A Fixar");

    private String descricao;

    OperacaoProdutoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}

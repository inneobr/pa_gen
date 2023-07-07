package br.coop.integrada.api.pa.domain.enums;

public enum NaturezaEnum {
	PF("PF"),
	PJ("PJ"),
	COOP("Coop"),
	PJ_NF("PJ C/ NF");

    private String descricao;

    NaturezaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
	
}

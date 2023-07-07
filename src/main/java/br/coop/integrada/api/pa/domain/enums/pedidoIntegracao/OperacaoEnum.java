package br.coop.integrada.api.pa.domain.enums.pedidoIntegracao;

public enum OperacaoEnum {
	WRITE ("Write"),
    DELETE ("Delete"),
    CREATE ("Create");
    

    private String descricao;

    OperacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}

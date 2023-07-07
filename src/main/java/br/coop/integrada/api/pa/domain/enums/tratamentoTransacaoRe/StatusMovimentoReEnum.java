package br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe;

public enum StatusMovimentoReEnum {
		
    OK("Ok"),
    FALHA("Falha");    

    private String descricao;

    StatusMovimentoReEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

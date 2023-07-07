package br.coop.integrada.api.pa.domain.enums.taxa;

public enum TipoCobrancaSecagemEnum {
    NENHUM ("Nenhum"),
    VALOR ("Valor"),
    EM_QUILOS ("Em quilos");

    private String descricao;
    TipoCobrancaSecagemEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

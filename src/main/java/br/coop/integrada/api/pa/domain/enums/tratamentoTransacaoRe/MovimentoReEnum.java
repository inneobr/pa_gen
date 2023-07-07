package br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe;

public enum MovimentoReEnum {
	
	CRIACAO_RE ("Criação de RE", 30, 1),
    SOLICITADO_NRO_RE ("Solicitado Nro RE", 31, 2),
    ENVIO_RE_DATASUL("Envio RE DATASUL", 33, 3),
    SOLICITADO_NF_REMESSA("Solicitado NF Remessa", 32, 4),
    ABERTO("Aberto", 1, 5);    

    private String descricao;
    private Integer codSit;
    private Integer ordem;

    MovimentoReEnum(String descricao, Integer codSit, Integer ordem) {
        this.descricao = descricao;
        this.codSit = codSit;
        this.ordem = ordem;
    }

    public String getDescricao() {
        return descricao;
    }

	public Integer getCodSit() {
		return codSit;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
}

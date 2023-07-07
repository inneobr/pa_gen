package br.coop.integrada.api.pa.domain.enums;

public enum HistoricoEnum {
	CADASTROU("Cadastrou"),  ATUALIZOU("Atualizou"), DELETOU("Deletou"), INATIVOU("Inativou"), ATIVOU("Ativou"), INTEGROU("Integrou");	
	private final String descricao;
	
	HistoricoEnum(String descricao){
        this.descricao = descricao;
    }
	
	public String getDescricao() {
		return descricao;
	}
}

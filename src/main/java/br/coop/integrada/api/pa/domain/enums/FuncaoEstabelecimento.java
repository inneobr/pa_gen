package br.coop.integrada.api.pa.domain.enums;

public enum FuncaoEstabelecimento {
	Entrada_RE("Entrada RE"), 
	Alterar_Padronizado("Alterar Padronizado"), 
	Cancelar_RE("Cancelar RE"),
	Fechamento("Fechamento"),
	Agendamento("Agendamento"),
	Retirada_Produto("Retirada de Produto"),
	Cancelar_Fechamento("Cancelar Fechamento"),
	Liberar_Senha("Liberar Senha"),
	Encerramento_Diario("Encerramento Diario"),
	Transferencia("Transferência"),
	Transferencia_RE_Cooperativa("Transferência RE Cooperativa"),
	Alterar_Contrato("Alterar Contrato"),
	Baixa_Contrato_massa("Baixa Contrato em Massa");
	
	private final String descricao;
	
	FuncaoEstabelecimento(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}

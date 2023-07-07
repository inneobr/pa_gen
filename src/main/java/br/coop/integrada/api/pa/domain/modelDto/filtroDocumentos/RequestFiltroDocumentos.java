package br.coop.integrada.api.pa.domain.modelDto.filtroDocumentos;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

@Data
public class RequestFiltroDocumentos {	
	private String username;
	
	@CPF
	private String cpf;
	
	@CNPJ
	private String cnpj;
}

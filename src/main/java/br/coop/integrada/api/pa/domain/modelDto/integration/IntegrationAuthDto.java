package br.coop.integrada.api.pa.domain.modelDto.integration;

import lombok.Data;

@Data
public class IntegrationAuthDto {
	private Long id;	
	private String login;
	private String senha;
	private String descricao;
}

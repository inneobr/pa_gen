package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.Data;

@Data
public class NaturezaOperacaoEstabelecimentoIntegrationDto {
	
	private String idUnico;
	private String codigo;
	private Integer codGrupo;
	private IntegrationOperacaoEnum operacao;	
	

}

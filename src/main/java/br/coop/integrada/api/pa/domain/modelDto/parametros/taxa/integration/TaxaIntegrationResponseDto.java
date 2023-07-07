package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.Data;

@Data
public class TaxaIntegrationResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idTaxa;
	private String message;
	private Boolean integrated;
    private IntegrationOperacaoEnum operacao;
		
}
	

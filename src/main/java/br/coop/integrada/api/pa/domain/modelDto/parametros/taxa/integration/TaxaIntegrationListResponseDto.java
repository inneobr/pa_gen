package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.util.List;

import br.coop.integrada.api.pa.domain.modelDto.ResultsDto;
import lombok.Data;

@Data
public class TaxaIntegrationListResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ResultsDto> results;
	private List<TaxaIntegrationResponseDto> taxas;
	
}

package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.util.List;

import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import lombok.Data;

@Data
public class TaxaIntegrationListDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<TaxaIntegrationDto> taxas;
	
	public static TaxaIntegrationListDto construir(List<Taxa> objs) {
		var objDto = new TaxaIntegrationListDto();
		List<TaxaIntegrationDto> taxas = TaxaIntegrationDto.construir(objs);
		objDto.setTaxas(taxas);
		return objDto;
	}
}

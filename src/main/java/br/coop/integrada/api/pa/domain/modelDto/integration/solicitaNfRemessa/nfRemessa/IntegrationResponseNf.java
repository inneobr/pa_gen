package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa;

import lombok.Data;
import java.util.List;

@Data
public class IntegrationResponseNf {
	private List<ResponseNfRemessa> integrationNf;
}

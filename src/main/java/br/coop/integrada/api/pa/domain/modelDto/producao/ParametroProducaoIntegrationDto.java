package br.coop.integrada.api.pa.domain.modelDto.producao;

import java.io.Serializable;

import lombok.Data;

@Data
public class ParametroProducaoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ParametroProducaoDto parametroProducao;
}

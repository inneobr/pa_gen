package br.coop.integrada.api.pa.domain.modelDto.parametros;

import lombok.Data;

@Data
public class ValidaEntradaCooperativaDto {
	private String codEstabelecimento;
	private String codProdutor;
	private Long codImovel;
}

package br.coop.integrada.api.pa.domain.modelDto.semente;

import lombok.Data;

@Data
public class VerificaTotleranciaRecebimentoDto {
	
	private String codEstabelecimento;
	private Integer safra;
	private String grupoProduto;
	private Integer numeroOrdemCampo;
	private Long numeroLaudo;
	
}

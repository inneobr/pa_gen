package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class validacaoDesmembramentoPendente {
	private String codProdutorFavorecido;
	private String nomeProdutorFavorecido;
	private Long codImovel; 
	private BigDecimal quantidade;
	private Boolean responsavelPagaKit;
}

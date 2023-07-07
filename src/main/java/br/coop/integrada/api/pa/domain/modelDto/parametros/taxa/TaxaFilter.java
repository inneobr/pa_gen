package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaxaFilter implements Serializable {
	
	private String descricao;
    private Integer safra;
    private Long idGrupoProduto;
    private Long idEstabelecimento;
}

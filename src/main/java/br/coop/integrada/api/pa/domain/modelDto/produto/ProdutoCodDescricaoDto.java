package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.Data;

@Data
public class ProdutoCodDescricaoDto {
	private Long id;
	private String codItem;
	private String descItem;
	
	public String getCodigoDescricao() {
		return codItem + " - " + descItem;
	}
}

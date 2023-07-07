package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.Data;

@Data
public class ProdutoFilterDto {
	private Long id;
	private String codItem;	
	private String descItem;
	
	public String getCodDescricao() {
		return codItem + " - " + descItem.toUpperCase();
	}
}

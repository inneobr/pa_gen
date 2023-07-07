package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.Data;

@Data
public class ProdutoValidDto {
	private Boolean cadastrado;
	private String  codigoGrupoProduto;	
	private String  descricaoProduto;	

	public static ProdutoValidDto construir(Boolean cadastrado, String descricaoProduto, String codigoGrupoProduto) {
		var objDto = new ProdutoValidDto();
		objDto.setCadastrado(cadastrado);
		objDto.setDescricaoProduto(descricaoProduto);
		objDto.setCodigoGrupoProduto(codigoGrupoProduto);
		return objDto;
	}
}

package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.Data;

@Data
public class ProdutoReferenciaValidDto {
	private boolean cadastrado;
	private String descricaProduto;
	private String codigoReferecia;
	
	public static ProdutoReferenciaValidDto construir(Boolean cadastrado, String descricaoProduto, String codigoReferencia) {
		var objDto = new ProdutoReferenciaValidDto();
		objDto.setCadastrado(cadastrado);
		objDto.setDescricaProduto(descricaoProduto);
		objDto.setCodigoReferecia(codigoReferencia);
		return objDto;
	}
}

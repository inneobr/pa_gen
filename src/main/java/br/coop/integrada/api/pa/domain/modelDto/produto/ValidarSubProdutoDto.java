package br.coop.integrada.api.pa.domain.modelDto.produto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidarSubProdutoDto {
	private Boolean isSubProduto;
	private String mensagem;

	public ValidarSubProdutoDto(String mensagem) {
		this.mensagem = mensagem;
	}
}

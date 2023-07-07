package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.io.Serializable;

import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPostDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoDto;
import lombok.Data;

@Data
public class ProdutoPesagemDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ProdutoDto produto;
	private GrupoProdutoDto grupoProduto;
	private PesagemPostDto pesagem;
	
	public static ProdutoPesagemDto construir(Produto produto, GrupoProduto grupoProduto, Pesagem pesagem) {
		var objDto = new ProdutoPesagemDto();
		
		if(produto != null) {
			ProdutoDto produtoDto = ProdutoDto.construir(produto);
			objDto.setProduto(produtoDto);
		}
		
		if(grupoProduto != null) {
			GrupoProdutoDto grupoProdutoDto = GrupoProdutoDto.construir(grupoProduto, null);
			objDto.setGrupoProduto(grupoProdutoDto);
		}
		
		PesagemPostDto pesagemDto = PesagemPostDto.construir(pesagem);
		objDto.setPesagem(pesagemDto);
		
		return objDto;
	}
}

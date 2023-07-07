package br.coop.integrada.api.pa.domain.model.rependente;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import lombok.Data;

@Data
public class RePendenteProdutoDto {
	private String codItem;
	private String descItem;
	private GrupoProduto grupoProduto;
}

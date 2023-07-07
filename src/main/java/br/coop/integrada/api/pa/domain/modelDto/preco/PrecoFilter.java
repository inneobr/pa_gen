package br.coop.integrada.api.pa.domain.modelDto.preco;

import java.io.Serializable;
import lombok.Data;


@Data 
public class PrecoFilter implements Serializable{
	private Long idGrupoProduto;
	private String codigoProduto;
	private String codigoGrupoProduto;
	private String codigoRegional;
	private String codigoEstabelecimento;
	private String descricao;
	
}

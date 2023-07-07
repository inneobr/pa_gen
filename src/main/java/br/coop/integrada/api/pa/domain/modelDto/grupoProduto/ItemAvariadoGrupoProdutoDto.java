package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;



import java.util.ArrayList;
import java.util.Collection;

import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import lombok.Data;

@Data
public class ItemAvariadoGrupoProdutoDto {
	private Long id;
	private String fmCodigo;
	private String descricao;
	
	public String getCodigoGrupo() {
		return fmCodigo + " - " + descricao;
	}
	
	private Collection<ProdutoGetDto> produto = new ArrayList<>();

}

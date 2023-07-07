package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import lombok.Data;

@Data
public class GrupoProdutoFilter {
	private String pesquisar;
	private Long   tipoProduto;
	private String fmCodigo;
	private String descricao;
	private String fmCodigoOudescricao;
	private String tipoAvariado;
	
}

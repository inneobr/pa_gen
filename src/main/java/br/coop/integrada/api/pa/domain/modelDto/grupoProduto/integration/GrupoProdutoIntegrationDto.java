package br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GrupoProdutoIntegrationDto implements Serializable {

    private static final long serialVersionUID = 1L;
	List<GrupoProdutoIntegrationSimplesDto> grupoProdutos;
}

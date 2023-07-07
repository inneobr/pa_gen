package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ProdutoIntegrationDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<ProdutoDto> produtos;
}

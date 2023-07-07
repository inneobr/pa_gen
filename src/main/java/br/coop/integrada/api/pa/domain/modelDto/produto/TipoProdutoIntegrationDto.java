package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoProdutoIntegrationDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<TipoProdutoDto> tipoProduto;

}

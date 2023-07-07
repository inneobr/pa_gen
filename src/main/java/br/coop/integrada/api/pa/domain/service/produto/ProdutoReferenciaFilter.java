package br.coop.integrada.api.pa.domain.service.produto;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ProdutoReferenciaFilter {
	private String referencia;
	private Situacao situacao;
}

package br.coop.integrada.api.pa.domain.repository.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface TipoProdutoQueriesRep {
	
	Page<TipoProduto> findAll(Pageable pageable, String nome, Situacao situacao);

}

package br.coop.integrada.api.pa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;


@Repository
public interface ProdutoReferenciaQueriesRep {
	Page<ProdutoReferencia> findAll(Pageable pageable, String filter);
}

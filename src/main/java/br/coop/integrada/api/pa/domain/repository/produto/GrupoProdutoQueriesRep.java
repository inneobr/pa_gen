package br.coop.integrada.api.pa.domain.repository.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface GrupoProdutoQueriesRep {
	Page<GrupoProduto> findAll(Pageable pageable, Situacao situacao);
	Page<GrupoProduto> findAll(Pageable pageable, String filtro, Situacao situacao);
}

package br.coop.integrada.api.pa.domain.repository.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface ProdutoQueriesRep {
	
	Page<Produto> findAll(String codigoOuDescricao, String codigoGrupoProduto, Pageable pageable);
	Page<Produto> findAll(Pageable pageable, String filtro, Situacao situacao);
	Page<Produto> findAll(Pageable pageable, ProdutoFilter filtro, Situacao situacao);
}

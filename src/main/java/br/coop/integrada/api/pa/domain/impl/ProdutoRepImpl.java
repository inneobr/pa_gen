package br.coop.integrada.api.pa.domain.impl;

import static br.coop.integrada.api.pa.domain.spec.ProdutoSpecs.codigoGrupoProdutoEquals;
import static br.coop.integrada.api.pa.domain.spec.ProdutoSpecs.situacaoEquals;
import static br.coop.integrada.api.pa.domain.spec.enums.Situacao.ATIVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilter;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.spec.GrupoProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.ProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class ProdutoRepImpl implements ProdutoQueriesRep {

	@Lazy
	@Autowired
	private ProdutoRep produtoRep;

	@Override
	public Page<Produto> findAll(String codigoOuDescricao, String codigoGrupoProduto, Pageable pageable) {
		return produtoRep.findAll(
				ProdutoSpecs.codigoOuDescricaoLike(codigoOuDescricao)
				.and(codigoGrupoProdutoEquals(codigoGrupoProduto))
				.and(situacaoEquals(ATIVO)),
				pageable);
	}
	
	public Page<Produto> findAll(Pageable pageable, String filtro, Situacao situacao) {
		return produtoRep.findAll(
				ProdutoSpecs.codigoOuDescricaoLike(filtro)
                		.or(ProdutoSpecs.codigoGrupoProdutoEquals(filtro))
                		.and(ProdutoSpecs.situacaoEquals(situacao)),
                pageable
        );
	}
	
	public Page<Produto> findAll(Pageable pageable, ProdutoFilter filtro, Situacao situacao) {
		return produtoRep.findAll(
				ProdutoSpecs.codigoOuDescricaoLike(filtro.getCodigoNome())
                		.and(ProdutoSpecs.codigoGrupoProdutoEquals(filtro.getIdGrupoProduto()))
                		.and(ProdutoSpecs.situacaoEquals(situacao)),
                pageable
        );
	}
	
}

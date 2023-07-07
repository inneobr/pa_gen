package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.repository.produto.TipoProdutoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.produto.TipoProdutoRep;
import br.coop.integrada.api.pa.domain.spec.TipoProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class TipoProdutoRepImpl implements TipoProdutoQueriesRep {
	
	@Autowired
    @Lazy
    private TipoProdutoRep tipoProdutoRep; 
	
	public Page<TipoProduto> findAll(Pageable pageable, String nome, Situacao situacao) {
        return tipoProdutoRep.findAll(
                TipoProdutoSpecs.doNome(nome)
                        	    .and(TipoProdutoSpecs.daSituacao(situacao)),
                        	    pageable
        );
    }

}

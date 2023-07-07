package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.repository.ProdutoReferenciaQueriesRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoReferenciaRep;
import br.coop.integrada.api.pa.domain.spec.ProdutoReferenciaSpecs;



@Repository
public class ProdutoReferenciaRepImpl implements ProdutoReferenciaQueriesRep {

    public Page<ProdutoReferencia> findAll(Pageable pageable, String filter) {
        return produtoReferenciaRep.findAll( ProdutoReferenciaSpecs.doCodRef(filter), pageable);
    }

    @Autowired
    @Lazy
    private ProdutoReferenciaRep produtoReferenciaRep;
}

package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorQueriesRep;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorRep;
import br.coop.integrada.api.pa.domain.spec.ProdutorSpecs;

@Repository
public class ProdutorRepImpl implements ProdutorQueriesRep{
	
	@Autowired
    @Lazy
    private ProdutorRep produtorRep;
	
	public Page<Produtor> findAll(Pageable pageable, String nome) {
        return produtorRep.findAll(
                ProdutorSpecs.doCodigoOuNomeOuCpfCnpj(nome), pageable
        );
    }
}

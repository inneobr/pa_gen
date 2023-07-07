package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.repository.recEntrega.SituacaoReQueriesRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.SituacaoReRep;
import br.coop.integrada.api.pa.domain.spec.SituacaoReSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class SituacaoReRepImpl implements SituacaoReQueriesRep{
	
	@Autowired
    @Lazy
    private SituacaoReRep situacaoReRep;
	
	
	public Page<SituacaoRe> findAll(Pageable pageable, String filtro, Situacao situacao) {
		return situacaoReRep.findAll(
                SituacaoReSpecs.codigoOuDescricaoLike(filtro)
                        .and(SituacaoReSpecs.doSituacao(situacao)),
                pageable
        );
	}

}

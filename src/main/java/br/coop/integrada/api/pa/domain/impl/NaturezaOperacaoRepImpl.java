package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoRep;
import br.coop.integrada.api.pa.domain.spec.NaturezaOperacaoSpecs;
import br.coop.integrada.api.pa.domain.spec.TipoGmoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class NaturezaOperacaoRepImpl implements NaturezaOperacaoQueriesRep {
	
	public Page<NaturezaOperacao> findAll(Pageable pageable, String filter, Situacao situacao) {
               
        return naturezaOperacaoRep.findAll(
                NaturezaOperacaoSpecs.doDescricao(filter)
                		.or(NaturezaOperacaoSpecs.doCodGrupo(filter))
                		.and(NaturezaOperacaoSpecs.doSituacao(situacao)),
                pageable
        );
    }
	
	
	

    @Autowired
    @Lazy
    private NaturezaOperacaoRep naturezaOperacaoRep;

}

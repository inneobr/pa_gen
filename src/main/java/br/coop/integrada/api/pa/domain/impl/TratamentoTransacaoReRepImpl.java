package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;
import br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe.TransacaoReQueriesRep;
import br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe.TransacaoReRep;
import br.coop.integrada.api.pa.domain.spec.TransacaoReSpecs;

@Repository
public class TratamentoTransacaoReRepImpl implements TransacaoReQueriesRep {
	
	@Autowired
    @Lazy
    private TransacaoReRep trasacaoReRep;

	
	public Page<TransacaoRe> findAll(Pageable pageable, String filter) {
		return trasacaoReRep.findAll(
				TransacaoReSpecs.doEstabelecimento(filter),
                pageable
        );
	}
}

package br.coop.integrada.api.pa.domain.repository.naturezaOperacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public interface NaturezaOperacaoQueriesRep {
	Page<NaturezaOperacao> findAll(Pageable pageable, String filter, Situacao situacao);
}

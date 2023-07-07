package br.coop.integrada.api.pa.domain.repository.recEntrega;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface SituacaoReQueriesRep {
	Page<SituacaoRe> findAll(Pageable pageable, String filtro, Situacao situacao);
}

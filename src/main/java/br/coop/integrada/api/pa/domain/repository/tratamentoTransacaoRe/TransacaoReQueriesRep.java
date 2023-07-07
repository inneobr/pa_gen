package br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;

public interface TransacaoReQueriesRep {
	public Page<TransacaoRe> findAll(Pageable pageable, String filter);
}

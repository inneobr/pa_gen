package br.coop.integrada.api.pa.domain.repository.cadastro;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface TipoClassificacaoRepQueries {
	Page<TipoClassificacao> findAll(Pageable pageable, String filter, Situacao situacao);
	
}

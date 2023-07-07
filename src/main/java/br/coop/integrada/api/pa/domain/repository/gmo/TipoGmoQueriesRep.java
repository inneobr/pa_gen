package br.coop.integrada.api.pa.domain.repository.gmo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface TipoGmoQueriesRep {
	Page<TipoGmo> findAll(Pageable pageable, TipoGmoFilter filter, Situacao situacao);
}

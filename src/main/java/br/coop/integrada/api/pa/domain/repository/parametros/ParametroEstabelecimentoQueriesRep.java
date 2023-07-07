package br.coop.integrada.api.pa.domain.repository.parametros;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;


public interface ParametroEstabelecimentoQueriesRep {
	Page<ParametroEstabelecimento> findAll(Pageable pageable, String filtro, Situacao situacao );
}

package br.coop.integrada.api.pa.domain.repository;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeFederacaoQueriesRep {

    Page<UnidadeFederacao> findAll(Pageable pageable, String filter, Situacao situacao);
}

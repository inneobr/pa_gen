package br.coop.integrada.api.pa.domain.repository.semente;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SementeClasseQueriesRep {

    Page<SementeClasse> findAll(Pageable pageable, String descricao, Situacao situacao);
}

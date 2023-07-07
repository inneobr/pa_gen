package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.repository.semente.SementeClasseQueriesRep;
import br.coop.integrada.api.pa.domain.repository.semente.SementeClasseRep;
import br.coop.integrada.api.pa.domain.spec.SementeClasseSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class SementeClasseRepImpl implements SementeClasseQueriesRep {

    @Autowired
    @Lazy
    private SementeClasseRep sementeClasseRep;

    public Page<SementeClasse> findAll(Pageable pageable, String filtro, Situacao situacao) {
        return sementeClasseRep.findAll(
                SementeClasseSpecs.codigoOuDescricaoLike(filtro)
                .and(SementeClasseSpecs.doSituacao(situacao)),
                pageable
        );
    }
}

package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.repository.UnidadeFederacaoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.UnidadeFederacaoRep;
import br.coop.integrada.api.pa.domain.spec.UnidadeFederacaoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UnidadeFederacaoRepImpl implements UnidadeFederacaoQueriesRep {

    public Page<UnidadeFederacao> findAll(Pageable pageable, String filter, Situacao situacao) {
        return unidadeFederacaoRep.findAll(
                UnidadeFederacaoSpecs.doEstado(filter)
                        .or(UnidadeFederacaoSpecs.doEstadoNome(filter))
                        .or(UnidadeFederacaoSpecs.doCodigoIbge(filter))
                        .and(UnidadeFederacaoSpecs.doSituacao(situacao)),
                pageable
        );
    }

    @Autowired
    @Lazy
    private UnidadeFederacaoRep unidadeFederacaoRep;
}

package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaFilter;
import br.coop.integrada.api.pa.domain.repository.parametros.SafraCompostaQueriesRep;
import br.coop.integrada.api.pa.domain.repository.parametros.SafraCompostaRep;
import br.coop.integrada.api.pa.domain.spec.SafraCompostaSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class SafraCompostaRepImpl implements SafraCompostaQueriesRep {

    @Autowired
    @Lazy
    private SafraCompostaRep safraCompostaRep;

    public Page<SafraComposta> findAll(Pageable pageable, SafraCompostaFilter filter, Situacao situacao) {
        return safraCompostaRep.findAll(
                SafraCompostaSpecs.doNomeSafra(filter.getNomeSafra())
                        .and(SafraCompostaSpecs.doIdEstabelecimento(filter.getIdEstabelecimento()))
                        .and(SafraCompostaSpecs.doSituacao(situacao)),
                pageable
        );
    }
}

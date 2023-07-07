package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaFilter;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaQueriesRep;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaRep;
import br.coop.integrada.api.pa.domain.spec.TaxaSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TaxaRepImpl implements TaxaQueriesRep {

    @Autowired
    @Lazy
    private TaxaRep taxaRep;

    public Page<Taxa> findAll(Pageable pageable, TaxaFilter filter, Situacao situacao) {
        return taxaRep.findAll(
                TaxaSpecs.doSafra(filter.getSafra())
                		.and(TaxaSpecs.doDescricao(filter.getDescricao()))
                        .and(TaxaSpecs.doIdGrupoProduto(filter.getIdGrupoProduto()))
                        .and(TaxaSpecs.doIdEstabelecimento(filter.getIdEstabelecimento()))
                        .and(TaxaSpecs.doSituacao(situacao)),
                pageable
        );
    }
}

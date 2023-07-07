package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoFilter;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoRep;
import br.coop.integrada.api.pa.domain.spec.ClassificacaoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ClassificacaoRepImpl implements ClassificacaoQueriesRep {

    @Autowired
    @Lazy
    private ClassificacaoRep classificacaoRep;

    public Page<Classificacao> findAll(Pageable pageable, ClassificacaoFilter filter, Situacao situacao) {
        return classificacaoRep.findAll(
                ClassificacaoSpecs.doDescricao(filter.getDescricao())
                        .and(ClassificacaoSpecs.doSafra(filter.getSafra()))
                        .and(ClassificacaoSpecs.doIdTipoClassificacao(filter.getIdTipoClassificacao()))
                        .and(ClassificacaoSpecs.doIdGrupoProduto(filter.getIdGrupoProduto()))
                        .and(ClassificacaoSpecs.doSituacao(situacao)),
                pageable
        );
    }
}

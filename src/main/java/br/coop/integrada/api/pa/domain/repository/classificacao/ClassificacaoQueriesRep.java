package br.coop.integrada.api.pa.domain.repository.classificacao;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassificacaoQueriesRep {

    Page<Classificacao> findAll(Pageable pageable, ClassificacaoFilter filter, Situacao situacao);

}

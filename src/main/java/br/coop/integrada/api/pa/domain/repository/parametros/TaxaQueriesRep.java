package br.coop.integrada.api.pa.domain.repository.parametros;

import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxaQueriesRep {

    Page<Taxa> findAll(Pageable pageable, TaxaFilter filter, Situacao situacao);
}

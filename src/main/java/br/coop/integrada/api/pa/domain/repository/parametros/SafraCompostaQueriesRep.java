package br.coop.integrada.api.pa.domain.repository.parametros;

import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SafraCompostaQueriesRep {

    Page<SafraComposta> findAll(Pageable pageable, SafraCompostaFilter filter, Situacao situacao);
}

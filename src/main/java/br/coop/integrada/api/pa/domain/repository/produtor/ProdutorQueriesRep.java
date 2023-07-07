package br.coop.integrada.api.pa.domain.repository.produtor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.Produtor;

public interface ProdutorQueriesRep {

    Page<Produtor> findAll(Pageable pageable, String nome);
}

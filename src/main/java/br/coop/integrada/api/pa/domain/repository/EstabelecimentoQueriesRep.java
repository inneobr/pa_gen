package br.coop.integrada.api.pa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface EstabelecimentoQueriesRep {
	Page<Estabelecimento> findAll(Pageable pageable, EstabelecimentoFilter filter, Situacao situacao);
	Page<Estabelecimento> findAll(String codigoOuNomeFantasia, Pageable pageable, Situacao situacao);
}

package br.coop.integrada.api.pa.domain.impl;

import static br.coop.integrada.api.pa.domain.spec.EstabelecimentoSpecs.doSituacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoFilter;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.spec.EstabelecimentoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class EstabelecimentoRepImpl implements EstabelecimentoQueriesRep {
	 @Autowired
	 @Lazy
	 private EstabelecimentoRep estabelecimentoRep;
	 
	 public Page<Estabelecimento> findAll(Pageable pageable, EstabelecimentoFilter filter, Situacao situacao) {
		 
		 return estabelecimentoRep.findAll(
	                EstabelecimentoSpecs.doNomeFantasia(filter.getNomeFantasia())
	                .or(EstabelecimentoSpecs.doCodigo(filter.getCodigo()))
	                .or(EstabelecimentoSpecs.doCodigoRegional(filter.getCodigoRegional()))
	                .or(EstabelecimentoSpecs.doEmail(filter.getEmail()))
	                .or(EstabelecimentoSpecs.doTelefone(filter.getTelefone()))
                	.or(EstabelecimentoSpecs.doCnpj(filter.getCnpj()))
                	.or(EstabelecimentoSpecs.doCidade(filter.getCidade()))
                	.or(EstabelecimentoSpecs.doEstado(filter.getEstado()))
	                .and(EstabelecimentoSpecs.doSituacao(situacao)) ,
	                
	                pageable );
	 }
	 
	 public Page<Estabelecimento> findAll(String codigoOuNomeFantasia, Pageable pageable, Situacao situacao) {
		 return estabelecimentoRep.findAll(
				 EstabelecimentoSpecs.codigoOuNomeFantasiaLike(codigoOuNomeFantasia)
				 .and(doSituacao(situacao)),
				 pageable);
	 }
}

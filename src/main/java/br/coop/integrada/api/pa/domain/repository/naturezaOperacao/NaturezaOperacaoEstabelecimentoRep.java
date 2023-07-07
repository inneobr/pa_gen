package br.coop.integrada.api.pa.domain.repository.naturezaOperacao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoEstabelecimento;

public interface NaturezaOperacaoEstabelecimentoRep extends JpaRepository<NaturezaOperacaoEstabelecimento, Long>{
	void deleteByNaturezaOperacao(NaturezaOperacao naturezaOperacao);
	Page<NaturezaOperacaoEstabelecimento> findByNaturezaOperacaoIdOrderByEstabelecimentoCodigo(Long id, Pageable pageable);
	Page<NaturezaOperacaoEstabelecimento> findByNaturezaOperacaoOrderByIdDesc(NaturezaOperacao naturezaOperacao, Pageable pageable);
	List<NaturezaOperacaoEstabelecimento> findByNaturezaOperacao(NaturezaOperacao naturezaOperacao);
	NaturezaOperacaoEstabelecimento findByNaturezaOperacaoAndEstabelecimento(NaturezaOperacao naturezaOperacao, Estabelecimento estabelecimento);
	NaturezaOperacaoEstabelecimento findByIdUnico(String idUnico);
	
} 

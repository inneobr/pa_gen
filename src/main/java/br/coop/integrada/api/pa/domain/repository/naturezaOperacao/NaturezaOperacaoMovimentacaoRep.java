package br.coop.integrada.api.pa.domain.repository.naturezaOperacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoMovimentacao;

public interface NaturezaOperacaoMovimentacaoRep extends JpaRepository<NaturezaOperacaoMovimentacao, Long>{	
	NaturezaOperacaoMovimentacao findByIdUnico(String idUnico);
	Page<NaturezaOperacaoMovimentacao> findByCodGrupoOrderByDataCadastroDesc(Integer codGrupo, Pageable pageable);
}
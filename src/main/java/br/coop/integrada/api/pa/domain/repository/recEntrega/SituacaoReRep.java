package br.coop.integrada.api.pa.domain.repository.recEntrega;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.repository.semente.SementeClasseQueriesRep;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public interface SituacaoReRep extends JpaRepository<SituacaoRe, Long>, SituacaoReQueriesRep, JpaSpecificationExecutor<SituacaoRe>{

	Page<SituacaoRe> findByDataInativacaoNull(Pageable pageable);

	List<SituacaoRe> findByDescricaoContainingIgnoreCaseAndDataInativacaoNull(String descricao, Pageable limit);

	SituacaoRe findByCodigo(Long codigo);

	SituacaoRe findByDescricaoIgnoreCase(String descricao);
		
}

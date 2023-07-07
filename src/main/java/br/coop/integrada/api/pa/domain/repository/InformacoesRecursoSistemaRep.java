package br.coop.integrada.api.pa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.enums.PaginaAreaEnum;
import br.coop.integrada.api.pa.domain.model.InformacoesRecursoSistema;

public interface InformacoesRecursoSistemaRep extends JpaRepository<InformacoesRecursoSistema, Long> {
	Page<InformacoesRecursoSistema> findByDataInativacaoNull(Pageable pageable);
	InformacoesRecursoSistema findByPaginaAreaAndDataInativacaoNull(PaginaAreaEnum paginaArea);
	InformacoesRecursoSistema findByPaginaArea(PaginaAreaEnum paginaArea);
	
}

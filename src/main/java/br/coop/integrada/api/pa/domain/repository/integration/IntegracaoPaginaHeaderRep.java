package br.coop.integrada.api.pa.domain.repository.integration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;

public interface IntegracaoPaginaHeaderRep extends JpaRepository<IntegracaoPaginaHeader, Long>{
	List<IntegracaoPaginaHeader> findByIntegracaoPagina(IntegracaoPagina pagina);
	void deleteByIntegracaoPagina(IntegracaoPagina pagina);
}	

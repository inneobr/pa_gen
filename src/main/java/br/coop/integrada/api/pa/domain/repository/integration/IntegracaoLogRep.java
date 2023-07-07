package br.coop.integrada.api.pa.domain.repository.integration;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;

public interface IntegracaoLogRep extends JpaRepository<IntegracaoLog, Long>{
	
	

}

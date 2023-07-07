package br.coop.integrada.api.pa.domain.service.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;
import br.coop.integrada.api.pa.domain.repository.integration.IntegracaoLogRep;

@Service
public class IntegracaoLogService {
	
	@Autowired
	private IntegracaoLogRep integracaoLogRep;
	
	public void salvar(IntegracaoLog integracaoLog) {
		integracaoLogRep.save(integracaoLog);
	}

}

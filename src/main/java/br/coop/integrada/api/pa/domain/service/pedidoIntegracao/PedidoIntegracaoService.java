package br.coop.integrada.api.pa.domain.service.pedidoIntegracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.pedidoIntegracao.Integracao;
import br.coop.integrada.api.pa.domain.repository.pedidoIntegracao.PedidoIntegracaoRep;

@Service
public class PedidoIntegracaoService {
	
	@Autowired
	PedidoIntegracaoRep pedidoIntegracaoRep;
	
	public Integracao gerarPedidoIntegracao(Integracao integracao) {
		
		integracao.setStatus(StatusIntegracao.INTEGRAR);
		
		return pedidoIntegracaoRep.save(integracao);
	}
	
}

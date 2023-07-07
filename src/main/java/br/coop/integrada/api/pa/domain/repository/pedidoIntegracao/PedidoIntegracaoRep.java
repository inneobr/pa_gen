package br.coop.integrada.api.pa.domain.repository.pedidoIntegracao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.pedidoIntegracao.Integracao;

@Repository
public interface PedidoIntegracaoRep  extends JpaRepository<Integracao, Long> {
	
	
	
	
}

package br.coop.integrada.api.pa.domain.repository.integration;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;

public interface IntegrationAuthRep extends JpaRepository<IntegrationAuth, Long>{
	Optional<IntegrationAuth> findById(Long id);
	IntegrationAuth findByLogin(String login);
	IntegrationAuth findByDescricao(String descricao);
	List<IntegrationAuth> findByLoginContainingIgnoreCase(String login);
	List<IntegrationAuth> findByLoginContainingIgnoreCase(String login, Pageable pageable);
	List<IntegrationAuth> findByDescricaoContainingIgnoreCase(String descricao);
	List<IntegrationAuth> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}

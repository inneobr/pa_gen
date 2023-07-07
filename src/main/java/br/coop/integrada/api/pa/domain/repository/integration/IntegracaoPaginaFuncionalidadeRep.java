package br.coop.integrada.api.pa.domain.repository.integration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.enums.integration.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;

public interface IntegracaoPaginaFuncionalidadeRep extends JpaRepository<IntegracaoPaginaFuncionalidade, Long>{
	List<IntegracaoPaginaFuncionalidade> findByIntegracaoPaginaTipoEnum(TipoIntegracaoEnum tipo);
	List<IntegracaoPaginaFuncionalidade> findByIntegracaoPagina(IntegracaoPagina pagina);
	void deleteByIntegracaoPagina(IntegracaoPagina pagina);
}

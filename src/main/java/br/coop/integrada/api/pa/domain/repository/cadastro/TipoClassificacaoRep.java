package br.coop.integrada.api.pa.domain.repository.cadastro;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;

public interface TipoClassificacaoRep extends PagingAndSortingRepository<TipoClassificacao, Long>, TipoClassificacaoRepQueries, JpaSpecificationExecutor<TipoClassificacao>{
	List<TipoClassificacao> findByDataInativacaoNull();
	Optional<TipoClassificacao> findByTipoClassificacao(TipoClassificacaoEnum tipoClassificacao);
	public List<TipoClassificacao> findByStatusIntegracao(StatusIntegracao statusIntegracao);
	
	public TipoClassificacao findByTipoClassificacaoAndDataInativacaoIsNull(TipoClassificacaoEnum tipoClassificacao);
	public List<TipoClassificacao> findByStatusIntegracaoOrStatusIntegracaoIsNull(StatusIntegracao statusIntegracao); 
	
}

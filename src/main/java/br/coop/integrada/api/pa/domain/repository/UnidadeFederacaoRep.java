package br.coop.integrada.api.pa.domain.repository;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeFederacaoRep extends JpaRepository<UnidadeFederacao, Long>, UnidadeFederacaoQueriesRep, JpaSpecificationExecutor<UnidadeFederacao> {

    UnidadeFederacao findByCodigoIbgeIgnoreCaseAndDataInativacaoIsNull(String codigoIgbe);

    UnidadeFederacao findByEstadoIgnoreCaseAndDataInativacaoIsNull(String estado);

    List<UnidadeFederacao> findByCodigoIbgeIgnoreCaseAndDataInativacaoIsNullOrEstadoIgnoreCaseAndDataInativacaoIsNullOrEstadoNomeIgnoreCaseAndDataInativacaoIsNull(String codigoIbge, String estado, String estadoNome);
    List<UnidadeFederacao> findByCodigoIbgeIgnoreCaseOrEstadoIgnoreCaseOrEstadoNomeIgnoreCase(String codigoIbge, String estado, String estadoNome);
    
    Optional<UnidadeFederacao> findByIdAndDataInativacaoIsNull(Long id);
}

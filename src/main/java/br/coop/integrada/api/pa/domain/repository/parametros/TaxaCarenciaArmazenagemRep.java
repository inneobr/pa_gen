package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;

@Repository
public interface TaxaCarenciaArmazenagemRep extends JpaRepository<TaxaCarenciaArmazenagem, Long> {

	List<TaxaCarenciaArmazenagem> findByTaxaId(Long taxaId);
	List<TaxaCarenciaArmazenagem> findByStatusIntegracao(StatusIntegracao status);
	List<TaxaCarenciaArmazenagem> findByTaxaIdAndDataInativacaoIsNull(Long taxaId);
	List<TaxaCarenciaArmazenagem> findByTaxaIdAndStatusIntegracao(Long idTaxa, StatusIntegracao status);
	Page<TaxaCarenciaArmazenagem> findByTaxaIdAndDataInativacaoIsNull(Long idTaxa, Pageable pageable);
}

package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;

@Repository
public interface ItemAvariadoEstabelecimentoRep extends JpaRepository<ItemAvariadoEstabelecimento, Long> {
	List<ItemAvariadoEstabelecimento> findByStatusIntegracao(StatusIntegracao statusIntegracao);
	List<ItemAvariadoEstabelecimento> findByItemAvariadoIdAndDataInativacaoIsNull(Long idItemAvariado);
	ItemAvariadoEstabelecimento findByItemAvariadoIdAndEstabelecimentoCodigo(Long idItemAvariado, String codigoEstabelecimento);
	List<ItemAvariadoEstabelecimento> findByItemAvariadoAndStatusIntegracaoAndDataInativacaoIsNull(ItemAvariado itemAvariado, StatusIntegracao integrar);
	
	List<ItemAvariadoEstabelecimento> findByItemAvariadoId(Long id);
	Page<ItemAvariadoEstabelecimento> findByItemAvariadoIdOrderByEstabelecimentoAsc(Long id, Pageable pageable);
	
	@Transactional
    @Modifying
    @Query(value = "UPDATE ItemAvariadoEstabelecimento SET statusIntegracao = :status WHERE itemAvariado.id = :idItemAvariado")
	public void updateStatus(StatusIntegracao status, Long idItemAvariado);
	
	@Transactional
    @Modifying
    @Query(value = "DELETE ItemAvariadoEstabelecimento WHERE itemAvariado.id = :idItemAvariado")
	public void deleteEstabelecimentos(Long idItemAvariado);
}

package br.coop.integrada.api.pa.domain.repository.parametros;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;

@Repository
public interface ItemAvariadoDetalheRep extends JpaRepository<ItemAvariadoDetalhe, Long>, JpaSpecificationExecutor<ItemAvariadoDetalhe> {

	List<ItemAvariadoDetalhe> findByStatusIntegracao(StatusIntegracao statusIntegracao);
	List<ItemAvariadoDetalhe> findByItemAvariado(ItemAvariado itemAvariado);
	List<ItemAvariadoDetalhe> findByItemAvariadoId(Long idItemAvariado);
	ItemAvariadoDetalhe findByItemAvariadoIdAndProdutoCodItemAndPercentualInicialAndPercentualFinal(Long itemAvariadoId, String codigoProduto, BigDecimal percentualInicial, BigDecimal percentualFinal);
	List<ItemAvariadoDetalhe> findByItemAvariadoAndStatusIntegracaoAndDataInativacaoIsNull(ItemAvariado itemAvariado, StatusIntegracao integrar);
	List<ItemAvariadoDetalhe> findByItemAvariadoIdAndDataInativacaoIsNull(Long idItemAvariado);
	
	@Transactional
    @Modifying
    @Query(value = "UPDATE ItemAvariadoDetalhe SET statusIntegracao = :status WHERE itemAvariado.id = :idItemAvariado")
	public void updateStatus(StatusIntegracao status, Long idItemAvariado);
	
	@Transactional
    @Modifying
    @Query(value = "DELETE ItemAvariadoDetalhe WHERE itemAvariado.id = :idItemAvariado")
	public void deleteDetalhes(Long idItemAvariado);
}

package br.coop.integrada.api.pa.domain.repository.recEntrega;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoDto;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;



@Repository
public interface RecEntregaItemRep extends JpaRepository<RecEntregaItem, Long> {
	
	//Optional<RecEntregaItem> findByCodEstabelAndNrReAndItCodigoAndCodRefer(Long codEstabel, Integer nrRe, String itCodigo, String codRefer);
	List<RecEntregaItem> findByCodEstabelAndCodProdutoAndLoteAndLoteIsNotNull(String codEstab, String codItem, String lote);
	Optional<RecEntregaItem> findByRecEntregaAndNrReAndItCodigoAndCodRefer(RecEntrega recEntrega, Long nrRe, String itCodigo, String codRefer);
	
	/*@Query(value = """
			SELECT rei.* 
			FROM REC_ENTREGA re INNER JOIN REC_ENTREGA_ITEM rei 
			ON re.ID = rei.ID_REC_ENTREGA 
			WHERE re.COD_ESTABEL = :codEstabelecimento
			AND re.SAFRA = :safra
			AND re.FM_CODIGO = :grupoProduto
			AND NR_ORD_CAMPO = :numeroOrdemCampo
			AND NR_LAUDO = :numeroLaudo
		""", nativeQuery = true)
	List<RecEntregaItem> verificaQtRecebida(VerificaTotleranciaRecebimentoDto input);*/
	
	
}

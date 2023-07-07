package br.coop.integrada.api.pa.domain.repository.recEntrega;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;


public interface RecEntregaRep extends PagingAndSortingRepository<RecEntrega, Long>, RecEntregaRepQueries, JpaSpecificationExecutor<RecEntrega>{
	List<RecEntrega> findByCodEstabelAndNrReAndReDevolvidoFalse(String codEstabel, Long nrRe);
	Optional<RecEntrega> findByCodEstabelAndNrRe(String estabelecimento, Long nrRe);
	Optional<RecEntrega> findByCodEstabelAndId(String estabelecimento, Long id);
	RecEntrega findByCodEstabel(String codEstabelecimento);
	
	@Query(value = """
    		SELECT re.* FROM REC_ENTREGA re
    		INNER JOIN REC_ENTREGA_ITEM rei
			 	ON re.ID = rei.ID_REC_ENTREGA
			 	
			INNER JOIN ESTABELECIMENTO e 
				ON e.CODIGO = re.COD_ESTABEL 
				AND e.CODIGO = :codEstabelecimento
			INNER JOIN GRUPO_PRODUTO gp 
				ON re.FM_CODIGO = gp.FM_CODIGO 
				AND gp.FM_CODIGO  = :grupoProduto
			WHERE re.SAFRA = :safra
			AND re.NR_ORD_CAMPO = :numeroOrdemCampo
			AND re.NR_LAUDO  = :numeroLaudo
            """,
            nativeQuery = true)
	RecEntrega buscarByCodEstabelAndSafraAndFmCodigoAndNrOrdCampoAndNrLaudo(
			String codEstabelecimento, 
			Integer safra,
			String grupoProduto, 
			Integer numeroOrdemCampo, 
			Long numeroLaudo);
	
	@Query(value = """
    		SELECT sum(rei.RENDA_LIQUIDA_ATU)  FROM REC_ENTREGA re
			INNER JOIN REC_ENTREGA_ITEM rei
			 	ON re.ID = rei.ID_REC_ENTREGA
			INNER JOIN ESTABELECIMENTO e 
				ON e.CODIGO = re.COD_ESTABEL 
				AND e.CODIGO = :codEstabelecimento
			INNER JOIN GRUPO_PRODUTO gp 
				ON re.FM_CODIGO = gp.FM_CODIGO 
				AND gp.FM_CODIGO  = :grupoProduto
			WHERE re.SAFRA = :safra
			AND re.NR_ORD_CAMPO = :numeroOrdemCampo
			AND re.NR_LAUDO  = :numeroLaudo
            """,
            nativeQuery = true)
	BigDecimal quantidadeTotalRendaLiquidaAtu(
			String codEstabelecimento, 
			Integer safra,
			String grupoProduto, 
			Integer numeroOrdemCampo, 
			Long numeroLaudo);

	
	@Query(value = """
    		SELECT sum(rei.RENDA_LIQUIDA)  FROM REC_ENTREGA re
			INNER JOIN REC_ENTREGA_ITEM rei
			 	ON re.ID = rei.ID_REC_ENTREGA
			WHERE
				re.ID = :id
				AND re.COD_ESTABEL = :codEstabelecimento
            """,
            nativeQuery = true)
	BigDecimal quantidadeTotalRendaLiquida(
			String codEstabelecimento, 
			Long id);
}

package br.coop.integrada.api.pa.domain.repository.dashBoard;

import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;



public interface DashBoardRep extends PagingAndSortingRepository<RecEntrega, Long>, JpaSpecificationExecutor<RecEntrega>{
		
	
	@Query(value = """
	SELECT 
	    SUM(ROMANEIOS) AS ROMANEIOS,
	    ROUND(SUM(PESO_LIQUIDO), 2) AS PESO_LIQUIDO,
	    ROUND(SUM(TON_PESO_LIQUIDO), 2) AS TON_PESO_LIQUIDO,
	    ROUND(SUM(SC_PESO_LIQUIDO), 2) AS SC_PESO_LIQUIDO,
	    ROUND(SUM(TON_RENDA_LIQUIDA), 2) AS TON_RENDA_LIQUIDA,
	    ROUND(SUM(SC_RENDA_LIQUIDA), 2) AS SC_RENDA_LIQUIDA,
	    SUM(ENTRADA_SEM_TICKET) AS ENTRADA_SEM_TICKET,
	    SUM(ENTRADA_COM_TICKET) AS ENTRADA_COM_TICKET
	FROM (
	    SELECT 
	        COUNT(RE.ID) AS ROMANEIOS,
	        SUM(RE.PESO_LIQUIDO) AS PESO_LIQUIDO,
	        SUM(RE.PESO_LIQUIDO)/1000 AS TON_PESO_LIQUIDO,
	        ROUND(SUM(RE.PESO_LIQUIDO)/GP.KG_SC, 2) AS SC_PESO_LIQUIDO,
	        SUM(REI.RENDA_LIQUIDA)/1000 AS TON_RENDA_LIQUIDA,
	        ROUND(SUM(REI.RENDA_LIQUIDA)/GP.KG_SC, 2) AS SC_RENDA_LIQUIDA,
	        CASE WHEN PES.ID IS NULL THEN COUNT(RE.ID) ELSE 0 END AS ENTRADA_SEM_TICKET,
	        CASE WHEN PES.ID IS NOT NULL THEN COUNT(RE.ID) ELSE 0 END AS ENTRADA_COM_TICKET 
	    FROM REC_ENTREGA RE
	        INNER JOIN ESTABELECIMENTO E ON RE.COD_ESTABEL = E.CODIGO
	        INNER JOIN PRODUTOR P ON RE.COD_EMITENTE = P.COD_PRODUTOR
	        INNER JOIN ESTABELECIMENTO ER ON E.CODIGO_REGIONAL = ER.CODIGO
	        INNER JOIN IMOVEL I ON RE.MATRICULA = I.MATRICULA
	        INNER JOIN GRUPO_PRODUTO GP ON RE.FM_CODIGO = GP.FM_CODIGO
	        INNER JOIN REC_ENTREGA_ITEM REI ON  RE.ID = REI.ID_REC_ENTREGA
	        INNER JOIN PRODUTO PDT ON REI.COD_PRODUTO = PDT.COD_ITEM
	        LEFT JOIN PESAGEM PES ON PES.NRO_DOC_PESAGEM = RE.NR_DOC_PES AND PES.COD_ESTABELECIMENTO = RE.COD_ESTABEL
	    WHERE
	        E.CODIGO_REGIONAL = :regional
	        AND RE.COD_ESTABEL IN ( :codEstabel )
	        AND RE.SAFRA IN ( :safra )  
	        AND GP.TIPO_PRODUTO = :tipoProduto
	    GROUP BY GP.KG_SC, PES.ID
	    )
            """,
    nativeQuery = true)
	Map<String, BigDecimal> consultaPrincipal(String regional, List<String> codEstabel, List<String> safra, Long tipoProduto);
	
	
	
}

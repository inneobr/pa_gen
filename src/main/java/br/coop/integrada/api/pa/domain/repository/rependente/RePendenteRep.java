package br.coop.integrada.api.pa.domain.repository.rependente;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;

public interface RePendenteRep extends JpaRepository<RePendente, Long>{
    Page<RePendente> findByEstabelecimentoCodigo(String codigoEstabelecimento, Pageable pageable);

    @Query(value = """
            SELECT
                   RP.*
               FROM RE_PENDENTE RP
               INNER JOIN ESTABELECIMENTO E
                   ON E.ID = RP.ID_ESTABELECIMENTO
                   AND E.CODIGO = :codigoEstabelecimento
               INNER JOIN GRUPO_PRODUTO GP
                   ON GP.ID = RP.ID_GRUPO_PRODUTO
                   AND GP.FM_CODIGO = :codigoGrupoProduto
               INNER JOIN PRODUTOR P
                   ON P.ID = RP.ID_PRODUTOR
                   AND P.COD_PRODUTOR = :codigoProdutor
               WHERE
                   RP.PLACA = :placa
            """,
            nativeQuery = true)
    RePendente findByCodigoEstabelecimentoAndPlacaAndCodigoGrupoProdutoAndCodigoProdutor(String codigoEstabelecimento, String placa, String codigoGrupoProduto, String codigoProdutor);
    
    Optional<RePendente> findByEstabelecimentoCodigoIgnoreCaseAndPlacaIgnoreCaseAndGrupoProdutoFmCodigoIgnoreCaseAndProdutorCodProdutorIgnoreCaseAndNomeProdIgnoreCase(String codigoEstabelecimento, String placa, String codigoGrupoProduto, String codigoProdutor, String nomeProd);
    
    @Query(value = """
    		SELECT rp.* 
    		FROM RE_PENDENTE rp
			INNER JOIN ESTABELECIMENTO e 
				ON rp.ID_ESTABELECIMENTO  = e.ID
				AND e.CODIGO = :codigoEstabelecimento
			INNER JOIN GRUPO_PRODUTO GP
                ON GP.ID = RP.ID_GRUPO_PRODUTO
                AND GP.FM_CODIGO = :grupoProduto
			WHERE EXTRACT (YEAR FROM rp.DT_ENTRADA) = :safra
				AND rp.NR_ORD_CAMPO = :numeroOrdemCampo
				AND rp.NR_LAUDO = :numeroLaudo
            """,
            nativeQuery = true)
    Optional<RePendente> buscarByCodigoEstabelecimentoAndSafraAndCodigoGrupoProdutoAndNrOrdemCampoAndNrLaudo(
    		String codigoEstabelecimento, 
    		Integer safra, 
    		String grupoProduto, 
    		Integer numeroOrdemCampo, 
    		Long numeroLaudo);
    
    @Query(value = """
    		SELECT sum(rpi.RENDA_LIQUIDA) 
    		FROM RE_PENDENTE rp
    		INNER JOIN RE_PENDENTE_ITEM rpi 
    		 	ON rp.ID = rpi.ID_RE_PENDENTE
			INNER JOIN ESTABELECIMENTO e 
				ON rp.ID_ESTABELECIMENTO  = e.ID
				AND e.CODIGO = :codigoEstabelecimento
			INNER JOIN GRUPO_PRODUTO GP
                ON GP.ID = RP.ID_GRUPO_PRODUTO
                AND GP.FM_CODIGO = :grupoProduto
			WHERE EXTRACT (YEAR FROM rp.DT_ENTRADA) = :safra
				AND rp.NR_ORD_CAMPO = :numeroOrdemCampo
				AND rp.NR_LAUDO = :numeroLaudo
            """,
            nativeQuery = true)
    BigDecimal quantidadeTotalRendaLiquida(
    		String codigoEstabelecimento, 
    		Integer safra, 
    		String grupoProduto, 
    		Integer numeroOrdemCampo, 
    		Long numeroLaudo);

    
}

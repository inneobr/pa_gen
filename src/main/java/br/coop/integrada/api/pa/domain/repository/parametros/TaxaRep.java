package br.coop.integrada.api.pa.domain.repository.parametros;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaxaRep extends JpaRepository<Taxa, Long>, TaxaQueriesRep, JpaSpecificationExecutor<Taxa> {	

    @Query(value = """
                SELECT 'Taxa: '|| t.ID ||', Safra: '|| t.SAFRA ||', FM: '|| gp.fm_codigo||', Estabelecimento: '|| e.CODIGO
			    FROM TAXA t
			    INNER JOIN TAXA_GRUPO_PRODUTO tgp
			        ON tgp.ID_TAXA = t.ID
			        AND tgp.ID_GRUPO_PRODUTO IN (:grupoProdutoIds)
			        AND tgp.DATA_INATIVACAO IS NULL
			    INNER JOIN GRUPO_PRODUTO gp
			        ON gp.ID = tgp.ID_GRUPO_PRODUTO
			    INNER JOIN TAXA_ESTABELECIMENTO te 
			        ON te.ID_TAXA  = t.ID
			        AND te.DATA_INATIVACAO IS NULL
			    INNER JOIN ESTABELECIMENTO e
			        ON e.ID = te.ID_ESTABELECIMENTO
			        AND e.CODIGO IN (:estabelecimentoCodigos)
			    WHERE
			        t.ID <> :id
			        AND t.SAFRA = :safra
			        AND t.DATA_INATIVACAO IS NULL
            """, nativeQuery=true)
    public List<String> buscarPorSafraGrupoProdutoEstabelecimento(Long id, Integer safra, List<Long> grupoProdutoIds, List<String> estabelecimentoCodigos);

	public List<Taxa> findByStatusIntegracao(StatusIntegracao status);
	
	
	@Query(value = """
            SELECT t.*
			FROM TAXA t 
			JOIN TAXA_GRUPO_PRODUTO tgp
				ON t.ID = tgp.ID_TAXA
				AND tgp.DATA_INATIVACAO IS NULL
			JOIN TAXA_ESTABELECIMENTO te 
				ON t.ID = te.ID_TAXA  
				AND te.DATA_INATIVACAO IS NULL
			WHERE
				SAFRA = :safra
				AND te.ID_ESTABELECIMENTO = :idEstabelecimento
				AND tgp.ID_GRUPO_PRODUTO = :idGrupoProduto 
				AND t.DATA_INATIVACAO IS NULL
        """,
        nativeQuery=true)
	public Optional<Taxa> TaxaProducaoEstabelecimento(int safra, Long idEstabelecimento, Long idGrupoProduto);
	
	
	
	
}

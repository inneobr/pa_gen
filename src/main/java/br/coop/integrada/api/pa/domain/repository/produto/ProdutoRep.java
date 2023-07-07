package br.coop.integrada.api.pa.domain.repository.produto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;

public interface ProdutoRep extends JpaRepository<Produto, Long>, ProdutoQueriesRep, JpaSpecificationExecutor<Produto> {
	Page<Produto> findByDataInativacaoNull(Pageable pageable);
	Produto findByCodItemAndDataInativacaoNull(String codItem);
	List<Produto> findByGrupoProduto(GrupoProduto grupoProduto);
	Produto findByCodItem(String codItem);
	Produto findByCodItemIgnoreCase(String codItem);
	List<Produto> findByGrupoProdutoId(Long id);
	List<Produto> findByCodItemContainingIgnoreCaseOrDescItemContainingIgnoreCase(String codigo, String descricao, Pageable pageable);	
	
	@Query(value = """
			SELECT P.* FROM PRODUTO P
			JOIN V_PRODUTO_REFERENCIA VPR ON VPR.PRODUTO_ID = P.ID
			WHERE VPR.REFERENCIA_ID IN (
				SELECT PR.ID  
				FROM PRODUTO_REFERENCIA PR
				WHERE PR.COD_REREFERENCIA = :codReferencia
				AND PR.DATA_INATIVACAO IS NULL)
			AND P.DATA_INATIVACAO IS NULL 
			AND VPR.DATA_INATIVACAO IS NULL""", nativeQuery = true)
		
		List<Produto> getByprodutosReferencia(String codReferencia);
	
	@Query(value = """
		SELECT P.COD_ITEM FROM PRODUTO P
		INNER JOIN GRUPO_PRODUTO GP 
		ON P.ID_GRUPO_PRODUTO = GP.ID
		WHERE GP.FM_CODIGO = :fmCodigo
		AND p.DATA_INATIVACAO IS NULL
		ORDER BY P.COD_ITEM """, nativeQuery = true)
	List<Produto> getByIdGrupoProduto(String fmCodigo);
}

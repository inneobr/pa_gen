package br.coop.integrada.api.pa.domain.repository.produto;


import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.repository.ProdutoReferenciaQueriesRep;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoReferenciaRep extends JpaRepository<ProdutoReferencia, Long>, ProdutoReferenciaQueriesRep, JpaSpecificationExecutor<ProdutoReferencia>{
	ProdutoReferencia findByCodRefIgnoreCase(String codRef);
	ProdutoReferencia findByCodRefAndDataInativacaoNull(String codRef);
	Page<ProdutoReferencia> findByDataInativacaoNull(Pageable pageable);
	List<ProdutoReferencia> findByCodRefContainingIgnoreCaseAndDataInativacaoNullOrderByCodRefAsc(String codRef, Pageable pageable);
	
	@Query(value = "SELECT DISTINCT PR.* FROM PRODUTO_REFERENCIA PR "
			+ "JOIN V_PRODUTO_REFERENCIA VRP "
			+ "ON PR.ID = VRP.REFERENCIA_ID "
			+ "JOIN PRODUTO P "
			+ "ON P.ID = VRP.PRODUTO_ID "
			+ "WHERE VRP.PRODUTO_ID = :id  "
			+ "AND VRP.DATA_INATIVACAO IS NULL "
			+ "AND PR.DATA_INATIVACAO IS NULL "
			+ "AND P.DATA_INATIVACAO IS NULL "
			+ "ORDER BY PR.COD_REREFERENCIA", nativeQuery = true)
	List<ProdutoReferencia> findByProduto(Long id);
}

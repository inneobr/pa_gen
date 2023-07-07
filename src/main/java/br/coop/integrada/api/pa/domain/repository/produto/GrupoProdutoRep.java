package br.coop.integrada.api.pa.domain.repository.produto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface GrupoProdutoRep extends JpaRepository<GrupoProduto, Long>, GrupoProdutoQueriesRep, JpaSpecificationExecutor<GrupoProduto>{
	GrupoProduto findByFmCodigo(String fmCodigo);
	Page<GrupoProduto> findByDataInativacaoNull(Pageable pageable);
	List<GrupoProduto> findByDataInativacaoNullOrderByDescricaoAsc();
	List<GrupoProduto> findByDataInativacaoNullOrderByFmCodigoAsc();
	List<GrupoProduto> findByFmCodigoContainingOrDescricaoContainingIgnoreCase(String codigo, String descricao, Pageable pageable);
	
	
	List<GrupoProduto> findByItSubCoopAndDataInativacaoNull(String codigoProduto);
		
	
}

package br.coop.integrada.api.pa.domain.repository.produto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.produto.RelacionamentosDto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public interface TipoProdutoRep extends JpaRepository<TipoProduto, Long>, TipoProdutoQueriesRep, JpaSpecificationExecutor<TipoProduto> {

	Page<TipoProduto> findByNomeContainingIgnoreCaseAndDataInativacaoNull(String nome, Pageable pageable);
	
	List<TipoProduto> findByDataInativacaoNullAndNomeContainingIgnoreCase(String nome, Pageable pageable);

	List<TipoProduto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
	
	//List<TipoProduto> findByDataInativacaoNull(Pageable pageable);

	TipoProduto findByNomeIgnoreCaseAndDataInativacaoIsNull(String nome);
	
	TipoProduto findByIdUnico(String idUnico);
	
	List<TipoProduto> findByStatusIntegracao(StatusIntegracao status);
	
	Page<TipoProduto> findAll(Pageable pageable, String nome, Situacao situacao);

	Slice<TipoProduto> findByDataInativacaoNull(Pageable limit);

	TipoProduto findByNomeIgnoreCase(String nome);
	
	@Query(value = """
    		SELECT * 
    		FROM TIPO_PRODUTO 
    		WHERE NOME = :nome collate binary_ai
            """,
            nativeQuery = true)
	List<TipoProduto> findByNomeIgnoreCaseAndIgnoreAccents(String nome);
	
	@Query(nativeQuery = true)
	List<RelacionamentosDto> verificarRelacionamentos(Long idTipoProduto);
	
}

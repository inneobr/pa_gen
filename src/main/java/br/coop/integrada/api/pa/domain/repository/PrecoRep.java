package br.coop.integrada.api.pa.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.coop.integrada.api.pa.domain.model.preco.Preco;



public interface PrecoRep extends PagingAndSortingRepository<Preco, Long>, PrecoQueriesRep, JpaSpecificationExecutor<Preco>{
	
	Page<Preco> findByCodigoEstabelecimento(String codigoEstabelecimento, Pageable pageable);

	Page<Preco> findByCodigoEstabelecimentoAndCodigoProduto(String codigoEstabelecimento, String codigoProduto, Pageable pageable);

	Preco findByCodigoEstabelecimentoAndCodigoProdutoAndCodigoReferencia(String codigoEstabelecimento, String codigoProduto, String codigoReferencia);

	Preco findByIdUnico(String idUnico);
	
	List<Preco> findByHoraValidade(String hora);
}

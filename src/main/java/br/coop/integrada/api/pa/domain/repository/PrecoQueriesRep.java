package br.coop.integrada.api.pa.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoFilter;


public interface PrecoQueriesRep {
	
	Page<Preco> findByEstabelecimento_codigoRegional(Pageable pageable, PrecoFilter filter);
	Optional<Preco> findByPrecoValido(String codigoEstabelecimento, String codigoProduto, String codigoReferencia); 
}

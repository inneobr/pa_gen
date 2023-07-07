package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.coop.integrada.api.pa.domain.model.Pesagem;

public interface PesagemRep extends PagingAndSortingRepository<Pesagem, Long>, PesagemRepQueries, JpaSpecificationExecutor<Pesagem>{
	List<Pesagem> findByDataInativacaoNull();

	Pesagem findByNroDocPesagem(Integer nrDocumento);
	Page<Pesagem> findByCodEstabelecimentoAndDataInativacaoNullOrderByDataCadastroDesc(String codigo, Pageable pageable);
	Pesagem findByNroDocPesagemAndCodEstabelecimentoAndSafra(Integer nroDocPesagem, String codEstabelecimento, Integer safra);
}

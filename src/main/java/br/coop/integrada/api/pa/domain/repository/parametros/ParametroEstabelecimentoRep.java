package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;

public interface ParametroEstabelecimentoRep extends PagingAndSortingRepository<ParametroEstabelecimento, Long>, JpaSpecificationExecutor<ParametroEstabelecimento>{ 

    Page<ParametroEstabelecimento> findByDataInativacaoNull(Pageable pageable);

    List<ParametroEstabelecimento> findByDataInativacaoNullOrderByDataCadastroDesc();

    Page<ParametroEstabelecimento> findByEstabelecimentoAndDataInativacaoNull(Estabelecimento estabelecimento, Pageable pageable);

    ParametroEstabelecimento findByEstabelecimento(Estabelecimento estabelecimento);
    ParametroEstabelecimento findByEstabelecimentoCodigo(String codigo);
    ParametroEstabelecimento findByEstabelecimentoId(Long id);
    	
}

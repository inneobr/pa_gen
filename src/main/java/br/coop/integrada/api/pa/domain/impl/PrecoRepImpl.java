package br.coop.integrada.api.pa.domain.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoFilter;
import br.coop.integrada.api.pa.domain.repository.PrecoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.PrecoRep;
import br.coop.integrada.api.pa.domain.spec.PrecoSpecs;

@Service
public class PrecoRepImpl implements PrecoQueriesRep{
	
	@Autowired
    @Lazy
    private PrecoRep precoRep;
	
	public Page<Preco> findByEstabelecimento_codigoRegional(Pageable pageable, PrecoFilter filter) {
		return precoRep.findAll(
//                PrecoSpecs.doProduto(filter.getDescricao()),
                pageable
        );
	}

	public Optional<Preco> findByPrecoValido(String codigoEstabelecimento, String codigoProduto, String codigoReferencia) {
		return precoRep.findOne(
				PrecoSpecs.doCodigoEstabelecimento(codigoEstabelecimento)
				.and(PrecoSpecs.doProdutoCodItem(codigoProduto))
				.and(PrecoSpecs.doReferencia(codigoReferencia))
				.and(PrecoSpecs.doDataInativacaoIsNull())
		);
	}
}

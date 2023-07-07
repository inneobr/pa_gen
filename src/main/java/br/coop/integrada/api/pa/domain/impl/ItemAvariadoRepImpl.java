package br.coop.integrada.api.pa.domain.impl;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoFilter;
import br.coop.integrada.api.pa.domain.repository.parametros.ItemAvariadoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ItemAvariadoRep;
import br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

import static br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs.doCampoValidacao;
import static br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs.doCodigoEstabelecimento;
import static br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs.doCodigoGrupoProduto;
import static br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs.doPercentualEntreInicialEFinal;
import static br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs.doSituacao;
import static br.coop.integrada.api.pa.domain.spec.enums.Situacao.ATIVO;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ItemAvariadoRepImpl implements ItemAvariadoQueriesRep {

    @Autowired
    @Lazy
    private ItemAvariadoRep itemAvariadoRep;

    public Page<ItemAvariado> findAll(Pageable pageable, ItemAvariadoFilter filter) {
        return itemAvariadoRep.findAll(
                ItemAvariadoSpecs.doIdGrupoProduto(filter.getGrupoProduto())
                        .and(ItemAvariadoSpecs.doIdEstabelecimento(filter.getEstabelecimento()))
                        .and(ItemAvariadoSpecs.doSituacao(Situacao.ATIVO)),
                pageable
        );
    }

	@Override
	public Optional<ItemAvariado> buscarPor(String codigoGrupoProduto, String codigoEstabelecimento, ItemAvariadoValidacaoEnum campoValidacao, BigDecimal percentual) {
		return itemAvariadoRep.findOne(
				doCodigoGrupoProduto(codigoGrupoProduto)
				.and(doCodigoEstabelecimento(codigoEstabelecimento))
				.and(doCampoValidacao(campoValidacao))
				.and(doPercentualEntreInicialEFinal(percentual))
				.and(doSituacao(ATIVO))
			);
	}
}

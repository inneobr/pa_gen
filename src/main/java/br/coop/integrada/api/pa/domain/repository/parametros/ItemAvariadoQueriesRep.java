package br.coop.integrada.api.pa.domain.repository.parametros;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoFilter;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemAvariadoQueriesRep {

    Page<ItemAvariado> findAll(Pageable pageable, ItemAvariadoFilter filter);
    Optional<ItemAvariado> buscarPor(String codigoGrupoProduto, String codigoEstabelecimento, ItemAvariadoValidacaoEnum validacao, BigDecimal percentual);
}

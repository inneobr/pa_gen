package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemFilter;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemModalFilter;

public interface PesagemRepQueries {

	Page<Pesagem> buscarPesagens(Pageable pageable, PesagemFilter filter);
	Page<Pesagem> buscarPesagensPendentes(Pageable pageable, PesagemFilter filter);
	Page<Pesagem> buscarPesagensPendentesModel(Pageable pageable, PesagemModalFilter filter);
	Optional<Pesagem> buscarPorCodigoEstabelecimentoSafraDocPesagem(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem);
}
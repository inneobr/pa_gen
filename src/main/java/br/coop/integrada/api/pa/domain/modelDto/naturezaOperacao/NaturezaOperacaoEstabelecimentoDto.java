package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao;

import java.util.List;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import lombok.Data;

@Data
public class NaturezaOperacaoEstabelecimentoDto {	
	private Long id;
	private String idUnico;
	private List<Estabelecimento> naturezaOperacao;
	private NaturezaOperacao estabelecimento;

}

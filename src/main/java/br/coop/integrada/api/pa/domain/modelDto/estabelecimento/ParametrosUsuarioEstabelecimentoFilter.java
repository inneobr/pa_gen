package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ParametrosUsuarioEstabelecimentoFilter {
	private String usuario;
	private String estabelecimento;
	private Situacao situacao;
}

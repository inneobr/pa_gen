package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import java.io.Serializable;

import lombok.Data;

@Data
public class ParametroUsuarioEstabelecimentoFiltro implements Serializable {
	private String estabelecimentoCodigoOuNomeFantasia;
	private Boolean re;
}

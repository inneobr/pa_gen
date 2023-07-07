package br.coop.integrada.api.pa.domain.modelDto.rependente;

import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.gerarRe.TipoValidacaoEnum;
import lombok.Data;

@Data
public class GerarReParametros {
	private Object param1;
	private Object param2;
	private Object param3;
	private Object param4;
	private Object param5;
	private Object param6;
	private Object param7;
	private Object param8;
	private Boolean retorno;
	private String mensagem;	
	private TipoValidacaoEnum tipoValidacaoEnum;
	private FuncionalidadePaginaEnum funcionalidadePaginaEnum;
}

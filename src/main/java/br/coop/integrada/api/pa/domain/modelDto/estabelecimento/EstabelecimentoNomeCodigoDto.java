package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.Data;

@Data
public class EstabelecimentoNomeCodigoDto {
	private Long id;
	private String codigo;
	private String idRegistro;
	private String nomeFantasia;
	private IntegrationOperacaoEnum operacao;
	
	public String getCodNome() {
		return codigo +" - " + nomeFantasia;
	}
}

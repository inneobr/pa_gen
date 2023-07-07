package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ClassificacaoEstabelecimentoIntegrationDto {
	
	private String codigo;
    private String nomeFantasia;
    private IntegrationOperacaoEnum operacao;
	
	public static ClassificacaoEstabelecimentoIntegrationDto construir (Estabelecimento estabelecimento) {
		ClassificacaoEstabelecimentoIntegrationDto dto = new ClassificacaoEstabelecimentoIntegrationDto();
    	dto.setCodigo(estabelecimento.getCodigo());
    	dto.setNomeFantasia(estabelecimento.getNomeFantasia());
    	dto.setOperacao(IntegrationOperacaoEnum.WRITE);
    	return dto;
    }

}

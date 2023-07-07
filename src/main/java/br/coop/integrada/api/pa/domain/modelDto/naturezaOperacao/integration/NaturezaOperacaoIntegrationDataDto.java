package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class NaturezaOperacaoIntegrationDataDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
	List<NaturezaOperacaoIntegrationDto> naturezas;
	List<NaturezaOperacaoEstabelecimentoIntegrationDto> estabelecimentos;
	List<NaturezaOperacaoMovimentoIntegrationDto> movimentos;

}

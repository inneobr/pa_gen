package br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto;

import lombok.Data;
import java.util.List;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;

@Data
public class NaturezaTributariaDto {
	private Long id;
	private Integer codigo;
	private String descricao;
	private String idRegistro;
	private String natFixCoop;
	private String serFixCoop;
	private String modNatFixCoop;
	private String natFixTerc;
	private String serFixTerc;
	private String modNatFixTerc;
	private String natFixPjCnfCoop;
	private String serFixPjCnfCoop;
	private String modFixPjCnfCoop;
	private String natFixPjCnfTerc;
	private String serFixPjCnfTerc;
	private String modFixPjCnfTerc;
	private String natFixPjSnfCoop;
	private String serFixPjSnfCoop;
	private String modFixPjSnfCoop;
	private String natFixPjSnfTerc;
	private String serFixPjSnfTerc;
	private String modFixPjSnfTerc;
	private IntegrationOperacaoEnum operacao;
	
	private List<GrupoProdutoResumidoDto> grupoProdutos;
	private List<EstabelecimentoNomeCodigoDto> estabelecimentos;
}

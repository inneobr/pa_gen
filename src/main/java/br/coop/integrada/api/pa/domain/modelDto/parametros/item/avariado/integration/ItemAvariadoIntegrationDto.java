package br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAvariadoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idGrupo;
	private String descricao;
	private ItemAvariadoValidacaoEnum campoValidacao;
	private IntegrationOperacaoEnum operacao;
	private List<ItemAvariadoDetalheIntegrationDto> detalhes;
	private List<ItemAvariadoEstabelecimentoIntegrationDto> estabelecimentos;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public static ItemAvariadoIntegrationDto construir(ItemAvariado itemAvariado, List<ItemAvariadoDetalhe> detalhes, List<ItemAvariadoEstabelecimento> estabelecimentos) {
		var objDto = new ItemAvariadoIntegrationDto();
		objDto.setIdGrupo(itemAvariado.getId());
		objDto.setDescricao(itemAvariado.getDescricao());
		objDto.setCampoValidacao(itemAvariado.getCampoValidacao());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(itemAvariado.getAtivo()));

		List<ItemAvariadoDetalheIntegrationDto> detalheDtos = ItemAvariadoDetalheIntegrationDto.construir(detalhes);
		objDto.setDetalhes(detalheDtos);

		List<ItemAvariadoEstabelecimentoIntegrationDto> estabelecimentoDtos = ItemAvariadoEstabelecimentoIntegrationDto.construir(estabelecimentos);
		objDto.setEstabelecimentos(estabelecimentoDtos);

		return objDto;
	}

	public static ItemAvariadoIntegrationDto construir(ItemAvariado obj) {
		var objDto = new ItemAvariadoIntegrationDto();
		objDto.setIdGrupo(obj.getId());
		objDto.setDescricao(obj.getDescricao());
		objDto.setCampoValidacao(obj.getCampoValidacao());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));

		List<ItemAvariadoDetalheIntegrationDto> detalheDtos = ItemAvariadoDetalheIntegrationDto.construir(obj.getDetalhes());
		objDto.setDetalhes(detalheDtos);

		List<ItemAvariadoEstabelecimentoIntegrationDto> estabelecimentoDtos = ItemAvariadoEstabelecimentoIntegrationDto.construir(obj.getEstabelecimentos());
		objDto.setEstabelecimentos(estabelecimentoDtos);

		return objDto;
	}
	
	public static List<ItemAvariadoIntegrationDto> construir(List<ItemAvariado> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(itemAvariado -> {
			return construir(itemAvariado);
		}).toList();
	}
}

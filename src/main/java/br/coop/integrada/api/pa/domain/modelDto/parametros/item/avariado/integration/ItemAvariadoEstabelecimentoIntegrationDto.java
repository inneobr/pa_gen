package br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAvariadoEstabelecimentoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codigo;
	private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public static ItemAvariadoEstabelecimentoIntegrationDto construir(ItemAvariadoEstabelecimento obj) {
		var objDto = new ItemAvariadoEstabelecimentoIntegrationDto();
		objDto.setCodigo(obj.getEstabelecimento().getCodigo());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		return objDto;
	}

	public static List<ItemAvariadoEstabelecimentoIntegrationDto> construir(List<ItemAvariadoEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();

		return objs.stream().map(itemAvariadoEstabelecimento -> {
			return construir(itemAvariadoEstabelecimento);
		}).toList();
	}
}

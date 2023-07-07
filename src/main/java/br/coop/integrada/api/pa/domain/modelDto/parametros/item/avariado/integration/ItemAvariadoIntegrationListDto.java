package br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration;

import java.io.Serializable;
import java.util.List;

import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAvariadoIntegrationListDto implements Serializable {
	private static final long serialVersionUID = 1L;

	List<ItemAvariadoIntegrationDto> itensAvariados;

	public static ItemAvariadoIntegrationListDto construir(List<ItemAvariado> objs) {
		var objDto = new ItemAvariadoIntegrationListDto();
		List<ItemAvariadoIntegrationDto> itemAvariado = ItemAvariadoIntegrationDto.construir(objs);
		objDto.setItensAvariados(itemAvariado);
		return objDto;
	}
}

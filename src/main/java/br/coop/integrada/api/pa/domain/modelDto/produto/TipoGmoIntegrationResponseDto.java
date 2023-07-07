package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TipoGmoIntegrationResponseDto implements Serializable {

	private List<TipoGmoResponseDto> tipoGmo;
	
	public static TipoGmoIntegrationResponseDto construir(List<TipoGmoResponseDto> tipoGmoResponseDtos) {
		var objDto = new TipoGmoIntegrationResponseDto();
        objDto.setTipoGmo(tipoGmoResponseDtos);
        

        return objDto;
	}
	
}

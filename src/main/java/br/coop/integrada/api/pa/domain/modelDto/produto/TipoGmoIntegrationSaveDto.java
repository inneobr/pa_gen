package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;
import java.util.List;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import lombok.Data;

@Data
public class TipoGmoIntegrationSaveDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<TipoGmoDto> tipoGmo;
}

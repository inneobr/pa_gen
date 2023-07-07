package br.coop.integrada.api.pa.domain.modelDto.preco;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PrecoIntegrationSaveDto implements Serializable {
		
	private static final long serialVersionUID = 1L;
	private List<PrecoSaveDto> precos;
	
}

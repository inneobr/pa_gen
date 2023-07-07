package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ParametroEstabelecimentoIntegrationSaveDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<ParametroEstabelecimentoDto> parametros;
}

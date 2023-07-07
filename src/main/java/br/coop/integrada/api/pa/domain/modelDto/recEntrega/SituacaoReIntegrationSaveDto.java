package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class SituacaoReIntegrationSaveDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<SituacaoReDto> situacao;
}

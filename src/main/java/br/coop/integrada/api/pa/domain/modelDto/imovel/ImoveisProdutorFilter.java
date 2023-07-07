package br.coop.integrada.api.pa.domain.modelDto.imovel;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ImoveisProdutorFilter {
	private String codProdutor;
	private String matriculaNome;
	private Situacao status;
}

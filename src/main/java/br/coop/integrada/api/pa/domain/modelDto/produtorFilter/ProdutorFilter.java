package br.coop.integrada.api.pa.domain.modelDto.produtorFilter;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ProdutorFilter {	 
	private String codProdutor;
	private String cpfCnpj;
	private String nome;
	private Boolean emiteNotaFiscal;
	private Situacao situacao;
}

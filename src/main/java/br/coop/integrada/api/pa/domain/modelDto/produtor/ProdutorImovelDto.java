package br.coop.integrada.api.pa.domain.modelDto.produtor;

import lombok.Data;

@Data
public class ProdutorImovelDto {
	private String nome;
	private String codProdutor;
	private String cpfCnpj;
	private Long cadpro;
	private Boolean baixado;
	private String matriculaNome;
	private Boolean ativo;
}

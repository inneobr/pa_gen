package br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente;

import lombok.Data;

@Data
public class CampoProdutorFilter {
	private Integer safra; 
	private Integer ordemCampo; 
	private String codEstab; 
	private String fmCodigo; 
	private Long codClasse;
}

package br.coop.integrada.api.pa.domain.modelDto.parametros;

import lombok.Data;

@Data
public class ValidaEntradaCooperativaResponseDto {
	
	private Boolean permitido;
	private String mensagemPermitido;
	
	private String codProd;//(Diferente/igual)
	private String mensagemCodProd;
	
	private String codImovel; //(Diferente/igual)
	private String mensagemCodImovel;
	
	private String mensagem;
	
	public ValidaEntradaCooperativaResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
}

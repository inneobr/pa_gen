package br.coop.integrada.api.pa.domain.modelDto.externo;

import java.io.Serializable;

import lombok.Data;

@Data
public class NotaFiscalEntradaResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Boolean existe;
}

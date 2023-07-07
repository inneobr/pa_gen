package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen;

import lombok.Data;

@Data
public class NfErpGenResponse {
	private String id;
	private Boolean integrated;
	private String message;
}

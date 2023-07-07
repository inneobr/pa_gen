package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa;

import lombok.Data;

@Data
public class ResponseNfRemessa {
	private String id;
	private Boolean integrated;
    private String message;
}

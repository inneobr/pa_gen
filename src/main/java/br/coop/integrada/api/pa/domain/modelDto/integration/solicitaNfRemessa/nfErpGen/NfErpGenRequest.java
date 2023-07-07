package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen;

import lombok.Data;

@Data
public class NfErpGenRequest {
	 private String id;
	 private String serieDocto;
	 private String nrDocto;
	 private String natOperacao;
	 private String situacaoNfe;
	 private String chaveAcessoNfe;
	 private String message;
}

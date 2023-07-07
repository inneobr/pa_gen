package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class IntegracaoResponseNfErpGen {
	private List<NfErpGenResponse>  integracaoNfErpGen;
	
	public void adicionar(NfErpGenRequest nfErpGenRequest, Boolean integrated, String message) {
		NfErpGenResponse nfErpGenResponse = new NfErpGenResponse();
		nfErpGenResponse.setId(nfErpGenRequest.getId());
		nfErpGenResponse.setIntegrated(integrated);
		nfErpGenResponse.setMessage(message);
		
		if(this.integracaoNfErpGen == null) {
			this.integracaoNfErpGen = new ArrayList<>();
		}
		this.integracaoNfErpGen.add(nfErpGenResponse);		
	}
}

package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;

@Data
public class IntegrationRequestNf {
	private List<RequestNfRemessa> integrationNf;
	
	public static IntegrationRequestNf construir(List<NfRemessa> obj) {
		IntegrationRequestNf integrationRequestNf = new IntegrationRequestNf();
		integrationRequestNf.setIntegrationNf(new ArrayList<>());
		
		if(obj != null) {
			for(NfRemessa nfRemessa: obj) {
				RequestNfRemessa objDto = new RequestNfRemessa();
				objDto.setId(nfRemessa.getId().toString());
				objDto.setCodEstabel(nfRemessa.getCodEstabel());
				objDto.setNrRe(nfRemessa.getNrRe());
				objDto.setFuncao(nfRemessa.getFuncaoNota());
				objDto.setNatOperacao(nfRemessa.getNatOperacao());
				objDto.setSerieDocto(nfRemessa.getSerieDocto());
				objDto.setNrDocto(nfRemessa.getNrDocto());
				objDto.setSeqItem(nfRemessa.getSeqItem());
				objDto.setCodRefer(nfRemessa.getCodRefer());
				objDto.setDtCriacaoPedido(nfRemessa.getDataCadastro());
				objDto.setHrCriacaoPedido(nfRemessa.getHrCriacao());
				objDto.setQuantidade(nfRemessa.getQuantidade());
				objDto.setOrigem("GENESIS");
				integrationRequestNf.getIntegrationNf().add(objDto);
			}    
		}
		
        return integrationRequestNf;
    }	
}
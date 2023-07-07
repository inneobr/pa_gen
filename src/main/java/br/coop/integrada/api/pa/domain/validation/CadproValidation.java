package br.coop.integrada.api.pa.domain.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteDesmembramento;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;

@Service
public class CadproValidation {
	
	@Autowired
	private ImovelService imovelService;
	
	public void validarRecEntrega(List<RecEntrega> recEntregas) {
		List<String> mensagemCadpro = new ArrayList<String>();
		HashMap<String, String> cadproValidados = new HashMap<>();
		
		for(RecEntrega recEntrega : recEntregas) {
			String codProdutor = recEntrega.getCodEmitente();
			Long matriculaImovel = recEntrega.getMatricula();
			
			String cadpro = validarCadpro(codProdutor, matriculaImovel, cadproValidados, mensagemCadpro);
			
			if(Strings.isNotEmpty(cadpro)) {
				recEntrega.setNrCadPro(Long.parseLong(cadpro));
			}
		}
		
		cadproValidados.clear();
		
		if(!CollectionUtils.isEmpty(mensagemCadpro)) {
			String mensagem =  String.join("\n", mensagemCadpro);
			throw new ObjectDefaultException(mensagem);
		}
	}

	public void validarDesmembramento(List<RePendenteDesmembramento> desmembramentos) {
		List<String> mensagemCadpro = new ArrayList<String>();
		HashMap<String, String> cadproValidados = new HashMap<>();
		
		for(RePendenteDesmembramento desmembramento : desmembramentos) {
			String codProdutor = desmembramento.getProdutorFavorecido().getCodProdutor();
			Long matriculaImovel = desmembramento.getImovel().getMatricula();
			validarCadpro(codProdutor, matriculaImovel, cadproValidados, mensagemCadpro);
		}
		
		cadproValidados.clear();
		
		if(!CollectionUtils.isEmpty(mensagemCadpro)) {
			String mensagem =  String.join("\n", mensagemCadpro);
			throw new ObjectDefaultException(mensagem);
		}
	}
	
	public void validarRePendente(RePendente rePendente) {
		String codProdutor = rePendente.getProdutor().getCodProdutor();
		Long matriculaImovel = rePendente.getImovel().getMatricula();
		imovelService.buscarCadProPor(codProdutor, matriculaImovel);
	}
	
	private String validarCadpro(String codProdutor, Long matriculaImovel, HashMap<String, String> cadproValidados, List<String> mensagemCadpro) {
		String chave = codProdutor + matriculaImovel;
		String cadpro = "";
		
		if(cadproValidados.containsKey(chave)) {
			cadpro = cadproValidados.get(chave);
		}
		else {
			try {
				cadpro = imovelService.buscarCadProPor(codProdutor, matriculaImovel);
			}
			catch (Exception e) {
				mensagemCadpro.add(e.getMessage());
			}
			
			cadproValidados.put(chave, cadpro);
		}
		
		return cadpro;
	}
}

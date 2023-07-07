package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration;

import java.io.Serializable;
import java.util.List;

import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import lombok.Data;

@Data
public class SafraCompostaIntegrationListDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<SafraCompostaIntegrationDto> safrasCompostas;
	
	public static SafraCompostaIntegrationListDto construir(List<SafraComposta> objs) {
    	var objDto = new SafraCompostaIntegrationListDto();
    	objDto.setSafrasCompostas(SafraCompostaIntegrationDto.construir(objs));
    	return objDto;
    }
}

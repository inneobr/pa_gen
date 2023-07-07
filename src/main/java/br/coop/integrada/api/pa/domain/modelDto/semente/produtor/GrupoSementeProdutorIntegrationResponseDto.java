package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GrupoSementeProdutorIntegrationResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<GrupoSementeProdutorResponseDto>  grupoSementesProdutores; 
    private List<SementeCampoProdutorSimplesResponseDto> produtores;
    private List<SementeLaudoInspecaoSimplesResponseDto> laudos;
    
    public static GrupoSementeProdutorIntegrationResponseDto construir() {
    	GrupoSementeProdutorIntegrationResponseDto responseDto = new GrupoSementeProdutorIntegrationResponseDto();
    	responseDto.setGrupoSementesProdutores(new ArrayList<>());
    	responseDto.setProdutores(new ArrayList<>());
    	responseDto.setLaudos(new ArrayList<>());
    	return responseDto;
    }
    
}
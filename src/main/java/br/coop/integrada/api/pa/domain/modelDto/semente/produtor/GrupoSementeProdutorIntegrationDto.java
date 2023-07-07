package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class GrupoSementeProdutorIntegrationDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private List<GrupoSementeProdutorDto>  grupoSementesProdutores; 
    private List<SementeCampoProdutorSimplesDto> produtores;
    private List<SementeLaudoInspecaoSimplesDto> laudos;
    
}
	
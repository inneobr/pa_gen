package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class ImovelIntegratioinResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    DataImovelIntegrationResponseDto data;
    
    public static ImovelIntegratioinResponseDto construir() {
    	ImovelIntegratioinResponseDto dto = new ImovelIntegratioinResponseDto();
    	dto.setData(new DataImovelIntegrationResponseDto());
    	dto.getData().setImoveisProdutor(new ArrayList<>());
    	dto.getData().setImoveis(new ArrayList<>());
    	return dto;
    }
    	
}





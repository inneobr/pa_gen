package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ImovelIntegratioinDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private DataImovelIntegrationDto data;
    
    
    @Data
    public class DataImovelIntegrationDto implements Serializable {
    	private static final long serialVersionUID = 1L;
		private List<ImovelDto> imoveis;        
        private List<ImovelProdutorDto> imoveisProdutor;
    }

	
}

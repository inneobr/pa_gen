package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.util.List;

import lombok.Data;

@Data
public class DataImovelIntegrationResponseDto {
    private List<ImovelProdutorResponseDto> imoveisProdutor;
    private List<ImovelResponseDto> imoveis;        
            
}
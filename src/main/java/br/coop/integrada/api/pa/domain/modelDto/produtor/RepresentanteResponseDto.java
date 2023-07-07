package br.coop.integrada.api.pa.domain.modelDto.produtor;

import lombok.Data;

@Data
public class RepresentanteResponseDto {
    
    private String codRepres;
    
    public static RepresentanteResponseDto construir(String codigo) {
        RepresentanteResponseDto dto = new RepresentanteResponseDto();
        dto.setCodRepres(codigo);
        return dto;
    }
    
}

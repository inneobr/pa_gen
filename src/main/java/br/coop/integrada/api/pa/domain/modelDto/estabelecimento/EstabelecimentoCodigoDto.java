package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class EstabelecimentoCodigoDto {
    
    private String codigo;
    private String nomeFantasia;
    
    public static EstabelecimentoCodigoDto construir (Estabelecimento estabelecimento) {
    	EstabelecimentoCodigoDto dto = new EstabelecimentoCodigoDto();
    	dto.setCodigo(estabelecimento.getCodigo());
    	dto.setNomeFantasia(estabelecimento.getNomeFantasia());
    	return dto;
    }

}

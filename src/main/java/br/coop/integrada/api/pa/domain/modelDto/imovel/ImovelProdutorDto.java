package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ImovelProdutorDto implements Serializable{ 
    
    private static final long serialVersionUID = 1L;
    
    private String idUnico;
    private Long matricula;
    private String codProdutor;
    private Long cadpro;
    private String cpfCnpj;
    private Boolean transferencia; 
    private Boolean baixado;	
    private Boolean ativo;

}

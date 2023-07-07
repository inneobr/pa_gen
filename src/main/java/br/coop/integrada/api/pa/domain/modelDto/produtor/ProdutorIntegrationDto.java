package br.coop.integrada.api.pa.domain.modelDto.produtor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ProdutorIntegrationDto implements Serializable {
    private static final long serialVersionUID = 1L;
	List<ProdutorDto> produtores; 
}

package br.coop.integrada.api.pa.domain.modelDto.produtor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ProdutorIntegrationSaveDto implements Serializable {
    private List<ProdutorDto> produtores;

}

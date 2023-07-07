package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EstabelecimentoIntegrationDto implements Serializable {

    List<EstabelecimentoDto> estabelecimentos;
}

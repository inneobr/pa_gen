package br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration;

import lombok.Data;

import java.io.Serializable;

@Data
public class GrupoProdutoGmoIntegrationSimplesDto implements Serializable {

    private String tipoGmo;
    private Boolean ativo;
}

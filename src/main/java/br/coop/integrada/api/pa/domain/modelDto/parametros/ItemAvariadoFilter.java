package br.coop.integrada.api.pa.domain.modelDto.parametros;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ItemAvariadoFilter{
    private Long grupoProduto;
    private Long estabelecimento;
    private Situacao situacao;
}

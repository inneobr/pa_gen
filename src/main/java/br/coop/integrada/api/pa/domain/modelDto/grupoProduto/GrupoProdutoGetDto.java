package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class GrupoProdutoGetDto {
    private Long id;
    private String fmCodigo;
    private String descricao;
    
    private Collection<ProdutoGetDto> produtos = new ArrayList<>();
}

package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoFilter implements Serializable {
    private String descricao;
    private Integer safra;
    private Long idTipoClassificacao;
    private Long idGrupoProduto;
}

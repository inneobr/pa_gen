package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SafraCompostaComGrupoProdutoSimplesDto implements Serializable {
    private Long id;
    private String fmCodigo;
    private String descricao;
    private String nomeSafra;
    private Date dataInicio;
    private Date dataFinal;
    private String safraComposta;
}

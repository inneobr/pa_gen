package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SafraCompostaSimplesDto implements Serializable {
    private static final long serialVersionUID = 1L;
	private Long id;
    private Long tipoProdutoId;
    private String tipoProdutoNome;
    private String nomeSafra;
    private Date dataInicio;
    private Date dataFinal;
    private String safraComposta;
    private Boolean ativo;
    private Date dataIntegracao;
    private String descricaoStatusIntegracao; 
}

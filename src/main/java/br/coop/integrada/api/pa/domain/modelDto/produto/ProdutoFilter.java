package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;

import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.Data;

@Data
public class ProdutoFilter implements Serializable {
    private Long idGrupo;	
	private String codigoNome;
    private String idGrupoProduto;
    private Situacao situacao;
    
    
}

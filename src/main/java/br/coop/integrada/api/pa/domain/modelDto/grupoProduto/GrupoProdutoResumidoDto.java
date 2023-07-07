package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.Data;

@Data
public class GrupoProdutoResumidoDto {
	private Long id;
	private String fmCodigo;
	private String descricao;	
	private boolean fnt;
	private String idRegistro;
	private IntegrationOperacaoEnum operacao;
	   
    public String getCodDesc() {
        return fmCodigo + " - "+ descricao.toUpperCase();
    }
}

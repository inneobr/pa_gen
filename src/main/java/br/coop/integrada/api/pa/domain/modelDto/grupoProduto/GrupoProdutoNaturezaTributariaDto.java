package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GrupoProdutoNaturezaTributariaDto{
	
	@NotBlank(message = "Campo {fmCodigo} obrigatório")
	private String fmCodigo;
	
	@NotBlank(message = "Campo {descricao} obrigatório")
	private String descricao;
	
	private boolean ativo;
	private Long id;
	private Date dataCadastro;
    private Date dataAtualizacao;
    private Long idNaturezaTributariaGrupo;
}

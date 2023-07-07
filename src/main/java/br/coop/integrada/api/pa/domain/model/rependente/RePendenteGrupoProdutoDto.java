package br.coop.integrada.api.pa.domain.model.rependente;

import lombok.Data;

@Data
public class RePendenteGrupoProdutoDto {
	private Long id;
	private String fmCodigo;
	private String descricao;
	
	public String getCodigoDescricao() {
        return fmCodigo + " - "+ descricao;
    }
}

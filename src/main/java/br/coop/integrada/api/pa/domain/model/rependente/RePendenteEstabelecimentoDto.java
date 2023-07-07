package br.coop.integrada.api.pa.domain.model.rependente;

import lombok.Data;

@Data
public class RePendenteEstabelecimentoDto {
	private Long id;
	private String codigo;
	private String nomeFantasia;	
	
	public String getCodigoNome() {
        return codigo + " - "+ nomeFantasia;
    }
}

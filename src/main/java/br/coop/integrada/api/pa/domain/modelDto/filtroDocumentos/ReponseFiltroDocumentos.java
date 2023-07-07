package br.coop.integrada.api.pa.domain.modelDto.filtroDocumentos;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReponseFiltroDocumentos {
	private boolean bloqueado;	
	private String  message;
	private String  nomeCadastro;
}

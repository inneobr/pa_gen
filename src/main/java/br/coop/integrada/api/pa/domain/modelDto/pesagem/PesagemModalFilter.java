package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import lombok.Data;
import java.io.Serializable;

import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PesagemModalFilter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String estabelecimento;
	private String safra;
	private String tipoPesagem;
	private String situacao;
	private String logReJava;
	private StatusPesagem status;
}

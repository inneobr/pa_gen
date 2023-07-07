package br.coop.integrada.api.pa.domain.modelDto.dashBoard;

import java.util.List;
import lombok.Data;

@Data
public class ConsultaPrincipalFilter {
		
	private String regional;
	private List<String> codEstabel;
	private List<String> safra;
	private Long tipoProduto;
	
	
}

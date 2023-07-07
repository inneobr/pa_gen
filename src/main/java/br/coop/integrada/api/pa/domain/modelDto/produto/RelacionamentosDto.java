package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelacionamentosDto {
	public String tabela;
	public Integer quantidade;
	
	public Boolean contemRelacionamento() {
		
		if(quantidade > 0) {
			return true;
		}
		
		return false;
	}
	
}

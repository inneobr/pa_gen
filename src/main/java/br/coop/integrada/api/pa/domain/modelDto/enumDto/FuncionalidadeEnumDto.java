package br.coop.integrada.api.pa.domain.modelDto.enumDto;

import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import lombok.Data;

@Data
public class FuncionalidadeEnumDto {
	private FuncionalidadePaginaEnum funcionalidade;  
	private String descricao;
	
	public static FuncionalidadeEnumDto construir(FuncionalidadePaginaEnum funcionalidade) {
		var funcionalidadeDto = new FuncionalidadeEnumDto();
		funcionalidadeDto.setFuncionalidade(funcionalidade);
		funcionalidadeDto.setDescricao(funcionalidade.getDescricao());
		return funcionalidadeDto;
	}

}

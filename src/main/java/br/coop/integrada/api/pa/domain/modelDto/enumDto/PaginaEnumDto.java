package br.coop.integrada.api.pa.domain.modelDto.enumDto;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import lombok.Data;

@Data
public class PaginaEnumDto {
	private PaginaEnum pagina;  
	private String descricao;
	
	public static PaginaEnumDto construir(PaginaEnum pagina) {
		var paginaDto = new PaginaEnumDto();
		paginaDto.setPagina(pagina);
		paginaDto.setDescricao(pagina.getDescricao());
		return paginaDto;
	}
}

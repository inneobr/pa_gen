package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import lombok.Data;

@Data
public class TipoDesmembramentoEnumDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String descricao;
	
	public static TipoDesmembramentoEnumDto construir(TipoDesmembramentoEnum tipoDesmembramentoEnum) {
		var objDto = new TipoDesmembramentoEnumDto();
		objDto.setCodigo(tipoDesmembramentoEnum.name());
		objDto.setDescricao(tipoDesmembramentoEnum.getDescricao());
		return objDto;
	}
	
	public static List<TipoDesmembramentoEnumDto> construir(TipoDesmembramentoEnum[] tipos) {
		if(tipos == null || tipos.length == 0) return Collections.emptyList();
		
		List<TipoDesmembramentoEnumDto> tipoDesmembramentoEnumDtos = new ArrayList<>();
		for(TipoDesmembramentoEnum tipoDesmebramentoEnum: tipos) {
			tipoDesmembramentoEnumDtos.add(construir(tipoDesmebramentoEnum));
    	}
		
		return tipoDesmembramentoEnumDtos;
	}
}

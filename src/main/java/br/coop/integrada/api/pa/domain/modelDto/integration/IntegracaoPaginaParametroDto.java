package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaParametros;
import lombok.Data;

@Data
public class IntegracaoPaginaParametroDto {
	private Long id;	
	private String chave;
	private String valor;
	private String descricao;	
	private Boolean desenvolvimento;
	private Boolean homologacao;
	private Boolean producao;
	
	public static IntegracaoPaginaParametroDto construir(IntegracaoPaginaParametros obj) {
		var objDto = new IntegracaoPaginaParametroDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}
	
	public static List<IntegracaoPaginaParametroDto> construir(List<IntegracaoPaginaParametros> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		
		return objs.stream().map(parametro -> {
			return construir(parametro);
		}).toList();
	}
}

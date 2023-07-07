package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import lombok.Data;

@Data
public class IntegracaoPaginaHeaderDto {
	private Long id;	
	private String chave;
	private String valor;
	private String descricao;	
	private Boolean desenvolvimento;
	private Boolean homologacao;
	private Boolean producao;
	
	public static IntegracaoPaginaHeaderDto construir(IntegracaoPaginaHeader obj) {
		var objDto = new IntegracaoPaginaHeaderDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}
	
	public static List<IntegracaoPaginaHeaderDto> construir(List<IntegracaoPaginaHeader> objs) {
		if(CollectionUtils.isEmpty(objs)) new ArrayList<>();
		
		return objs.stream().map(header -> {
			return construir(header);
		}).toList();
	}
}

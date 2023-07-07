package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraCompostaEstabelecimento;
import lombok.Data;

@Data
public class SafraCompostaEstabelecimentoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codigo;
	private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static SafraCompostaEstabelecimentoIntegrationDto construir(SafraCompostaEstabelecimento obj) {
		var objDto = new SafraCompostaEstabelecimentoIntegrationDto();
		objDto.setCodigo(obj.getCodigoEstabelecimento());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		return objDto;
	}
	
	public static List<SafraCompostaEstabelecimentoIntegrationDto> construir(List<SafraCompostaEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		
		return objs.stream().map(safraCompostaEstabelecimento -> {
			return construir(safraCompostaEstabelecimento);
		}).toList();
	}
}

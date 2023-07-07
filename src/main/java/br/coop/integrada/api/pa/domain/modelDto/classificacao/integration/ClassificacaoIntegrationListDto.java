package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificacaoIntegrationListDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	List<TipoClassificacaoIntegrationDto> tipoClassificacoes;
	List<ClassificacaoIntegrationDto> classificacoes;
	//List<Results> results;
	
	public static ClassificacaoIntegrationListDto construir (List<Classificacao> classificacoes, List<TipoClassificacao> tipos) {
		ClassificacaoIntegrationListDto objDto = new ClassificacaoIntegrationListDto();
		objDto.setTipoClassificacoes(new ArrayList<	>());
		objDto.setClassificacoes(new ArrayList<>());
		
		if(!CollectionUtils.isEmpty(classificacoes)) {
			List<ClassificacaoIntegrationDto> classificacoesDto = ClassificacaoIntegrationDto.construir(classificacoes);
			if(classificacoesDto != null) {
				objDto.setClassificacoes(classificacoesDto);
			}
		}

		if(!CollectionUtils.isEmpty(tipos)) {
			List<TipoClassificacaoIntegrationDto> tiposDto = tipos.stream().map(tipo -> {
				TipoClassificacaoIntegrationDto tipoDto = TipoClassificacaoIntegrationDto.construir(tipo);				
				return tipoDto;
			}).toList();
			objDto.setTipoClassificacoes(tiposDto);
		}
		
		return objDto;
	}	
	
	public class Results{
		
		@JsonProperty(access = Access.WRITE_ONLY)
		private String message;
		
		@JsonProperty(access = Access.WRITE_ONLY)
		private Boolean sucess;
	}

}

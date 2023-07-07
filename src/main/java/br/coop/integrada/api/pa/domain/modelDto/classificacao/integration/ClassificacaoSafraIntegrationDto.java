package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import lombok.Data;

@Data
public class ClassificacaoSafraIntegrationDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idAgrupamento;
	private Integer safra;
    private IntegrationOperacaoEnum operacao;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static ClassificacaoSafraIntegrationDto construir(ClassificacaoSafra obj) {
		ClassificacaoSafraIntegrationDto objDto = new ClassificacaoSafraIntegrationDto();
		objDto.setIdAgrupamento(obj.getClassificacao().getId());
		objDto.setSafra(obj.getSafra());
        objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
        return objDto;
    }

}

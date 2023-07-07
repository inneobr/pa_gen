package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import lombok.Data;

@Data
public class TipoClassificacaoIntegrationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private TipoClassificacaoEnum codigo;
    private String descricao;
    private Boolean resultadoDesconto;
    private Boolean resultadoTaxaSecagemKg;
    private Boolean resultadoTaxaSecagemValor;
    private Boolean teorPorFaixa;
    private Boolean controlePorSafra;
    private Boolean ph;
    private Boolean phCorrigido;
    private Boolean referencia;
    private IntegrationOperacaoEnum operacao;
    
    @JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

    public static TipoClassificacaoIntegrationDto construir(TipoClassificacao obj) {
    	TipoClassificacaoIntegrationDto objDto = new TipoClassificacaoIntegrationDto();
        BeanUtils.copyProperties(obj, objDto);
        objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
        objDto.setDescricao(obj.getTipoClassificacao().getDescricao()); 
        objDto.setCodigo(obj.getTipoClassificacao());
        return objDto;
    }

}

package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import lombok.Data;

@Data
public class ClassificacaoGrupoProdutoIntegrationDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long idAgrupamento;
	private String codigoGrupoProduto;
	private String descricao;
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static ClassificacaoGrupoProdutoIntegrationDto construir(ClassificacaoGrupoProduto obj) {
		ClassificacaoGrupoProdutoIntegrationDto objDto = new ClassificacaoGrupoProdutoIntegrationDto();

        objDto.setIdAgrupamento(obj.getClassificacao().getId());
        objDto.setCodigoGrupoProduto(obj.getGrupoProduto().getFmCodigo());
        objDto.setDescricao(obj.getGrupoProduto().getDescricao());
        objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
       		
        return objDto;
    }
}

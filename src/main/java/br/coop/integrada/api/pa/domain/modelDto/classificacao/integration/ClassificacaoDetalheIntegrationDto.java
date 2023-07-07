package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import lombok.Data;

@Data
public class ClassificacaoDetalheIntegrationDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long idAgrupamento;
	private Long idDetalhe;
	private BigDecimal teorClassificacaoInicial;
	private BigDecimal teorClassificacaoFinal;
	private BigInteger phEntrada;
	private BigDecimal percentualDesconto;
	private BigInteger phCorrigido;
	private String codigoReferencia;
	private BigDecimal taxaSecagemQuilo;
	private BigDecimal taxaSecagemValor;
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static ClassificacaoDetalheIntegrationDto construir(ClassificacaoDetalhe obj) {
		ClassificacaoDetalheIntegrationDto detalheDto = new ClassificacaoDetalheIntegrationDto();
        BeanUtils.copyProperties(obj, detalheDto);
        detalheDto.setIdDetalhe(obj.getId());
        detalheDto.setIdAgrupamento(obj.getClassificacao().getId());
        detalheDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
        if(obj.getProdutoReferencia() != null) {
        	detalheDto.setCodigoReferencia(obj.getProdutoReferencia().getCodRef());
        }

        return detalheDto;
    }

}

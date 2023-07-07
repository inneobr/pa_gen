package br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAvariadoDetalheIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idDetalhe;
	private String codigoGrupoProduto;
	private String codigoProduto;
	private String codigoProdutoReferencia;
	private BigDecimal percentualInicial;
	private BigDecimal percentualFinal;
	private BigDecimal phInicial;
    private BigDecimal phFinal;
    private BigDecimal fntIni;
    private BigDecimal fntFim;
	private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public static ItemAvariadoDetalheIntegrationDto construir(ItemAvariadoDetalhe obj) {
		var objDto = new ItemAvariadoDetalheIntegrationDto();
		objDto.setIdDetalhe(obj.getId());
		objDto.setPercentualInicial(obj.getPercentualInicial());
		objDto.setPercentualFinal(obj.getPercentualFinal());
		objDto.setPhInicial(obj.getPhInicial());
		objDto.setPhFinal(obj.getPhFinal());
		objDto.setFntIni(obj.getFntInicial());
		objDto.setFntFim(obj.getFntFinal());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));

		if(obj.getProduto() != null && obj.getProduto().getGrupoProduto() != null) {
			String codigoGrupoProduto = obj.getProduto().getGrupoProduto().getFmCodigo();
			objDto.setCodigoGrupoProduto(codigoGrupoProduto);
		}

		if(obj.getProduto() != null) {
			String codigoProduto = obj.getProduto().getCodItem();
			objDto.setCodigoProduto(codigoProduto);
		}

		if(obj.getProdutoReferencia() != null) {
			String codigoProdutoReferencia = obj.getProdutoReferencia().getCodRef();
			objDto.setCodigoProdutoReferencia(codigoProdutoReferencia);
		}

		return objDto;
	}

	public static List<ItemAvariadoDetalheIntegrationDto> construir(List<ItemAvariadoDetalhe> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();

		return objs.stream().map(itemAvariadoDetalhe -> {
			return construir(itemAvariadoDetalhe);
		}).toList();
	}
}

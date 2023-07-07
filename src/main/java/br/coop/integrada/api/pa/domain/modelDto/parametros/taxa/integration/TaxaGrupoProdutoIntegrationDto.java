package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import lombok.Data;

@Data
public class TaxaGrupoProdutoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idTaxaGrupoProduto;
	
	private String codigoGrupoProduto;
	    
    @JsonProperty(access = Access.READ_ONLY)
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static TaxaGrupoProdutoIntegrationDto construir(TaxaGrupoProduto obj) {
		var objDto = new TaxaGrupoProdutoIntegrationDto();
		objDto.setIdTaxaGrupoProduto(obj.getId());
		objDto.setCodigoGrupoProduto(obj.getCodigoGrupoProduto());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		return objDto;
	}

	public static List<TaxaGrupoProdutoIntegrationDto> construir(List<TaxaGrupoProduto> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(taxaGrupoProduto -> {
			return construir(taxaGrupoProduto);
		}).toList();
	}
}

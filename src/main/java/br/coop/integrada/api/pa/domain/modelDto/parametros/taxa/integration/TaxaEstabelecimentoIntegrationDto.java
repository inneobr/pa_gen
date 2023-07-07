package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;
import lombok.Data;

@Data
public class TaxaEstabelecimentoIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idTaxaEstabelecimento;
	
	private String codigo;
	
    @JsonProperty(access = Access.READ_ONLY)
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public static TaxaEstabelecimentoIntegrationDto construir(TaxaEstabelecimento obj) {
		var objDto = new TaxaEstabelecimentoIntegrationDto();
		BeanUtils.copyProperties(obj, objDto);
		objDto.setIdTaxaEstabelecimento(obj.getId());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		return objDto;
	}
	
	public static List<TaxaEstabelecimentoIntegrationDto> construir(List<TaxaEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(taxaEstabelecimento -> {
			return construir(taxaEstabelecimento);
		}).toList();
	}
}

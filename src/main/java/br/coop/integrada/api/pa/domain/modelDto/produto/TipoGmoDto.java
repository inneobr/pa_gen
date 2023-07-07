package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TipoGmoDto{
	private Long id;
	
	@NotBlank(message = "Campo {idUnico} obrigatório")
	private String idUnico;
	
	@NotBlank(message = "Campo {tipoGmo} obrigatório")
	private String tipoGmo;
	
	private String obsRomaneio;
	
	@NotNull(message = "Campo {perDeclarada} obrigatório")
	private BigDecimal perDeclarada;
	
	@NotNull(message = "Campo {perTestada} obrigatório")
	private BigDecimal perTestada;
	
	@NotNull(message = "Campo {vlKit} obrigatório")
	private BigDecimal vlKit;

	private Boolean ativo;
	
	private IntegrationOperacaoEnum operacao;	
	
	public static TipoGmoDto construir(TipoGmo tipoGmo) {
		TipoGmoDto objDto = new TipoGmoDto();
        BeanUtils.copyProperties(tipoGmo, objDto);
		return objDto;
	}
	
	public static List<TipoGmoDto> construir(List<TipoGmo> tiposGmo) {
		if(CollectionUtils.isEmpty(tiposGmo)) return Collections.emptyList();
		return tiposGmo.stream().map(tipoGmo -> {
			return construir(tipoGmo);
		}).toList();
	}
}

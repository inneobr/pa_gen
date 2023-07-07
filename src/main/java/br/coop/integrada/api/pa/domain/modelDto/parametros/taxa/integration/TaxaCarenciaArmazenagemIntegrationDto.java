package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;
import lombok.Data;

@Data
public class TaxaCarenciaArmazenagemIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Long idCarenciaArmazenagem;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataInicial;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataFinal;
    
    private Integer quantidadeDiasCarencia;
        
    @JsonProperty(access = Access.READ_ONLY)
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public static TaxaCarenciaArmazenagemIntegrationDto construir(TaxaCarenciaArmazenagem obj) {
		var objDto = new TaxaCarenciaArmazenagemIntegrationDto();
		BeanUtils.copyProperties(obj, objDto);
		
		objDto.setIdCarenciaArmazenagem(obj.getId());
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		
		return objDto;
	}
	
	public static List<TaxaCarenciaArmazenagemIntegrationDto> construir(List<TaxaCarenciaArmazenagem> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(taxaCarenciaArmazenagem -> {
			return construir(taxaCarenciaArmazenagem);
		}).toList();
	}	
}

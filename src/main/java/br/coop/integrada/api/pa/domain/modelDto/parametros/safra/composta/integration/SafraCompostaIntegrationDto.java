package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import lombok.Data;

@Data
public class SafraCompostaIntegrationDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String tipoProduto;
    private String tipoSafra;
    private String nomeSafra;
    private Integer diaInicial;
    private Integer mesInicial;
    private String anoInicialOperacao;
    private Integer anoInicialQuantidade;
    private Integer diaFinal;
    private Integer mesFinal;
    private String anoFinalOperacao;
    private Integer anoFinalQuantidade;
    private String safraPlantioOperacao;
    private Integer safraPlantioQuantidade;
    private String safraColheitaOperacao;
    private Integer safraColheitaQuantidade;
    private List<SafraCompostaEstabelecimentoIntegrationDto> estabelecimentos;
    
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
    
    public static SafraCompostaIntegrationDto construir(SafraComposta obj) {
    	var objDto = new SafraCompostaIntegrationDto();
    	BeanUtils.copyProperties(obj, objDto);
    	
    	objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
    	objDto.setTipoSafra(obj.getTipoSafra().toString());
    	objDto.setTipoProduto(obj.getTipoProduto().getIdUnico());
    	objDto.setAnoInicialOperacao(obj.getAnoInicialOperacao().getValorEnvioErp());
    	objDto.setAnoFinalOperacao(obj.getAnoFinalOperacao().getValorEnvioErp());
    	objDto.setSafraPlantioOperacao(obj.getSafraPlantioOperacao().getValorEnvioErp());
    	objDto.setSafraColheitaOperacao(obj.getSafraColheitaOperacao().getValorEnvioErp());
    	
    	if(objDto.getOperacao().equals(IntegrationOperacaoEnum.WRITE)) {
	    	List<SafraCompostaEstabelecimentoIntegrationDto> estabelecimentos = SafraCompostaEstabelecimentoIntegrationDto.construir(obj.getEstabelecimentos());
	    	objDto.setEstabelecimentos(estabelecimentos);
	    	
    	}else {
    		objDto.setEstabelecimentos(new ArrayList<>());    		
    	}
		
    	return objDto;
    }

    public static List<SafraCompostaIntegrationDto> construir(List<SafraComposta> objs) {
    	if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
    	
    	return objs.stream().map(safraComposta -> {
    		return construir(safraComposta);
    	}).toList();
    }
}

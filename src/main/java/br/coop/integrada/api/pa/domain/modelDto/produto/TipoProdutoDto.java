package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TipoProdutoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String idUnico;
    private String nome;
    private Boolean ativo;    
	private IntegrationOperacaoEnum operacao;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dataIntegracao;
    private String descricaoStatusIntegracao;   
    
    @JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
    
    public static TipoProdutoIntegrationDto construirIntegrationDto(List<TipoProduto> tipos) {
    	TipoProdutoIntegrationDto objDto = new TipoProdutoIntegrationDto();
    	objDto.setTipoProduto(new ArrayList<>());
    	
    	if(tipos != null && !tipos.isEmpty()) {
    		for(TipoProduto tipo : tipos) {
    			TipoProdutoDto dto = TipoProdutoDto.construir(tipo);
    			dto.setOperacao(IntegrationOperacaoEnum.toEnum(tipo.getAtivo()));
    			objDto.getTipoProduto().add(dto);
    		}
    	}
    	
    	return objDto;
    }
    
    public static TipoProdutoDto construir(TipoProduto tipo) {
    	TipoProdutoDto tipoProdutoDto = new TipoProdutoDto();
    	tipoProdutoDto.setIdUnico(tipo.getIdUnico());
    	tipoProdutoDto.setNome(tipo.getNome());
    	tipoProdutoDto.setAtivo(tipo.getDataInativacao() == null);
    	tipoProdutoDto.setDataIntegracao(tipo.getDataIntegracao());
    	if(tipo.getStatusIntegracao() != null) {
    		tipoProdutoDto.setDescricaoStatusIntegracao(tipo.getStatusIntegracao().getDescricao());
    	}
    	return tipoProdutoDto;
    }
    
    public static List<TipoProdutoDto> construir(List<TipoProduto> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(tipoProduto -> {
            return TipoProdutoDto.construir(tipoProduto);
        }).toList();
    }
    
    public static TipoProduto convertDto(TipoProdutoDto objDto) {
		var obj = new TipoProduto();
		BeanUtils.copyProperties(objDto, obj);

		return obj;
	}
    
    
}

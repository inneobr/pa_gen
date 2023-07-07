package br.coop.integrada.api.pa.domain.modelDto.classificacao.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import lombok.Data;

@Data
public class ClassificacaoIntegrationDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long idAgrupamento;
    private String descricao;
    private String tipoClassificacao;
    private IntegrationOperacaoEnum operacao;       
    private List<ClassificacaoSafraIntegrationDto> safras;
    private List<ClassificacaoDetalheIntegrationDto> detalhes;
    private List<ClassificacaoGrupoProdutoIntegrationDto> grupoProdutos;
	private List<ClassificacaoEstabelecimentoIntegrationDto> estabelecimentos;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static List<ClassificacaoIntegrationDto> construir (List<Classificacao> classificacoes) {
		List<ClassificacaoIntegrationDto> lista = classificacoes.stream().map(classificacao -> {
			return converterClassificacaoToIntegrationDto(classificacao);			
		}).toList();
		
		return lista;
	}
	
	public static ClassificacaoIntegrationDto converterClassificacaoToIntegrationDto(Classificacao classificacao) {
		ClassificacaoIntegrationDto classificacaoDto = new ClassificacaoIntegrationDto();
		classificacaoDto.setIdAgrupamento(classificacao.getId());
		classificacaoDto.setDescricao(classificacao.getDescricao());
		classificacaoDto.setTipoClassificacao(classificacao.getTipoClassificacao().getTipo());
		classificacaoDto.setSafras(new ArrayList<>());
		classificacaoDto.setDetalhes(new ArrayList<>());
		classificacaoDto.setGrupoProdutos(new ArrayList<>());
		classificacaoDto.setEstabelecimentos(new ArrayList<>());
		classificacaoDto.setOperacao(IntegrationOperacaoEnum.toEnum(classificacao.getAtivo()));
		
		if(!CollectionUtils.isEmpty(classificacao.getSafras())) {
			classificacaoDto.setSafras(classificacao.getSafras().stream().map(safra -> {				
				return ClassificacaoSafraIntegrationDto.construir(safra);
			}).toList());
		}
		
		if(!CollectionUtils.isEmpty(classificacao.getDetalhes())) {
			classificacaoDto.setDetalhes(classificacao.getDetalhes().stream().map(detalhe -> {				
				return ClassificacaoDetalheIntegrationDto.construir(detalhe);
			}).toList());
		}
		
		if(!CollectionUtils.isEmpty(classificacao.getGrupoProdutos())) {
			classificacaoDto.setGrupoProdutos(classificacao.getGrupoProdutos().stream().map(grupoProduto -> {				
				return ClassificacaoGrupoProdutoIntegrationDto.construir(grupoProduto);
			}).toList());
		}
		
        if(!CollectionUtils.isEmpty(classificacao.getEstabelecimentos())) {
        	classificacaoDto.setEstabelecimentos( classificacao.getEstabelecimentos().stream().map(classificacaoEstabelecimento -> {
	        	return ClassificacaoEstabelecimentoIntegrationDto.construir(classificacaoEstabelecimento.getEstabelecimento());
	        }).toList());
        }
		
		
		return classificacaoDto;
	}

}

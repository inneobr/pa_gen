package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaSecagemEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoDescontoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import lombok.Data;

@Data
public class TaxaIntegrationDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idTaxa;
    private String descricao;
    private Integer safra;
    private Integer manutencaoCarencia;
    private BigDecimal manutencaoValorPorSaca;
    private TipoDescontoEnum quebraTecnicaCobrancaPor;
    private Integer quebraTecnicaCarencia;
    private Integer quebraTecnicaDiasMaximo;
    private BigDecimal quebraTecnicaPercentualAoDia;
    private Integer taxaSeguroCarencia;
    private BigDecimal taxaSeguroPercentualPorSaca;
    private BigDecimal taxaValorOperacional;
    private BigDecimal taxaReceitaRegional;
    private BigDecimal taxaRetencaoEspecial;
    private BigDecimal taxaOutras;
    private Boolean cobraRecepcao;
    private TipoCobrancaEnum tipoCobraRecepcaoPor;
    private BigDecimal valorTaxaRecepcao;
    private Boolean cobraSecagem;
    private TipoCobrancaEnum tipoCobraSecagemNa;
    private TipoCobrancaSecagemEnum tipoCobrancaSecagemPor;
    private Boolean cobrancaArmazenagem;
    private BigDecimal valorArmazenagem;
    private BigDecimal valorTaxaSaca60;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataCobrancaArmazenagem;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataCobrancaArmazenagem2;
    
    private List<TaxaCarenciaArmazenagemIntegrationDto> carenciaArmazenagens;
    private List<TaxaGrupoProdutoIntegrationDto> grupoProdutos;
    private List<TaxaEstabelecimentoIntegrationDto> estabelecimentos;
    
    @JsonProperty(access = Access.READ_ONLY)
    private IntegrationOperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static TaxaIntegrationDto construir(Taxa obj) {
		var objDto = new TaxaIntegrationDto();
		BeanUtils.copyProperties(obj, objDto);
		
		List<TaxaCarenciaArmazenagemIntegrationDto> carenciaArmazenagens = TaxaCarenciaArmazenagemIntegrationDto.construir(obj.getCarenciaArmazenagens());
		List<TaxaGrupoProdutoIntegrationDto> grupoProdutos = TaxaGrupoProdutoIntegrationDto.construir(obj.getGrupoProdutos());
		List<TaxaEstabelecimentoIntegrationDto> estabelecimentos = TaxaEstabelecimentoIntegrationDto.construir(obj.getEstabelecimentos() );
		
		objDto.setIdTaxa(obj.getId());
		objDto.setGrupoProdutos(grupoProdutos);
		objDto.setEstabelecimentos(estabelecimentos);
		objDto.setCarenciaArmazenagens(carenciaArmazenagens);
		objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
		
		return objDto;
	}

	public static List<TaxaIntegrationDto> construir(List<Taxa> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(taxa -> {
			return construir(taxa);
		}).toList();
	}	
}

package br.coop.integrada.api.pa.domain.modelDto.rependente.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.TipoDesmembramentoEnumDto;
import lombok.Data;

@Data
public class RePendenteItemCompletoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ProdutoGetDto produto;
	private String referencia;
	private BigDecimal pesoLiquido;
	private BigInteger phEntrada;
	private BigDecimal impureza;
	private BigDecimal umidade;
	private BigDecimal chuvadoAvariado;
	private BigDecimal tbm;
	private Integer tipo;
	private BigDecimal qtdCafeEscolha;
	private Integer defeitos;
	private Boolean qualidadeProdutoNormal;
	private Boolean qualidadeProdutoSementeira;
	private Boolean qualidadeProdutoTerra;
	private Boolean qualidadeProdutoVagem;
	private Boolean desmembramento;
	private UnidadeDesmembramento unidadeDesmembramento;
	private TipoDesmembramentoEnumDto tipoDesmembramento;
	private String bebida;
	private String lote;
	private BigDecimal rendaLiquida;
	private BigInteger phCorrigido;
	private BigDecimal percentualDescontoImpureza;
	private BigDecimal percentualDescontoUmidade;
	private BigDecimal percentualDescontoChuvadoAvariado;
	private BigDecimal qtdDescontoImpureza;
	private BigDecimal qtdDescontoUmidade;
	private BigDecimal qtdDescontoChuvadoAvariado;
	private BigDecimal qtdTbm;
	private BigDecimal valorFiscal;
	private BigDecimal tabelaUmidadeTaxaSecagemKg;
	private BigDecimal tabelaUmidadeTaxaSecagemValor;
	private Integer cafeRmTipo;
	private String cafeRmBebida;
	private BigDecimal cafeRmCafeEscolha;
	private BigDecimal cafeRmRendaLiquida;
	private Integer cafeRmDefeito;
	private String cafeRmCodigoReferencia;
	private BigDecimal cafeRmRenda;
	private BigDecimal cafeRmRendaEscolha;
	private BigDecimal cafeRmRendaTotal;
	private BigDecimal percentualPeneira14Acima;
	private BigDecimal percentualBandinha;
	private BigDecimal rendaSemDescontoDpi;
	private BigDecimal tabelaValorRecepcao;
	private BigDecimal valorCalculadoRecepcao;
	private BigDecimal qtdRecepcao;
	private BigDecimal valorCalculadoSecagem;
	private BigDecimal qtdSecagem;
	private BigDecimal valorPonto;
	private String controleSemente;
	private Boolean indiceDpi;
	private BigDecimal percentualDpi;
	private String observacaoDpi;
	private Boolean teraSubProduto;
	private String familiaSubProduto;
	private String itemSubProduto;
	private Boolean indiceSecagemLimpeza;
	private String tipoCobrancaSecagem;
	private String itemParaSecagem;
	private Boolean incideTaxaRecepcao;
	private BigDecimal valorRecepcao;
	private BigDecimal fnt;
	private Integer densidade;
	
	public static RePendenteItemCompletoDto construir(RePendenteItem item) {
		var objDto = new RePendenteItemCompletoDto();
		BeanUtils.copyProperties(item, objDto);
		
		if(item.getProduto() != null) {
			ProdutoGetDto produtoDto = ProdutoGetDto.construir(item.getProduto());
			objDto.setProduto(produtoDto);
		}
		
		if(item.getTipoDesmembramento() != null) {
			TipoDesmembramentoEnumDto tipoDesmembramentoDto = TipoDesmembramentoEnumDto.construir(item.getTipoDesmembramento());
			objDto.setTipoDesmembramento(tipoDesmembramentoDto);
		}
		
		return objDto;
	}
	
	public static List<RePendenteItemCompletoDto> construir(List<RePendenteItem> itens) {
		if(CollectionUtils.isEmpty(itens)) return Collections.emptyList();
		
		return itens.stream().map(item -> {
			return construir(item);
		}).toList();
	}
}

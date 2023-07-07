package br.coop.integrada.api.pa.domain.modelDto.rependente.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import lombok.Data;

@Data
public class RePendenteItemDto implements Serializable {
	private static final long serialVersionUID = 1L;

    private String produtoCodigo;
    
    @JsonProperty(access = Access.READ_ONLY)
    private String produtoDescricao;
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
	private TipoDesmembramentoEnum tipoDesmembramento;
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
	
	public Boolean getDesmembramento() {
		if(desmembramento == null) return false;
		return desmembramento;
	}
	
	public BigInteger getPhEntrada() {
		if(phEntrada == null) return BigInteger.ZERO;
		return phEntrada;
	}
	
	public BigInteger getPhCorrigido() {
		if(phCorrigido == null) return BigInteger.ZERO;
		return phCorrigido;
	}

	public BigDecimal getPesoLiquido() {
		if(pesoLiquido == null) return BigDecimal.ZERO;
		return pesoLiquido;
	}

	public BigDecimal getImpureza() {
		if(impureza == null) return BigDecimal.ZERO;
		return impureza;
	}

	public BigDecimal getUmidade() {
		if(umidade == null) return BigDecimal.ZERO;
		return umidade;
	}

	public BigDecimal getChuvadoAvariado() {
		if(chuvadoAvariado == null) return BigDecimal.ZERO;
		return chuvadoAvariado;
	}

	public BigDecimal getTbm() {
		if(tbm == null) return BigDecimal.ZERO;
		return tbm;
	}

	public BigDecimal getQtdCafeEscolha() {
		if(qtdCafeEscolha == null) return BigDecimal.ZERO;
		return qtdCafeEscolha;
	}

	public BigDecimal getRendaLiquida() {
		if(rendaLiquida == null) return BigDecimal.ZERO;
		return rendaLiquida;
	}

	public BigDecimal getPercentualDescontoImpureza() {
		if(percentualDescontoImpureza == null) return BigDecimal.ZERO;
		return percentualDescontoImpureza;
	}

	public BigDecimal getPercentualDescontoUmidade() {
		if(percentualDescontoUmidade == null) return BigDecimal.ZERO;
		return percentualDescontoUmidade;
	}

	public BigDecimal getPercentualDescontoChuvadoAvariado() {
		if(percentualDescontoChuvadoAvariado == null) return BigDecimal.ZERO;
		return percentualDescontoChuvadoAvariado;
	}

	public BigDecimal getQtdDescontoImpureza() {
		if(qtdDescontoImpureza == null) return BigDecimal.ZERO;
		return qtdDescontoImpureza;
	}

	public BigDecimal getQtdDescontoUmidade() {
		if(qtdDescontoUmidade == null) return BigDecimal.ZERO;
		return qtdDescontoUmidade;
	}

	public BigDecimal getQtdDescontoChuvadoAvariado() {
		if(qtdDescontoChuvadoAvariado == null) return BigDecimal.ZERO;
		return qtdDescontoChuvadoAvariado;
	}

	public BigDecimal getQtdTbm() {
		if(qtdTbm == null) return BigDecimal.ZERO;
		return qtdTbm;
	}

	public BigDecimal getValorFiscal() {
		if(valorFiscal == null) return BigDecimal.ZERO;
		return valorFiscal;
	}

	public BigDecimal getTabelaUmidadeTaxaSecagemKg() {
		if(tabelaUmidadeTaxaSecagemKg == null) return BigDecimal.ZERO;
		return tabelaUmidadeTaxaSecagemKg;
	}

	public BigDecimal getTabelaUmidadeTaxaSecagemValor() {
		if(tabelaUmidadeTaxaSecagemValor == null) return BigDecimal.ZERO;
		return tabelaUmidadeTaxaSecagemValor;
	}

	public BigDecimal getCafeRmCafeEscolha() {
		if(cafeRmCafeEscolha == null) return BigDecimal.ZERO;
		return cafeRmCafeEscolha;
	}

	public BigDecimal getCafeRmRendaLiquida() {
		if(cafeRmRendaLiquida == null) return BigDecimal.ZERO;
		return cafeRmRendaLiquida;
	}

	public BigDecimal getCafeRmRenda() {
		if(cafeRmRenda == null) return BigDecimal.ZERO;
		return cafeRmRenda;
	}

	public BigDecimal getCafeRmRendaEscolha() {
		if(cafeRmRendaEscolha == null) return BigDecimal.ZERO;
		return cafeRmRendaEscolha;
	}

	public BigDecimal getCafeRmRendaTotal() {
		if(cafeRmRendaTotal == null) return BigDecimal.ZERO;
		return cafeRmRendaTotal;
	}

	public BigDecimal getPercentualPeneira14Acima() {
		if(percentualPeneira14Acima == null) return BigDecimal.ZERO;
		return percentualPeneira14Acima;
	}

	public BigDecimal getPercentualBandinha() {
		if(percentualBandinha == null) return BigDecimal.ZERO;
		return percentualBandinha;
	}

	public BigDecimal getRendaSemDescontoDpi() {
		if(rendaSemDescontoDpi == null) return BigDecimal.ZERO;
		return rendaSemDescontoDpi;
	}

	public BigDecimal getTabelaValorRecepcao() {
		if(tabelaValorRecepcao == null) return BigDecimal.ZERO;
		return tabelaValorRecepcao;
	}

	public BigDecimal getValorCalculadoRecepcao() {
		if(valorCalculadoRecepcao == null) return BigDecimal.ZERO;
		return valorCalculadoRecepcao;
	}

	public BigDecimal getQtdRecepcao() {
		if(qtdRecepcao == null) return BigDecimal.ZERO;
		return qtdRecepcao;
	}

	public BigDecimal getValorCalculadoSecagem() {
		if(valorCalculadoSecagem == null) return BigDecimal.ZERO;
		return valorCalculadoSecagem;
	}

	public BigDecimal getQtdSecagem() {
		if(qtdSecagem == null) return BigDecimal.ZERO;
		return qtdSecagem;
	}

	public BigDecimal getValorPonto() {
		if(valorPonto == null) return BigDecimal.ZERO;
		return valorPonto;
	}

	public BigDecimal getPercentualDpi() {
		if(percentualDpi == null) return BigDecimal.ZERO;
		return percentualDpi;
	}

	public BigDecimal getValorRecepcao() {
		if(valorRecepcao == null) return BigDecimal.ZERO;
		return valorRecepcao;
	}

	public BigDecimal getFnt() {
		if(fnt == null) return BigDecimal.ZERO;
		return fnt;
	}
	
	public Integer getDensidade() {
		if(densidade == null) return 0;
		return densidade;
	}

	public static RePendenteItemDto construir(RePendenteItem obj) {
		var objDto = new RePendenteItemDto();
		BeanUtils.copyProperties(obj, objDto);
		
		if(obj.getProduto() != null) {
			objDto.setProdutoCodigo(obj.getProduto().getCodItem());
			objDto.setProdutoDescricao(obj.getProduto().getDescItem());
		}
		
		return objDto;
	}

	public static List<RePendenteItemDto> construir(List<RePendenteItem> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList(); 
		return objs.stream().map(rePendenteItem -> {
			return construir(rePendenteItem);
		}).toList();
	}
}

package br.coop.integrada.api.pa.domain.model.rependente;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "re_pendente_item")
public class RePendenteItem extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_re_pendente", nullable = false)
	private RePendente rePendente;

    @ManyToOne
    @JoinColumn(name = "id_produto")
	private Produto produto;
    
    @Column(name = "referencia")
	private String referencia;
	
	@Column(name = "peso_liquido")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal pesoLiquido;
	
	@Column(name = "ph_entrada")
	@Digits(integer = 3, fraction = 2)
	private BigInteger phEntrada;
	
	@Column(name = "impureza")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal impureza;
	
	@Column(name = "umidade")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal umidade;
	
	@Column(name = "chuvado_avariado")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal chuvadoAvariado;
	
	@Column(name = "tbm")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal tbm;
	
	@Column(name = "tipo")
	private Integer tipo;
	
	@Column(name = "qtd_cafe_escolha")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal qtdCafeEscolha;
	
	@Column(name = "defeitos")
	private Integer defeitos;
	
	@Column(name = "qualidade_produto_normal")
	private Boolean qualidadeProdutoNormal;
	
	@Column(name = "qualidade_produto_sementeira")
	private Boolean qualidadeProdutoSementeira;
	
	@Column(name = "qualidade_produto_terra")
	private Boolean qualidadeProdutoTerra;
	
	@Column(name = "qualidade_produto_vagem")
	private Boolean qualidadeProdutoVagem;
	
	@Column(name = "desmembramento")
	private Boolean desmembramento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unidade_desmembramento")
	private UnidadeDesmembramento unidadeDesmembramento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_desmembramento")
	private TipoDesmembramentoEnum tipoDesmembramento;
	
	@Column(name = "bebida")
	private String bebida;
	
	@Column(name = "lote")
	private String lote;
	
	@Column(name = "renda_liquida")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal rendaLiquida;
	
	@Column(name = "ph_corrigido")
	private BigInteger phCorrigido;
	
	@Column(name = "percentual_desconto_impureza")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal percentualDescontoImpureza;
	
	@Column(name = "percentual_desconto_umidade")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal percentualDescontoUmidade;
	
	@Column(name = "percentual_desconto_chuvado_avariado")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal percentualDescontoChuvadoAvariado;
	
	@Column(name = "qtd_desconto_impureza")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtdDescontoImpureza;
	
	@Column(name = "qtd_desconto_umidade")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtdDescontoUmidade;
	
	@Column(name = "qtd_desconto_chuvado_avariado")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtdDescontoChuvadoAvariado;
	
	@Column(name = "qtd_tbm")
	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtdTbm;
	
	@Column(name = "valor_fiscal")
	@Digits(integer = 6, fraction = 9)
	private BigDecimal valorFiscal;
	
	@Column(name = "tabela_umidade_taxa_secagem_kg")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal tabelaUmidadeTaxaSecagemKg;
	
	@Column(name = "tabela_umidade_taxa_secagem_valor")
	@Digits(integer = 8, fraction = 5)
	private BigDecimal tabelaUmidadeTaxaSecagemValor;
	
	@Column(name = "cafe_rm_tipo")
	private Integer cafeRmTipo;
	
	@Column(name = "cafe_rm_bebida")
	private String cafeRmBebida;
	
	@Column(name = "cafe_rm_cafe_escolha")
	@Digits(integer = 8, fraction = 5)
	private BigDecimal cafeRmCafeEscolha;
	
	@Column(name = "cafe_rm_renda_liquida")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal cafeRmRendaLiquida;
	
	@Column(name = "cafe_rm_defeito")
	private Integer cafeRmDefeito;
	
	@Column(name = "cafe_rm_codigo_referencia")
	private String cafeRmCodigoReferencia;
	
	@Column(name = "cafe_rm_renda")
	@Digits(integer = 3, fraction = 3)
	private BigDecimal cafeRmRenda;
	
	@Column(name = "cafe_rm_renda_escolha")
	@Digits(integer = 3, fraction = 3)
	private BigDecimal cafeRmRendaEscolha;
	
	@Column(name = "cafe_rm_renda_total")
	@Digits(integer = 3, fraction = 3)
	private BigDecimal cafeRmRendaTotal;
	
	@Column(name = "percentual_peneira_14_acima")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal percentualPeneira14Acima;
	
	@Column(name = "percentual_bandinha")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal percentualBandinha;
	
	@Column(name = "renda_sem_desconto_dpi")
	@Digits(integer = 12, fraction = 4)
	private BigDecimal rendaSemDescontoDpi;
	
	@Column(name = "tabela_valor_recepcao")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal tabelaValorRecepcao;
	
	@Column(name = "valor_calculado_recepcao")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal valorCalculadoRecepcao;
	
	@Column(name = "qtd_recepcao")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal qtdRecepcao;
	
	@Column(name = "valor_calculado_secagem")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal valorCalculadoSecagem;
	
	@Column(name = "qtd_secagem")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal qtdSecagem;
	
	@Column(name = "valor_ponto")
	@Digits(integer = 8, fraction = 9)
	private BigDecimal valorPonto;
	
	@Column(name = "controle_semente")
	private String controleSemente;
	
	@Column(name = "indice_dpi")
	private Boolean indiceDpi;
	
	@Column(name = "percentual_dpi")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal percentualDpi;
	
	@Column(name = "observacao_dpi")
	private String observacaoDpi;
	
	@Column(name = "tera_sub_produto")
	private Boolean teraSubProduto;
	
	@Column(name = "familia_sub_produto")
	private String familiaSubProduto;
	
	@Column(name = "item_sub_produto")
	private String itemSubProduto;
	
	@Column(name = "indice_secagem_limpeza")
	private Boolean indiceSecagemLimpeza;
	
	@Column(name = "tipo_cobranca_secagem")
	private String tipoCobrancaSecagem;
	
	@Column(name = "item_para_secagem")
	private String itemParaSecagem;
	
	@Column(name = "incide_taxa_recepcao")
	private Boolean incideTaxaRecepcao;
	
	@Column(name = "valor_recepcao")
	@Digits(integer = 6, fraction = 9)
	private BigDecimal valorRecepcao;

	@Digits(integer = 4, fraction = 2)
	@Column(name="fnt")
	private BigDecimal fnt;
	
	@Column(name = "densidade", columnDefinition = "NUMBER(10,0) default 0")
	private Integer densidade;
	
	public Boolean getQualidadeProdutoNormal() {
	    if(qualidadeProdutoNormal == null) return false;
	    return qualidadeProdutoNormal;
	}

	public Boolean getQualidadeProdutoSementeira() {
	    if(qualidadeProdutoSementeira == null) return false;
	    return qualidadeProdutoSementeira;
	}

	public Boolean getQualidadeProdutoTerra() {
	    if(qualidadeProdutoTerra == null) return false;
	    return qualidadeProdutoTerra;
	}

	public Boolean getQualidadeProdutoVagem() {
	    if(qualidadeProdutoVagem == null) return false;
	    return qualidadeProdutoVagem;
	}

	public Boolean getDesmembramento() {
	    if(desmembramento == null) return false;
	    return desmembramento;
	}

	public Boolean getIndiceDpi() {
	    if(indiceDpi == null) return false;
	    return indiceDpi;
	}

	public Boolean getTeraSubProduto() {
	    if(teraSubProduto == null) return false;
	    return teraSubProduto;
	}

	public Boolean getIndiceSecagemLimpeza() {
	    if(indiceSecagemLimpeza == null) return false;
	    return indiceSecagemLimpeza;
	}

	public Boolean getIncideTaxaRecepcao() {
	    if(incideTaxaRecepcao == null) return false;
	    return incideTaxaRecepcao;
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
}

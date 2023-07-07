package br.coop.integrada.api.pa.domain.model.recEntrega;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "rec_entrega_item")
public class RecEntregaItem extends AbstractEntity implements Cloneable{
    
	private static final long serialVersionUID = 1L;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rec_entrega", nullable = false)
	private RecEntrega recEntrega;
		
	@Column(name = "cod_estabel")
	private String codEstabel;

	@Column(name="nr_re")
	private Long nrRe; 
	
    @Column(name = "cod_produto")
	private String codProduto;

	@Column(name="it_codigo")
	private String itCodigo;

	@Column(name="cod_refer")
	private String codRefer;

	@Digits(integer = 7, fraction = 4)
	@Column(name="peso_liquido")
	private BigDecimal pesoLiquido;

	@Column(name="ph_entrada")
	@Digits(integer = 3, fraction = 2)
	private BigInteger phEntrada;

	@Column(name="ph_corrigido")
	private BigInteger phCorrigido;

	@Digits(integer = 3, fraction = 2)
	@Column(name="impureza")
	private BigDecimal impureza;

	@Digits(integer = 7, fraction = 4)
	@Column(name="per_desc_impur")
	private BigDecimal perDescImpur;

	@Digits(integer = 7, fraction = 4)
	@Column(name="qt_desc_impur")
	private BigDecimal qtDescImpur;

	@Digits(integer = 3, fraction = 2)
	@Column(name="umidade")
	private BigDecimal umidade;

	@Digits(integer = 3, fraction = 2)
	@Column(name="per_desc_umid")
	private BigDecimal perDescUmid;

	@Digits(integer = 7, fraction = 4)
	@Column(name="qt_desc_umid")
	private BigDecimal qtDescUmid;

	@Digits(integer = 3, fraction = 2)
	@Column(name="chuv_avar")
	private BigDecimal chuvAvar;

	@Digits(integer = 3, fraction = 2)
	@Column(name="per_desc_chuv")
	private BigDecimal perDescChuv;

	@Digits(integer = 7, fraction = 4)
	@Column(name="qt_desc_chuv")
	private BigDecimal qtDescChuv;

	@Digits(integer = 7, fraction = 4)
	@Column(name="tbm")
	private BigDecimal tbm;

	@Digits(integer = 7, fraction = 4)
	@Column(name="qt_tbm")
	private BigDecimal qtTbm;

	@Column(name="tipo")
	private Integer tipo;

	@Column(name="bebida")
	private String bebida;

	@Digits(integer = 6, fraction = 2)
	@Column(name="cafe_escolha")
	private BigDecimal cafeEscolha;

	@Digits(integer = 7, fraction = 4)
	@Column(name="renda_liquida")
	private BigDecimal rendaLiquida;

	@Column(name="defeitos")
	private Integer defeitos; 

	@Column(name="normal")
	private Boolean normal;

	@Column(name="sementeira")
	private Boolean sementeira;

	@Column(name="terra")
	private Boolean terra;

	@Column(name="vagem")
	private Boolean vagem;

	@Column(name="lote")
	private String lote;

	@Digits(integer = 7, fraction = 4)
	@Column(name="renda_liquida_atu")
	private BigDecimal rendaLiquidaAtu;

	@Digits(integer = 7, fraction = 4)
	@Column(name="peso_liquido_atu")
	private BigDecimal pesoLiquidoAtu;

	@Digits(integer = 6, fraction = 9)
	@Column(name="vl_fiscal")
	private BigDecimal vlFiscal;

	@Digits(integer = 3, fraction = 2)
	@Column(name="tab_tx_sec_kg")
	private BigDecimal tabTxSecKg;

	@Column(name="tab_tx_sec_vl")
	@Digits(integer = 8, fraction = 5)
	private BigDecimal tabTxSecVl;

	@Column(name="caferr_tipo")
	private Integer caferrTipo;

	@Column(name="caferr_bebida")
	private String caferrBebida;

	@Digits(integer = 6, fraction = 2)
	@Column(name="caferr_cafe_escolha")
	private BigDecimal caferrCafeEscolha;

	@Digits(integer = 7, fraction = 4)
	@Column(name="caferr_renda_liquida")
	private BigDecimal caferrRendaLiquida;

	@Column(name="caferm_tipo")
	private Integer cafermTipo;

	@Column(name="caferm_bebida")
	private String cafermBebida;

	@Digits(integer = 6, fraction = 2)
	@Column(name="caferm_cafe_escolha")
	private BigDecimal cafermCafeEscolha;

	@Digits(integer = 7, fraction = 4)
	@Column(name="caferm_renda_liquida")
	private BigDecimal cafermRendaLiquida;

	@Column(name="caferm_Defeitos")
	private Integer cafermDefeitos;

	@Column(name="caferrDefeitos")
	private Integer caferrDefeitos;

	@Column(name="caferm_cod_refer")
	private String cafermCodRefer;

	@Column(name="caferr_cod_refer")
	private String caferrCodRefer;

	@Digits(integer = 3, fraction = 3)
	@Column(name="caferm_rendasc_liq")
	private BigDecimal cafermRendascLiq;

	@Digits(integer = 3, fraction = 3)
	@Column(name="caferm_rendasc_esc")
	private BigDecimal cafermRendascEsc;

	@Digits(integer = 3, fraction = 3)
	@Column(name="caferm_rendasc_tot")
	private BigDecimal cafermRendascTot;

	@Digits(integer = 5, fraction = 2)
	@Column(name="per_pen14_acima")
	private BigDecimal perPen14Acima;

	@Column(name="seq_nota_enc")
	private Integer seqNotaEnc;

	@Digits(integer = 3, fraction = 2)
	@Column(name="perc_bandinha")
	private BigDecimal percBandinha;

	@Digits(integer = 12, fraction = 4)
	@Column(name="renda_sem_desc_dpi")
	private BigDecimal rendaSemDescDpi;

	@Digits(integer = 12, fraction = 4)
	@Column(name="qt_alocada")
	private BigDecimal qtAlocada;

	@Digits(integer = 8, fraction = 9)
	@Column(name="tab_vl_recepcao")
	private BigDecimal tabVlRecepcao;

	@Digits(integer = 8, fraction = 9)
	@Column(name="vl_recepcao")
	private BigDecimal vlRecepcao;

	@Digits(integer = 8, fraction = 9)
	@Column(name="qt_recepcao")
	private BigDecimal qtRecepcao;

	@Digits(integer = 8, fraction = 9)
	@Column(name="vl_secagem")
	private BigDecimal vlSecagem;

	@Digits(integer = 7, fraction = 9)
	@Column(name="qt_secagem")
	private BigDecimal qtSecagem;

	@Digits(integer = 8, fraction = 9)
	@Column(name="vl_ponto")
	private BigDecimal vlPonto;

	@Column(name="seq_item_docum")
	private Integer seqItemDocum;

	@Column(name="dt_ult_int")
	private Date dtUltInt;

	@Column(name="hr_ult_int")
	private String hrUltInt;

	@Column(name="log_integrado")
	private Boolean logIntegrado;

	@Digits(integer = 4, fraction = 2)
	@Column(name="fnt")
	private BigDecimal fnt;
	
	@Column(name = "densidade", columnDefinition = "NUMBER(10,0) default 0")
	private Integer densidade;
	
	@Override 
	public Object clone() throws CloneNotSupportedException { 
		return super.clone();
	}

	public Long getNrRe() {
		if(nrRe == null) return 0l;
		return nrRe;
	}
	
	public BigDecimal getPesoLiquido() {
	    if(pesoLiquido == null) return BigDecimal.ZERO;
	    return pesoLiquido;
	}

	public BigDecimal getImpureza() {
	    if(impureza == null) return BigDecimal.ZERO;
	    return impureza;
	}

	public BigDecimal getPerDescImpur() {
	    if(perDescImpur == null) return BigDecimal.ZERO;
	    return perDescImpur;
	}

	public BigDecimal getQtDescImpur() {
	    if(qtDescImpur == null) return BigDecimal.ZERO;
	    return qtDescImpur;
	}

	public BigDecimal getUmidade() {
	    if(umidade == null) return BigDecimal.ZERO;
	    return umidade;
	}

	public BigDecimal getPerDescUmid() {
	    if(perDescUmid == null) return BigDecimal.ZERO;
	    return perDescUmid;
	}

	public BigDecimal getQtDescUmid() {
	    if(qtDescUmid == null) return BigDecimal.ZERO;
	    return qtDescUmid;
	}

	public BigDecimal getChuvAvar() {
	    if(chuvAvar == null) return BigDecimal.ZERO;
	    return chuvAvar;
	}

	public BigDecimal getPerDescChuv() {
	    if(perDescChuv == null) return BigDecimal.ZERO;
	    return perDescChuv;
	}

	public BigDecimal getQtDescChuv() {
	    if(qtDescChuv == null) return BigDecimal.ZERO;
	    return qtDescChuv;
	}

	public BigDecimal getTbm() {
	    if(tbm == null) return BigDecimal.ZERO;
	    return tbm;
	}

	public BigDecimal getQtTbm() {
	    if(qtTbm == null) return BigDecimal.ZERO;
	    return qtTbm;
	}

	public BigDecimal getCafeEscolha() {
	    if(cafeEscolha == null) return BigDecimal.ZERO;
	    return cafeEscolha;
	}

	public BigDecimal getRendaLiquida() {
	    if(rendaLiquida == null) return BigDecimal.ZERO;
	    return rendaLiquida;
	}

	public BigDecimal getRendaLiquidaAtu() {
	    if(rendaLiquidaAtu == null) return BigDecimal.ZERO;
	    return rendaLiquidaAtu;
	}

	public BigDecimal getPesoLiquidoAtu() {
	    if(pesoLiquidoAtu == null) return BigDecimal.ZERO;
	    return pesoLiquidoAtu;
	}

	public BigDecimal getVlFiscal() {
	    if(vlFiscal == null) return BigDecimal.ZERO;
	    return vlFiscal;
	}

	public BigDecimal getTabTxSecKg() {
	    if(tabTxSecKg == null) return BigDecimal.ZERO;
	    return tabTxSecKg;
	}

	public BigDecimal getTabTxSecVl() {
	    if(tabTxSecVl == null) return BigDecimal.ZERO;
	    return tabTxSecVl;
	}

	public BigDecimal getCaferrCafeEscolha() {
	    if(caferrCafeEscolha == null) return BigDecimal.ZERO;
	    return caferrCafeEscolha;
	}

	public BigDecimal getCaferrRendaLiquida() {
	    if(caferrRendaLiquida == null) return BigDecimal.ZERO;
	    return caferrRendaLiquida;
	}

	public BigDecimal getCafermCafeEscolha() {
	    if(cafermCafeEscolha == null) return BigDecimal.ZERO;
	    return cafermCafeEscolha;
	}

	public BigDecimal getCafermRendaLiquida() {
	    if(cafermRendaLiquida == null) return BigDecimal.ZERO;
	    return cafermRendaLiquida;
	}

	public BigDecimal getCafermRendascLiq() {
	    if(cafermRendascLiq == null) return BigDecimal.ZERO;
	    return cafermRendascLiq;
	}

	public BigDecimal getCafermRendascEsc() {
	    if(cafermRendascEsc == null) return BigDecimal.ZERO;
	    return cafermRendascEsc;
	}

	public BigDecimal getCafermRendascTot() {
	    if(cafermRendascTot == null) return BigDecimal.ZERO;
	    return cafermRendascTot;
	}

	public BigDecimal getPerPen14Acima() {
	    if(perPen14Acima == null) return BigDecimal.ZERO;
	    return perPen14Acima;
	}

	public BigDecimal getPercBandinha() {
	    if(percBandinha == null) return BigDecimal.ZERO;
	    return percBandinha;
	}

	public BigDecimal getRendaSemDescDpi() {
	    if(rendaSemDescDpi == null) return BigDecimal.ZERO;
	    return rendaSemDescDpi;
	}

	public BigDecimal getQtAlocada() {
	    if(qtAlocada == null) return BigDecimal.ZERO;
	    return qtAlocada;
	}

	public BigDecimal getTabVlRecepcao() {
	    if(tabVlRecepcao == null) return BigDecimal.ZERO;
	    return tabVlRecepcao;
	}

	public BigDecimal getVlRecepcao() {
	    if(vlRecepcao == null) return BigDecimal.ZERO;
	    return vlRecepcao;
	}

	public BigDecimal getQtRecepcao() {
	    if(qtRecepcao == null) return BigDecimal.ZERO;
	    return qtRecepcao;
	}

	public BigDecimal getVlSecagem() {
	    if(vlSecagem == null) return BigDecimal.ZERO;
	    return vlSecagem;
	}

	public BigDecimal getQtSecagem() {
	    if(qtSecagem == null) return BigDecimal.ZERO;
	    return qtSecagem;
	}

	public BigDecimal getVlPonto() {
	    if(vlPonto == null) return BigDecimal.ZERO;
	    return vlPonto;
	}
	
	public BigInteger getPhEntrada() {
		if(phEntrada == null) return BigInteger.ZERO;
		return phEntrada;
	}
	
	public BigInteger getPhCorrigido() {
		if(phCorrigido == null) return BigInteger.ZERO;
		return phCorrigido;
	}
	
	public Boolean getNormal() {
	    if(normal == null) return false;
	    return normal;
	}

	public Boolean getSementeira() {
	    if(sementeira == null) return false;
	    return sementeira;
	}

	public Boolean getTerra() {
	    if(terra == null) return false;
	    return terra;
	}

	public Boolean getVagem() {
	    if(vagem == null) return false;
	    return vagem;
	}

	public Boolean getLogIntegrado() {
	    if((logIntegrado == null || logIntegrado == false) && this.getDataIntegracao() != null) {
	    	return true;
	    }
	    else if(logIntegrado == null) {
	    	return false;
	    }
	    
	    return logIntegrado;
	}

	public BigDecimal getFnt() {
		if(fnt == null) return BigDecimal.ZERO;
		return fnt;
	}
	
	public Integer getTipo() {
	    if(tipo == null) return 0;
	    return tipo;
	}

	public Integer getDefeitos() {
	    if(defeitos == null) return 0;
	    return defeitos;
	}

	public Integer getCaferrTipo() {
	    if(caferrTipo == null) return 0;
	    return caferrTipo;
	}

	public Integer getCafermTipo() {
	    if(cafermTipo == null) return 0;
	    return cafermTipo;
	}

	public Integer getCafermDefeitos() {
	    if(cafermDefeitos == null) return 0;
	    return cafermDefeitos;
	}

	public Integer getCaferrDefeitos() {
	    if(caferrDefeitos == null) return 0;
	    return caferrDefeitos;
	}

	public Integer getSeqNotaEnc() {
	    if(seqNotaEnc == null) return 0;
	    return seqNotaEnc;
	}

	public Integer getSeqItemDocum() {
	    if(seqItemDocum == null) return 0;
	    return seqItemDocum;
	}
	
	public Integer getDensidade() {
		if(densidade == null) return 0;
		return densidade;
	}

	public Date getDtUltInt() {
		if(dtUltInt == null) {
			return this.getDataIntegracao();
		}
		
		return dtUltInt;
	}
	
	public String getHrUltInt() {
		if(hrUltInt == null && this.getDataIntegracao() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			return formatter.format(this.getDataIntegracao());
		}
		
		return hrUltInt;
	}
}

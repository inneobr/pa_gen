package br.coop.integrada.api.pa.domain.model.recEntrega;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "rec_entrega")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RecEntrega extends AbstractEntity implements Cloneable{
	
	private static final long serialVersionUID = 1L;

	
	@Column(name="cod_estabel")
	private String codEstabel;
	
	@Column(name="nr_re")
	private Long nrRe;
	
	@Column(name="log_tem_saldo")
	private Boolean logTemSaldo;
	
	@Column(name="dt_emissao")
	private Date dtEmissao;
	
	@Column(name="log_nfe")
	private Boolean logNfe;
	
	@Column(name="nat_ent_armaz")
	private String natEntArmaz;
	
	@Column(name="serie")
	private String serie;
	
	@Column(name="nr_nota_fis")
	private String nrNotaFis;
	
	@Column(name="nf_pj")
	private Boolean nfPj;
	
	@Column(name="nr_re_opcao")
	private Integer nrReOpcao;
	
	@Column(name="cod_unid_parc")
	private Integer codUnidParc;
	
	@Column(name="cod_est_off_line")
	private String codEstOffLine;
	
	@Column(name="placa")
	private String placa;
	
	@Column(name="fm_codigo")
	private String fmCodigo;
	
	@Column(name="safra")
	private Integer safra;
	
	@Column(name="cod_emitente")
	private String codEmitente;
	
	@Column(name="nome_prod")
	private String nomeProd;
	
    @Temporal(TemporalType.DATE)
	@Column(name="dt_entrada")
	private Date dtEntrada;
	
	@Column(name="hr_entrada")
	private String hrEntrada;
	
	@Column(name="matricula")
	private Long matricula;
	
	@Column(name="nr_doc_pes")
	private Integer nrDocPes;
	
	@Column(name="motorista")
	private String motorista;
	
	@Column(name="tulha")
	private String tulha;
	
	@Column(name="impresso")
	private Boolean impresso;
	
	@Column(name="nr_ord_campo")
	private Integer nrOrdCampo;
	
	@Column(name="nr_laudo")
	private Long nrLaudo;
	
	@Column(name="nr_cont_sem")
	private String nrContSem;
		
	@Column(name="classe")
	private String classe;
	
	@Column(name="observacoes", columnDefinition = "CLOB")
	private String observacoes;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="peso_bruto")
	private BigDecimal pesoBruto;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="tara_veiculo")
	private BigDecimal taraVeiculo;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="tara_sacaria")
	private BigDecimal taraSacaria;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="peso_liquido")
	private BigDecimal pesoLiquido;
	
	@Column(name="tipo_rr")
	private String tipoRr;
	
	@Column(name="tipo_gmo")
	private String tipoGmo;
	
	@Column(name="cdn_repres")
	private String cdnRepres;
	
	@Column(name="prod_padr")
	private Boolean prodPadr;
	
	@Column(name="descar_unid")
	private Boolean descarUnid;
	
	@Column(name="safra_compos")
	private String safraCompos;
	
	@Column(name="nome_safra_compos")
	private String nomeSafraCompos;
	
	@Column(name="nr_cadpro")
	private Long nrCadPro;
	
	@Column(name="nr_nf_prod")
	private Long nrNfProd;
	
	@Column(name="ser_nf_prod")
	private String serNfProd;
	
	@Column(name="dt_nf_prod")
	private Date dtNfProd;
	
	@Column(name="nr_aut_transf")
	private Long nrAutTransf;
	
	@Column(name="ubs_log_liberado")
	private Boolean ubsLogLiberado;
	
	@Column(name="ubs_log_requisitado")
	private Boolean ubsLogRequisitado;
	
	@Column(name="ubs_data_requisicao")
	private Date ubsDataRequisicao;
	
	@Column(name="ubs_hora_requisicao")
	private String ubsHoraRequisicao;
	
	@Column(name="nr_bcqs")
	private Long nrBcqs;
		
	@Column(name="cod_regional")
	private String codRegional;
	
	@Column(name="nat_docum")
	private String natDocum;
	
	@Column(name="serie_docum")
	private String serieDocum;
	
	@Column(name="nro_docum")
	private String nroDocum;
	
	@Column(name="re_devolvido")
	private Boolean reDevolvido;
	
	@Column(name="nr_ord_prod")
	private Long nrOrdProd;
	
	@Column(name="entrada_manual")
	private Boolean entradaManual;
	
	@Column(name="hr_saida")
	private String hrSaida;
	
	@Column(name="cod_sit")
	private Integer codSit;
	
	@Column(name="nr_re_orig_dfx")
	private Integer nrReOrigDfx;
	
	@Column(name="motivo_bloqueio")
	private String motivoBloqueio;
	
	@Column(name="beneficiado")
	private Boolean beneficiado;
	
	@Column(name="tipo_renda_cfe")
	private String tipoRendaCafe;
	
	@Column(name="notif_portal")
	private Boolean notifPortal;
	
	@Column(name="entrada_rr")
	private String entradaRr;
	
	@Column(name="kit_cobrado")
	private Boolean kitCobrado;
	
	@Column(name="com_cobranca")
	private Boolean comCobranca;
	
	@Column(name="log_tx_sp_retida")
	private Boolean logTxSpRetida;
	
	@Column(name="nr_re_tx_sp_retida")
	private Long nrReTxSpRetida;
	
	@Column(name="re_bloqueado")
	private Boolean reBloqueado;
	
	@Column(name="re_disponivel")
	private Boolean reDisponivel;
	
	@Column(name="nr_pri_ent")
	private Long nrPriEnt;
	
	@Column(name="log_Re_dpi")
	private Boolean logReDpi;
	
	@Column(name="log_bloq_dpi")
	private Boolean logBloqDpi;
	
	@Column(name="nr_re_dpi")
	private Long nrReDpi;
	
	@Digits(integer = 9, fraction = 2)
	@Column(name="peso_liq_sem_desc_dpi")
	private BigDecimal pesoLiqSemDescDpi;
	
	@Column(name="nr_re_parcial_dpi")
	private Long nrReParcialDpi;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="qt_dpi")
	private BigDecimal qtdDpi;
	
	@Digits(integer = 5, fraction = 2)
	@Column(name="per_dpi")
	private BigDecimal perDpi;
	
	@Column(name="re_origem")
	private String origemRe;
	
	@Column(name="nr_re_orig")
	private Long nrReOrig;
	
	@Column(name="nr_re_pai")
	private Long nrRePai;
	
	@Column(name="pj_nro_nota")
	private String pjNroNota;
	
	@Column(name="pj_serie")
	private String pjSerie;
	
    @Temporal(TemporalType.DATE)
	@Column(name="pj_dt_emissao")
	private Date pjDtEmissao;
	
	@Column(name="pj_vl_tot_nota")
	private BigDecimal pjVlTotNota;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="pj_qt_tot_nota")
	private BigDecimal pjQtTotNota;
	
	@Column(name="Pj_qt_entrada")
	private Integer pjQtEntrada;
	
	@Column(name="pj_chave_acesso")
	private String pjChaveAcesso;
	
	@Column(name="pj_log_nota_propria")
	private Boolean pjLogNotaPropria;
	
	@Column(name="pj_nat_oper")
	private String pjNatOper;
	
	@Enumerated(EnumType.STRING)
	@Column(name="natureza")
	private NaturezaEnum natureza;
	
	@Column(name="dt_ult_int")
	private Date dtUltInt;
	
	@Column(name="hr_ult_int")
	private String hrUltInt;
	
	@Column(name="log_integrado")
	private Boolean logIntegrado;
	
	@Column(name="sequencia")
	private Long sequencia;
	
	@Column(name="situacao_nfe")
	private String situacaoNfe;
	
	@Column(name="chave_acesso_nfe")
	private String chaveAcessoNfe;
	
	@Column(name = "pendencias_fiscais", columnDefinition = "NUMBER(1,0) default 0")
	private Boolean pendenciasFiscais;

	@JsonManagedReference
	@OneToMany(mappedBy = "recEntrega", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<RecEntregaItem> itens = new ArrayList<>();
	
	@Override 
	public Object clone() throws CloneNotSupportedException { 
		return super.clone();
	}
	
	public Boolean getLogTemSaldo() {
	    if(logTemSaldo == null) return false;
	    return logTemSaldo;
	}

	public Boolean getLogNfe() {
	    if(logNfe == null) return false;
	    return logNfe;
	}

	public Boolean getNfPj() {
	    if(nfPj == null) return false;
	    return nfPj;
	}

	public Boolean getImpresso() {
	    if(impresso == null) return false;
	    return impresso;
	}

	public Boolean getProdPadr() {
	    if(prodPadr == null) return false;
	    return prodPadr;
	}

	public Boolean getDescarUnid() {
	    if(descarUnid == null) return false;
	    return descarUnid;
	}

	public Boolean getUbsLogLiberado() {
	    if(ubsLogLiberado == null) return false;
	    return ubsLogLiberado;
	}

	public Boolean getUbsLogRequisitado() {
	    if(ubsLogRequisitado == null) return false;
	    return ubsLogRequisitado;
	}

	public Boolean getReDevolvido() {
	    if(reDevolvido == null) return false;
	    return reDevolvido;
	}

	public Boolean getEntradaManual() {
	    if(entradaManual == null) return false;
	    return entradaManual;
	}

	public Boolean getBeneficiado() {
	    if(beneficiado == null) return false;
	    return beneficiado;
	}

	public Boolean getNotifPortal() {
	    if(notifPortal == null) return false;
	    return notifPortal;
	}

	public Boolean getKitCobrado() {
	    if(kitCobrado == null) return false;
	    return kitCobrado;
	}

	public Boolean getComCobranca() {
	    if(comCobranca == null) return false;
	    return comCobranca;
	}

	public Boolean getLogTxSpRetida() {
	    if(logTxSpRetida == null) return false;
	    return logTxSpRetida;
	}

	public Boolean getReBloqueado() {
	    if(reBloqueado == null) return false;
	    return reBloqueado;
	}

	public Boolean getReDisponivel() {
	    if(reDisponivel == null) return false;
	    return reDisponivel;
	}

	public Boolean getLogReDpi() {
	    if(logReDpi == null) return false;
	    return logReDpi;
	}

	public Boolean getLogBloqDpi() {
	    if(logBloqDpi == null) return false;
	    return logBloqDpi;
	}

	public Boolean getPjLogNotaPropria() {
	    if(pjLogNotaPropria == null) return false;
	    return pjLogNotaPropria;
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
	
	public Boolean getPendenciasFiscais() {
		if(pendenciasFiscais == null) return false;
		return pendenciasFiscais;
	}
	
	public BigDecimal getPesoBruto() {
	    if(pesoBruto == null) return BigDecimal.ZERO;
	    return pesoBruto;
	}

	public BigDecimal getTaraVeiculo() {
	    if(taraVeiculo == null) return BigDecimal.ZERO;
	    return taraVeiculo;
	}

	public BigDecimal getTaraSacaria() {
	    if(taraSacaria == null) return BigDecimal.ZERO;
	    return taraSacaria;
	}

	public BigDecimal getPesoLiquido() {
	    if(pesoLiquido == null) return BigDecimal.ZERO;
	    return pesoLiquido;
	}

	public BigDecimal getPesoLiqSemDescDpi() {
	    if(pesoLiqSemDescDpi == null) return BigDecimal.ZERO;
	    return pesoLiqSemDescDpi;
	}

	public BigDecimal getQtdDpi() {
	    if(qtdDpi == null) return BigDecimal.ZERO;
	    return qtdDpi;
	}

	public BigDecimal getPerDpi() {
	    if(perDpi == null) return BigDecimal.ZERO;
	    return perDpi;
	}

	public BigDecimal getPjVlTotNota() {
	    if(pjVlTotNota == null) return BigDecimal.ZERO;
	    return pjVlTotNota;
	}

	public BigDecimal getPjQtTotNota() {
	    if(pjQtTotNota == null) return BigDecimal.ZERO;
	    return pjQtTotNota;
	}

	public Long getNrRe() {
	    if(nrRe == null) return 0l;
	    return nrRe;
	}

	public Long getNrAutTransf() {
	    if(nrAutTransf == null) return 0l;
	    return nrAutTransf;
	}

	public Long getNrOrdProd() {
	    if(nrOrdProd == null) return 0l;
	    return nrOrdProd;
	}

	public Long getNrReTxSpRetida() {
	    if(nrReTxSpRetida == null) return 0l;
	    return nrReTxSpRetida;
	}

	public Long getNrPriEnt() {
	    if(nrPriEnt == null) return 0l;
	    return nrPriEnt;
	}

	public Long getNrReDpi() {
	    if(nrReDpi == null) return 0l;
	    return nrReDpi;
	}

	public Long getNrReParcialDpi() {
	    if(nrReParcialDpi == null) return 0l;
	    return nrReParcialDpi;
	}

	public Long getNrReOrig() {
	    if(nrReOrig == null) return 0l;
	    return nrReOrig;
	}

	public Long getNrRePai() {
	    if(nrRePai == null) return 0l;
	    return nrRePai;
	}

	public Long getSequencia() {
	    if(sequencia == null) return 0l;
	    return sequencia;
	}
	
	public String getPlaca() {
		if(placa == null) return "";
		return placa.toUpperCase();
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

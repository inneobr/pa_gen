package br.coop.integrada.api.pa.domain.model.rependente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "re_pendente",  
uniqueConstraints = {@UniqueConstraint(name = "key_un_re_pendente" , columnNames = { "id_estabelecimento", "placa", "id_grupo_produto", "id_produtor", "nome_prod" }) })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RePendente extends AbstractEntity{
private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "id_estabelecimento", nullable = false)
	private Estabelecimento estabelecimento;
	
	@Column(name="placa", nullable = false)
	private String placa;
	
	@ManyToOne
	@JoinColumn(name = "id_grupo_produto", nullable = false)
	private GrupoProduto grupoProduto;
	
	@ManyToOne
	@JoinColumn(name = "id_produtor", nullable = false)
	private Produtor produtor;
	
	@Column(name="nome_prod")
	private String nomeProd;
	
	@ManyToOne
	@JoinColumn(name = "id_imovel")
	private Imovel imovel;
	
	@Column(name="dt_entrada")
	private Date dtEntrada;
	
	@Column(name="hr_entrada")
	private String hrEntrada;
	
	@Column(name="matricula")
	private Long matricula;
	
	@Column(name="nr_doc_pes")
	private Integer nrDocPes;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="peso_bruto")
	private BigDecimal pesoBruto;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="tara_veiculo")
	private BigDecimal taraVeiculo;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="tara_sacaria")
	private BigDecimal taraSacaria;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="peso_liquido")
	private BigDecimal pesoLiquido;
	
	@Column(name="motorista")
	private String motorista;	

	@Column(name="tulha")
	private String tulha;
	
	@Column(name="nr_ord_campo")
	private Integer nrOrdCampo;
	
	@Column(name="nr_laudo")
	private Long nrLaudo;
	
	@Column(name="observacoes", columnDefinition = "CLOB")
	private String observacoes;
	
	@Column(name="tp_re")
	private Integer tpRe;
	
	@Column(name="nr_re_orig")
	private Integer nrReOrig;
	
	@Column(name="nr_re_pai")
	private Integer nrRePai;
	
	@ManyToOne
	@JoinColumn(name = "id_classe")
	private SementeClasse classe;
	
	@Column(name="cod_sit")
	private Integer codSit;
	
	@Digits(integer = 9, fraction = 2)
	@Column(name="nr_aut_transf")
	private BigDecimal nrAutTransf;
	
	@Column(name="tipo_rr")
	private String tipoRr;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="desc_credito")
	private BigDecimal descCredito;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="qt_depi")
	private BigDecimal qtDepi;
	
	@Digits(integer = 3, fraction = 2)
	@Column(name="per_depi")
	private BigDecimal perDepi;
	
	@Column(name="kit_cobrado")
	private Boolean kitCobrado;
	
	@Column(name="entrada_rr")
	private String entradaRr;
	
	@Column(name="com_cobranca")
	private Boolean comCobranca;
	
	@Column(name="nr_re_orig_dfx")
	private Integer nrReOrigDfx;
	
	@Column(name="re_disponivel")
	private Boolean reDisponivel;
	
	@Column(name="tipo_renda_cfe")
	private String tipoRendaCfe;
	
	@Column(name="dt_emissao_ori")
	private Date dtEmissaoOri;
	
	@Column(name="entrada_manual")
	private Boolean entradaManual;
	
	@Column(name="nr_cont_sem")
	private String nrContSem;
	
	@Column(name="safra_compos")
	private String safraCompos;
	
	@Column(name="nome_safra_compos")
	private String nomeSafraCompos;
	
	@ManyToOne
	@JoinColumn(name = "tipo_gmo")
	private TipoGmo tipoGmo;
	
	@Column(name="nr_pri_ent")
	private Integer nrPriEnt;
	
	@Column(name="cdn_repres")
	private Integer cdnRepres;	

	@Column(name="log_Re_dpi")
	private Boolean logReDpi;
	
	@Column(name="log_bloq_dpi")
	private Boolean logBloqDpi;
	
	@Column(name="nr_re_dpi")
	private Integer nrReDpi;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="peso_liq_sem_desc_dpi")
	private BigDecimal pesoLiqSemDescDpi;
	
	@Column(name="nr_nf_prod")
	private Long nrNfProd;
	
	@Column(name="ser_nf_prod")
	private String serNfProd;
	
	@Column(name="dt_nf_prod")
	private Date dtNfProd;
	
	@Column(name="prod_padr")
	private Boolean prodPadr;
	
	@Column(name="descar_unid")
	private Boolean descarUnid;
	
	@Column(name="log_tx_sp_retida")
	private Boolean logTxSpRetida;
	
	@Column(name="nr_re_tx_sp_retida")
	private Long nrReTxSpRetida;
	
	@Column(name="pj_nro_nota")
	private String pjNroNota;
	
	@Column(name="pj_serie")
	private String pjSerie;
	
	@Column(name="pj_dt_emissao")
	private Date pjDtEmissao;
	
	@Digits(integer = 9, fraction = 2)
	@Column(name="pj_vl_tot_nota")
	private BigDecimal pjVlTotNota;
	
	@Digits(integer = 9, fraction = 3)
	@Column(name="pj_qt_tot_nota")
	private BigDecimal pjQtTotNota;
	
	@Column(name="pj_chave_acesso")
	private String pjChaveAcesso;
	
	@Column(name="pj_log_nota_propria")
	private Boolean pjLogNotaPropria;
	
	@Column(name="pj_nat_oper")
	private String pjNatOper;
	
	@OneToMany(mappedBy = "rePendente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RePendenteItem> itens = new ArrayList<>();
	
	@OneToMany(mappedBy = "rePendente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RePendenteDesmembramento> desmembramentos = new ArrayList<>();
	
	public Boolean getKitCobrado() {
	    if(kitCobrado == null) return false;
	    return kitCobrado;
	}

	public Boolean getComCobranca() {
	    if(comCobranca == null) return false;
	    return comCobranca;
	}

	public Boolean getReDisponivel() {
	    if(reDisponivel == null) return false;
	    return reDisponivel;
	}

	public Boolean getEntradaManual() {
	    if(entradaManual == null) return false;
	    return entradaManual;
	}

	public Boolean getLogReDpi() {
	    if(logReDpi == null) return false;
	    return logReDpi;
	}

	public Boolean getLogBloqDpi() {
	    if(logBloqDpi == null) return false;
	    return logBloqDpi;
	}

	public Boolean getProdPadr() {
	    if(prodPadr == null) return false;
	    return prodPadr;
	}

	public Boolean getDescarUnid() {
	    if(descarUnid == null) return false;
	    return descarUnid;
	}

	public Boolean getLogTxSpRetida() {
	    if(logTxSpRetida == null) return false;
	    return logTxSpRetida;
	}

	public Boolean getPjLogNotaPropria() {
	    if(pjLogNotaPropria == null) return false;
	    return pjLogNotaPropria;
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
	
	public BigDecimal getNrAutTransf() {
	    if(nrAutTransf == null) return BigDecimal.ZERO;
	    return nrAutTransf;
	}
	
	public BigDecimal getDescCredito() {
	    if(descCredito == null) return BigDecimal.ZERO;
	    return descCredito;
	}
	
	public BigDecimal getQtDepi() {
	    if(qtDepi == null) return BigDecimal.ZERO;
	    return qtDepi;
	}
	
	public BigDecimal getPerDepi() {
	    if(perDepi == null) return BigDecimal.ZERO;
	    return perDepi;
	}
	
	public BigDecimal getPesoLiqSemDescDpi() {
	    if(pesoLiqSemDescDpi == null) return BigDecimal.ZERO;
	    return pesoLiqSemDescDpi;
	}
	
	public BigDecimal getPjVlTotNota() {
	    if(pjVlTotNota == null) return BigDecimal.ZERO;
	    return pjVlTotNota;
	}
	
	public BigDecimal getPjQtTotNota() {
	    if(pjQtTotNota == null) return BigDecimal.ZERO;
	    return pjQtTotNota;
	}
}

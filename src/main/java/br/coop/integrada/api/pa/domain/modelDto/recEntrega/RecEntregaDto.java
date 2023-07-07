package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReDto;
import lombok.Data;


@Data
public class RecEntregaDto {
	
	
	private String codEstabel;
	
	private Long nrRe;
	
	private String idRe;
	
	private Boolean logTemSaldo;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtEmissao;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dataIntegracao;
	
	private Boolean logNfe;
	
	private String natEntArmaz;
	
	private String serie;
	
	private String nrNotaFis;
	
	private Boolean nfPj;
	
	private Integer nrReOpcao;
	
	private Integer codUnidParc;
	
	private String codEstOffLine;
	
	private String placa;
	
	private String fmCodigo;
	
	private Integer safra;
	
	private Integer codEmitente;
	
	private String nomeProd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtEntrada;
	
	private String hrEntrada;
	
	private Long matricula;
	
	private Integer nrDocPes;
	
	private String motorista;
	
	private String tulha;
	
	private Boolean impresso;
	
	private Integer nrOrdCampo;
	
	private Long nrLaudo;
	
	private String nrContSem;
	
	private Long classe;
	
	private String observacoes;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal pesoBruto;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal taraVeiculo;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal taraSacaria;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal pesoLiquido;
	
	private String tipoRr;
	
	private String tipoGmo;
	
	private String cdnRepres;
	
	private Boolean prodPadr;
	
	private Boolean descarUnid;
	
	private String safraCompos;
	
	private String nomeSafraCompos;
	
	private String nrCadPro;
	
	private Long nrNfProd;
	
	private String serNfProd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtNfProd;
	
	private Long nrAutTransf;
	
	private Boolean ubsLogLiberado;
	
	private Boolean ubsLogRequisitado;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date ubsDataRequisicao;
	
	private String ubsHoraRequisicao;
	
	private String nrBcqs;
	
	private String codRegional;
	
	private String natDocum;
	
	private String serieDocum;
	
	private String nroDocum;
	
	private Boolean reDevolvido;
	
	private Long nrOrdProd;
	
	private Boolean entradaManual;
	
	private String hrSaida;
	
	private Integer codSit;
	
	private Integer nrReOrigDfx;
	
	private String motivoBloqueio;
	
	private Boolean beneficiado;
	
	private String tipoRendaCafe;
	
	private Boolean notifPortal;
	
	private String entradaRr;
	
	private Boolean kitCobrado;
	
	private Boolean comCobranca;
	
	private Boolean logTxSpRetida;
	
	private Long nrReTxSpRetida;
	
	private Boolean reBloqueado;
	
	private Boolean reDisponivel;
	
	private Long nrPriEnt;
	
	private Boolean logReDpi;
	
	private Boolean logBloqDpi;
	
	private Long nrReDpi;
	
	private BigDecimal pesoLiqSemDescDpi;
	
	private Long nrReParcialDpi;
	
	private Long qtdDpi;
	
	private BigDecimal perDpi;
	
	private String origemRe;
	
	private Long nrReOrig;
	
	private Long nrRePai;
	
	private String pjNroNota;
	
	private String pjSerie;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date pjDtEmissao;
	
	private BigDecimal pjVlTotNota;
	
	private BigDecimal pjQtTotNota;
	
	private Integer pjQtEntrada;
	
	private String pjChaveAcesso;
	
	private Boolean pjLogNotaPropria;
	
	private String pjNatOper;
	
	private NaturezaEnum natureza;
	
	private Boolean logIntegrado;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtUltInt;
	
	private String hrUltInt;
	
	private OperacaoEnum operacao;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String chaveAcessoNfe;
		
	private List<RecEntregaItemDto> itemRecEntrega;
	
	private List<MovimentoReDto> movRe;
	
	private List<BaixaCreditoDto> bxaCredito;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public List<RecEntregaItemDto> getItemRecEntrega() {
		if(CollectionUtils.isEmpty(itemRecEntrega)) return Collections.emptyList();
		return itemRecEntrega;
	}
	
	public List<MovimentoReDto> getMovRe() {
		if(CollectionUtils.isEmpty(movRe)) return Collections.emptyList();
		return movRe;
	}
	
	public List<BaixaCreditoDto> getBxaCredito() {
		if(CollectionUtils.isEmpty(bxaCredito)) return Collections.emptyList();
		return bxaCredito;
	}
	
	public String getCodEstabel() {
	    if(codEstabel == null) return "";
	    return codEstabel;
	}

	public String getIdRe() {
	    if(idRe == null) return "";
	    return idRe;
	}

	public String getNatEntArmaz() {
	    if(natEntArmaz == null) return "";
	    return natEntArmaz;
	}

	public String getSerie() {
	    if(serie == null) return "";
	    return serie;
	}

	public String getNrNotaFis() {
	    if(nrNotaFis == null) return "";
	    return nrNotaFis;
	}

	public String getCodEstOffLine() {
	    if(codEstOffLine == null) return "";
	    return codEstOffLine;
	}

	public String getPlaca() {
	    if(placa == null) return "";
	    return placa;
	}

	public String getFmCodigo() {
	    if(fmCodigo == null) return "";
	    return fmCodigo;
	}

	public String getNomeProd() {
	    if(nomeProd == null) return "";
	    return nomeProd;
	}

	public String getHrEntrada() {
	    if(hrEntrada == null) return "";
	    return hrEntrada;
	}

	public String getMotorista() {
	    if(motorista == null) return "";
	    return motorista;
	}

	public String getTulha() {
	    if(tulha == null) return "";
	    return tulha;
	}

	public String getNrContSem() {
	    if(nrContSem == null) return "";
	    return nrContSem;
	}

	public String getObservacoes() {
	    if(observacoes == null) return "";
	    return observacoes;
	}

	public String getTipoRr() {
	    if(tipoRr == null) return "";
	    return tipoRr;
	}

	public String getTipoGmo() {
	    if(tipoGmo == null) return "";
	    return tipoGmo;
	}

	public String getCdnRepres() {
	    if(cdnRepres == null) return "";
	    return cdnRepres;
	}

	public String getSafraCompos() {
	    if(safraCompos == null) return "";
	    return safraCompos;
	}

	public String getNomeSafraCompos() {
	    if(nomeSafraCompos == null) return "";
	    return nomeSafraCompos;
	}

	public String getNrCadPro() {
	    if(nrCadPro == null) return "";
	    return nrCadPro;
	}

	public String getSerNfProd() {
	    if(serNfProd == null) return "";
	    return serNfProd;
	}

	public String getUbsHoraRequisicao() {
	    if(ubsHoraRequisicao == null) return "";
	    return ubsHoraRequisicao;
	}

	public String getNrBcqs() {
	    if(nrBcqs == null) return "";
	    return nrBcqs;
	}

	public String getCodRegional() {
	    if(codRegional == null) return "";
	    return codRegional;
	}

	public String getNatDocum() {
	    if(natDocum == null) return "";
	    return natDocum;
	}

	public String getSerieDocum() {
	    if(serieDocum == null) return "";
	    return serieDocum;
	}

	public String getNroDocum() {
	    if(nroDocum == null) return "";
	    return nroDocum;
	}

	public String getHrSaida() {
	    if(hrSaida == null) return "";
	    return hrSaida;
	}

	public String getMotivoBloqueio() {
	    if(motivoBloqueio == null) return "";
	    return motivoBloqueio;
	}

	public String getTipoRendaCafe() {
	    if(tipoRendaCafe == null) return "";
	    return tipoRendaCafe;
	}

	public String getEntradaRr() {
	    if(entradaRr == null) return "";
	    return entradaRr;
	}

	public String getPjNroNota() {
	    if(pjNroNota == null) return "";
	    return pjNroNota;
	}

	public String getPjSerie() {
	    if(pjSerie == null) return "";
	    return pjSerie;
	}

	public String getPjChaveAcesso() {
	    if(pjChaveAcesso == null) return "";
	    return pjChaveAcesso;
	}

	public String getPjNatOper() {
	    if(pjNatOper == null) return "";
	    return pjNatOper;
	}

	public String getHrUltInt() {
	    if(hrUltInt == null) return "";
	    return hrUltInt;
	}
	
	public Long getNrRe() {
	    if(nrRe == null) return 0L;
	    return nrRe;
	}

	public Long getMatricula() {
	    if(matricula == null) return 0L;
	    return matricula;
	}

	public Long getNrLaudo() {
	    if(nrLaudo == null) return 0L;
	    return nrLaudo;
	}

	public Long getClasse() {
	    if(classe == null) return 0L;
	    return classe;
	}

	public Long getNrNfProd() {
	    if(nrNfProd == null) return 0L;
	    return nrNfProd;
	}

	public Long getNrAutTransf() {
	    if(nrAutTransf == null) return 0L;
	    return nrAutTransf;
	}

	public Long getNrOrdProd() {
	    if(nrOrdProd == null) return 0L;
	    return nrOrdProd;
	}

	public Long getNrReTxSpRetida() {
	    if(nrReTxSpRetida == null) return 0L;
	    return nrReTxSpRetida;
	}

	public Long getNrPriEnt() {
	    if(nrPriEnt == null) return 0L;
	    return nrPriEnt;
	}

	public Long getNrReDpi() {
	    if(nrReDpi == null) return 0L;
	    return nrReDpi;
	}

	public Long getNrReParcialDpi() {
	    if(nrReParcialDpi == null) return 0L;
	    return nrReParcialDpi;
	}

	public Long getQtdDpi() {
	    if(qtdDpi == null) return 0L;
	    return qtdDpi;
	}

	public String getOrigemRe() {
	    if(origemRe == null) return "";
	    return origemRe.toUpperCase();
	}
	
	public Long getNrReOrig() {
	    if(nrReOrig == null) return 0L;
	    return nrReOrig;
	}

	public Long getNrRePai() {
	    if(nrRePai == null) return 0L;
	    return nrRePai;
	}
	
	public Integer getNrReOpcao() {
	    if(nrReOpcao == null) return 0;
	    return nrReOpcao;
	}

	public Integer getCodUnidParc() {
	    if(codUnidParc == null) return 0;
	    return codUnidParc;
	}

	public Integer getSafra() {
	    if(safra == null) return 0;
	    return safra;
	}

	public Integer getCodEmitente() {
	    if(codEmitente == null) return 0;
	    return codEmitente;
	}

	public Integer getNrDocPes() {
	    if(nrDocPes == null) return 0;
	    return nrDocPes;
	}

	public Integer getNrOrdCampo() {
	    if(nrOrdCampo == null) return 0;
	    return nrOrdCampo;
	}

	public Integer getCodSit() {
	    if(codSit == null) return 0;
	    return codSit;
	}

	public Integer getNrReOrigDfx() {
	    if(nrReOrigDfx == null) return 0;
	    return nrReOrigDfx;
	}

	public Integer getPjQtEntrada() {
	    if(pjQtEntrada == null) return 0;
	    return pjQtEntrada;
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
	    if(logIntegrado == null) return false;
	    return logIntegrado;
	}
	
	public static RecEntregaDto construir(RecEntrega recEntrega) {
		RecEntregaDto recEntregaDto = new RecEntregaDto();
		
		if(recEntrega != null) {
			BeanUtils.copyProperties(recEntrega, recEntregaDto);
			recEntregaDto.setIdRe(recEntrega.getId().toString());
			recEntregaDto.setOperacao(OperacaoEnum.WRITE);
			
			if(recEntrega.getCodEmitente() != null) {
				recEntregaDto.setCodEmitente(Integer.parseInt(recEntrega.getCodEmitente()));
			}
			
			if(recEntrega.getNrCadPro() != null) {
				recEntregaDto.setNrCadPro(recEntrega.getNrCadPro().toString());
			}
			
			if(recEntrega.getNrBcqs() != null) {
				recEntregaDto.setNrBcqs(recEntrega.getNrBcqs().toString());
			}
			
			List<RecEntregaItemDto> itens = RecEntregaItemDto.construir(recEntrega.getItens());
			recEntregaDto.setItemRecEntrega(itens);
		}
		
		return recEntregaDto;
	}
	
	public static RecEntregaDto construir(
			RecEntrega recEntrega,
			List<MovimentoRe> movimentoReList,
			BaixaCredito baixaCredito,
			BaixaCreditoMov baixaCreditoMov) {
		
		RecEntregaDto recEntregaDto = null;
		
		if(recEntrega != null) {
			recEntregaDto = RecEntregaDto.construir(recEntrega);
			recEntregaDto.adicionarMovimentoRe(movimentoReList);
			recEntregaDto.adicionarBxaCredito(baixaCredito, baixaCreditoMov);
		}
		
		return recEntregaDto;
		
	}
	
	public void adicionarMovimentoRe(List<MovimentoRe> movimentoReList) {
		
		this.movRe = new ArrayList<>();
		
		if(movimentoReList != null) {
			for (MovimentoRe movimentoRe : movimentoReList) {
				MovimentoReDto dto = MovimentoReDto.construir(movimentoRe);
				dto.setOperacao(OperacaoEnum.WRITE);
				this.movRe.add(dto);
			}
			
		}
		
	}
	
	public void adicionarBxaCredito(BaixaCredito baixaCredito, BaixaCreditoMov baixaCreditoMov) {
		
		this.bxaCredito = new ArrayList<>();
		
		if(baixaCredito != null) {
			BaixaCreditoDto baixaCreditoDto = BaixaCreditoDto.construir(baixaCredito, baixaCreditoMov);
			baixaCreditoDto.setOperacao(OperacaoEnum.WRITE);
			this.bxaCredito.add(baixaCreditoDto);
			
		}
		
	}
}

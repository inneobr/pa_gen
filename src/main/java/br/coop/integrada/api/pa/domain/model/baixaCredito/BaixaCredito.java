package br.coop.integrada.api.pa.domain.model.baixaCredito;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "baixa_credito")
public class BaixaCredito  extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "cod_estabel")
    private String codEstabel;
	
	@Column(name = "nr_re", nullable = false)
    private Long nrRe;
	
	@Column(name = "id_re")
    private Long idRe;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "dt_emi_re", nullable = false)
    private Date dtEmiRe;
	
	private String horaEmiteRe;
	
	private String cpfProdutor;
	
	@Column(name = "qt_para_bxa")
	private BigDecimal qtParaBxa;
	
	@Column(name = "sld_para_bxa")
	private BigDecimal sldParaBxa;
	
	@Column(name = "situacao")
	private String situacao;
	
	@Column(name = "qt_dpi")
	private BigDecimal qtDpi;
	
	@Column(name = "cod_emitente")
	private String codEmitente;
	
	@Column(name = "nr_agendamento")
	private String nrAgendamento;
	
	@Column(name = "safra")
	private Integer safra;
	
	@Column(name = "cod_regional")
	private Long codRegional;
	
	@Column(name = "id_lote")
	private BigDecimal idLote;
	
	@Column(name = "bayer_id_consumo")
	private String bayerIdConsumo;
		
	@Column(name = "bayer_bid")
	private	String bayerBid;
	
	@Column(name = "bayer_qt_baixada")
	private BigDecimal bayerQtBaixada;

	@Column(name="log_integrado")
	private Boolean logIntegrado;

	public Long getNrRe() {
		if(nrRe == null) return 0l;
		return nrRe;
	}
	
	public BigDecimal getQtParaBxa() {
	    if(qtParaBxa == null) return BigDecimal.ZERO;
	    return qtParaBxa;
	}

	public BigDecimal getSldParaBxa() {
	    if(sldParaBxa == null) return BigDecimal.ZERO;
	    return sldParaBxa;
	}

	public BigDecimal getQtDpi() {
	    if(qtDpi == null) return BigDecimal.ZERO;
	    return qtDpi;
	}

	public BigDecimal getIdLote() {
	    if(idLote == null) return BigDecimal.ZERO;
	    return idLote;
	}

	public BigDecimal getBayerQtBaixada() {
	    if(bayerQtBaixada == null) return BigDecimal.ZERO;
	    return bayerQtBaixada;
	}
	
	public Boolean getLogIntegrado() {
		if(logIntegrado == null) return false;
		return logIntegrado;
	}
}

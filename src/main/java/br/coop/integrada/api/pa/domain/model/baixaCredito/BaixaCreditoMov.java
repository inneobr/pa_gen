package br.coop.integrada.api.pa.domain.model.baixaCredito;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "baixa_credito_mov")
public class BaixaCreditoMov extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "cod_estabel")
	@NotNull(message = "O campo {códido estabelecimento} não pode ser nulo!")
    private String codEstabel;
	
	@Column(name = "nr_re")
    private Long nrRe;
	
	@NotNull(message = "O campo {id re} não pode ser nulo!")
	@Column(name = "id_re")
    private Long idRe;	
	
	@Column(name = "id_movto_bxa_cred")
    private String idMovtoBxaCred;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "dt_movto", nullable = false)
    private Date dtMovto;
	
	@Column(name = "hr_Movto")
    private String hrMovto;
	
	@NotNull(message = "O campo {códido usuário} não pode ser nulo!")
	@Column(name = "cod_usuario")
    private String codUsuario;
	
	@Column(name = "transacao")
    private String transacao;
	
	@Column(name = "observacao")
    private String observacao;
	
	@Column(name = "id_transacao")
    private BigDecimal idTransacao;
	
	@Column(name = "cod_autentic_transacao")
    private String codAutenticTransacao;
	
	@Column(name = "qtdade")
    private BigDecimal qtdade;
	
	@Column(name = "log_integrado")
	private Boolean logIntegrado;

	public Long getNrRe() {
		if(nrRe == null) return 0l;
		return nrRe;
	}
	
	public BigDecimal getIdTransacao() {
	    if(idTransacao == null) return BigDecimal.ZERO;
	    return idTransacao;
	}

	public BigDecimal getQtdade() {
	    if(qtdade == null) return BigDecimal.ZERO;
	    return qtdade;
	}
	
	public Boolean getLogIntegrado() {
		if(logIntegrado == null) return false;
		return logIntegrado;
	}
}

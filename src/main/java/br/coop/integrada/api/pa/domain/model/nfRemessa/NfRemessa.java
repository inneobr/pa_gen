package br.coop.integrada.api.pa.domain.model.nfRemessa;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "nf_remessa")
public class NfRemessa extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Column(name="cod_estabel")
	private String codEstabel;

	
	@Column(name="id_rec_entrega")
	private Long idRecEntrega;
	
	@Column(name="nr_re")
	private Long nrRe;
	
	@Column(name="funcaoNota")
	private String funcaoNota;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable = false)
	private StatusNotaFiscalEnum status;
	
	@Column(name="serieDocto")
	private String serieDocto;
	
	@Column(name="nrDocto")
	private String nrDocto;
	
	@Column(name="natOperacao")
	private String natOperacao;
	
	@Column(name="quantidade", nullable = false)
	@Digits(integer = 9, fraction = 4)
	private BigDecimal quantidade;
	
	@Column(name="dtCriacao")
	private Date dtCriacao;
	
	@Column(name="hrCriacao")
	private String hrCriacao;
	
	@Column(name="dtUltMov")
	private Date dtUltMov;
	
	@Column(name="hrUltMov")
	private String hrUltMov;
	
	@Column(name="seq_item")
	private Integer seqItem;
	
	@Column(name="cod_refer")
	private String codRefer;
	
	@Column(name = "pendencias_fiscais", columnDefinition = "NUMBER(1,0) default 0")
	private Boolean pendenciasFiscais;
	
	@Column(name = "info_nf_produtor_manual", columnDefinition = "NUMBER(1,0) default 0")
	private Boolean infoNfProdutorManual;
	
	public String getDescricaoStatus() {
		if(status != null) {
			return status.getDescricao();
		}else {
			return null;
		}
	}
	
	public Boolean getPendenciasFiscais() {
		if(pendenciasFiscais == null) return false;
		return pendenciasFiscais;
	}
	 
}

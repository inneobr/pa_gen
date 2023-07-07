package br.coop.integrada.api.pa.domain.model.movtoPesagem;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "movto_pesagem")
public class MovtoPesagem extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne()
	@NotNull(message = "Campo { estabelecimento } obrigatório")
    private Estabelecimento estabelecimento;
	
	@Column(name = "safra", nullable = false)
    private Integer safra;
	
	@Column(name = "nro_doc_pesagem", nullable = false)
    private Integer nroDocPesagem;
	
	@Column(name = "nro_movto", nullable = false)
    private Integer nroMovto;
	
	@Column(name = "id_movto_pesagem", nullable = false)  	
    private String idMovtoPesagem;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dt_movto", nullable = false)
    private Date dtMovto;
	
	@Column(name = "serie_docto")
    private String serieDocto;
	
	@Column(name = "nro_docto")
    private String nroDocto;
	
	@Column(name = "cod_emitente")
    private String codEmitente;
	
	@Column(name = "nat_operacao")
    private String natOperacao;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name = "peso_movto")
	private BigDecimal pesoMovto;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name = "renda_movto")
	private BigDecimal rendaMovto;

	@Column(name = "operacao")
    private String operacao;
	
	@Column(name = "movto")
    private String movto;
	
	@Column(name = "estornado")
    private boolean estornado;
	
	@Column(name = "nr_re")
    private Long nrRe;
}

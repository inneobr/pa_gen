package br.coop.integrada.api.pa.domain.model.rependente;

import java.math.BigDecimal;
import java.sql.Date;

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
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import lombok.Data;

@Data
@Entity
@Table(name = "re_pendente_desmembramento")
public class RePendenteDesmembramento extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_re_pendente", nullable = false)
	private RePendente rePendente;
	
	@ManyToOne
	@JoinColumn(name = "id_produtor", nullable = false)
	private Produtor produtorFavorecido;
	
	@Column(name="nome_produtor_favorecido")
	private String produtorFavorecidoNome;
	
	@Digits(integer = 3, fraction = 4)
	@Column(name = "percentual")
	private BigDecimal percentual;
	
	@Digits(integer = 8, fraction = 4)
	@Column(name = "quantidade_quilos")
	private BigDecimal quantidadeQuilos;
	
	@Digits(integer = 8, fraction = 4)
	@Column(name = "quantidade_sacas")
	private BigDecimal quantidadeSacas;
	
	@Column(name = "cobra_kit")
	private boolean cobraKit;

	@ManyToOne
	@JoinColumn(name = "id_imovel")
	private Imovel imovel;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transferir_pela")
	private TipoDesmembramentoEnum transferirPela;
	
	@Column(name = "nr_nf_prod")
	private Long nfProdutor;
	
	@Column(name = "serie_nf_prod")
	private String serieNfProdutor;
	
	@Column(name = "dt_nf_prod")
	private Date dataNfProdutor;
	
	public BigDecimal getPercentual() {
		if(percentual == null) return BigDecimal.ZERO;
		return percentual;
	}

	public BigDecimal getQuantidadeQuilos() {
		if(quantidadeQuilos == null) return BigDecimal.ZERO;
		return quantidadeQuilos;
	}

	public BigDecimal getQuantidadeSacas() {
		if(quantidadeSacas == null) return BigDecimal.ZERO;
		return quantidadeSacas;
	}
}

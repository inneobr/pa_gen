package br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.StatusMovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "transacao_re_mov")
public class TransacaoReMov extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transacao_re", nullable = false)
	private TransacaoRe transacaoRe;
	
	@Column(name="cod_estabel", nullable = false)
	private String codEstabel;
	
	
	@Column(name="id_re", nullable = false)
	private Long idRe;
	
	
	@Column(name="nr_re", nullable = false)
	private Long nrRe;
	
	
	@Column(name="data_hora_movimento", nullable = false)
	private Date dataHoraMovimento;
	
	@Column(name="mensagem")
	private String mensagem;
	
	
	@NotNull(message = "Campo {movimento} é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name="movimento", nullable = false)
	private MovimentoReEnum movimento;
	
	@Transient
	public String getDescricaoMovimento()
	{
		if(this.movimento != null)
			return this.movimento.getDescricao();
		
		return null;
	}
	
	
	@NotNull(message = "Campo {statusMovimento} é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name="status_movimento", nullable = false)
	private StatusMovimentoReEnum statusMovimento;
	
	@Transient
	public String getDescricaoStatusMovimento()
	{
		if(this.statusMovimento != null)
			return this.statusMovimento.getDescricao();
		
		return null;
	}
	
	
}

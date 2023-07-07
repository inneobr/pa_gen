package br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.StatusMovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "transacao_re")
public class TransacaoRe extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Column(name="cod_estabel")
	private String codEstabel;
	
	@Column(name="id_re")
	private Long idRe;
	
	@Column(name="nr_re")
	private Long nrRe;
	
	//Status Atual:
	@NotNull(message = "Campo {movimento} é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name="movimento_atual", nullable = false)
	private MovimentoReEnum movimentoAtual;
	
	@Transient
	public String getDescricaoMovimentoAtual()
	{
		if(this.movimentoAtual != null)
			return this.movimentoAtual.getDescricao();
		
		return null;
	}
	
	
	@NotNull(message = "Campo {statusMovimento} é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name="status_movimento_atual", nullable = false)
	private StatusMovimentoReEnum statusMovimentoAtual;
	
	@Transient
	public String getDescricaoStatusMovimentoAtual()
	{
		if(this.statusMovimentoAtual != null)
			return this.statusMovimentoAtual.getDescricao();
		
		return null;
	}
	
	@Column(name="data_ultimo_movimento", nullable = false)
	private Date dataUltimoMovimento;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "transacaoRe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransacaoReMov> transacaoReMov = new ArrayList<>();
	
	
}

package br.coop.integrada.api.pa.domain.model.naturezaOperacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "natureza_operacao_movimentacao")
public class NaturezaOperacaoMovimentacao extends AbstractEntity{
	private static final long serialVersionUID = 1L;	
	
	@Column(name = "id_unico", unique = true)
	private String idUnico;
	
	@Column(name = "cod_grupo")
	private Integer codGrupo;		

	@Column(name = "id_suario")
	private String usuario;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;
	
	@Column(name = "hora")
	private String hora;
	
	@Lob
	@Column(name = "movimento")
	private String movimento;
	
	@Lob
	@Column(name = "observacao")
	private String observacao;
}

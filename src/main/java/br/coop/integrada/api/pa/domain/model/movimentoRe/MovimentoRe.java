package br.coop.integrada.api.pa.domain.model.movimentoRe;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
        name = "movimento_re",
        uniqueConstraints = {
                @UniqueConstraint(name = "KEY_MOVIMENTO_RE", columnNames = {"cod_estabel", "id_re", "id_mov_re"})
        }
)
public class MovimentoRe extends AbstractEntity{
	
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "Campo {idRe} obrigatório")
	@Column(name = "id_re", nullable = false)
	private Long idRe;

	@Column(name = "id_mov_re")
	private String idMovRe;
    
    @NotBlank(message = "Campo {codEstabel} obrigatório")
	@Column(name = "cod_estabel", nullable = false)
	private String codEstabel;
    
    @Column(name="nr_re")
	private Long nrRe;
    
    @NotNull(message = "Campo {data} obrigatório")
    @Temporal(TemporalType.DATE)
	@Column(name = "data")
    private Date data;
	
	@Column(name="hora", length = 8)
	private String hora;
	
	@Column(name="transacao")
	private String transacao;
	
	@Digits(integer = 9, fraction = 4)
	@Column(name="quantidade")
	private BigDecimal quantidade;
	
	@Column(name="observacao", length = 800)
	private String observacao;
	
	@Column(name="usuario")
	private String usuario;
	
    @Temporal(TemporalType.DATE)
	@Column(name = "dt_ult_int")
    private Date dtUltInt;
	
	@Column(name="hr_ult_int", length = 8)
	private String hrUltInt;
	
	@Column(name="log_integrado")
	private Boolean logIntegrado;	
	
	public String getOrigemRe() {
		if(idMovRe != null) {
			String origem = idMovRe.substring(0, 3);
			
			if(origem.equalsIgnoreCase("DTS")) {
				return "DATASUL";
			}
		}
		
		return "GENESIS";
	}

	public Long getNrRe() {
		if(nrRe == null) return 0l;
		return nrRe;
	}
	
	public BigDecimal getQuantidade() {
		if(quantidade == null) return BigDecimal.ZERO;
		return quantidade;
	}
	
	public Boolean getLogIntegrado() {
		if(logIntegrado == null) return false;
		return logIntegrado;
	}
}

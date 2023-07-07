package br.coop.integrada.api.pa.domain.model.movimentoDiario;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
        name = "movimento_diario",
        uniqueConstraints = {
                @UniqueConstraint(name = "KEY_ID_UNICO_MOVIMENTO_DIARIO", columnNames = {"cod_estabel", "dt_movto"})
        }
)
public class MovimentoDiario extends AbstractEntity{
	
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "Campo {codEstabel} obrigatório")
	@Column(name = "cod_estabel", nullable = false)
	private String codEstabel;
    
    @NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;
    
    @NotNull(message = "Campo {dtMovto} obrigatório")
    @Temporal(TemporalType.DATE)
	@Column(name = "dt_movto")
    private Date dtMovto;

    @NotNull(message = "Campo {movtoFech} obrigatório")
    @Column(name = "movto_fech")
    private Boolean movtoFech;
    
    @Transient
    private Date paramEstabDtMovto;
    
    

}

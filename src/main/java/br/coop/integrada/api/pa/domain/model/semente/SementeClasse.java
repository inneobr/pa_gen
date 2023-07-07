package br.coop.integrada.api.pa.domain.model.semente;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "semente_classe")
public class SementeClasse extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "codigo", nullable = false)
    private Long codigo;

    @Column(name = "descricao", length = 30, nullable = false)
    private String descricao;
}

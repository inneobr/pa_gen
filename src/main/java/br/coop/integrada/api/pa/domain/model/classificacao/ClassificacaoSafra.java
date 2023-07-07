package br.coop.integrada.api.pa.domain.model.classificacao;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classificacao_safra")
public class ClassificacaoSafra extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classificacao", nullable = false)
    private Classificacao classificacao;

	@ComparaObjeto
    @Column(name = "safra", nullable = false)
    private Integer safra;
}

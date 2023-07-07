package br.coop.integrada.api.pa.domain.model.classificacao;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v_classificacao_estabelecimento")
public class ClassificacaoEstabelecimento extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classificacao", nullable = false)
    private Classificacao classificacao;

	@ComparaObjeto
    @ManyToOne
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

}

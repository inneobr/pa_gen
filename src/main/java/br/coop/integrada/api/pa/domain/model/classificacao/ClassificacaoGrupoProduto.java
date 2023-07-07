package br.coop.integrada.api.pa.domain.model.classificacao;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
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
@Table(name = "v_classificacao_grupo_produto")
public class ClassificacaoGrupoProduto extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classificacao", nullable = false)
    private Classificacao classificacao;

	@ComparaObjeto
    @ManyToOne
    @JoinColumn(name = "id_grupo_produto", nullable = false)
    private GrupoProduto grupoProduto;

    /*@JoinTable(name = "v_classificacao_grupo_produto_estab",
            joinColumns = {@JoinColumn(name = "id_classificacao_grupo_produto") },
            inverseJoinColumns = { @JoinColumn(name = "id_estabelecimento") })
    @ManyToMany
    private List<Estabelecimento> estabelecimentos;*/

}

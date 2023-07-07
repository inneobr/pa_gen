package br.coop.integrada.api.pa.domain.model.classificacao;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classificacao")
public class Classificacao extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "id_tipo_classificacao", nullable = false)
    private TipoClassificacao tipoClassificacao;

	@ComparaObjeto
    @Column(name = "descricao", unique = true, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "classificacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassificacaoDetalhe> detalhes;

    @OneToMany(mappedBy = "classificacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassificacaoSafra> safras;

    @OneToMany(mappedBy = "classificacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassificacaoGrupoProduto> grupoProdutos;
    
    @OneToMany(mappedBy = "classificacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassificacaoEstabelecimento> estabelecimentos;
       
    
}

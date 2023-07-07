package br.coop.integrada.api.pa.domain.model.semente.produtor;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(
        name = "semente_campo_produtor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "key_semente_campo_produtor",
                        columnNames = { "safra", "nr_ord_campo", "id_produtor", "id_estabelecimento", "id_grupo_produto", "id_classe" }
                )
        }
)
public class SementeCampoProdutor extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    
	@NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;

    @Column(name = "safra")
    private Integer safra;

    @Column(name = "nr_ord_campo")
    private Integer ordemCampo;

    @ManyToOne
    @JoinColumn(name = "id_produtor", nullable = false)
    private Produtor produtor;

    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

    @ManyToOne
    @JoinColumn(name = "id_grupo_produto")
    private GrupoProduto grupoProduto;

    @ManyToOne
    @JoinColumn(name = "id_classe", nullable = false)
    private SementeClasse classe;

    public String getCodigoEstabelecimento() {
        if(estabelecimento != null) {
            return estabelecimento.getCodigo();
        }

        return "";
    }

    public String getCodigoFamilia() {
        if(grupoProduto != null) {
            return grupoProduto.getFmCodigo();
        }

        return "";
    }

    public Long getClasseCodigo() {
        if(classe != null) {
            return classe.getCodigo();
        }

        return null;
    }

    public String getClasseDescricao() {
        if(classe != null) {
            return classe.getDescricao();
        }

        return "";
    }
    
    public String getCodigoProdutor() {
        if(produtor != null) {
            return produtor.getCodProdutor().toString();
        }

        return "";
    }
}

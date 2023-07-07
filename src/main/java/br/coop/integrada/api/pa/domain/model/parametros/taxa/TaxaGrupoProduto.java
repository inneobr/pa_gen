package br.coop.integrada.api.pa.domain.model.parametros.taxa;

import static javax.persistence.FetchType.LAZY;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Where;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxa_grupo_produto")
public class TaxaGrupoProduto extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_taxa", nullable = false)
    private Taxa taxa;

    @ManyToOne
    @JoinColumn(name = "id_grupo_produto", nullable = false)
    private GrupoProduto grupoProduto;

    
    
    public String getCodigoGrupoProduto() {
    	if(grupoProduto == null || Strings.isEmpty(grupoProduto.getFmCodigo())) return "";
    	return grupoProduto.getFmCodigo();
    }
}

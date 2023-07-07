package br.coop.integrada.api.pa.domain.model.parametros.avariado;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "item_avariado_detalhe")
public class ItemAvariadoDetalhe extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_avariado", nullable = false)
    private ItemAvariado itemAvariado;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "percentual_inicial", nullable = false)
    private BigDecimal percentualInicial;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "percentual_final", nullable = false)
    private BigDecimal percentualFinal;
    
    @Digits(integer = 3, fraction = 2)
    @Column(name = "ph_inicial")
    private BigDecimal phInicial;
    
    @Digits(integer = 3, fraction = 2)
    @Column(name = "ph_final")
    private BigDecimal phFinal;

    @Digits(integer = 4, fraction = 2)
    @Column(name="fnt_inicial")
    private BigDecimal fntInicial;

    @Digits(integer = 4, fraction = 2)
    @Column(name="fnt_final")
    private BigDecimal fntFinal;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_produto_referencia")
    private ProdutoReferencia produtoReferencia;

    public static List<ItemAvariadoDetalhe> alterarStatusIntegracao(List<ItemAvariadoDetalhe> obj,
    		StatusIntegracao statusIntegracao) {
    	if(CollectionUtils.isEmpty(obj)) return new ArrayList<>();

    	return obj.stream().map(itemAvariadoDetalhe -> {
    		itemAvariadoDetalhe.setStatusIntegracao(statusIntegracao);
    		return itemAvariadoDetalhe;
    	}).toList();
    }
}

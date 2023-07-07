package br.coop.integrada.api.pa.domain.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v_produto_referencia")
public class VinculoProdutoReferencia extends AbstractEntity {

    
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
	@JsonBackReference
    private Produto produto;
    
    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "referencia_id", nullable = false)
    private ProdutoReferencia produtoReferencia;    
    

}

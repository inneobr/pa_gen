package br.coop.integrada.api.pa.domain.model.produto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
		name = "produto",
		uniqueConstraints = {
				@UniqueConstraint(name = "KEY_CODIGO", columnNames = { "cod_item" })
		}
)
public class Produto extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	

    @NotNull(message = "O campo {codItem} não pode nulo!")
    @NotBlank(message = "O campo {codItem} não pode ser vazio!")
	@Column(name="cod_item", nullable = false)
	private String codItem;

	@NotNull(message = "O campo {descItem} não pode nulo!")
	@NotBlank(message = "O campo {descItem} não pode ser vazio!")
	@Column(name="des_item", nullable = false)
	private String descItem;
	
	@ManyToOne
	@JoinColumn(name = "id_grupo_produto")
	@NotNull(message = "O campo {fmCodigo} não pode nulo!")
	private GrupoProduto grupoProduto;
	
	@Column(name="cod_item_af_coop")
	private String codItemAfCoop;
	
	@Column(name="desc_item_af_coop")
	private String descItemAfCoop;
	
	@Column(name="cod_item_af_terc")
	private String codItemAfTerc;
	
	@Column(name="desc_item_af_terc")
	private String descItemAfTerc;
	
	@Column(name="cod_item_fixa_coop")
	private String codItemFixaCoop;
	
	@Column(name="desc_item_fixa_coop")
	private String descItemFixaCoop;
	
	@Column(name="cod_item_fixa_terc")
	private String codItemFicaTerc;

	@Column(name="desc_item_fixa_terc")
	private String descItemFicaTerc;
	
	@Column(name="id_erp")
    private Integer idErp;
	
	@Transient
	private String descReferencia;
	
	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
    private List<VinculoProdutoReferencia> referencias = new ArrayList<VinculoProdutoReferencia>();
	
	@Transient
	public String getDescReferencia() {
	    descReferencia = null;
	    
		for(VinculoProdutoReferencia item: referencias) {
			if(descReferencia == null) {
				descReferencia = item.getProdutoReferencia().getCodRef();
			}else {
				descReferencia += ", " + item.getProdutoReferencia().getCodRef();
			}
		}		
		return descReferencia;
	}	
}

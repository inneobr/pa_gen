package br.coop.integrada.api.pa.domain.model.produto;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "v_grupo_produto_gmo")
public class GrupoProdutoGmo extends AbstractEntity{
		private static final long serialVersionUID = 1L;
		
		@ManyToOne
		@JoinColumn(name = "id_grupo_produto")
		private GrupoProduto grupoProduto;	
		
		@ManyToOne
		@JoinColumn(name = "id_tipo_gmo")
		private TipoGmo tipoGmo;
}

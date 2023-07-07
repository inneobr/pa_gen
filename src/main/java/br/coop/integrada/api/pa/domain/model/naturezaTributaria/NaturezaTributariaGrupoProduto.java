package br.coop.integrada.api.pa.domain.model.naturezaTributaria;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;

@Getter
@Setter
@Entity
@Table(name = "natureza_tributaria_grupo_produto", uniqueConstraints = {
		@UniqueConstraint(name = "KEY_ID_GRUPO_PRODUTO", columnNames = { "id_natureza_tributaria", "id_grupo_produto" })
})
public class NaturezaTributariaGrupoProduto extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(name="id_registro")
	private String idRegistro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "id_natureza_tributaria", nullable = false)
	private NaturezaTributaria naturezaTributaria;

	@ManyToOne	
	@JoinColumn(name = "id_grupo_produto", nullable = false)
	private GrupoProduto grupoProduto;
	
	public static NaturezaTributariaGrupoProduto builder(NaturezaTributaria naturezaTributaria, GrupoProdutoResumidoDto grupoProdutoDto) {
		NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto = new NaturezaTributariaGrupoProduto();
		GrupoProduto grupoProduto = new GrupoProduto();
		BeanUtils.copyProperties(grupoProdutoDto, grupoProduto);
		naturezaTributariaGrupoProduto.setNaturezaTributaria(naturezaTributaria);
		naturezaTributariaGrupoProduto.setGrupoProduto(grupoProduto);
		return naturezaTributariaGrupoProduto;
	}
}

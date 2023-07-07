package br.coop.integrada.api.pa.domain.model.parametros.avariado;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.Data;

@Data
@Entity
@Table(name = "item_avariado_estabelecimento")
public class ItemAvariadoEstabelecimento extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_item_avariado", nullable = false)
	private ItemAvariado itemAvariado;

	@ManyToOne
	@JoinColumn(name = "id_estabelecimento", nullable = false)
	private Estabelecimento estabelecimento;
}

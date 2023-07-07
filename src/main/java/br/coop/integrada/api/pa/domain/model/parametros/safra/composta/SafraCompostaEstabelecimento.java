package br.coop.integrada.api.pa.domain.model.parametros.safra.composta;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "safra_composta_estabelecimento")
public class SafraCompostaEstabelecimento extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_safra_composta")
    private SafraComposta safraComposta;

    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

	public String getCodigoEstabelecimento() {
		if(estabelecimento == null) return "";
		return estabelecimento.getCodigo();
	}
}

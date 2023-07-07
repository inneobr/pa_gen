package br.coop.integrada.api.pa.domain.model.parametros.taxa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.util.Strings;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxa_estabelecimento")
public class TaxaEstabelecimento extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_taxa", nullable = false)
    private Taxa taxa;

	@ManyToOne
	@JoinColumn(name = "id_estabelecimento")
	private Estabelecimento estabelecimento;
	
	public String getCodigo() {
		if(estabelecimento == null || Strings.isEmpty(estabelecimento.getCodigo())) return "";
		return estabelecimento.getCodigo();
	}
		
}

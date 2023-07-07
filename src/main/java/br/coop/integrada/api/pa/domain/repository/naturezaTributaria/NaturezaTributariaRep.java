package br.coop.integrada.api.pa.domain.repository.naturezaTributaria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;

public interface NaturezaTributariaRep extends JpaRepository<NaturezaTributaria, Long>, JpaSpecificationExecutor<NaturezaTributaria>{	
	NaturezaTributaria findByCodigo(Integer codigo);
	NaturezaTributaria findByGrupoProdutoGrupoProdutoFmCodigoAndEstabelecimentosEstabelecimentoCodigo(String fmCodigo, String codigo);
}

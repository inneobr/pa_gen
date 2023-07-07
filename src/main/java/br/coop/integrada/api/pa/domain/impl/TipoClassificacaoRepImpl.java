package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.repository.cadastro.TipoClassificacaoRep;
import br.coop.integrada.api.pa.domain.spec.TipoClassificacaoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class TipoClassificacaoRepImpl {
	
	@Autowired
	@Lazy
	private TipoClassificacaoRep tipoClassificacaoRep;
	 
	public Page<TipoClassificacao> findAll(Pageable pageable, String filter, Situacao situacao) {
		 
		 return tipoClassificacaoRep.findAll( 
				 TipoClassificacaoSpecs.doTipoClassificacao(filter)
				 .and(TipoClassificacaoSpecs.doSituacao(situacao) ) , 
				 pageable );
		 
	}
}

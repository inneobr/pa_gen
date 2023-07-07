package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoFilter;
import br.coop.integrada.api.pa.domain.repository.gmo.*;
import br.coop.integrada.api.pa.domain.spec.TaxaSpecs;
import br.coop.integrada.api.pa.domain.spec.TipoGmoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class TipoGmoRepImpl implements TipoGmoQueriesRep{
	
	@Autowired
    @Lazy
    private TipoGmoRep tipoGmoRep;
	
	public Page<TipoGmo> findAll(Pageable pageable, TipoGmoFilter filter, Situacao situacao) {
        return tipoGmoRep.findAll(
                TipoGmoSpecs.doTipoGmo(filter.getTipoGmo())
                		.or(TipoGmoSpecs.doObsRomaneio(filter.getObsRomaneio()))
                		.and(TipoGmoSpecs.doSituacao(situacao)),
                pageable
        );
	}
	
	
}

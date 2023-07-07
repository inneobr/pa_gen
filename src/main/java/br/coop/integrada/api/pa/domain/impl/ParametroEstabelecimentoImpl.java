package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoQueriesRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.spec.ParametroEstabelecimentoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Service//@Repository
public class ParametroEstabelecimentoImpl implements ParametroEstabelecimentoQueriesRep{
	
	@Autowired
    @Lazy
    private ParametroEstabelecimentoRep parametroEstabelecimentoRep;
	
	
	public Page<ParametroEstabelecimento> findAll(Pageable pageable, String filtro, Situacao situacao ) {
		
		if(StringUtils.hasText(filtro) && filtro.matches("[0-9]+"))
        {
			return parametroEstabelecimentoRep.findAll(
	                ParametroEstabelecimentoSpecs.doNomeFantasia(filtro)
	                		.or(ParametroEstabelecimentoSpecs.doCodigo(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoEmitente(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoImovel(filtro))
	                        .and(ParametroEstabelecimentoSpecs.doSituacao(situacao)),
	                pageable
			);
        }
        else {
        	
        	return parametroEstabelecimentoRep.findAll(	
	                ParametroEstabelecimentoSpecs.doNomeFantasia(filtro)
	                		.or(ParametroEstabelecimentoSpecs.doCodigo(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoEmitente(filtro))
	                        .and(ParametroEstabelecimentoSpecs.doSituacao(situacao)),
	                pageable
			);
        	
        }
		
    }

    
	
}

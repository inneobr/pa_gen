package br.coop.integrada.api.pa.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.spec.GrupoProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.TipoGmoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Repository
public class GrupoProdutoRepImpl {
	
	@Autowired
	@Lazy
	private GrupoProdutoRep grupoProdutoRep;
	
	public Page<GrupoProduto> findAll(Pageable pageable, Situacao situacao) {
		 
		 return grupoProdutoRep.findAll( 
				 GrupoProdutoSpecs.doSituacao(situacao) 
				 , pageable
				 );
		 
	 }
	
	public Page<GrupoProduto> findAll(Pageable pageable, String filtro, Situacao situacao) {
		return grupoProdutoRep.findAll(
				GrupoProdutoSpecs.doDescricao(filtro)
                		.or(GrupoProdutoSpecs.doFmCodigo(filtro))
                		.and(GrupoProdutoSpecs.doSituacao(situacao)),
                pageable
        );
	}
}

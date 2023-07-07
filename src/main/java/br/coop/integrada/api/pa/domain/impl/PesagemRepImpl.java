package br.coop.integrada.api.pa.domain.impl;

import static br.coop.integrada.api.pa.domain.enums.StatusPesagem.AGUARDANDO_RE;
import static br.coop.integrada.api.pa.domain.spec.PesagemSpecs.daSafra;
import static br.coop.integrada.api.pa.domain.spec.PesagemSpecs.nroDocPesagemEquals;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.aplication.utils.DataUtil;
import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemFilter;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemModalFilter;
import br.coop.integrada.api.pa.domain.repository.parametros.PesagemRep;
import br.coop.integrada.api.pa.domain.repository.parametros.PesagemRepQueries;
import br.coop.integrada.api.pa.domain.spec.PesagemSpecs;

@Repository
public class PesagemRepImpl implements PesagemRepQueries{
		
	@Autowired @Lazy
	private PesagemRep pesagemRep;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<Pesagem> buscarPesagensPendentesModel(Pageable pageable, PesagemModalFilter filter) {

		return pesagemRep.findAll(
				PesagemSpecs.doEstabelecimentoEqual(filter.getEstabelecimento())
				.and(PesagemSpecs.daSafra(DataUtil.extrairAno(new Date())))
				.and(PesagemSpecs.doTipoPesagem("P"))
				.and(PesagemSpecs.situacaoNotEquals("C"))
				.and(PesagemSpecs.doLogReJava(false))
				.and(PesagemSpecs.doStatus(AGUARDANDO_RE)), pageable);
	}
	
	public Page<Pesagem> buscarPesagensPendentes(Pageable pageable, PesagemFilter filter){	
		
		return pesagemRep.findAll(
				PesagemSpecs.doEstabelecimento(filter.getCodEstabelecimento())
				.and(PesagemSpecs.doNroDocPesagem(filter.getNroDocPesagemInicial(), filter.getNroDocPesagemFinal()))
				.and(PesagemSpecs.doPeriodo(filter.getInicio(), filter.getTermino()))
				.and(PesagemSpecs.comPesoAutomatico(filter.getTipoPesagemBalanca()))
				.and(PesagemSpecs.doGrupoProduto(filter.getGrupoProdutoId()))
				.and(PesagemSpecs.doStatus(StatusPesagem.AGUARDANDO_RE))
				.and(PesagemSpecs.doMotorista(filter.getMotorista()))
				.and(PesagemSpecs.doProdutor(filter.getProdutor()))
				.and(PesagemSpecs.doProduto(filter.getProduto()))
				.and(PesagemSpecs.doImovel(filter.getImovel()))
				.and(PesagemSpecs.daPlaca(filter.getPlaca()))
				.and(PesagemSpecs.daSafra(filter.getSafra()))
				.and(PesagemSpecs.ativo()), pageable);
	}
	
	public Page<Pesagem> buscarPesagens(Pageable pageable, PesagemFilter filter){	
		
		return pesagemRep.findAll(
				PesagemSpecs.doEstabelecimento(filter.getCodEstabelecimento())
				.and(PesagemSpecs.doNroDocPesagem(filter.getNroDocPesagemInicial(), filter.getNroDocPesagemFinal()))
				.and(PesagemSpecs.doPeriodo(filter.getInicio(), filter.getTermino()))
				.and(PesagemSpecs.comPesoAutomatico(filter.getTipoPesagemBalanca()))
				.and(PesagemSpecs.doGrupoProduto(filter.getGrupoProdutoId()))
				.and(PesagemSpecs.doMotorista(filter.getMotorista()))
				.and(PesagemSpecs.doProdutor(filter.getProdutor()))
				.and(PesagemSpecs.doProduto(filter.getProduto()))
				.and(PesagemSpecs.doImovel(filter.getImovel()))
				.and(PesagemSpecs.doStatus(filter.getStatus()))
				.and(PesagemSpecs.daPlaca(filter.getPlaca()))
				.and(PesagemSpecs.daSafra(filter.getSafra()))
				.and(PesagemSpecs.ativo()), pageable);
	}

	@Override
	public Optional<Pesagem> buscarPorCodigoEstabelecimentoSafraDocPesagem(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem) {
		return pesagemRep.findOne(
				PesagemSpecs.doEstabelecimento(codigoEstabelecimento)
				.and(daSafra(safra.toString()))
				.and(nroDocPesagemEquals(nroDocPesagem))
				);
	}

}

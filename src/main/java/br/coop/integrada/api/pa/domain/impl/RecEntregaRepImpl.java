package br.coop.integrada.api.pa.domain.impl;


import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaFilter;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaReportFilterDTO;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRepQueries;
import br.coop.integrada.api.pa.domain.spec.RecEntregaSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
public class RecEntregaRepImpl implements RecEntregaRepQueries{
		
	@Autowired @Lazy
	private RecEntregaRep recEntregaRep;
	
	@PersistenceContext
	private EntityManager entityManager;
		
	public Page<RecEntrega> buscarRomaneios(Pageable pageable, RecEntregaFilter filter){	
		
		return recEntregaRep.findAll(
				RecEntregaSpecs.codigoEstabelecimentoEquals(filter.getCodEstabelecimento())
				.and(RecEntregaSpecs.codEmitenteEquals(filter.getProdutor()))
				.and(RecEntregaSpecs.matriculaImovelEquals(filter.getMatriculaImovel()))
				.and(RecEntregaSpecs.codigoGrupoProdutoEquals(filter.getCodigoGrupoProduto()))
				.and(RecEntregaSpecs.comPesoAutomatico(filter.getTipoPesagemBalanca()))
				.and(RecEntregaSpecs.doNroDocPesagem(filter.getNroDocPesagemInicial(), filter.getNroDocPesagemFinal()))
				.and(RecEntregaSpecs.doPeriodo(filter.getInicio(), filter.getTermino()))
				.and(RecEntregaSpecs.doMotorista(filter.getMotorista()))
				.and(RecEntregaSpecs.doProduto(filter.getProduto()))
				.and(RecEntregaSpecs.doStatus(filter.getStatus()))
				.and(RecEntregaSpecs.daPlaca(filter.getPlaca()))
				.and(RecEntregaSpecs.safraEquals(filter.getSafra()))
				.and(RecEntregaSpecs.nrReBetween(filter.getNrReInicial(), filter.getNrReFinal()))
				.and(RecEntregaSpecs.ativo()), pageable);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateIntegracaoRecEntrega(Date dataIntegracao, Boolean logIntegrado, StatusIntegracao statusIntegracao, Long id) {
		Query query = entityManager.createQuery("""
				UPDATE RecEntrega re SET re.dataIntegracao = :dataIntegracao, re.logIntegrado = :logIntegrado, re.statusIntegracao = :statusIntegracao 
				WHERE re.id = :id
	            """);
		query.setParameter("dataIntegracao", dataIntegracao);
		query.setParameter("logIntegrado", logIntegrado);
		query.setParameter("statusIntegracao", statusIntegracao);
		query.setParameter("id", id);
		query.executeUpdate();
	};
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateIntegracaoRecEntregaItem(Date dataIntegracao, Boolean logIntegrado, StatusIntegracao statusIntegracao, Long id) {
		Query query = entityManager.createQuery("""
				UPDATE RecEntregaItem rei SET rei.dataIntegracao = :dataIntegracao, rei.logIntegrado = :logIntegrado, rei.statusIntegracao = :statusIntegracao 
				WHERE rei.id = :id
	            """);
		query.setParameter("dataIntegracao", dataIntegracao);
		query.setParameter("logIntegrado", logIntegrado);
		query.setParameter("statusIntegracao", statusIntegracao);
		query.setParameter("id", id);
		query.executeUpdate();
	};
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateCodSitRecEntrega(Integer codSit, List<Long> ids) {
		Query query = entityManager.createQuery("UPDATE RecEntrega r SET r.codSit = :codSit WHERE r.id IN (:ids)");
		query.setParameter("codSit", codSit);
		query.setParameter("ids", ids);
		query.executeUpdate();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateCodSitRecEntrega(Integer codSit, Long id) {
		Query query = entityManager.createQuery("UPDATE RecEntrega r SET r.codSit = :codSit WHERE r.id = :id");
		query.setParameter("codSit", codSit);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateImpressoRecEntrega(RecEntregaReportFilterDTO filterDTO) {

		var updateQuery = """
    			UPDATE RecEntrega re SET re.impresso = 1
				WHERE re.codEstabel = :estabelecimento
				AND re.nrRe BETWEEN :reInicial AND :reFinal
				AND re.dtEmissao BETWEEN :dataInicial AND :dataFinal
				AND re.codEmitente BETWEEN :produtorInicial AND :produtorFinal
				AND (re.impresso = 0 OR re.impresso IS NULL)
				""";

		Query query = entityManager.createQuery(updateQuery);
		query.setParameter("estabelecimento", filterDTO.estabelecimento());
		query.setParameter("reInicial", filterDTO.numeroInicialRE());
		query.setParameter("reFinal", filterDTO.numeroFinalRE());
		query.setParameter("dataInicial", filterDTO.dataInicialEmissao());
		query.setParameter("dataFinal", filterDTO.dataFinalEmissao());
		query.setParameter("produtorInicial", filterDTO.produtorInicial().toString());
		query.setParameter("produtorFinal", filterDTO.produtorFinal().toString());

		query.executeUpdate();
	}
}

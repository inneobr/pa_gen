package br.coop.integrada.api.pa.domain.service.recEntrega;

import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.SituacaoReDto;
import br.coop.integrada.api.pa.domain.repository.recEntrega.SituacaoReRep;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Service
public class SituacaoReService {
	
	private static final Logger logger = LoggerFactory.getLogger(SituacaoReService.class);	
	
	@Autowired private SituacaoReRep situacaoReRep;	
	
	public void salvarListSituacaoRe(List<SituacaoRe> situacaoRe) {
		logger.info("Salvando lista de SituacaoRe...");
		situacaoReRep.saveAll(situacaoRe);
	}
	
	public Page<SituacaoRe> listarSituacaoReAtivos(Pageable pageable) {
		logger.info("Listando situacaoRe ativos...");
		return situacaoReRep.findByDataInativacaoNull(pageable);
	}
	
	public Page<SituacaoRe> buscarTodasSituacaoRe(Pageable pageable) {
		logger.info("Listando todas SituacaoRe...");
		return situacaoReRep.findAll(pageable);
	}
	
	public List<SituacaoRe> buscaSituacaoRePorDescricao(String descricao) {
		logger.info("buscando Situação Re por descricao...");
		Pageable limit = PageRequest.of(0, 10);
		return situacaoReRep.findByDescricaoContainingIgnoreCaseAndDataInativacaoNull(descricao, limit);
	}
	
	public void atualizarSituacaoReAtivos(SituacaoReDto SituacaoReDto) throws Exception{
		if(SituacaoReDto.getId() == null) throw new Exception("Necessário informar o ID para atualização!");
		SituacaoRe SituacaoRe = situacaoReRep.findById(SituacaoReDto.getId()).orElse(null);
		if(SituacaoRe == null) throw new Exception("Não foi encontrado o registro para atualizar!");
		BeanUtils.copyProperties(SituacaoReDto, SituacaoRe);
		SituacaoRe.setDataAtualizacao(new Date());
		situacaoReRep.save(SituacaoRe);
	}
	
	public SituacaoRe inativarSituacaoRe(Long id) {
		SituacaoRe SituacaoRe = situacaoReRep.findById(id).orElse(null);
		SituacaoRe.setDataInativacao(new Date());
		logger.info("Inativando SituacaoRe...");
		return situacaoReRep.save(SituacaoRe);
	}
	
	public SituacaoRe ativarSituacaoRe(Long id) {		
		SituacaoRe SituacaoRe = situacaoReRep.findById(id).orElse(null);
		SituacaoRe.setDataInativacao(null);
		SituacaoRe.setDataAtualizacao(new Date());
		logger.info("Ativando SituacaoRe...");
		return situacaoReRep.save(SituacaoRe);
	}
	
	public SituacaoRe buscarSituacaoRePorId(Long id) {
		logger.info("Buscando id SituacaoRe...");
		return situacaoReRep.findById(id).orElse(null);
	}
	
	public SituacaoRe buscarSituacaoRePorCodigo(Long codigo) {
		logger.info("Buscando código SituacaoRe...");
		return situacaoReRep.findByCodigo(codigo);
	}

	public SituacaoRe buscarPorDescricao(String descricao) {
		return situacaoReRep.findByDescricaoIgnoreCase(descricao);
	}
		
	public void remove(SituacaoRe SituacaoRe) {
		situacaoReRep.delete(SituacaoRe);
	}

	public SituacaoRe cadastrar(@Valid SituacaoRe situacaoRe) throws Exception {
		
		SituacaoRe SituacaoReAux = buscarSituacaoRePorCodigo(situacaoRe.getCodigo() );
		
		if(SituacaoReAux != null) {
			throw new DataIntegrityViolationException("Esta Situacao Re já foi integrada anteriormente no sistema.");
		}
		
		return situacaoReRep.save(situacaoRe);
	}
	
	public SituacaoRe atualizar(@Valid SituacaoRe obj) throws Exception {
		
		SituacaoRe situacaoRe = situacaoReRep.findByCodigo( obj.getCodigo() );

		if(situacaoRe == null) {
			throw new ObjectNotFoundException("Não foi encontrada SituacaoRe com o código " + obj.getCodigo() );
		}

		obj.setDataAtualizacao(new Date());
		BeanUtils.copyProperties(obj, situacaoRe);
		return situacaoReRep.save(situacaoRe);
		
	}

	public void save(SituacaoRe situacaoRe) {
		situacaoReRep.save(situacaoRe);
		
	}

	@SuppressWarnings("rawtypes")
	public Page<SituacaoReDto> buscarComPaginacao(Pageable pageable, String descricao, Situacao situacao) {
		Page<SituacaoRe> situacaoRePage = situacaoReRep.findAll(pageable, descricao, situacao);
        List<SituacaoReDto> situacaoReDtos = SituacaoReDto.construir(situacaoRePage.getContent());

        return new PageImpl(situacaoReDtos, pageable, situacaoRePage.getTotalElements());
	}

	/*public Page<SituacaoRe> buscarTodosGmo(Pageable pageable, SituacaoReFilter filter, Situacao situacao) {
		logger.info("Listando todos SituacaoRe...");
		Page<SituacaoRe> pageSituacaoRe = situacaoReRep.findAll(pageable, filter, situacao);
		return pageSituacaoRe;
	}*/
	
	

	/*public Page<SituacaoRe> pesquisarPorSituacaoRe(String SituacaoRe, Situacao situacao, Pageable pageable) {
		Page<SituacaoRe> SituacaoRePage = situacaoReRep.findAll(
				SituacaoReSpecs.doSituacaoRe(SituacaoRe)
				.and(SituacaoReSpecs.doSituacao(situacao)), 
				pageable);
		return SituacaoRePage;
	}*/

	/*public Page<SituacaoRe> buscarPorCodigoGrupoProduto(String codigoGrupoProduto, String filtro, Situacao situacao,
			Pageable pageable) {
		return situacaoReRep.findAll(
				SituacaoReSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(SituacaoReSpecs.doSituacaoRe(filtro))
				.and(SituacaoReSpecs.doSituacao(situacao)), pageable);
	}*/

	/*public Optional<SituacaoRe> buscarPorSituacaoReECodigoGrupoProduto(String SituacaoRe, String codigoGrupoProduto, Situacao situacao) {
		return situacaoReRep.findOne(
				SituacaoReSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(SituacaoReSpecs.SituacaoReEquals(SituacaoRe))
				.and(SituacaoReSpecs.doSituacao(situacao)));
	}*/
	
	
}

package br.coop.integrada.api.pa.domain.service.produto;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoFilter;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoKitDto;
import br.coop.integrada.api.pa.domain.repository.gmo.TipoGmoRep;
import br.coop.integrada.api.pa.domain.spec.TipoGmoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional
public class TipoGmoService {
	private static final Logger logger = LoggerFactory.getLogger(TipoGmoService.class);	
	@Autowired private TipoGmoRep tipoGmoRep;	

	public void salvarListTipoGmo(List<TipoGmo> tipoGmo) {
		logger.info("Salvando novo tipoGmo...");
		tipoGmoRep.saveAll(tipoGmo);
	}
	
	public Page<TipoGmo> listarTipoGmoAtivos(Pageable pageable) {
		logger.info("Listando tipoGmo ativo...");
		return tipoGmoRep.findByDataInativacaoNull(pageable);
	}
	
	public Page<TipoGmo> buscarTodosGmo(Pageable pageable) {
		logger.info("Listando todos tipoGmo...");
		return tipoGmoRep.findAll(pageable);
	}
	
	public List<TipoGmo> buscaTipoGmoPorNome(String nome) {
		logger.info("buscando tipo gmo por nome...");
		Pageable limit = PageRequest.of(0, 10);
		return tipoGmoRep.findByTipoGmoContainingIgnoreCaseAndDataInativacaoNull(nome, limit);
	}
	
	public void atualizarTipoGmoAtivos(TipoGmoDto tipoGmoDto) throws Exception{
		if(tipoGmoDto.getId() == null) throw new Exception("Necessário informar o ID para atualização!");
		TipoGmo tipoGmo = tipoGmoRep.findById(tipoGmoDto.getId()).orElse(null);
		if(tipoGmo == null) throw new Exception("Não foi encontrado o registro para atualizar!");
		BeanUtils.copyProperties(tipoGmoDto, tipoGmo);
		tipoGmo.setDataAtualizacao(new Date());
		tipoGmoRep.save(tipoGmo);
	}
	
	public TipoGmo inativarTipoGmo(Long id) {
		TipoGmo tipoGmo = tipoGmoRep.findById(id).orElse(null);
		tipoGmo.setDataInativacao(new Date());
		logger.info("Inativando tipoGmo...");
		return tipoGmoRep.save(tipoGmo);
	}
	
	public TipoGmo ativarTipoGmo(Long id) {		
		TipoGmo tipoGmo = tipoGmoRep.findById(id).orElse(null);
		tipoGmo.setDataInativacao(null);
		tipoGmo.setDataAtualizacao(new Date());
		logger.info("Ativando tipoGmo...");
		return tipoGmoRep.save(tipoGmo);
	}
	
	public TipoGmo buscarTipoGmoPorId(Long id) {
		logger.info("Buscando id tipoGmo...");
		return tipoGmoRep.findById(id).orElse(null);
	}

	public TipoGmo buscarPorTipoGmo(String tipoGmo) {
		return tipoGmoRep.findByTipoGmoIgnoreCase(tipoGmo);
	}
	
	public TipoGmo findByIdUnico(String idUnico) {
		return tipoGmoRep.findByIdUnicoIgnoreCase(idUnico);
	}
	
	public void remove(TipoGmo tipoGmo) {
		tipoGmoRep.delete(tipoGmo);
	}

	public TipoGmo cadastrar(@Valid TipoGmo tipoGmo) throws Exception {
		TipoGmo tipoGmoAux = buscarPorTipoGmo(tipoGmo.getTipoGmo());
		
		if(tipoGmoAux != null) {
			throw new DataIntegrityViolationException("Este tipo GMO já foi integrado anteriormente no sistema.");
		}
		
		return tipoGmoRep.save(tipoGmo);
	}
	
	public TipoGmo atualizar(@Valid TipoGmo obj) throws Exception {
		
		TipoGmo tipoGmo = tipoGmoRep.findByIdUnicoIgnoreCase(obj.getIdUnico());

		if(tipoGmo == null) {
			throw new ObjectNotFoundException("Não foi encontrado tipoGmo com o IdUnico " + obj.getIdUnico());
		}

		obj.setDataAtualizacao(new Date());
		BeanUtils.copyProperties(obj, tipoGmo);
		return tipoGmoRep.save(tipoGmo);
		
	}

	public Page<TipoGmo> buscarTodosGmo(Pageable pageable, TipoGmoFilter filter, Situacao situacao) {
		logger.info("Listando todos tipoGmo...");
		Page<TipoGmo> pageTipoGmo = tipoGmoRep.findAll(pageable, filter, situacao);
		return pageTipoGmo;
	}
	
	public TipoGmoKitDto getCobrancaKit(Long tipoGmoId) {
		TipoGmoKitDto tipoGmoKit = new TipoGmoKitDto();
		TipoGmo tipoGmo = tipoGmoRep.getReferenceById(tipoGmoId);		
		if(tipoGmo == null) throw new NullPointerException("TipoGmo não encontrado.");
		
		logger.info("Valor kit teste gmo: {}", tipoGmo.getVlKit());
		if(new BigDecimal("0").compareTo(tipoGmo.getVlKit()) != 0) {
			tipoGmoKit.setValorKit(tipoGmo.getVlKit());
			tipoGmoKit.setPagaKit(true);
			return tipoGmoKit;			
		}		
		tipoGmoKit.setValorKit(new BigDecimal("0"));
		tipoGmoKit.setPagaKit(false);
		return tipoGmoKit;
	}

	public Page<TipoGmo> pesquisarPorTipoGmo(String tipoGmo, Situacao situacao, Pageable pageable) {
		Page<TipoGmo> tipoGmoPage = tipoGmoRep.findAll(
				TipoGmoSpecs.doTipoGmo(tipoGmo)
				.and(TipoGmoSpecs.doSituacao(situacao)), 
				pageable);
		return tipoGmoPage;
	}

	public Page<TipoGmo> buscarPorCodigoGrupoProduto(String codigoGrupoProduto, String filtro, Situacao situacao,
			Pageable pageable) {
		return tipoGmoRep.findAll(
				TipoGmoSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(TipoGmoSpecs.doTipoGmo(filtro))
				.and(TipoGmoSpecs.doSituacao(situacao)), pageable);
	}

	public Optional<TipoGmo> buscarPorTipoGmoECodigoGrupoProduto(String tipoGmo, String codigoGrupoProduto, Situacao situacao) {
		return tipoGmoRep.findOne(
				TipoGmoSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(TipoGmoSpecs.tipoGmoEquals(tipoGmo))
				.and(TipoGmoSpecs.doSituacao(situacao)));
	}
}

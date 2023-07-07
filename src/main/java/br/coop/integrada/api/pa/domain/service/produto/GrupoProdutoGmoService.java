package br.coop.integrada.api.pa.domain.service.produto;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoGmoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration.GrupoProdutoGmoIntegrationSimplesDto;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoGmoRep;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class GrupoProdutoGmoService {
private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
	
	@Autowired
	private GrupoProdutoGmoRep grupoProdutoGmoRep;

	@Autowired
	private TipoGmoService tipoGmoService;

	public void salvar(List<GrupoProdutoGmoDto> grupoProdutoGmoDtos, GrupoProduto grupoProduto) {
		logger.info("Salvando novo grupo produto gmo...");
		List<GrupoProdutoGmo> grupoProdutoGmos = converterGrupoProdutoGmoDto(grupoProdutoGmoDtos,  grupoProduto);
		grupoProdutoGmoRep.saveAll(grupoProdutoGmos);
	}

	public void integrationSave(List<GrupoProdutoGmoIntegrationSimplesDto> gmoDtos, GrupoProduto grupoProduto) {
		logger.info("Salvando novo grupo produto gmo...");
		List<GrupoProdutoGmo> grupoProdutoGmosBD = buscarPorGrupoProduto(grupoProduto);
		
		if(gmoDtos != null && !gmoDtos.isEmpty()) {
			List<GrupoProdutoGmo> grupoProdutoGmos = converterGrupoProdutoGmoIntegrationSimplesDto(gmoDtos,  grupoProduto);
			grupoProdutoGmoRep.saveAll(grupoProdutoGmos);
			
			if(grupoProdutoGmosBD != null && !grupoProdutoGmosBD.isEmpty()) {
				
				boolean encontrou = false;
				for(GrupoProdutoGmo gmoAnt : grupoProdutoGmosBD) {					
					
					for(GrupoProdutoGmo gmo : grupoProdutoGmos) {
						if(gmoAnt.getTipoGmo().getId().equals(gmo.getTipoGmo().getId()) && gmoAnt.getTipoGmo().getAtivo()) {
							encontrou = true;
							break;
						}
					}
					
					if(!encontrou) {
						grupoProdutoGmoRep.delete(gmoAnt);
					}
					encontrou = false;
				}
			}
			
		}else {
			if(grupoProdutoGmosBD != null && !grupoProdutoGmosBD.isEmpty()) {
				grupoProdutoGmoRep.deleteAll(grupoProdutoGmosBD);
			}
		}
	}
	
	public Page<GrupoProdutoGmo> buscarTodos(Pageable pageable) {
		logger.info("Listando todos grupo produto...");
		return grupoProdutoGmoRep.findAll(pageable);
	}
	
	public Page<GrupoProdutoGmo> buscarTodosPage(Pageable pageable) {
		logger.info("Listando todos grupo produto gmo...");
		return grupoProdutoGmoRep.findByDataInativacaoNull(pageable);
	}
	
	public void excluirGrupoProdutoGmo(Long id) {
		GrupoProdutoGmo produtoGmo  = grupoProdutoGmoRep.getReferenceById(id);
		produtoGmo.setDataInativacao(new Date());
		logger.info("Excluindo grupo produto gmo...");
		grupoProdutoGmoRep.save(produtoGmo);
	}
	
	public GrupoProdutoGmo buscarIdGrupoProdutoGmo(Long id) {
		GrupoProdutoGmo produtoGmo  = grupoProdutoGmoRep.getReferenceById(id);
		logger.info("Buscando id grupo produto gmo...");
		return produtoGmo;
	}	
	
	public List<GrupoProdutoGmo> buscarPorGrupoProduto(GrupoProduto grupoProduto) {
		logger.info("Listando por grupo produto...");
		return grupoProdutoGmoRep.findByGrupoProduto(grupoProduto);
	}

	private GrupoProdutoGmo converterGrupoProdutoGmoDto(GrupoProdutoGmoDto objDto, GrupoProduto grupoProduto) {
		if(objDto.getTipoGmo() == null || Strings.isEmpty(objDto.getTipoGmo().getTipoGmo())) {
			throw new ObjectDefaultException("Necessário informar o tipo de GMO!");
		}

		TipoGmo tipoGmo = tipoGmoService.findByIdUnico(objDto.getTipoGmo().getIdUnico());
		if(tipoGmo == null) {
			throw new ObjectNotFoundException("Não foi encontrado o tipo de GMO " + objDto.getTipoGmo().getTipoGmo());
		}

		GrupoProdutoGmo grupoProdutoGmo = grupoProdutoGmoRep.findByTipoGmoAndGrupoProduto(tipoGmo, grupoProduto);
		if(grupoProdutoGmo == null) {
			grupoProdutoGmo = new GrupoProdutoGmo();
			grupoProdutoGmo.setTipoGmo(tipoGmo);
			grupoProdutoGmo.setGrupoProduto(grupoProduto);
		}

		Date dataAtual = objDto.getAtivo() ? null : new Date();
		grupoProdutoGmo.setDataInativacao(dataAtual);

		return grupoProdutoGmo;
	}

	private List<GrupoProdutoGmo> converterGrupoProdutoGmoDto(List<GrupoProdutoGmoDto> objDtos, GrupoProduto grupoProduto) {
		if(objDtos == null) new ArrayList<>();

		return objDtos.stream().map(grupoProdutoGmoDto -> {
			return converterGrupoProdutoGmoDto(grupoProdutoGmoDto, grupoProduto);
		}).toList();
	}

	private GrupoProdutoGmo converterGrupoProdutoGmoIntegrationSimplesDto(GrupoProdutoGmoIntegrationSimplesDto objDto, GrupoProduto grupoProduto) {
		if(objDto.getTipoGmo() == null || Strings.isEmpty(objDto.getTipoGmo())) {
			throw new ObjectDefaultException("Necessário informar o tipo de GMO!");
		}

		String tipoGmoNome = objDto.getTipoGmo();
		TipoGmo tipoGmo = tipoGmoService.buscarPorTipoGmo(tipoGmoNome);
		if(tipoGmo == null) {
			throw new ObjectNotFoundException("Não foi encontrado o tipo de GMO " + tipoGmoNome);
		}

		GrupoProdutoGmo grupoProdutoGmo = grupoProdutoGmoRep.findByTipoGmoAndGrupoProduto(tipoGmo, grupoProduto);
		if(grupoProdutoGmo == null) {
			grupoProdutoGmo = new GrupoProdutoGmo();
			grupoProdutoGmo.setTipoGmo(tipoGmo);
			grupoProdutoGmo.setGrupoProduto(grupoProduto);
		}

		Date dataAtual = objDto.getAtivo() ? null : new Date();
		grupoProdutoGmo.setDataInativacao(dataAtual);
		grupoProdutoGmo.setDataIntegracao(new Date());
		grupoProdutoGmo.setStatusIntegracao(StatusIntegracao.INTEGRADO);

		return grupoProdutoGmo;
	}

	private List<GrupoProdutoGmo> converterGrupoProdutoGmoIntegrationSimplesDto(
			List<GrupoProdutoGmoIntegrationSimplesDto> objDtos, GrupoProduto grupoProduto
	) {
		if(objDtos == null) new ArrayList<>();

		return objDtos.stream().map(grupoProdutoGmoIntegrationSimplesDto -> {
			return converterGrupoProdutoGmoIntegrationSimplesDto(grupoProdutoGmoIntegrationSimplesDto, grupoProduto);
		}).toList();
	}
	
	public void deleteAll(List<GrupoProdutoGmo> lista) {
		grupoProdutoGmoRep.deleteAll(lista);
	}
}

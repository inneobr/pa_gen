package br.coop.integrada.api.pa.domain.service.produto;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.PaginaAreaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.model.produto.*;
import br.coop.integrada.api.pa.domain.modelDto.ChaveValorDropDownDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoFilter;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration.GrupoProdutoIntegrationSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.informacaoRecursoSistema.PaginaAreaDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ReferenciaGrupoDto;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.spec.GrupoProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class GrupoProdutoService {
	private static final Logger logger = LoggerFactory.getLogger(GrupoProdutoService.class);
	
	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	@Autowired
    private ProdutoRep produtoRep;
	
	@Autowired
	private GrupoProdutoGmoService grupoProdutoGmoService;

	@Autowired
	private TipoProdutoService tipoProdutoService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public GrupoProduto salvar(GrupoProdutoDto grupoProdutoDto) {
		logger.info("Salvando o grupo de produto...");
		
		GrupoProduto grupoProduto = grupoProdutoDto.getId() != null ? buscarIdGrupoProduto(grupoProdutoDto.getId()): new GrupoProduto();

		TipoProduto tipoProduto = null;
		if(grupoProdutoDto.getTipoProduto() != null) {
			String nome = grupoProdutoDto.getTipoProduto().getNome();
			tipoProduto = tipoProdutoService.buscarPorNome(nome);

			if(tipoProduto == null) {
				throw new ObjectNotFoundException("Não foi encontrado o tipo de produto com o nome \"" + nome + "\"!");
			}
		}else {
			throw new ObjectNotFoundException("Favor informar o tipo de produto.");
		}

		//Verifica se o fmCodigo do subproduto existe cadastrado na tabela de Grupo de Produtos.
		if(grupoProdutoDto.getFmCodigoSub() != null && !grupoProdutoDto.getFmCodigoSub().isEmpty()) {

			GrupoProduto grupoProdutosub = grupoProdutoRep.findByFmCodigo(grupoProdutoDto.getFmCodigoSub());
			
			if(grupoProdutosub == null) {
				throw new ObjectNotFoundException("Não foi encontrado o código do grupo de produto do subproduto: " + grupoProdutoDto.getFmCodigoSub());
			}
		}
		
		if(grupoProdutoDto.getItSubCoop() != null) {
			System.out.println(grupoProdutoDto.getItSubCoop());
			Produto produtoSub = produtoRep.findByCodItemAndDataInativacaoNull(grupoProdutoDto.getItSubCoop());
			
			//Procura o codigo do produto existe
			if(produtoSub == null) {
				throw new ObjectNotFoundException("Não foi encontrado o código do produto do subproduto: " + grupoProdutoDto.getItSubCoop() );
			}
			
			//Verifica se o produto está dentro do grupo informado
			if(!produtoSub.getGrupoProduto().getFmCodigo().equals(grupoProdutoDto.getFmCodigoSub())){
				throw new ObjectNotFoundException("O produto informado: " + grupoProdutoDto.getItSubCoop() + " no campo Produto Sub Produto, não pretençe ao Grupo:" + grupoProdutoDto.getFmCodigoSub() );
			}
			
		}
		
		//Verifica se o codigo informado no campo Secagem - Produto Taxa existe cadastrado na tabela de produto. 
		if(grupoProdutoDto.getItTaxaCoop() != null && !grupoProdutoDto.getItTaxaCoop().isEmpty()) {
			Produto produtoTaxa = produtoRep.findByCodItemAndDataInativacaoNull(grupoProdutoDto.getItTaxaCoop());
			//Verifica se o produto informado é do tipo informado.
			//Procura o codigo do produto se o mesmo existe
			if(produtoTaxa == null) {
				throw new ObjectNotFoundException("Não foi encontrado o código do produto: " + grupoProdutoDto.getItTaxaCoop() + " informado no campo Produto Taxa" );
			}
		}
			
		BeanUtils.copyProperties(grupoProdutoDto, grupoProduto);
		grupoProduto.setTipoProduto(tipoProduto);

		grupoProduto = grupoProdutoRep.save(grupoProduto);

		if(grupoProdutoDto.getListGrupoProdutoGmo() != null) {
			grupoProdutoGmoService.salvar(grupoProdutoDto.getListGrupoProdutoGmo(),  grupoProduto);
		}

		return grupoProduto;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public GrupoProduto integrationSave(GrupoProdutoIntegrationSimplesDto objDto) {
		logger.info("Salvando grupo produto...");

		if(Strings.isEmpty(objDto.getFmCodigo())) {
			throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");
		}

		TipoProduto tipoProduto = null;
		if(Strings.isNotEmpty(objDto.getTipoProdutoNome())) {
			String nome = objDto.getTipoProdutoNome();
			tipoProduto = tipoProdutoService.buscarPorNome(nome);

			if(tipoProduto == null) {
				throw new ObjectNotFoundException("Não foi encontrado o tipo de produto com o nome \"" + nome + "\"!");
			}			
		}

		String fmCodigo = objDto.getFmCodigo();
		GrupoProduto grupoProduto = grupoProdutoRep.findByFmCodigo(fmCodigo);

		if(grupoProduto == null) {
			grupoProduto = new GrupoProduto();
		}

		BeanUtils.copyProperties(objDto, grupoProduto);
		grupoProduto.setCafeBeneficiado(objDto.getCafeBenef());
		grupoProduto.setTipoProduto(tipoProduto);
		
		if(objDto.getAtivo()) {
			grupoProduto.setDataInativacao(null);
		}else {
			if(grupoProduto.getDataInativacao() == null) {
				grupoProduto.setDataInativacao(new Date());
			}
		}
		grupoProduto.setDataIntegracao(new Date());
		grupoProduto.setStatusIntegracao(StatusIntegracao.INTEGRADO);

		grupoProduto = grupoProdutoRep.save(grupoProduto);

		grupoProdutoGmoService.integrationSave(objDto.getGmos(),  grupoProduto);

		return grupoProduto;
	}
	
	public List<GrupoProduto> autoCompleteCodigoDescricao(String dados) {
		Pageable pageable = PageRequest.of(0, 5, Sort.by("descricao"));
		logger.info("Buscando grupo produto por codigo ou descrição...");
		return grupoProdutoRep.findByFmCodigoContainingOrDescricaoContainingIgnoreCase(dados, dados, pageable);
	}
	
	public Page<GrupoProduto> buscarGrupoProdutoAtivo(Pageable pageable) {
		logger.info("Listando todos grupo produto gmo ativo...");
		return grupoProdutoRep.findByDataInativacaoNull(pageable);
	}
	
	public List<GrupoProduto> listarGrupoProdutoAtivo() {
		logger.info("Listando todos grupo produto gmo ativo...");
		return grupoProdutoRep.findByDataInativacaoNullOrderByFmCodigoAsc();
	}
	
	public List<GrupoProdutoResumidoDto> listarGrupoProdutoAtivoResumido() {
		List<GrupoProduto> todos = grupoProdutoRep.findByDataInativacaoNullOrderByDescricaoAsc();
		if(todos.isEmpty()) todos = new ArrayList<>();
		List<GrupoProdutoResumidoDto> response = new ArrayList<>();
		for(GrupoProduto grupo: todos) {
			GrupoProdutoResumidoDto grupoDto = new GrupoProdutoResumidoDto();
			BeanUtils.copyProperties(grupo, grupoDto);
			response.add(grupoDto);
		}		
		logger.info("Buscando lista grupo produto resumido.");
		return response;
	}
	
	public Page<GrupoProduto> buscarTodos(Pageable pageable) {
		logger.info("Listando todos grupo produto gmo...");
		return grupoProdutoRep.findAll(pageable);
	}
	
	public Page<GrupoProduto> buscarTodos(Pageable pageable, Situacao situacao) {
		logger.info("Listando todos grupo produto gmo...");
		return grupoProdutoRep.findAll(pageable, situacao);
	}
	
	public Page<GrupoProduto> buscarTodos(Pageable pageable, String filtro, Situacao situacao) {
		logger.info("Listando todos grupo produto gmo...");
		return grupoProdutoRep.findAll(pageable, filtro, situacao);
	}
	
	public GrupoProduto buscarGrupoFmCodigo(String fmCodigo) {
		logger.info("Buscando grupo produto por fmCodigo...");
		GrupoProduto grupoProduto = grupoProdutoRep.findByFmCodigo(fmCodigo);

		if(grupoProduto == null) {
			throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o código " + fmCodigo);
		}

		return grupoProduto;
	}
	
	public GrupoProduto atualizarGrupoProduto(GrupoProdutoDto produtoDto) {
		logger.info("Atualizando gmo...");
		GrupoProduto grupoProduto = grupoProdutoRep.getReferenceById(produtoDto.getId());
		BeanUtils.copyProperties(produtoDto, grupoProduto);
		grupoProduto.setDataAtualizacao(new Date());
		return grupoProdutoRep.save(grupoProduto);
	}	
	
	public GrupoProduto inativarGrupoProduto(Long id) {
		GrupoProduto grupoProduto  = grupoProdutoRep.getReferenceById(id);
		grupoProduto.setDataInativacao(new Date());
		logger.info("Excluindo grupo produto...");
		return grupoProdutoRep.save(grupoProduto);
	}
	
	public GrupoProduto ativarGrupoProduto(Long id) {
		GrupoProduto grupoProduto  = grupoProdutoRep.getReferenceById(id);
		grupoProduto.setDataInativacao(null);
		grupoProduto.setDataAtualizacao(new Date());
		logger.info("Excluindo grupo produto...");
		return grupoProdutoRep.save(grupoProduto);
	}
	
	public GrupoProduto buscarIdGrupoProduto(Long id) {
		GrupoProduto grupoProduto  = grupoProdutoRep.getReferenceById(id);
		logger.info("Buscando id grupo produto...");
		return grupoProduto;
	}
	
	public GrupoProdutoDto buscarGrupoProdutoDto(Long id) {
		GrupoProduto grupoProduto  = grupoProdutoRep.findById(id).orElse(null);

		if(grupoProduto == null) {
			throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o ID " + id);
		}

		List<GrupoProdutoGmo> grupoProdutoGmoList = grupoProdutoGmoService.buscarPorGrupoProduto(grupoProduto);
		return GrupoProdutoDto.construir(grupoProduto, grupoProdutoGmoList);
	}	
	
	public List<GrupoProdutoGetDto> buscarGrupoProdutoReferecia() {
	    List<GrupoProdutoGetDto> grupoProdutoDto = new ArrayList<>();;
	    for(GrupoProduto grupoProduto: grupoProdutoRep.findByDataInativacaoNullOrderByDescricaoAsc()) {
	        GrupoProdutoGetDto grupoProdutoGetDto = new GrupoProdutoGetDto();
	        BeanUtils.copyProperties(grupoProduto, grupoProdutoGetDto);
	        
	        List<ProdutoGetDto> produtos = new ArrayList<>();
	        for(Produto produto: produtoRep.findByGrupoProduto(grupoProduto)) {
	            ProdutoGetDto produtoDto = new  ProdutoGetDto();
	            BeanUtils.copyProperties(produto, produtoDto);
	            
	            List<ReferenciaGrupoDto> referencias = new ArrayList<>();
	            for(VinculoProdutoReferencia vProdutoreferencia: produto.getReferencias()) {
	                ReferenciaGrupoDto referenciaDto = new ReferenciaGrupoDto();
	                BeanUtils.copyProperties(vProdutoreferencia.getProdutoReferencia(), referenciaDto);
	                referencias.add(referenciaDto);
	            }
	            produtoDto.setReferencia(referencias);	            
	            produtos.add(produtoDto);
	        }
	        grupoProdutoGetDto.setProdutos(produtos);
	        grupoProdutoDto.add(grupoProdutoGetDto);
	    }       
       return grupoProdutoDto;
    }

	public Boolean validarProdutoeSubProduto(String codigoProduto) {
		
		List<GrupoProduto> lista = grupoProdutoRep.findByItSubCoopAndDataInativacaoNull(codigoProduto);
		
		if(lista!= null && !lista.isEmpty())
			return true;

		return false;

	}
	
	public List<GrupoProdutoResumidoDto> getCodigoDescricao(String filter) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("descricao"));
		logger.info("Buscando grupo produto FmCodigo / Descrição: {}", filter);		
		Page<GrupoProduto> grupoProdutosPage = grupoProdutoRep.findAll(
				GrupoProdutoSpecs.codigoOuDescricaoLike(filter)
				.and(GrupoProdutoSpecs.doSituacao(Situacao.ATIVO)),
				pageable);
		
		List<GrupoProdutoResumidoDto>  response = new ArrayList<>();
		for(GrupoProduto grupo: grupoProdutosPage.getContent()) {
			GrupoProdutoResumidoDto grupoProduto = new GrupoProdutoResumidoDto();
			BeanUtils.copyProperties(grupo, grupoProduto);
			response.add(grupoProduto);
		}
		return response;
	}
	
	public void delete(GrupoProduto grupoProduto) {
		
		List<GrupoProdutoGmo> grupoProdutoGmos = grupoProdutoGmoService.buscarPorGrupoProduto(grupoProduto);
		if(grupoProdutoGmos != null && !grupoProdutoGmos.isEmpty()) {
			grupoProdutoGmoService.deleteAll(grupoProdutoGmos);
		}
		
		grupoProdutoRep.delete(grupoProduto);
	}
	
	public void save(GrupoProduto grupoProduto) {
		grupoProdutoRep.save(grupoProduto);
	}

	public List<ChaveValorDropDownDto> buscarEntradaEnum() {
		
		//HashMap<String, String> retorno = new HashMap<>();
		List<ChaveValorDropDownDto> retorno = new ArrayList<>();
		
		for(EntradaReEnum op : EntradaReEnum.values()) {
			//retorno.put( op.name(), op.getDescricao() );
			ChaveValorDropDownDto cv = new ChaveValorDropDownDto();
			
			cv.Chave = op.name();
			cv.Valor = op.getDescricao();
			
			retorno.add(cv);
		}
		
		return retorno;
	}

	public Page<GrupoProduto> buscarPorCodigoOuDescricaoPaginado(String filtro, Situacao situacao, Pageable pageable) {
		return grupoProdutoRep.findAll(
				GrupoProdutoSpecs.codigoOuDescricaoLike(filtro)
				.and(GrupoProdutoSpecs.doSituacao(situacao))
				, pageable);
	}

	public Page<GrupoProduto> buscarPorCodigoOuDescricaoComParametroEntradaReDiferenteNaoPermitePaginado(String filtro, Situacao situacao, Pageable pageable) {
		return grupoProdutoRep.findAll(
				GrupoProdutoSpecs.codigoOuDescricaoLike(filtro)
				.and(GrupoProdutoSpecs.entradaReNotIguals(EntradaReEnum.NAO_PERMITE))
				.and(GrupoProdutoSpecs.doSituacao(situacao))
				, pageable);
	}
	
	public List<GrupoProdutoResumidoDto> getGrupoProdutoProdutoSemente(GrupoProdutoFilter filter) {
		logger.info("Buscando grupo produto, pesquisar: {} tipoProduto: {}", filter.getPesquisar(), filter.getTipoProduto());
		List<GrupoProduto> grupoProdutosList = grupoProdutoRep.findAll(
				GrupoProdutoSpecs.codigoOuDescricaoLike(filter.getPesquisar())
				.and(GrupoProdutoSpecs.doTipoProduto(filter.getTipoProduto()))
				.and(GrupoProdutoSpecs.doSituacao(Situacao.ATIVO))
				.and(GrupoProdutoSpecs.isSemente())				
		);
		
		List<GrupoProdutoResumidoDto>  response = new ArrayList<>();
		for(GrupoProduto grupo: grupoProdutosList) {
			GrupoProdutoResumidoDto grupoProduto = new GrupoProdutoResumidoDto();
			BeanUtils.copyProperties(grupo, grupoProduto);
			response.add(grupoProduto);
		}
		return response;
	}
	
	public List<GrupoProdutoResumidoDto> findByGrupoProdutoFilter(GrupoProdutoFilter filter) {
		logger.info("Buscando grupo produto filter: {}", filter);		
		List<GrupoProduto> grupoProdutos = grupoProdutoRep.findAll(
				GrupoProdutoSpecs.codigoOuDescricaoLike(filter.getFmCodigoOudescricao())
				.and(GrupoProdutoSpecs.doTipoAvariado(filter.getTipoAvariado()))
				.and(GrupoProdutoSpecs.doSituacao(Situacao.ATIVO))
		);
		
		if(grupoProdutos.isEmpty()) {
			throw new ObjectNotFoundException("Não encontramos grupos para sua pesquisa");
		}
		
		List<GrupoProdutoResumidoDto>  response = new ArrayList<>();
		for(GrupoProduto grupo: grupoProdutos) {
			GrupoProdutoResumidoDto grupoProduto = new GrupoProdutoResumidoDto();
			BeanUtils.copyProperties(grupo, grupoProduto);
			response.add(grupoProduto);
		}
		return response;
	}
}

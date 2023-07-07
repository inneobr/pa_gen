package br.coop.integrada.api.pa.domain.service.produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.OperacaoProdutoEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.model.produto.VinculoProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoCodDescricaoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilter;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilterDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaValidDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoValidDto;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.spec.ProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class ProdutoService {
	private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

	@Autowired
	private final ProdutoRep produtoRep;

	@Autowired
	private final GrupoProdutoService grupoProdutoService;

	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	@Autowired
	private ProdutoReferenciaService referenciaService;
	
	@Autowired
	private ProdutorService produtorService;

	public List<ProdutoCodDescricaoDto> getCodigoOrDescricao(String request) {		
		Pageable limite = PageRequest.of(0, 15);	
		List<ProdutoCodDescricaoDto> produtos = new ArrayList<>();
		for(Produto item: produtoRep.findByCodItemContainingIgnoreCaseOrDescItemContainingIgnoreCase(request, request, limite)) {			
			ProdutoCodDescricaoDto produto = new ProdutoCodDescricaoDto();
			BeanUtils.copyProperties(item, produto);
			produtos.add(produto);
		}
		return produtos;
	}

	private Produto salvar(Produto produto) {
		List<VinculoProdutoReferencia> referencias = new ArrayList<>();

		if (produto.getReferencias() != null || !produto.getReferencias().isEmpty()) {
			for (VinculoProdutoReferencia item : produto.getReferencias()) {
				if (Strings.isEmpty(item.getProdutoReferencia().getCodRef())) {
					throw new ObjectDefaultException("Necessário informar o código de referencia!");
				}

				ProdutoReferencia referencia = referenciaService.buscarPorCodigoReferencia(item.getProdutoReferencia().getCodRef());
				VinculoProdutoReferencia vProdutoReferencia = new VinculoProdutoReferencia();
                vProdutoReferencia.setProdutoReferencia(referencia);
                vProdutoReferencia.setProduto(produto);
				
				
				if(item.getId() != null){
					vProdutoReferencia.setId(item.getId());
				}
				
				referencias.add(vProdutoReferencia);
				
			}
		}

		produto.setReferencias(referencias);

		return produtoRep.save(produto);
	}

	public Produto cadastrar(Produto produto) {
		logger.info("Salvando novo produto...");
		produto.setId(null);

		if(produto.getGrupoProduto() == null) {
			throw new ObjectDefaultException("Necessário informar o grupo de produto!");
		}
		else if(Strings.isEmpty(produto.getGrupoProduto().getFmCodigo())) {
			throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");
		}
		return salvar(produto);
	}

	public Produto atualizar(ProdutoDto request) {
		if (request.getCodItem() == null) {throw new ObjectDefaultException("Necessário informar o código do item para atualização o produto!");} 
		else if (request.getFmCodigo() == null) {throw new ObjectDefaultException("Necessário informar o grupo de produto!");} 
		else if (Strings.isEmpty(request.getFmCodigo())) {throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");}
		
		Produto produto = produtoRep.findByCodItemIgnoreCase(request.getCodItem());
		if (produto == null) {throw new ObjectNotFoundException("Produto não encontrado, códItem: " + request.getCodItem());}
		produto.getReferencias().clear();
		Produto produtoNovo = new Produto();		
		//BeanUtils.copyProperties(produto, produtoNovo);
		BeanUtils.copyProperties(request, produtoNovo);
		
		produtoNovo.setId(produto.getId());
		produtoNovo.setDataCadastro(produto.getDataCadastro());
		produtoNovo.setDataAtualizacao(new Date());
		
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(request.getFmCodigo().toString());
		if(grupoProduto == null) {throw new ObjectNotFoundException("Grupo d eproduto obrigatório.");}
		produtoNovo.setGrupoProduto(grupoProduto);
		
		if(request.getReferencias() != null) {
			for(ProdutoReferenciaIntegrationDto referencia: request.getReferencias()) {			
				VinculoProdutoReferencia vinculoProdutoReferencia = new VinculoProdutoReferencia();						
				ProdutoReferencia produtoReferencia = referenciaService.buscarPorCodigoReferencia(referencia.getCodRef());						
				vinculoProdutoReferencia.setProdutoReferencia(produtoReferencia);											
				vinculoProdutoReferencia.setProduto(produtoNovo);
				vinculoProdutoReferencia.setDataCadastro(new Date());					
				produtoNovo.getReferencias().add(vinculoProdutoReferencia);				
			}
		}
		try{
			return produtoRep.save(produtoNovo);
		}catch (Exception e) {
			throw new ObjectNotFoundException("Não foi possivel atualizar o produto: ", e);
		}		
	}
	
	
	public Produto salvar(ProdutoDto produtoDto) {
		logger.info("Salvando produto...");

		if (produtoDto.getCodItem() == null) {
			throw new ObjectDefaultException("Necessário informar o código do item para salvar o produto!");
		} else if (produtoDto.getFmCodigo() == null) {
			throw new ObjectDefaultException("Necessário informar o grupo de produto!");
		} else if (Strings.isEmpty(produtoDto.getFmCodigo())) {
			throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");
		}
				

		Produto produtoNovo = new Produto();
		BeanUtils.copyProperties(produtoDto, produtoNovo);
			
		
		produtoNovo.setDataCadastro(new Date());
		
		
		// Vincular Grupo de Produto
		String fmCodigo = produtoDto.getFmCodigo();
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(fmCodigo);
		produtoNovo.setGrupoProduto(grupoProduto);

		
		if(produtoDto.getReferencias() != null) {
			for(ProdutoReferenciaIntegrationDto vinProdRef: produtoDto.getReferencias()) {
								
				VinculoProdutoReferencia vinculoProdutoReferencia = new VinculoProdutoReferencia();
			
				ProdutoReferencia produtoReferencia = referenciaService.buscarPorCodigoReferencia(vinProdRef.getCodRef());
				
				vinculoProdutoReferencia.setProdutoReferencia(produtoReferencia);
									
				vinculoProdutoReferencia.setProduto(produtoNovo);
				vinculoProdutoReferencia.setDataCadastro(new Date());
				
				if(produtoNovo.getReferencias() == null) {
					//produtoNovo.setReferencias() = new ArrayList();
				}
			
				produtoNovo.getReferencias().add(vinculoProdutoReferencia);
				
				
			}
		}
		
		return produtoRep.save(produtoNovo);
		
	}
	
	public Produto atualizar(Produto produto) {
		logger.info("Atualizando produto...");

		if (produto.getCodItem() == null) {
			throw new ObjectDefaultException("Necessário informar o código do item para atualização o produto!");
		} else if (produto.getGrupoProduto() == null) {
			throw new ObjectDefaultException("Necessário informar o grupo de produto!");
		} else if (Strings.isEmpty(produto.getGrupoProduto().getFmCodigo())) {
			throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");
		}

		Produto produtoaux = produtoRep.findByCodItemIgnoreCase(produto.getCodItem());

		if (produtoaux == null) {
			throw new ObjectNotFoundException("Não foi encontrado produto com o código: " + produto.getCodItem());
		}

		Produto produtoNovo = new Produto();
		BeanUtils.copyProperties(produtoaux, produtoNovo);
		BeanUtils.copyProperties(produto, produtoNovo);
		produtoNovo.setDataAtualizacao(new Date());

		return salvar(produtoNovo);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Produto integrationSave(ProdutoDto produtoDto) {
		logger.info("Salvando produto...");

		if(Strings.isEmpty(produtoDto.getCodItem())) {
			throw new ObjectDefaultException("O campo {codItem} é obrigatório!");
		}

		Produto produto = produtoRep.findByCodItemIgnoreCase(produtoDto.getCodItem());
		produtoRep.flush();
		
		if(produto == null) {
		    produto = ProdutoDto.convertDto(produtoDto);
		    
		    if(produtoDto.getAtivo() != null && !produtoDto.getAtivo()) {
		    	return produto;
		    }else {
		    	produto.setDataIntegracao(new Date());
				produto.setStatusIntegracao(StatusIntegracao.INTEGRADO);
		    }
		    
		}else {
			
			if(produtoDto.getAtivo() != null && !produtoDto.getAtivo()) {
				if(produto.getDataInativacao() == null) {
					produto.setDataInativacao(new Date());
				}
				produto.setDataIntegracao(new Date());
				produto.setStatusIntegracao(StatusIntegracao.INTEGRADO);
				return produtoRep.save(produto);
				
			}else {			
				BeanUtils.copyProperties(produtoDto,produto); 
				produto.setDataIntegracao(new Date());
				produto.setDataInativacao(null);
				produto.setStatusIntegracao(StatusIntegracao.INTEGRADO);
			}
			
		}
		
		if (produtoDto.getFmCodigo() == null) {
			throw new ObjectDefaultException("Necessário informar o grupo de produto!");
		}
		else if (Strings.isEmpty(produtoDto.getFmCodigo())) {
			throw new ObjectDefaultException("O campo {fmCodigo} é obrigatório!");
		}

		produto.setGrupoProduto(grupoProdutoService.buscarGrupoFmCodigo(produtoDto.getFmCodigo()));
				
		if (produtoDto.getReferencias() != null && !produtoDto.getReferencias().isEmpty()) {
			
			boolean encontrouPr = false;
			List<VinculoProdutoReferencia> referenciasRemovidas = new ArrayList<>();
			for(VinculoProdutoReferencia pr : produto.getReferencias()) {
				for (ProdutoReferenciaIntegrationDto prDto : produtoDto.getReferencias()) {
					if(pr.getProdutoReferencia().getCodRef().equals(prDto.getCodRef()) && prDto.getAtivo() != null && prDto.getAtivo()) {
						encontrouPr = true;
					}
				}
				
				if(!encontrouPr) {
					referenciasRemovidas.add(pr);
				}else {
					encontrouPr = false;
				}
			}
			
			if(referenciasRemovidas.size() > 0) {
				produto.getReferencias().removeAll(referenciasRemovidas);
			}
			
			for (ProdutoReferenciaIntegrationDto item : produtoDto.getReferencias()) {
				
				if (Strings.isEmpty(item.getCodRef())) {
					throw new ObjectDefaultException("Necessário informar o código de referencia!");
				}
				
				ProdutoReferencia referencia = referenciaService.buscarPorCodigoReferenciaSemValidacao(item.getCodRef());				
				if(referencia == null) {
					logger.info("A referência " + item.getCodRef() + " será cadastrada!");
					referencia = new ProdutoReferencia();
					referencia.setCodRef(item.getCodRef());
					referencia.setDataIntegracao(new Date());
					referencia.setStatusIntegracao(StatusIntegracao.INTEGRADO);
					referencia = referenciaService.salvar(referencia);
					
				}				

				boolean encontrou = false;
				for(VinculoProdutoReferencia produtoReferencia : produto.getReferencias()) {
					if(produtoReferencia.getProdutoReferencia() != null && produtoReferencia.getProdutoReferencia().getCodRef().equals(item.getCodRef())) {
						if(item.getAtivo() != null && !item.getAtivo()) {
							if(produtoReferencia.getDataInativacao() == null) {
								produtoReferencia.setDataInativacao(new Date());
							}
						}else {
							produtoReferencia.setDataInativacao(null);
						}

						produtoReferencia.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						produtoReferencia.setDataIntegracao(new Date());
						encontrou = true;
					}
				}
				
				if(!encontrou && item.getAtivo() != null && item.getAtivo()) {
					VinculoProdutoReferencia vProdutoReferencia = new VinculoProdutoReferencia();
					vProdutoReferencia.setStatusIntegracao(StatusIntegracao.INTEGRADO);
					vProdutoReferencia.setDataIntegracao(new Date());
					vProdutoReferencia.setProdutoReferencia(referencia);
					vProdutoReferencia.setProduto(produto);
					vProdutoReferencia.setDataInativacao(null);										
					produto.getReferencias().add(vProdutoReferencia);
				}

			}
		}else {
			if(produto.getReferencias() != null && !produto.getReferencias().isEmpty()) {
				produto.getReferencias().clear();
			}
		}

		return produtoRep.save(produto);
	}
	
	public Page<Produto> listarProdutoAtivos(Pageable pageable) {
		Page<Produto> produto = produtoRep.findByDataInativacaoNull(pageable);
		logger.info("Listando produtos ativos...");		
		return produto;

	}
	
	public Page<Produto> buscarTodosProduto(Pageable pageable, String filtro, Situacao situacao) {
		logger.info("Listando todos produtos...");
		return produtoRep.findAll(pageable, filtro, situacao);
	}
	
	public Page<Produto> buscarTodosProduto(Pageable pageable, ProdutoFilter filtro, Situacao situacao) {
		logger.info("Listando todos produtos...");
		return produtoRep.findAll(pageable, filtro, situacao);
	}
	
	public Produto inativarProduto(Long id) {
		Produto produto  = produtoRep.getReferenceById(id);
		produto.setDataInativacao(new Date());
		logger.info("Inativando produto...");
		return produtoRep.save(produto);
	}
	
	public Produto ativarProduto(Long id) {
		Produto produto  = produtoRep.getReferenceById(id);
		produto.setDataInativacao(null);
		produto.setDataAtualizacao(new Date());
		logger.info("Inativando produto...");
		return produtoRep.save(produto);
	}
	
	public Produto buscarProdutoPorId(Long id) { 
		logger.info("Buscando id produto...");
		return produtoRep.findById(id).orElse(null);
	}
	
	public Produto buscarProdutoAtivoPorCodItem(String codItem) { 
		logger.info("Buscando produto...");
		Produto produto = produtoRep.findByCodItemAndDataInativacaoNull(codItem);

		if(produto == null) {
			throw new NullPointerException("O produto não foi encontrado! CODIGO: " + codItem + ", Tipo: " + ProdutoService.class.getName());
		}

		return produto;
	}
	
	public Produto buscarPorCodigo(String codItem) {
		return produtoRep.findByCodItemAndDataInativacaoNull(codItem);
	}

	public Page<Produto> pesquisarProdutoPorCodigoOuDescricaoVinculadoAoGrupoProduto(String codigoOuDescricao, String codigoGrupoProduto, Pageable pageable) {
		return produtoRep.findAll(codigoOuDescricao, codigoGrupoProduto, pageable);
	}

	public ProdutoValidDto validaProduto(String codigoItem) {
		Produto produto = produtoRep.findByCodItemAndDataInativacaoNull(codigoItem);
		
		if(produto == null) {
			logger.info("Produto {} não encontrado.", codigoItem);
			return ProdutoValidDto.construir(false, null, null);
		}
		
		logger.info("Produto {} encontrado", codigoItem);
		
		String descricao = produto.getDescItem();
		String codigoGrupoProduto = produto.getGrupoProduto().getFmCodigo(); 
		return ProdutoValidDto.construir(true, descricao, codigoGrupoProduto);
	}
	
	public ProdutoReferenciaValidDto getProdutoReferencia(String codigoProduto, String codigoReferencia) {
		logger.info("Validando produto {} com referência {}.", codigoProduto, codigoReferencia);
		Produto produto = produtoRep.findByCodItem(codigoProduto);
		
		for(VinculoProdutoReferencia item: produto.getReferencias()) {
			if(item.getProdutoReferencia().getCodRef().equals(codigoReferencia)) {
				logger.info("Produto encntrado: {}", produto.getDescItem());
				return ProdutoReferenciaValidDto.construir(true, produto.getDescItem(), item.getProdutoReferencia().getCodRef());
			}			
		}
		
		logger.info("Não existem produto com a referência {} cadastrado", codigoReferencia);
		return ProdutoReferenciaValidDto.construir(false, null, null);
	}
	
	public void validarProdutoEGrupoProduto(String codigoProduto, String codigoGrupoProduto) {
		ProdutoValidDto produtoValidDto = validaProduto(codigoProduto);
		
		if(!produtoValidDto.getCadastrado()) {
			throw new ObjectNotFoundException("Produto não cadastrado");
		}
		
		Boolean isGrupoProdutoEhDiferenteDoGrupoProdutoInformadoNoParametro = !produtoValidDto.getCodigoGrupoProduto().equals(codigoGrupoProduto); 		
		if(isGrupoProdutoEhDiferenteDoGrupoProdutoInformadoNoParametro){
			throw new ObjectDefaultException("Produto não pertence ao grupo de produto informado na RE.");
		}
	}
	
	//Objetivo: Buscar na tabela de produto o código Datasul do item
	public String buscarItemDataSul(String codProduto, OperacaoProdutoEnum operacao, String codProdutor ) {
		
		Produtor produtor = produtorService.buscarPorCodigoProdutor(codProdutor);
		
		if(produtor == null) {
			throw new NotFoundException("produtor não cadastrado");
		}
		
		if(produtor.getTipoProdutor() == null) {
			throw new NotFoundException("O produtor informado não contém o campo 'Tipo do Produtor' preenchido.");
		}
		
		Produto produto = produtoRep.findByCodItemAndDataInativacaoNull(codProduto);
		
		if(produto == null) {
			throw new NotFoundException("Produto não cadastrado");
		}
				
		
		if(operacao.equals(OperacaoProdutoEnum.AFIXAR)) {

			if(produtor.getTipoProdutor().toUpperCase().equals("TERC")) {
				return produto.getCodItemAfTerc();
			}
			else {
				return produto.getCodItemAfCoop();
			}
		}
		
		if(operacao.equals(OperacaoProdutoEnum.FIXACAO)) {
			if(produtor.getTipoProdutor().toUpperCase().equals("TERC")) {
				return produto.getCodItemFicaTerc();
			}
			else {
				return produto.getCodItemFixaCoop();
			}
		}
		
		return null;
		
	}
	
	public List<ProdutoFilterDto> buscarProdutoPorGrupo(Long idGrupo) {
		logger.info("Buscando produto por grupo: ", idGrupo);
		List<ProdutoFilterDto> response = new ArrayList<>();
		for(Produto item: produtoRep.findByGrupoProdutoId(idGrupo)) {
			ProdutoFilterDto produto = new ProdutoFilterDto();
			BeanUtils.copyProperties(item, produto);
			response.add(produto);
		}
		return response;
	}
	
	public List<ProdutoFilterDto> findProdutoByGrupoFilter(ProdutoFilter filter) {
		logger.info("Buscando produto por grupo: ", filter);
		List<ProdutoFilterDto> response = new ArrayList<>();
		List<Produto> listaProdutos =  produtoRep.findAll(
				ProdutoSpecs.codigoOuDescricaoLike(filter.getCodigoNome())	
				.and(ProdutoSpecs.doGrupo(filter.getIdGrupo()))
				.and(ProdutoSpecs.situacaoEquals(filter.getSituacao()))
			);
		
		if(listaProdutos.isEmpty()) {
			throw new ObjectNotFoundException("Não encontramos produtos para sua pesquisa");
		}
		for(Produto item: listaProdutos) {
			ProdutoFilterDto produto = new ProdutoFilterDto();
			BeanUtils.copyProperties(item, produto);
			response.add(produto);
		}
		return response;
	}
	
}


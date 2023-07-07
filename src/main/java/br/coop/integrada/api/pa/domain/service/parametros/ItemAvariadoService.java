package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum.CHUVADO_AVARIADO;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.ITEM_AVARIADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRAR;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectFieldErrorsException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.VinculoProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.ItemAvariadoGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoDetalheDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration.ItemAvariadoDetalheIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration.ItemAvariadoEstabelecimentoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration.ItemAvariadoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.item.avariado.integration.ItemAvariadoIntegrationListDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ReferenciaGrupoDto;
import br.coop.integrada.api.pa.domain.repository.parametros.ItemAvariadoRep;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.spec.ItemAvariadoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemAvariadoService {
	private static final Logger logger = LoggerFactory.getLogger(ItemAvariadoService.class);

    @Autowired
    private ItemAvariadoRep itemAvariadoRep;

    @Autowired
    private ProdutoService produtoService;
    
	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	@Autowired
	private final ProdutoRep produtoRep;

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private ItemAvariadoDetalheService itemAvariadoDetalheService;

    @Autowired
    private ItemAvariadoEstabelecimentoService itemAvariadoEstabelecimentoService;

	@Value("${spring.profiles.active}")
	private String profileActive;

    public ItemAvariado cadastrar(ItemAvariadoDto objDto) {
    	logger.info("Cadastrando item avariado {}", objDto.getDescricao());
        objDto.setId(null);
        validarVinculosDuplicados(objDto);

        for(ItemAvariadoDetalheDto item : objDto.getDetalhes()) { 
        	item.setId(null); 
        }        
        ItemAvariado itemAvariado = new ItemAvariado();
        BeanUtils.copyProperties(objDto, itemAvariado);

        itemAvariado = salvar(itemAvariado, objDto.getDetalhes(), objDto.getEstabelecimentos());

        historicoGenericoService.salvar(itemAvariado.getId(),PaginaEnum.ITEM_AVARIADO,"Inclusão do Item Avariado",itemAvariado.getDescricao());

        return itemAvariado;
    }

    public ItemAvariado atualizar(ItemAvariadoDto objDto) {
        if(objDto.getId() == null) {
            throw new ObjectDefaultException("Para atualizar é necessário informar o ID do Item Avariado!");
        }

        ItemAvariado itemAvariado = itemAvariadoRep.findById(objDto.getId()).orElse(null);
        if(itemAvariado == null) {
            throw new ObjectDefaultException("Para atualizar é necessário informar o ID do Item Avariado!");
        }

        validarVinculosDuplicados(objDto);
        String alteracoesItemAvariado = getAlteracoesItemAvariado(objDto);
        String AlteracoesItemAvariadoDetalhes = getAlteracoesItemAvariadoDetalhes(objDto.getId(), objDto.getDetalhes());
        String detalhesExcluidos = getDetalhesExcluidos(objDto.getId(), objDto.getDetalhes());
        String estabelecimentosExcluidos = getEstabelecimentosExcluidos(objDto.getId(), objDto.getEstabelecimentos());

        BeanUtils.copyProperties(objDto, itemAvariado);

        List<ItemAvariadoDetalhe> detalhesParaInativar = itemAvariadoDetalheService.getDetalhesParaInativar(itemAvariado.getId(), objDto.getDetalhes());
        List<ItemAvariadoEstabelecimento> estabelecimentosParaInativar = itemAvariadoEstabelecimentoService.getEstabelecimentosParaInativar(itemAvariado.getId(), objDto.getEstabelecimentos());

        itemAvariado = salvar(itemAvariado, objDto.getDetalhes(), objDto.getEstabelecimentos());
        itemAvariadoDetalheService.inativar(detalhesParaInativar);
        itemAvariadoEstabelecimentoService.inativar(estabelecimentosParaInativar);

        if(!alteracoesItemAvariado.isEmpty()) {
            historicoGenericoService.salvar(
                    itemAvariado.getId(),
                    PaginaEnum.ITEM_AVARIADO,
                    "Atualização do Item Avariado",
                    alteracoesItemAvariado
            );
        }

        if(!AlteracoesItemAvariadoDetalhes.isEmpty()) {
            historicoGenericoService.salvar(
                    itemAvariado.getId(),
                    PaginaEnum.ITEM_AVARIADO,
                    "Atualização dos detalhes",
                    AlteracoesItemAvariadoDetalhes
            );
        }

        if(!detalhesExcluidos.isEmpty()) {
            historicoGenericoService.salvar(
                    itemAvariado.getId(),
                    PaginaEnum.ITEM_AVARIADO,
                    "Exclusão Detalhe",
                    "Lista de detalhes que foram excluídos:" + detalhesExcluidos.toString()
            );
        }

        if(!estabelecimentosExcluidos.isEmpty()) {
            historicoGenericoService.salvar(
                    itemAvariado.getId(),
                    PaginaEnum.ITEM_AVARIADO,
                    "Exclusão Estabelecimento",
                    "Lista de estabelecimentos que foram excluídos:" + estabelecimentosExcluidos.toString()
            );
        }

        return itemAvariado;
    }

    public ItemAvariado salvar(ItemAvariado obj, List<ItemAvariadoDetalheDto> detalheDtos, List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
    	if(obj.getCampoValidacao().equals(CHUVADO_AVARIADO)) validarDetalhes(detalheDtos);

    	StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(ITEM_AVARIADO);
    	obj.setStatusIntegracao(statusIntegracao);
    	obj.setDataIntegracao(null);

    	ItemAvariado itemAvariado = itemAvariadoRep.save(obj);
    	itemAvariadoDetalheService.salvar(itemAvariado, detalheDtos);
    	itemAvariadoEstabelecimentoService.salvar(itemAvariado, estabelecimentoDtos);

    	return itemAvariado;
    }

    public Page<ItemAvariadoSimplesDto> getItemAvariadoFilter(Pageable pageable, ItemAvariadoFilter filter) {
    	logger.info("Buscando item Avariado filter: {}", filter);
        Page<ItemAvariado> itemAvariados = itemAvariadoRep.findAll(
        		ItemAvariadoSpecs.doIdGrupoProduto(filter.getGrupoProduto())
                .and(ItemAvariadoSpecs.doIdEstabelecimento(filter.getEstabelecimento()))
                .and(ItemAvariadoSpecs.doSituacao(Situacao.ATIVO)),
                pageable);
        
        List<ItemAvariadoSimplesDto> itemAvariadoDtos = ItemAvariadoSimplesDto.construir(itemAvariados.getContent());
        return new PageImpl<>(
                itemAvariadoDtos,
                pageable,
                itemAvariados.getTotalElements()
        );
    }

    public ItemAvariado buscarPorId(Long id) {
        ItemAvariado obj = itemAvariadoRep.getReferenceById(id);

        if(obj == null) {
            throw new ObjectNotFoundException("O Item Avariado não foi encontrado! ID: " + id + ", Tipo: " + ItemAvariado.class.getName());
        }

        return obj;
    }

    public List<ItemAvariadoDetalhe> buscarDetalhesPorItemAvariado(Long id) {
        ItemAvariado obj = buscarPorId(id);
        return obj.getDetalhes();
    }
    
    public List<ItemAvariado> buscarPorStatus(StatusIntegracao status) {
    	return itemAvariadoRep.findByStatusIntegracao(status);
    }
    
    public void alterarStatusIntegracao(StatusIntegracao status, List<ItemAvariado> objs) {
    	if(CollectionUtils.isEmpty(objs)) return;
    	List<ItemAvariado> itemAvariados = objs.stream().map(itemAvariado -> {
    		itemAvariadoDetalheService.alterarStatusIntegracao(status, itemAvariado.getDetalhes());
    		itemAvariadoEstabelecimentoService.alterarStatusIntegracao(status, itemAvariado.getEstabelecimentos());

    		itemAvariado.setDataIntegracao(null);
    		itemAvariado.setStatusIntegracao(status);
    		return itemAvariado;
    	}).toList();
    	itemAvariadoRep.saveAll(itemAvariados);
    }
    
    public List<ItemAvariado> buscarItensAvariadoComStatus(StatusIntegracao status) {
    	List<ItemAvariado> itemAvariados = buscarPorStatus(status);
    	if(CollectionUtils.isEmpty(itemAvariados)) return new ArrayList<>();
    	
    	List<ItemAvariado> retItemAvariados = new ArrayList<ItemAvariado>();
    	for(ItemAvariado itemAvariado : itemAvariados) {
    		ItemAvariado retItemAvariado = new ItemAvariado();
    		BeanUtils.copyProperties(itemAvariado, retItemAvariado);
    		
    		List<ItemAvariadoDetalhe> detalhes = itemAvariadoDetalheService.buscarPorItemAvariadoComStatus(itemAvariado, status);
    		List<ItemAvariadoEstabelecimento> estabelecimentos = itemAvariadoEstabelecimentoService.buscarPorItemAvariadoComStatus(itemAvariado, status);
    		
    		retItemAvariado.setDetalhes(detalhes);
    		retItemAvariado.setEstabelecimentos(estabelecimentos);
    		retItemAvariados.add(retItemAvariado);
    	}
    	
    	return retItemAvariados;
    }

    public List<Estabelecimento> buscarEstabelecimentosPorItemAvariado(Long id) {
    	ItemAvariado obj = buscarPorId(id);

    	if(CollectionUtils.isEmpty(obj.getEstabelecimentos())) return new ArrayList<>();

    	return obj.getEstabelecimentos().stream().map(itemAvariadoEstabelecimento -> {
    		return itemAvariadoEstabelecimento.getEstabelecimento();
    	}).toList();
    }
    
    public void inativar(Long id) {
        ItemAvariado obj = buscarPorId(id);
        obj.setDataInativacao(new Date());
        itemAvariadoRep.save(obj);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.ITEM_AVARIADO,
                "Item Avariado",
                "Item Avariado inativado"
        );
    }

    public void ativar(Long id) {
        ItemAvariado obj = buscarPorId(id);
        obj.setDataInativacao(null);
        itemAvariadoRep.save(obj);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.ITEM_AVARIADO,
                "Item Avariado",
                "Item Avariado ativado"
        );
    }   
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void sincronizarItensAvariados(Long id) {
    	IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.ITEM_AVARIADO, FuncionalidadePaginaEnum.INT_LOTE_ITEM_AVARIADO);
    	if(pagina == null || ERP.equals(pagina.getOrigenEnum())) return; 
    	
    	IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_ITEM_AVARIADO);
		if(paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null || paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) return;

		List<ItemAvariado> itemAvariados = null;
    	if(id == null) {
    		itemAvariados = buscarItensAvariadoComStatus(INTEGRAR);	    		
    	}else {
    		ItemAvariado itemAvariado = itemAvariadoRep.getReferenceById(id);
    		if(itemAvariado != null) {
    			itemAvariados = new ArrayList<>();
    			itemAvariados.add(itemAvariado);
			}
    	}
    	
    	if(CollectionUtils.isEmpty(itemAvariados)) return;
    	alterarStatusIntegracao(PROCESSANDO, itemAvariados);	    	
    	ItemAvariadoIntegrationListDto objDto = ItemAvariadoIntegrationListDto.construir(itemAvariados);	
    	
	    try {
	    	IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
    		
    		LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();    		
    		if(pagina.getHeaders() != null) {
	    		for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
	    			if(pagina.isHearderProfileActive(header, profileActive)) {
		    			if(!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())){
		    				mvmap.add(header.getChave(), header.getValor());
		    			}
	    			}
	    		}	
    		}
    		
    		String url = pagina.getUrlPrincipalApi(profileActive);
    		if(url == null) return;

    		WebClient client = WebClient.builder().baseUrl(url)
    				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    				.filter(ExchangeFilterFunctions.basicAuthentication(auth.getLogin(), auth.getSenha()))
    				.build();
    		
    		HttpMethod httpMethod = IntegrationService.getMetodo(paginaFuncionalidade);

    		Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);
    		client.method(httpMethod)
    		.uri(paginaFuncionalidade.getEndPointSend())
			.headers(consumer)
    		.body(BodyInserters.fromValue(objDto))
    		.retrieve()
    		.bodyToFlux(ItemAvariadoIntegrationListDto.class)
    		.subscribe(itemAvariadoList -> {
    			List<ItemAvariadoIntegrationDto> itens = itemAvariadoList.getItensAvariados();

    			for(ItemAvariadoIntegrationDto item : itens) {
    				try {
    					StatusIntegracao statusIntegracaoCabecalho = item.getIntegrated() ? INTEGRADO : INTEGRAR;    					
    					ItemAvariado itemAvariado = itemAvariadoRep.findById(item.getIdGrupo()).orElse(null);
    					
    					if(itemAvariado != null) {
    						itemAvariado.setDataIntegracao(statusIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null);
    						itemAvariado.setStatusIntegracao(statusIntegracaoCabecalho);
    						itemAvariadoRep.save(itemAvariado);
    						
    						itemAvariadoDetalheService.updateStatus(statusIntegracaoCabecalho, itemAvariado.getId());
    						itemAvariadoEstabelecimentoService.updateStatus(statusIntegracaoCabecalho, itemAvariado.getId()); 
    					}
    				}
    				catch (Exception e) {
    					logger.error("Retorno da sincronização de ITEM AVARIADO");
    					logger.error("ITEM AVARIADO: " + item.toString());
    				}
    			}
    		});
		}catch (Exception e) {
			alterarStatusIntegracao(INTEGRAR, itemAvariados);
    		e.printStackTrace();
		}
    }
    
    private void validarDetalhes(List<ItemAvariadoDetalheDto> detalheDtos) {
        Collections.sort(detalheDtos, Comparator.comparing(ItemAvariadoDetalheDto::getPercentualInicial));
        List<FieldErrorItem> fieldErrorItems = new ArrayList<>();
        BigDecimal ultimoPercentual = null;

        for(ItemAvariadoDetalheDto detalheDto : detalheDtos) {
            try {
                ultimoPercentual = validarRangeDetalhe(detalheDto, ultimoPercentual);
            }
            catch (RuntimeException e) {
                fieldErrorItems.add(FieldErrorItem.construir("Percentual Inicial", e.getMessage()));
            }
        }

        if(fieldErrorItems.size() > 0) {
            throw new ObjectFieldErrorsException("Validação dos campos dos detalhes do Item Avariado", fieldErrorItems);
        }
    }

    private BigDecimal validarRangeDetalhe(ItemAvariadoDetalheDto detalheDto, BigDecimal ultimoPercentual) {
        var percentualInicial = detalheDto.getPercentualInicial();
        var diferencaPercentual = new BigDecimal("0.1");

        if(ultimoPercentual == null) {
            return getUltimoPercentual(detalheDto);
        }
        else {
            if(!percentualInicial.subtract(ultimoPercentual).equals(diferencaPercentual)) {
                throw new RuntimeException("Possui uma diferença entre o Percentual Final (" + ultimoPercentual + ") do detalhe anterior com o Percentual Inicial (" + percentualInicial + ")");
            }

            return getUltimoPercentual(detalheDto);
        }
    }

    private BigDecimal getUltimoPercentual(ItemAvariadoDetalheDto detalheDto) {
        var percentualInicial = detalheDto.getPercentualInicial();
        var percentualFinal = detalheDto.getPercentualFinal();

        if(percentualInicial.compareTo(percentualFinal) == 1) {
            throw new RuntimeException("O percentual inicial (" + percentualInicial + ") é maior que o percentual final (" + percentualFinal + ")");
        }

        return percentualFinal;
    }

    private List<String> getItemAvariadoComMesmasInformacoes(ItemAvariadoDto objDto) {
        List<Long> idGupoProdutos = new ArrayList<>();
        List<Long> idEstabelecimentos = new ArrayList<>();

        for(ItemAvariadoDetalheDto item : objDto.getDetalhes()) {
            Produto produto = produtoService.buscarProdutoPorId(item.getProduto().getId());

            if(produto == null) {
                throw new ObjectNotFoundException("O Produto não foi encontrado! ID: " + item.getProduto().getId() + ", Tipo: " + Produto.class.getName());
            }

            idGupoProdutos.add(produto.getGrupoProduto().getId());
        }

        for(EstabelecimentoSimplesDto item : objDto.getEstabelecimentos()) {
            Estabelecimento estabelecimento = estabelecimentoService.buscarPorId(item.getId());
            idEstabelecimentos.add(estabelecimento.getId());
        }

        if(objDto.getId() == null) {
            objDto.setId(0l);
        }

        String tipoValidacao = objDto.getCampoValidacao().toString();
        System.out.println(tipoValidacao);
        return itemAvariadoRep.findByTipoGrupoEstabelecimento(
                objDto.getId(),
                tipoValidacao,
                idGupoProdutos,
                idEstabelecimentos
        );
    }

    private void validarVinculosDuplicados(ItemAvariadoDto objDto) {
        List<String> vinculosDuplicados = getItemAvariadoComMesmasInformacoes(objDto);
        if(vinculosDuplicados.size() > 0) {
            StringBuilder resultado = new StringBuilder();

            for (String vinculo : vinculosDuplicados) {
                resultado.append(vinculo).append("; ");
            }

            throw new RuntimeException("Já existe um cadastro de item avariado para esse grupo de produto, estabelecimento e tipo de validação de chuvado e avariado:");
        }
    }

    private String getAlteracoesItemAvariado(ItemAvariadoDto objDto) {
        ItemAvariado itemAvariado = buscarPorId(objDto.getId());
        ItemAvariadoDto itemAvariadoDto = ItemAvariadoDto.construir(itemAvariado);

        try {
            return CompararObjetos.comparar(itemAvariadoDto, objDto);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return "";
    }

    private String getAlteracoesItemAvariadoDetalhes(Long idItemAvariado, List<ItemAvariadoDetalheDto> objDtos) {
        List<ItemAvariadoDetalhe> itemAvariadoDetalhes = buscarDetalhesPorItemAvariado(idItemAvariado);
        List<ItemAvariadoDetalheDto> itemAvariadoDetalheDtos = ItemAvariadoDetalheDto.construir(itemAvariadoDetalhes);

        String alteracoes = "";
        for(ItemAvariadoDetalheDto item : objDtos) {
            ItemAvariadoDetalheDto detalheAtual = itemAvariadoDetalheDtos.stream()
                    .filter(itemAvariadoDetalheDto -> { return itemAvariadoDetalheDto.getId() == item.getId(); })
                    .findFirst().orElse(null);
            if(detalheAtual != null) {
                try {
                    alteracoes += CompararObjetos.comparar(detalheAtual, item);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        return alteracoes;
    }

    private String getDetalhesExcluidos(Long idItemAvariado, List<ItemAvariadoDetalheDto> objDtos) {
        List<ItemAvariadoDetalhe> itemAvariadoDetalhes = buscarDetalhesPorItemAvariado(idItemAvariado);
        List<ItemAvariadoDetalheDto> itemAvariadoDetalheDtosAtual = ItemAvariadoDetalheDto.construir(itemAvariadoDetalhes);

        StringBuilder detalhesExcluidos = new StringBuilder();
        for(ItemAvariadoDetalheDto detalheAtual : itemAvariadoDetalheDtosAtual) {
            ItemAvariadoDetalheDto detalhe = objDtos.stream()
                    .filter(itemAvariadoDetalheDto -> { return itemAvariadoDetalheDto.getId() == detalheAtual.getId(); })
                    .findFirst().orElse(null);

            if(detalhe == null) {
                detalhesExcluidos.append("\nPercentual Inicial: \"");
                detalhesExcluidos.append(detalheAtual.getPercentualInicial());
                detalhesExcluidos.append("\", Percentual Final: \"");
                detalhesExcluidos.append(detalheAtual.getPercentualFinal());
                detalhesExcluidos.append("\", Grupo Produto: \"");
                detalhesExcluidos.append(detalheAtual.getGrupoProduto().getFmCodigo());
                detalhesExcluidos.append("\", Produto: \"");
                detalhesExcluidos.append(detalheAtual.getProduto().getCodigo());
                detalhesExcluidos.append("\"");
            }
        }

        return detalhesExcluidos.toString();
    }

    private String getEstabelecimentosExcluidos(Long idItemAvariado, List<EstabelecimentoSimplesDto> objDtos) {
        List<Estabelecimento> estabelecimentos = buscarEstabelecimentosPorItemAvariado(idItemAvariado);
        List<EstabelecimentoSimplesDto> estabelecimentoSimplesDtos = EstabelecimentoSimplesDto.construir(estabelecimentos);

        StringBuilder estabelecimentosExcluidos = new StringBuilder();
        for(EstabelecimentoSimplesDto estabelecimentoAtual : estabelecimentoSimplesDtos) {
            EstabelecimentoSimplesDto estabelecimento = objDtos.stream()
                    .filter(item -> { return item.getId() == estabelecimentoAtual.getId(); })
                    .findFirst().orElse(null);

            if(estabelecimento == null) {
                estabelecimentosExcluidos.append("\nCódigo: \"");
                estabelecimentosExcluidos.append(estabelecimentoAtual.getCodigo());
                estabelecimentosExcluidos.append("\", Nome fantasia: \"");
                estabelecimentosExcluidos.append(estabelecimentoAtual.getNomeFantasia());
                estabelecimentosExcluidos.append("\"");
            }
        }

        return estabelecimentosExcluidos.toString();
    }
    
    public List<ItemAvariadoGrupoProdutoDto> buscarGrupoProdutoCodigoDescricao(String dados) {
	    Pageable limite = PageRequest.of(0, 5);
	    List<ItemAvariadoGrupoProdutoDto> grupoProdutoLista = new ArrayList<>();	    
	    for(GrupoProduto grupoProdutoUnico: grupoProdutoRep.findByFmCodigoContainingOrDescricaoContainingIgnoreCase(dados, dados, limite)) {
	    	ItemAvariadoGrupoProdutoDto grupoProduto = new ItemAvariadoGrupoProdutoDto();
	        BeanUtils.copyProperties(grupoProdutoUnico, grupoProduto);
	       
	        
	        List<ProdutoGetDto> produtosList = new ArrayList<>();
	        for(Produto produto: produtoRep.findByGrupoProduto(grupoProdutoUnico)) {
	        	ProdutoGetDto produtoDto = new  ProdutoGetDto();
	            BeanUtils.copyProperties(produto, produtoDto);	 
	            
	            List<ReferenciaGrupoDto> referencias = new ArrayList<>();
	            for(VinculoProdutoReferencia vProdutoReferencia: produto.getReferencias()) {
	            	ReferenciaGrupoDto referenciaDto = new ReferenciaGrupoDto();
	                //BeanUtils.copyProperties(vProdutoReferencia, referenciaDto);
	            	
	            	referenciaDto.setId(vProdutoReferencia.getProdutoReferencia().getId());
	            	referenciaDto.setCodRef(vProdutoReferencia.getProdutoReferencia().getCodRef());
	            	
	                referencias.add(referenciaDto);
	            }
	            produtoDto.setReferencia(referencias);
	            produtosList.add(produtoDto);
	        }	
	        grupoProduto.setProduto(produtosList);
	        grupoProdutoLista.add(grupoProduto);
	    }       
	    return grupoProdutoLista;
	   
	}
    
    public ItemAvariadoDetalhe buscarPor(String codigoGrupoProduto, String codigoEstabelecimento, BigDecimal percentual, BigInteger ph, BigDecimal fnt) {
		ItemAvariadoDetalhe itemAvariadoDetalhe = null;
		
		if(ph != null && ph.compareTo(BigInteger.ZERO) > 0) {
			try {
				itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoPh(codigoGrupoProduto, codigoEstabelecimento, percentual, ph, fnt);
			}
			catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		else {
			// PRIMEIRO FAZ A BUSCA POR PRODUTO
			try {
				itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoProduto(codigoGrupoProduto, codigoEstabelecimento);
			}
			catch (Exception e) {
				logger.info(e.getMessage());
			}
			
			// CASO NÃO ENCONTRE POR PRODUTO, EH REALIZADO OUTRA BUSCA POR CHUVADO/AVARIADO PASSANDO O PERCENTUAL
			if(itemAvariadoDetalhe == null) {
				try {
					itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoChuvadoAvaraido(codigoGrupoProduto, codigoEstabelecimento, percentual);
				}
				catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}
		
		return itemAvariadoDetalhe;
	}
    
    public Page<EstabelecimentoNomeCodigoDto> findByItemAvariado(Long id, Pageable pageable) {
    	Page<ItemAvariadoEstabelecimento> itemPage = itemAvariadoEstabelecimentoService.findByItemAvariadoId(id, pageable);
    	List<EstabelecimentoNomeCodigoDto> estabelecimentosResponse = new ArrayList<>();
    	for(ItemAvariadoEstabelecimento item: itemPage.getContent()) {
    		EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
    		BeanUtils.copyProperties(item.getEstabelecimento(), estabelecimento);
    		estabelecimentosResponse.add(estabelecimento);
    	}
    	return new PageImpl<>(estabelecimentosResponse, pageable, itemPage.getTotalElements());
    }
    
    public void deletar(Long id) {
    	itemAvariadoDetalheService.deleteDetalhes(id);
    	itemAvariadoEstabelecimentoService.deleteEstabelecimentos(id);	
    	
    	logger.info("Deletar item avariado id: {}", id);
        itemAvariadoRep.deleteById(id);
    }   
    
}

package br.coop.integrada.api.pa.domain.service.produto;

import static br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum.INT_LOTE_SAFRA_COMPOSTA;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.SAFRA_COMPOSTA;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.util.ArrayList;
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

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.produto.RelacionamentosDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoResponseIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoResumidoDto;
import br.coop.integrada.api.pa.domain.repository.produto.TipoProdutoRep;
import br.coop.integrada.api.pa.domain.service.integration.IntegracaoLogService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.spec.TipoProdutoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoProdutoService {

	private static final Logger logger = LoggerFactory.getLogger(TipoProdutoService.class);
	private final TipoProdutoRep tipoProdutoRep;

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	private IntegracaoLogService integracaoLogService;

	@Value("${spring.profiles.active}")
	private String profileActive;

	public Page<TipoProduto> listarTipoProdutoAtivos(String nome, Pageable pageable) {
		logger.info("Listando tipo de produto ativo...");
		return tipoProdutoRep.findByNomeContainingIgnoreCaseAndDataInativacaoNull(nome, pageable);
	}

	public Page<TipoProduto> listarTodosTipoProduto(Pageable pageable) {
		logger.info("Listando todos tipo de produto...");
		return tipoProdutoRep.findAll(pageable);
	}

	public List<TipoProduto> buscaTipoProdutoPorNome(String nome) {
		Pageable limit = PageRequest.of(0, 10);
		logger.info("buscando tipo de produto por nome...");

		if (nome == null || nome.isEmpty()) {
			// return tipoProdutoRep.findAll(limit).getContent();
			return tipoProdutoRep.findByDataInativacaoNull(limit).getContent();
		}

		return tipoProdutoRep.findByDataInativacaoNullAndNomeContainingIgnoreCase(nome, limit);
	}

	public TipoProduto buscarPorNome(String nome) {
		return tipoProdutoRep.findByNomeIgnoreCaseAndDataInativacaoIsNull(nome);
	}

	public TipoProduto cadastrarOuAtualizar(TipoProdutoDto objDto) {

		if (Strings.isEmpty(objDto.getNome())) {
			throw new ObjectNotFoundException("Necessário informar o {nome} do tipo!");
		}
		
		//Busca pelo Nome Ignorando Maiúsculos e Acentos
		List<TipoProduto> tipoProdutoAtualList = tipoProdutoRep.findByNomeIgnoreCaseAndIgnoreAccents(objDto.getNome());
		
		if (tipoProdutoAtualList != null && tipoProdutoAtualList.size() > 0) {
			//Significa que existe um MATCH com o nome
			
			//Caso Cadastro Novo
			if(objDto.getIdUnico() == null)
				throw new ObjectNotFoundException("Já existe um tipo de produto cadastrado com este nome.");
			
			//Caso Edição de um item escrito CAFE para CAFÉ com acento.
			//A consulta irá encontrar o proprio registro q esta sendo editado
			if(tipoProdutoAtualList.size() == 1)
			{
				//Me certifico que se trada da edição do mesmo registro e se for continua
				if(!tipoProdutoAtualList.get(0).getIdUnico().equals(objDto.getIdUnico())) {
					//se caso nao for, disparo exception:
					throw new ObjectNotFoundException("Já existe um tipo de produto cadastrado com este nome.");
				}
			}
			else {
				throw new ObjectNotFoundException("Já existe um tipo de produto cadastrado com este nome.");
			}
		}
		
		if (objDto.getIdUnico() == null) {
			//Evita que o IdUnico fique com espaços.
			objDto.setIdUnico(objDto.getNome().replaceAll(" ", "_"));
		}
		
//		TipoProduto tipoProdutoAtual = tipoProdutoRep.findByNomeIgnoreCase(objDto.getNome());
//
//		if (tipoProdutoAtual != null && tipoProdutoAtual.getIdUnico() != null
//				&& !tipoProdutoAtual.getIdUnico().equals(objDto.getIdUnico())) {
//			throw new ObjectNotFoundException("Já existe um tipo de produto com este nome");
//		}

		TipoProduto tipoProduto = tipoProdutoRep.findByIdUnico(objDto.getIdUnico());
		if (tipoProduto == null) {
			tipoProduto = new TipoProduto();
		}

		BeanUtils.copyProperties(objDto, tipoProduto);

		tipoProduto.setStatusIntegracao(StatusIntegracao.INTEGRAR);
		return tipoProdutoRep.save(tipoProduto);

	}

	public TipoProduto inativarTipoProduto(String idUnico) throws Exception {
		TipoProduto tipoProduto = tipoProdutoRep.findByIdUnico(idUnico);

		List<RelacionamentosDto> listaRelacionamentos = tipoProdutoRep.verificarRelacionamentos(tipoProduto.getId());

		for (RelacionamentosDto r : listaRelacionamentos) {
			if (r.contemRelacionamento()) {
				throw new Exception(
						"Não é possível inativar este Tipo Produto pois ele possui relacionamentos com outras tabelas.");
			}
		}

		tipoProduto.setStatusIntegracao(StatusIntegracao.INTEGRAR);
		tipoProduto.setDataInativacao(new Date());
		logger.info("Inativando tipo de produto...");
		return tipoProdutoRep.save(tipoProduto);
	}

	public TipoProduto ativarTipoProduto(String idUnico) throws Exception {
		if (idUnico == null)
			throw new Exception("Favor informar o idUnico para ativar!");
		TipoProduto tipoProduto = tipoProdutoRep.findByIdUnico(idUnico);
		tipoProduto.setStatusIntegracao(StatusIntegracao.INTEGRAR);
		tipoProduto.setDataInativacao(null);

		logger.info("Ativando tipo de produto...");
		return tipoProdutoRep.save(tipoProduto);
	}

	public TipoProduto buscarTipoProdutoPorId(Long id) {
		return tipoProdutoRep.findById(id).orElse(null);
	}

	public TipoProduto buscarPorId(Long id) {
		TipoProduto tipoProdutoAtual = tipoProdutoRep.findById(id).orElse(null);

		if (tipoProdutoAtual == null) {
			throw new NullPointerException("O tipo produto não foi encontrado! ID: " + id + "!");
		}

		TipoProduto tipoProduto = new TipoProduto();
		BeanUtils.copyProperties(tipoProdutoAtual, tipoProduto);
		return tipoProduto;
	}

	public Page<TipoProdutoDto> buscarComPaginacao(Pageable pageable, String nome, Situacao situacao) {
		Page<TipoProduto> tipoProdutoPage = tipoProdutoRep.findAll(pageable, nome, situacao);
		List<TipoProdutoDto> tipoProdutoDtos = TipoProdutoDto.construir(tipoProdutoPage.getContent());

		return new PageImpl<TipoProdutoDto>(tipoProdutoDtos, pageable, tipoProdutoPage.getTotalElements());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sincronizar(String idUnico) {

		IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.TIPO_PRODUTO,
				FuncionalidadePaginaEnum.INT_LOTE_TIPO_PRODUTO);
		if (pagina == null || ERP.equals(pagina.getOrigenEnum()))
			return;

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina
				.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_TIPO_PRODUTO);
		if (paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null
				|| paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO))
			return;

		List<TipoProduto> tipos = null;
		TipoProdutoIntegrationDto objDto = null;
		IntegracaoLog integracaoLogRequest = IntegracaoLog.construirLogRequest(PaginaEnum.TIPO_PRODUTO,
				FuncionalidadePaginaEnum.INT_LOTE_TIPO_PRODUTO);

		try {

			if (idUnico == null) {
				tipos = tipoProdutoRep.findByStatusIntegracao(StatusIntegracao.INTEGRAR);
			} else {
				TipoProduto tipoProduto = tipoProdutoRep.findByIdUnico(idUnico);
				if (tipoProduto != null) {
					tipos = new ArrayList<>();
					tipos.add(tipoProduto);
				}
			}

			integracaoLogRequest.setTotalRegistros(tipos != null ? tipos.size() : 0);
			alterarStatusIntegracao(PROCESSANDO, tipos);

			if (!CollectionUtils.isEmpty(tipos)) {

				objDto = TipoProdutoDto.construirIntegrationDto(tipos);

				IntegrationAuth auth = integrationService
						.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());

				LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();
				if (pagina.getHeaders() != null) {
					for (IntegracaoPaginaHeader header : pagina.getHeaders()) {
						if (pagina.isHearderProfileActive(header, profileActive)) {
							if (!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())) {
								mvmap.add(header.getChave(), header.getValor());
							}
						}
					}
				}

				String url = pagina.getUrlPrincipalApi(profileActive);
				if (url == null)
					return;

				WebClient client = WebClient.builder().baseUrl(url)
						.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.filter(ExchangeFilterFunctions.basicAuthentication(auth.getLogin(), auth.getSenha())).build();

				HttpMethod httpMethod = IntegrationService.getMetodo(paginaFuncionalidade);

				Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);
				client.method(httpMethod).uri(paginaFuncionalidade.getEndPointSend()).headers(consumer)
						.body(BodyInserters.fromValue(objDto)).retrieve()
						.bodyToFlux(TipoProdutoResponseIntegrationDto.class).subscribe(result -> {

							Integer totalRegistros = 0;
							Integer sucessNotFound = 0;
							Integer sucess = 0;
							Integer error = 0;

							if (!CollectionUtils.isEmpty(result.getTipoProduto())) {
								for (TipoProdutoResponseDto item : result.getTipoProduto()) {

									TipoProduto tipo = tipoProdutoRep.findByIdUnico(item.getIdUnico());
									if (tipo != null) {
										tipo.setDataIntegracao(item.getIntegrated() ? new Date() : null);
										tipo.setStatusIntegracao(item.getIntegrated() ? StatusIntegracao.INTEGRADO
												: StatusIntegracao.INTEGRAR);
										tipoProdutoRep.save(tipo);

										if (item.getIntegrated()) {
											sucess++;
										}
									} else if (item.getIntegrated()) {
										sucessNotFound++;
									} else {
										error++;
									}

									totalRegistros++;
								}
							}

							StringBuilder obs = new StringBuilder();
							obs.append("[Resumo Integração ERP] \n\r");
							obs.append("Sucesso: ").append(sucess).append(", Sucesso ERP/NotFound Gênesis: ")
									.append(sucessNotFound).append(", Falha: ").append(error);

							SituacaoIntegracaoLogEnum situacao = (error > 0 || sucess == 0)
									? SituacaoIntegracaoLogEnum.FALHA
									: SituacaoIntegracaoLogEnum.SUCESSO;

							IntegracaoLog integracaoLogResponse = IntegracaoLog.construirLogResponse(SAFRA_COMPOSTA,
									INT_LOTE_SAFRA_COMPOSTA, situacao, totalRegistros, obs.toString());
							integracaoLogService.salvar(integracaoLogResponse);

						});
			}

		} catch (Exception e) {

			System.out.println("Erro sincronização de tipo de produto: " + e.getMessage());
			alterarStatusIntegracao(StatusIntegracao.INTEGRAR, tipos);
			integracaoLogRequest.registrarFalha("Error: " + e.getMessage());
			e.printStackTrace();

		} finally {
			integracaoLogService.salvar(integracaoLogRequest);
		}
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<TipoProduto> objs) {
		if (CollectionUtils.isEmpty(objs))
			return;
		List<TipoProduto> tipos = objs.stream().map(tipo -> {
			tipo.setDataIntegracao(null);
			tipo.setStatusIntegracao(status);
			return tipo;
		}).toList();
		tipoProdutoRep.saveAll(tipos);
	}
	
	public List<TipoProdutoResumidoDto> findByAutoComplete(String pesquisar) {
		List<TipoProduto> tipoProdutoList = tipoProdutoRep.findAll();
		logger.info("Buscando todos tipo de produto.");
		List<TipoProdutoResumidoDto> response = new ArrayList<>();
		for(TipoProduto item: tipoProdutoList) {
			TipoProdutoResumidoDto tipoProduto = new TipoProdutoResumidoDto();
			BeanUtils.copyProperties(item, tipoProduto);
			response.add(tipoProduto);
		}
		return response;		
	}

}

package br.coop.integrada.api.pa.domain.service.rependente;

import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.OperacaoPesagemEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessaFiltro;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen.IntegracaoRequestNfErpGen;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen.IntegracaoResponseNfErpGen;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen.NfErpGenRequest;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa.IntegrationRequestNf;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa.IntegrationResponseNf;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa.ResponseNfRemessa;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.CriarMovimentoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.EntradaReValidation;
import br.coop.integrada.api.pa.domain.service.externo.ErpExtService;
import br.coop.integrada.api.pa.domain.service.integration.IntegracaoLogService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.nfRemessa.NfRemessaService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;

@Service
@Transactional
public class EntradaReService {
	
	private static final Logger logger = LoggerFactory.getLogger(ErpExtService.class);
	
	@Autowired
	private IntegrationService integrationService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private NfRemessaService nfRemessaService;
	
	@Lazy
	@Autowired
	private RecEntregaService recEntregaService;
	
	@Autowired
	private PesagemService pesagemService;
	
	@Autowired
    private IntegracaoLogService integracaoLogService;

	@Value("${spring.profiles.active}")
	private String profileActive;

	public EntradaReValidation validarEntradaProducao(EntradaReValidation entradaReValidation) {
		IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.ENTRADA_PRODUCAO);
		if (pagina == null) {
			throw new ObjectDefaultException("A integração da página de Entrada de Produção não foi configurada.");
		}
		IntegracaoPaginaFuncionalidade funcionalidade = pagina
				.getFuncionalidade(FuncionalidadePaginaEnum.INT_IND_VALIDA_ENTRADA_PRODUCAO);

		if (funcionalidade == null) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_IND_VALIDA_ENTRADA_PRODUCAO.getDescricao()
					+ ") da página de Entrada de RE não foi configurada.");
		} else if (funcionalidade.getSituacao() == null
				|| funcionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_IND_VALIDA_ENTRADA_PRODUCAO.getDescricao()
					+ ") da página de Entrada de Produção está inativa.");
		}

		String url = pagina.getUrlPrincipalApi(profileActive) + funcionalidade.getEndPointSend();
		HttpMethod httpMethod = IntegrationService.getMetodo(funcionalidade);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (!CollectionUtils.isEmpty(pagina.getHeaders())) {
			for (IntegracaoPaginaHeader header : pagina.getHeaders()) {
				if (pagina.isHearderProfileActive(header, profileActive)) {
					if (Strings.isNotEmpty(header.getChave()) && Strings.isNotEmpty(header.getValor())) {
						httpHeaders.add(header.getChave(), header.getValor());
					}
				}
			}
		}

		HttpEntity<EntradaReValidation> httpEntity = new HttpEntity<EntradaReValidation>(entradaReValidation, httpHeaders);
		logger.info(FuncionalidadePaginaEnum.INT_IND_VALIDA_ENTRADA_PRODUCAO + ": (" + httpMethod.name() + ") " + url);

		if (pagina.getAuth(profileActive) != null) {
			try {
				IntegrationAuth auth = integrationService
						.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
				restTemplate.getInterceptors()
						.add(new BasicAuthenticationInterceptor(auth.getLogin(), auth.getSenha()));
			} catch (Exception e) {
				throw new ObjectDefaultException("Falha ao recuperar usuário e senha para a consulta.");
			}
		}

		try {
			ResponseEntity<EntradaReValidation> result = restTemplate.exchange(url, httpMethod, httpEntity,
					new ParameterizedTypeReference<EntradaReValidation>() {
					});
	
			if (result == null) {
				logger.info("A consulta não retornou dados.");
				throw new ObjectDefaultException("A consulta não retornou dados.");
			}
			return result.getBody();
		}
	    catch (HttpClientErrorException e) {
	    	if(HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração, por favor entre em contato com o Administrador!");
	    	}
	    	else if(HttpStatus.BAD_GATEWAY.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("O Datasul encontra-se temporariamente indisponível. Por favor, tente novamente mais tarde.");
	    	}
	    	
	    	throw new ObjectDefaultException(e.getMessage());
	    }
		catch (HttpStatusCodeException e) {
	    	if(HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração, por favor entre em contato com o Administrador!");
	    	}
	    	else if(HttpStatus.BAD_GATEWAY.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("O Datasul encontra-se temporariamente indisponível. Por favor, tente novamente mais tarde.");
	    	}
	    	
	    	throw new ObjectDefaultException(e.getMessage());
	    }
	}

	public void solicitarNfErp(Long idNfRemessa) {
		
		IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.ROMANEIO_ENTREGA);
		if (pagina == null || ERP.equals(pagina.getOrigenEnum())) {
			throw new ObjectDefaultException("A integração da página de Entrada de Produção não foi configurada.");
		}
		
		IntegracaoPaginaFuncionalidade funcionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA);
		if (funcionalidade == null) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA	+ ") da página de Entrada de RE não foi configurada.");
		} else if (funcionalidade.getSituacao() == null	|| funcionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA	+ ") da página de Entrada de Produção está inativa.");
		}
				
		List<NfRemessa> notasRemessa = null;
		IntegrationRequestNf integrationRequestNf = null;
		IntegracaoLog integracaoLogRequest = IntegracaoLog.construirLogRequest(PaginaEnum.ROMANEIO_ENTREGA, FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA);
		
		try {
			
			if(idNfRemessa == null) {
				notasRemessa = nfRemessaService.findByStatusAndPendenciasFiscais(StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO, false);
			}else {
				NfRemessa nfRemessa = nfRemessaService.findById(idNfRemessa);
				if(nfRemessa != null) {
					if(nfRemessa.getPendenciasFiscais()) {
						throw new ObjectDefaultException("A nota fiscal de remessa com pendências fiscais, necessário lançar a nota fiscal do produtor.");
					}
					notasRemessa =  new ArrayList<>();
					notasRemessa.add(nfRemessa);
				}else {
					throw new ObjectDefaultException("Nota fiscal de remessa não encontrada: "+ idNfRemessa);
				}
			}
				
			integracaoLogRequest.setTotalRegistros(notasRemessa != null ? notasRemessa.size() : 0);
			alterarStatusIntegracao(PROCESSANDO, StatusNotaFiscalEnum.EM_PROCESSAMENTO, notasRemessa);
			
			List<Long> idsRecEntrega = notasRemessa.stream()
					.filter(notaRemessa -> { return notaRemessa.getIdRecEntrega() != null; })
					.map(notaRemessa -> { return notaRemessa.getIdRecEntrega(); })
					.toList();
			
			recEntregaService.atualizarCampoCodSit(MovimentoReEnum.SOLICITADO_NF_REMESSA, idsRecEntrega);
			
			if (!CollectionUtils.isEmpty(notasRemessa)) {

				integrationRequestNf = IntegrationRequestNf.construir(notasRemessa);
				
				IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
				
				HttpMethod httpMethod = IntegrationService.getMetodo(funcionalidade);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
				if (!CollectionUtils.isEmpty(pagina.getHeaders())) {
					for (IntegracaoPaginaHeader header : pagina.getHeaders()) {
						if (pagina.isHearderProfileActive(header, profileActive)) {
							if (Strings.isNotEmpty(header.getChave()) && Strings.isNotEmpty(header.getValor())) {
								httpHeaders.add(header.getChave(), header.getValor());
							}
						}
					}
				}				

				String url = pagina.getUrlPrincipalApi(profileActive) + funcionalidade.getEndPointSend();
		
				HttpEntity<IntegrationRequestNf> httpEntity = new HttpEntity<IntegrationRequestNf>(integrationRequestNf, httpHeaders);
				logger.info(FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA + ": (" + httpMethod.name() + ") " + url);
		
				restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(auth.getLogin(), auth.getSenha()));
						
				ResponseEntity<IntegrationResponseNf> result = restTemplate.exchange(url, httpMethod, httpEntity,
						new ParameterizedTypeReference<IntegrationResponseNf>() {	});
								
    			Integer sucessNotFound = 0;
    			Integer sucess = 0;
    			Integer error = 0;
				
		
				if (result == null) {
					
					error = notasRemessa.size();	
					
					StringBuilder obs = new StringBuilder();
					obs.append("[Resumo Integração ERP] \n\r");
					obs.append("Sucesso: ").append(sucess)
						.append(", Sucesso ERP/NotFound Gênesis: ")
						.append(sucessNotFound).append(", Falha: ").append(error);
					
					integracaoLogRequest.setObservacao(obs.toString());
					integracaoLogRequest.setSituacao(SituacaoIntegracaoLogEnum.FALHA);
    				
					logger.info("A consulta não retornou dados.");
					throw new ObjectDefaultException("A consulta não retornou dados.");
				}
				
				
		
				for (ResponseNfRemessa item : result.getBody().getIntegrationNf()) {
					if(item.getIntegrated()) {
						alterarStatusIntegracao(StatusIntegracao.INTEGRADO, StatusNotaFiscalEnum.AGUARDANDO_TOTVS, notasRemessa, item);
						sucess++;
					}else {
						alterarStatusIntegracao(StatusIntegracao.INTEGRAR, StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO, notasRemessa, item);
						error++;
					}
				}
				
				StringBuilder obs = new StringBuilder();
				obs.append("[Resumo Integração ERP] \n\r");
				obs.append("Sucesso: ").append(sucess)
					.append(", Sucesso ERP/NotFound Gênesis: ")
					.append(sucessNotFound).append(", Falha: ").append(error);
				
				integracaoLogRequest.setObservacao(obs.toString());
				integracaoLogRequest.setSituacao((error > 0 || sucess == 0) ? SituacaoIntegracaoLogEnum.FALHA : SituacaoIntegracaoLogEnum.SUCESSO);
			}
			
		} catch (Exception e) {

			alterarStatusIntegracao(StatusIntegracao.INTEGRAR, StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO, notasRemessa);
			System.out.println("Erro sincronização de NF remessa: "+ e.getMessage());
			integracaoLogRequest.registrarFalha("Error: "+ e.getMessage());
			e.printStackTrace();			
			
		}finally {
			integracaoLogService.salvar(integracaoLogRequest);
		}

	}
	
	public void solicitarNfErpPorStatus(NfRemessaFiltro filter) {
		
		if(filter == null) {
			throw new ObjectDefaultException("Filtros obrigatórios");
		}
		
		if(Strings.isEmpty(filter.getCodEstabelecimento())) {
			throw new ObjectDefaultException("Código do estabelecimento obrigatório");
		}
		
		if(filter.getDataInicio() == null || filter.getDataFinal() == null) {
			throw new ObjectDefaultException("Favor informar o período inicial e final");
		}
		
		if(filter.getStatus() == null) {
			throw new ObjectDefaultException("Campo status obrigatório");
		}
		
		IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.ROMANEIO_ENTREGA);
		if (pagina == null || ERP.equals(pagina.getOrigenEnum())) {
			throw new ObjectDefaultException("A integração da página de Entrada de Produção não foi configurada.");
		}
		
		IntegracaoPaginaFuncionalidade funcionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA);
		if (funcionalidade == null) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA	+ ") da página de Entrada de RE não foi configurada.");
		} else if (funcionalidade.getSituacao() == null	|| funcionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) {
			throw new ObjectDefaultException("A funcionalidade(" + FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA	+ ") da página de Entrada de Produção está inativa.");
		}
				
		List<NfRemessa> notasRemessa = null;
		IntegrationRequestNf integrationRequestNf = null;
		IntegracaoLog integracaoLogRequest = IntegracaoLog.construirLogRequest(PaginaEnum.ROMANEIO_ENTREGA, FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA);
		
		try {
			
			notasRemessa = nfRemessaService.findByCodEstabelAndStatusAndPendenciasFiscaisAndDtCriacaoBetween(filter);			
			if(notasRemessa == null || notasRemessa.isEmpty()) {
				throw new ObjectDefaultException("Não encontramos nenhum pedido de nota com estes parâmetros.");
			}
				
			integracaoLogRequest.setTotalRegistros(notasRemessa != null ? notasRemessa.size() : 0);
			alterarStatusIntegracao(PROCESSANDO, StatusNotaFiscalEnum.EM_PROCESSAMENTO, notasRemessa);
			
			List<Long> idsRecEntrega = notasRemessa.stream()
					.filter(notaRemessa -> { return notaRemessa.getIdRecEntrega() != null; })
					.map(notaRemessa -> { return notaRemessa.getIdRecEntrega(); })
					.toList();
			recEntregaService.atualizarCampoCodSit(MovimentoReEnum.SOLICITADO_NF_REMESSA, idsRecEntrega);
			
			if (!CollectionUtils.isEmpty(notasRemessa)) {

				integrationRequestNf = IntegrationRequestNf.construir(notasRemessa);
				
				IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
				
				HttpMethod httpMethod = IntegrationService.getMetodo(funcionalidade);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
				if (!CollectionUtils.isEmpty(pagina.getHeaders())) {
					for (IntegracaoPaginaHeader header : pagina.getHeaders()) {
						if (pagina.isHearderProfileActive(header, profileActive)) {
							if (Strings.isNotEmpty(header.getChave()) && Strings.isNotEmpty(header.getValor())) {
								httpHeaders.add(header.getChave(), header.getValor());
							}
						}
					}
				}				

				String url = pagina.getUrlPrincipalApi(profileActive) + funcionalidade.getEndPointSend();
		
				HttpEntity<IntegrationRequestNf> httpEntity = new HttpEntity<IntegrationRequestNf>(integrationRequestNf, httpHeaders);
				logger.info(FuncionalidadePaginaEnum.INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA + ": (" + httpMethod.name() + ") " + url);
		
				restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(auth.getLogin(), auth.getSenha()));
						
				ResponseEntity<IntegrationResponseNf> result = restTemplate.exchange(url, httpMethod, httpEntity,
						new ParameterizedTypeReference<IntegrationResponseNf>() {	});
								
    			Integer sucessNotFound = 0;
    			Integer sucess = 0;
    			Integer error = 0;
				
		
				if (result == null) {
					
					error = notasRemessa.size();	
					
					StringBuilder obs = new StringBuilder();
					obs.append("[Resumo Integração ERP] \n\r");
					obs.append("Sucesso: ").append(sucess)
						.append(", Sucesso ERP/NotFound Gênesis: ")
						.append(sucessNotFound).append(", Falha: ").append(error);
					
					integracaoLogRequest.setObservacao(obs.toString());
					integracaoLogRequest.setSituacao(SituacaoIntegracaoLogEnum.FALHA);
    				
					logger.info("A consulta não retornou dados.");
					throw new ObjectDefaultException("A consulta não retornou dados.");
				}
				
				
		
				for (ResponseNfRemessa item : result.getBody().getIntegrationNf()) {
					if(item.getIntegrated()) {
						alterarStatusIntegracao(StatusIntegracao.INTEGRADO, StatusNotaFiscalEnum.AGUARDANDO_TOTVS, notasRemessa, item);
						sucess++;
					}else {
						alterarStatusIntegracao(StatusIntegracao.INTEGRAR, StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO, notasRemessa, item);
						error++;
					}
				}
				
				StringBuilder obs = new StringBuilder();
				obs.append("[Resumo Integração ERP] \n\r");
				obs.append("Sucesso: ").append(sucess)
					.append(", Sucesso ERP/NotFound Gênesis: ")
					.append(sucessNotFound).append(", Falha: ").append(error);
				
				integracaoLogRequest.setObservacao(obs.toString());
				integracaoLogRequest.setSituacao((error > 0 || sucess == 0) ? SituacaoIntegracaoLogEnum.FALHA : SituacaoIntegracaoLogEnum.SUCESSO);
			}
			
		} catch (Exception e) {

			alterarStatusIntegracao(StatusIntegracao.INTEGRAR, StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO, notasRemessa);
			System.out.println("Erro sincronização de NF remessa: "+ e.getMessage());
			integracaoLogRequest.registrarFalha("Error: "+ e.getMessage());
			e.printStackTrace();			
			
		}finally {
			integracaoLogService.salvar(integracaoLogRequest);
		}

	}
	
	public void alterarStatusIntegracao(StatusIntegracao status, StatusNotaFiscalEnum statusNF, List<NfRemessa> notasRemessa, ResponseNfRemessa itemResponse) {
		if(CollectionUtils.isEmpty(notasRemessa)) return;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		for (NfRemessa item : notasRemessa) {
			if(item.getId().equals(Long.parseLong(itemResponse.getId()))) {
				Date data = new Date();
				item.setDtUltMov(data);
				item.setDataIntegracao(data);
				item.setStatusIntegracao(status);
				item.setStatus(statusNF);
				item.setHrUltMov(simpleDateFormat.format(data));
				nfRemessaService.salvar(item);
			}
		}
	}
	
	public void alterarStatusIntegracao(StatusIntegracao status, StatusNotaFiscalEnum statusNF, List<NfRemessa> notasRemessa) {
		if(CollectionUtils.isEmpty(notasRemessa)) return;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		List<NfRemessa> remessas = notasRemessa.stream().map(remessa -> {
			Date data = new Date();
			remessa.setDtUltMov(data);
			remessa.setDataIntegracao(data);
			remessa.setStatusIntegracao(status);
			remessa.setStatus(statusNF);
			remessa.setHrUltMov(simpleDateFormat.format(data));
			return remessa;
		}).toList();
		notasRemessa = nfRemessaService.saveAll(remessas);
	}

	public IntegracaoResponseNfErpGen nfIntegrationReturn(IntegracaoRequestNfErpGen integracaoRequestNfErpGen) {
		
		IntegracaoResponseNfErpGen integracaoResponseNfErpGen = new IntegracaoResponseNfErpGen();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date data = new Date();
		
		for (NfErpGenRequest item : integracaoRequestNfErpGen.getIntegracaoNfErpGen()) {
			
			try {
				// ATUALIZA A NF_REMESSA
				NfRemessa nfRemessa = nfRemessaService.findById(Long.parseLong(item.getId()));
				if (nfRemessa != null) {
					if (nfRemessa.getStatus().equals(StatusNotaFiscalEnum.NFE_GERADA)) {
						integracaoResponseNfErpGen.adicionar(item, true, "NF de Remessa já integrada!");
					} else {
						nfRemessa.setStatus(StatusNotaFiscalEnum.NFE_GERADA);						
						nfRemessa.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						nfRemessa.setNatOperacao(item.getNatOperacao());
						nfRemessa.setNrDocto(item.getNrDocto());
						nfRemessa.setDtUltMov(data);
						nfRemessa.setDataIntegracao(data);
						nfRemessa.setHrUltMov(simpleDateFormat.format(data));

						if (nfRemessa.getSerieDocto() == null
								|| !nfRemessa.getSerieDocto().equals(item.getSerieDocto())) {
							nfRemessa.setSerieDocto(item.getSerieDocto());
						}
						nfRemessaService.salvar(nfRemessa);

						// ATUALIZA O REC_ENTRAGA
						RecEntrega recEntrega = recEntregaService.buscarRe(nfRemessa.getCodEstabel(), nfRemessa.getNrRe());
						if (recEntrega != null) {
														
							if(recEntrega.getNatureza() != null && !recEntrega.getNatureza().equals(NaturezaEnum.PJ_NF)) {
								recEntrega.setChaveAcessoNfe(item.getChaveAcessoNfe());
								
								if(!Strings.isEmpty(item.getNrDocto())) {
									if (recEntrega.getNroDocum() == null
											|| !recEntrega.getNroDocum().equals(item.getNrDocto())) {
										recEntrega.setNroDocum(item.getNrDocto());
									}
								}

								if(!Strings.isEmpty(item.getSerieDocto())) {
									if (recEntrega.getSerieDocum() == null
											|| !recEntrega.getSerieDocum().equals(item.getSerieDocto())) {
										recEntrega.setSerieDocum(item.getSerieDocto());
									}
								}

								if(!Strings.isEmpty(item.getSituacaoNfe())) {
									if (recEntrega.getSituacaoNfe() == null
											|| !recEntrega.getSituacaoNfe().equals(item.getSituacaoNfe())) {
										recEntrega.setSituacaoNfe(item.getSituacaoNfe());
									}
								}
							}
							
							recEntrega.setCodSit(MovimentoReEnum.ABERTO.getCodSit());
							// RecEntregaDto recEntregaDto = new RecEntregaDto();
							// BeanUtils.copyProperties(recEntrega, recEntregaDto);
							recEntrega = recEntregaService.atualizar(recEntrega);

							// CRIA O MOVIMENTO PESAGEM
							CriarMovimentoPesagemDto criarMovimentoPesagemDto = new CriarMovimentoPesagemDto();
							criarMovimentoPesagemDto.setCodEstabel(nfRemessa.getCodEstabel());
							criarMovimentoPesagemDto.setNrRe(nfRemessa.getNrRe());
							criarMovimentoPesagemDto.setOperacao(OperacaoPesagemEnum.REMESSA_DEPOSITO);
							criarMovimentoPesagemDto.setSerie(recEntrega.getSerie());
							criarMovimentoPesagemDto.setNroNota(recEntrega.getNrNotaFis());
							criarMovimentoPesagemDto.setNatOperacao(nfRemessa.getNatOperacao());
							criarMovimentoPesagemDto.setEstornado(false);
							criarMovimentoPesagemDto.setQuantidade(nfRemessa.getQuantidade());
							pesagemService.criarMovimentoPesagem(criarMovimentoPesagemDto, recEntrega);

						}
						integracaoResponseNfErpGen.adicionar(item, true, "Atualizado com sucesso!");
					}
				} else {
					integracaoResponseNfErpGen.adicionar(item, false, "Falha na integração, remessa não encontrada!");
				}
			} catch (Exception e) {
				integracaoResponseNfErpGen.adicionar(item, false, "Falha na integração!");
			}
		}
		return integracaoResponseNfErpGen;
	}
}

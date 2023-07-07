package br.coop.integrada.api.pa.domain.service.cadastro;

import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRAR;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoIntegrationListDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.TipoClassificacaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.informacaoRecursoSistema.PaginaAreaDto;
import br.coop.integrada.api.pa.domain.repository.cadastro.TipoClassificacaoRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class TipoClassificacaoService {
	private static final Logger logger = LoggerFactory.getLogger(TipoClassificacaoService.class);
	
	@Autowired
	private TipoClassificacaoRep tipoClassificacaoRepository;
	
	@Autowired
	private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
    private IntegrationService integrationService;
	
	@Value("${spring.profiles.active}")
	private String profileActive;

	
	public Optional<TipoClassificacao> unico(Long id) {
		return tipoClassificacaoRepository.findById(id);
	}
	
	public Optional<TipoClassificacao> unico(TipoClassificacaoEnum tipoClassificacaoEnum) {
		Optional<TipoClassificacao> tipClassificacao = tipoClassificacaoRepository.findByTipoClassificacao(tipoClassificacaoEnum);
		return tipClassificacao;
	}
	
	public void salvar(TipoClassificacao tipoClassificacao ) throws Exception {
		logger.info("Salvando Tipo de Classificação");
		
		if(tipoClassificacao.getId() != null)
		{
			Optional<TipoClassificacao> historicoTipoClassificacao = this.unico(tipoClassificacao.getId());
			
			if(historicoTipoClassificacao.isEmpty())
				throw new Exception("Não encontrado...");
			
			//Edição
			String observacao = CompararObjetos.comparar(historicoTipoClassificacao.get(), tipoClassificacao);
			
			historicoGenericoService.salvar(
					tipoClassificacao.getId(),
					PaginaEnum.TIPO_CLASSIFICACAO,
					"Atualização do Tipo de Classificação",
					"Tipo Classificação: " + tipoClassificacao.getTipo() + " | " + observacao);
			
			tipoClassificacaoRepository.save(tipoClassificacao);
		}
		else
		{
			tipoClassificacaoRepository.save(tipoClassificacao);
			
			//Inclusão
			historicoGenericoService.salvar(
					tipoClassificacao.getId(),
					PaginaEnum.TIPO_CLASSIFICACAO,
					"Inclusão do Tipo de Classificação",
					"Tipo Classificação: " + tipoClassificacao.getTipo() );
		}
		this.sincronizarTipoClassificacoes(tipoClassificacao.getId());
		
	}
	
	
	
	public void inativar(TipoClassificacaoEnum tipoClassificacaoEnum) {
		Optional<TipoClassificacao> tipoClassificacao = tipoClassificacaoRepository.findByTipoClassificacao(tipoClassificacaoEnum);
		if(tipoClassificacao.get() != null) {
			tipoClassificacao.get().setDataInativacao(new Date());
			tipoClassificacaoRepository.save(tipoClassificacao.get());
			
			historicoGenericoService.salvar(
					tipoClassificacao.get().getId(),
					PaginaEnum.TIPO_CLASSIFICACAO,
					"O Tipo de Classificação foi inativado",
					"Tipo Classificação: " + tipoClassificacao.get().getTipo()
			);
			
			
			this.sincronizarTipoClassificacoes(tipoClassificacao.get().getId());
			
		}
	}
	
	public void ativar(TipoClassificacaoEnum tipoClassificacaoEnum) {
		Optional<TipoClassificacao> tipoClassificacao = tipoClassificacaoRepository.findByTipoClassificacao(tipoClassificacaoEnum);
		if(tipoClassificacao.get() != null) {
			tipoClassificacao.get().setDataInativacao(null);
			tipoClassificacaoRepository.save(tipoClassificacao.get());
			
			historicoGenericoService.salvar(
					tipoClassificacao.get().getId(),
					PaginaEnum.TIPO_CLASSIFICACAO,
					"O Tipo de Classificação foi ativado",
					"Tipo Classificação: " + tipoClassificacao.get().getTipo()
			);
			
			this.sincronizarTipoClassificacoes(tipoClassificacao.get().getId());
		}
	}
	
	//Lista todos os registros para exibir
	public Page<TipoClassificacao> listarTodosPage(Pageable pageable){
		logger.info("Listando todos todos os tipos de classificação...");
		return tipoClassificacaoRepository.findAll(pageable);
	}
	
	//Lista todos os registros para exibir
	public List<TipoClassificacao> listarTodos(){
		logger.info("Listando todos todos os tipos de classificação...");
		return tipoClassificacaoRepository.findByDataInativacaoNull();
	}
	
	//Lista todos os Tipo para carregar a dropdown
	public List<PaginaAreaDto> buscarTipos() {
		List<PaginaAreaDto> listaTipos = new  ArrayList<>();
		
		for(TipoClassificacaoEnum tipoClassificacaoEnum : TipoClassificacaoEnum.values()){
			PaginaAreaDto paginaAreaDto = new PaginaAreaDto();
			
			paginaAreaDto.setChave(tipoClassificacaoEnum.name());
			paginaAreaDto.setValor(tipoClassificacaoEnum.getDescricao());
			
			listaTipos.add(paginaAreaDto);
		}
		return listaTipos;
	}
	
	public List<TipoClassificacao> buscarPorStatusIntegracao(StatusIntegracao status){
		return tipoClassificacaoRepository.findByStatusIntegracao(status);
	}
	
	public List<TipoClassificacao> buscarTiposSemIntegracao(){
		return tipoClassificacaoRepository.findByStatusIntegracaoOrStatusIntegracaoIsNull(StatusIntegracao.INTEGRAR);
	}
		
	public void alterarStatusIntegracao(StatusIntegracao status, List<TipoClassificacao> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<TipoClassificacao> tipos = objs.stream().map(tipo -> {
			tipo.setDataIntegracao(null);
			tipo.setStatusIntegracao(status);
			return tipo;
		}).toList();
		tipoClassificacaoRepository.saveAll(tipos);
	}

	public Page<TipoClassificacao> listarPage(Pageable pageable, String filtro, Situacao situacao) {
		logger.info("Listando tipos de classificação de acordo com o filtro e situação...");
		return tipoClassificacaoRepository.findAll(pageable, filtro, situacao);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sincronizarTipoClassificacoes(Long id) {
		
    	IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.TIPO_CLASSIFICACAO, FuncionalidadePaginaEnum.INT_LOTE_TIPO_CLASSIFICACAO);
    	if(pagina == null || ERP.equals(pagina.getOrigenEnum())) return;     	

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_TIPO_CLASSIFICACAO);
		if(paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null || paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) return;
    	
    	//List<Classificacao> classificacoes = null;
    	List<TipoClassificacao> tipos = null;
    	
    	try {
    		
    		if(id == null){
    			tipos = this.listarTodos();
    		}else {
    			TipoClassificacao tc = tipoClassificacaoRepository.findById(id).orElse(null);
    			tipos = new ArrayList<>();
    			tipos.add( tc );
    			
    		}
    		this.alterarStatusIntegracao(PROCESSANDO, tipos);
        	
        	if(CollectionUtils.isEmpty(tipos)) return;
        	
        	ClassificacaoIntegrationListDto objDto = ClassificacaoIntegrationListDto.construir(null, tipos);
        	   	
        
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
    		.bodyToFlux(ClassificacaoIntegrationListDto.class)
    		.subscribe(classificacaoList -> {
    			atualizarStatusRetornoIntegracaoComErp(classificacaoList.getTipoClassificacoes());

    			
    		});
    	}
    	catch (Exception e) {
    		if(!CollectionUtils.isEmpty(tipos)) 
    			this.alterarStatusIntegracao(INTEGRAR, tipos);
    		
    		e.printStackTrace();
    		return;
    	}
	    
		
	}
	
	public void atualizarStatusRetornoIntegracaoComErp(List<TipoClassificacaoIntegrationDto> tiposClassificacoes) {
		
		for(TipoClassificacaoIntegrationDto tipoClassificacaoDto: tiposClassificacoes) {
			try {
				
				StatusIntegracao statusIntegracaoCabecalho = tipoClassificacaoDto.getIntegrated() ? INTEGRADO : INTEGRAR;
				Date dataIntegracao = statusIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null;
					
				TipoClassificacao tipoClassificacao = tipoClassificacaoRepository.findByTipoClassificacao(tipoClassificacaoDto.getCodigo()).orElse(null);
					
				if(tipoClassificacao == null)
					throw new ObjectNotFoundException("");
				
				tipoClassificacao.setDataIntegracao(dataIntegracao);
				tipoClassificacao.setStatusIntegracao(statusIntegracaoCabecalho);

				tipoClassificacaoRepository.save(tipoClassificacao);
				
			}
			catch (Exception e) {
				System.out.println("Erro no retorno da sincronização de classificações: "+
						tipoClassificacaoDto.getDescricao() +" -> "+ e.getMessage());
			}
		}
    }
	
}

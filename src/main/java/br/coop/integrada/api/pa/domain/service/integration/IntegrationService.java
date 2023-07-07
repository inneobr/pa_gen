package br.coop.integrada.api.pa.domain.service.integration;

import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.utils.PasswordEncode;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.HttpRequestMethodEnum;
import br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoAuthenticationEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaParametros;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.modelDto.enumDto.FuncionalidadeEnumDto;
import br.coop.integrada.api.pa.domain.modelDto.enumDto.PaginaEnumDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoFuncionalidadeTeste;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaFuncionalidadeDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaHeaderDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaOptions;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaParametroDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegrationAuthDto;
import br.coop.integrada.api.pa.domain.repository.integration.IntegracaoPaginaFuncionalidadeRep;
import br.coop.integrada.api.pa.domain.repository.integration.IntegracaoPaginaHeaderRep;
import br.coop.integrada.api.pa.domain.repository.integration.IntegracaoPaginaParametroRep;
import br.coop.integrada.api.pa.domain.repository.integration.IntegracaoPaginaRep;
import br.coop.integrada.api.pa.domain.repository.integration.IntegrationAuthRep;
import reactor.core.publisher.Mono;

@Service
public class IntegrationService {
	private static final String token = "69359861-200b-40a3-97d6-5854f2647c64";

	@Autowired
	private IntegrationAuthRep intRep;	

	@Autowired
	private PasswordEncode passwordEncode; 

	@Autowired
	private IntegracaoPaginaRep paginaRep;

	@Autowired
	private IntegracaoPaginaParametroRep parametroRep;

	@Autowired
	private IntegracaoPaginaFuncionalidadeRep funcionalidadeRep;

	@Autowired
	private IntegracaoPaginaHeaderRep paginaHeaderRep;
	
	@SuppressWarnings("unused") /* Cadastrar novo login de autenticação para sites externos. */
	public IntegrationAuth cadastrarLoginExterno(IntegrationAuth obj) throws InvalidKeyException {		
		passwordEncode.token(token); obj.setSenha(passwordEncode.encode(obj.getSenha()));
		if(obj == null) throw new NullPointerException("Dados de login inválidos!");
		return intRep.save(obj);
	}
	
	/* Valida se o login de integração já existe */
	@SuppressWarnings("unused")
	public IntegrationAuth buscarPelaDescricao(String descricao) throws InvalidKeyException {
		IntegrationAuth integrationAuth = intRep.findByDescricao(descricao);
		IntegrationAuth response = new IntegrationAuth();
		BeanUtils.copyProperties(integrationAuth, response);
		if(response == null) throw new RuntimeException("Login não encotrado!");
		passwordEncode.token(token);
		response.setSenha( passwordEncode.dencode(response.getSenha()));
		return response;
	}
	
	public IntegrationAuth findById(Long id) throws InvalidKeyException {
		IntegrationAuth integrationAuth = intRep.findById(id).get();
		IntegrationAuth response = new IntegrationAuth();
		BeanUtils.copyProperties(integrationAuth, response);
		if(response == null) throw new RuntimeException("Login não encotrado!");
		passwordEncode.token(token);
		response.setSenha( passwordEncode.dencode(response.getSenha()));
		return response;
	}
	
	/* Listar todos os logins cadastrados */
	public List<IntegrationAuthDto> listarLoginCadastrados(String login) throws InvalidKeyException {
		List<IntegrationAuth> data = new ArrayList<>();
		if(login.equals("search"))   data = intRep.findAll();
		if(!login.equals("search"))  data = intRep.findByLoginContainingIgnoreCase(login);		
		if(data.isEmpty()) throw new NullPointerException("Não existem logins cadastrados.");
		
		List<IntegrationAuthDto> response = new ArrayList<>();
		for(IntegrationAuth item: data) {
			IntegrationAuthDto auth = new IntegrationAuthDto();
			passwordEncode.token(token);
			item.setSenha( passwordEncode.dencode(item.getSenha()));
			
			BeanUtils.copyProperties(item, auth);
			response.add(auth);
		}
		return response;
	}	
	
	/* Listar todos os logins cadastrados */
	public List<IntegrationAuthDto> listarLoginCadastradosPorDescricao(String descricao) throws InvalidKeyException {
		List<IntegrationAuth> data = new ArrayList<>();
		if(descricao.equals("search"))   data = intRep.findAll();
		if(!descricao.equals("search"))  data = intRep.findByDescricaoContainingIgnoreCase(descricao);		
		if(data.isEmpty()) throw new NullPointerException("Não existem logins cadastrados.");
		
		List<IntegrationAuthDto> response = new ArrayList<>();
		for(IntegrationAuth item: data) {
			IntegrationAuthDto auth = new IntegrationAuthDto();
			passwordEncode.token(token);
			item.setSenha( passwordEncode.dencode(item.getSenha()));
			
			BeanUtils.copyProperties(item, auth);
			response.add(auth);
		}
		return response;
	}
	
	public Page<IntegrationAuthDto> findAllLogins(Pageable pageable) {
		Page<IntegrationAuth> page = intRep.findAll(pageable);
		if(page.isEmpty()) throw new NullPointerException("Não existem logins cadastrados.");
		List<IntegrationAuthDto> response = new ArrayList<>();
		for(IntegrationAuth item: page) {
			IntegrationAuthDto auth = new IntegrationAuthDto();
			BeanUtils.copyProperties(item, auth);
			response.add(auth);
		}
		return new PageImpl<>(
			response,
            pageable,
            page.getTotalElements()
        );
	}

	/* SALVAR INTEGRAÇÃO DE PAGINA */
	public IntegracaoPagina cadastrarPagina(IntegracaoPaginaDto paginaDto) {
		IntegracaoPagina integracaoPagina = paginaDto.getId() != null ? paginaRep.getReferenceById(paginaDto.getId()) : new IntegracaoPagina();		
		
		BeanUtils.copyProperties(paginaDto, integracaoPagina);	
		
		integracaoPagina.getFuncionalidades().clear();
		if(paginaDto.getFuncionalidades() != null) {
			for(IntegracaoPaginaFuncionalidadeDto funcionalidadeDto: paginaDto.getFuncionalidades()) {
				IntegracaoPaginaFuncionalidade integracaoPaginaFuncionalidade = new IntegracaoPaginaFuncionalidade();
				BeanUtils.copyProperties(funcionalidadeDto, integracaoPaginaFuncionalidade);
				integracaoPaginaFuncionalidade.setIntegracaoPagina(integracaoPagina);					
				
				if(funcionalidadeDto.getSchedulerSend() != null) {
					integracaoPaginaFuncionalidade.setSchedulerSend(funcionalidadeDto.getSchedulerSend().getExpressions());
				}
				if(funcionalidadeDto.getSchedulerReturn() != null) {
					integracaoPaginaFuncionalidade.setSchedulerReturn(funcionalidadeDto.getSchedulerReturn().getExpressions());
				}
				
				if(integracaoPaginaFuncionalidade.getMethodSend() != null) {
					integracaoPagina.getFuncionalidades().add(integracaoPaginaFuncionalidade);
				}
			}
		}		
		
		integracaoPagina.getHeaders().clear();
		if(paginaDto.getPaginaHeader() != null) {
			for(IntegracaoPaginaHeaderDto item: paginaDto.getPaginaHeader()) {			
				IntegracaoPaginaHeader paginaHeader = new IntegracaoPaginaHeader();
				BeanUtils.copyProperties(item, paginaHeader);
				if(paginaHeader.getChave() != null) {
					paginaHeader.setIntegracaoPagina(integracaoPagina);
					integracaoPagina.getHeaders().add(paginaHeader);
				}
			}
		}	
		
		integracaoPagina.getParametros().clear();
		if(paginaDto.getPaginaParametros() != null) {
			for(IntegracaoPaginaParametroDto item: paginaDto.getPaginaParametros()) {			
				IntegracaoPaginaParametros paginaParametros = new IntegracaoPaginaParametros();
				BeanUtils.copyProperties(item, paginaParametros);
				if(paginaParametros.getChave() != null) {
					paginaParametros.setIntegracaoPagina(integracaoPagina);
					integracaoPagina.getParametros().add(paginaParametros);
				}
			}
		}			
		return paginaRep.save(integracaoPagina);
	}
	
	public IntegracaoPagina buscarPaginaCadastrada(PaginaEnum pagina) {
		IntegracaoPagina paginaIntegracao = paginaRep.findByPaginaEnum(pagina);
		if(paginaIntegracao == null) throw new NullPointerException("Página não cadastrada.");
		return paginaIntegracao;
	}

	public List<IntegrationAuth> getLoginContaing(String login) {
		Pageable pageable = PageRequest.of(0, 5); 
		List<IntegrationAuth> integrationAuth = intRep.findByLoginContainingIgnoreCase(login, pageable);
		if(integrationAuth.isEmpty()) throw new NullPointerException("Não existem logins cadastrados.");
		return integrationAuth;
	}
	
	public Page<IntegrationAuthDto> findAllLogin(Pageable pageable){
		Page<IntegrationAuth> page = intRep.findAll(pageable);
		if(page.isEmpty()) throw new NullPointerException("Não existem logins cadastrados.");
		List<IntegrationAuthDto> response = new ArrayList<>();;
		for(IntegrationAuth item: page.getContent()) {
			IntegrationAuthDto authDto = new IntegrationAuthDto();
			BeanUtils.copyProperties(item, authDto);
			response.add(authDto);
		}
		return new PageImpl<>(
				response,
                pageable,
                page.getTotalElements()
        );
		
	}

	public String gerarTokenBasic(IntegrationAuth integrationAuth) {
		try {
			passwordEncode.token(token);
			String usuarioSenha = integrationAuth.getLogin() + ":" + integrationAuth.getSenha();
			String base64 = Base64.getEncoder().encodeToString(usuarioSenha.getBytes());

			return "Basic " + base64;
		} catch (InvalidKeyException e) {
			throw new ObjectDefaultException("Falha ao descriptografar a senha de autenticação!");
		}
	}

	public List<IntegracaoPagina> getTodosIntegartionPage() {
		List<IntegracaoPagina> pagina = paginaRep.findAll();
		if(pagina.isEmpty()) throw new NullPointerException("Não existem páginas cadastradas.");
		return pagina;
	}

	public IntegracaoPagina buscarPorPagina(PaginaEnum paginaEnum, FuncionalidadePaginaEnum funcionalidade) {		
		IntegracaoPagina integracaoPagina = paginaRep.findByPaginaEnumAndFuncionalidadesFuncionalidade(paginaEnum, funcionalidade);
		if(integracaoPagina == null) throw new NullPointerException("Página ("+paginaEnum.getDescricao()+") com ("+funcionalidade.getDescricao()+") não encontradas.");
		return integracaoPagina;
	}
	
	public IntegracaoPagina buscarPorPagina(PaginaEnum pagina) {
		IntegracaoPagina integracaoPagina = paginaRep.findByPaginaEnum(pagina);
		if(integracaoPagina == null) {
			throw new ObjectDefaultException("Não foi encontrado o cadastro de integração \"" + pagina.getDescricao().toUpperCase() + "\".");
		}
		return integracaoPagina;
	}
	
	public IntegracaoPaginaOptions getPaginaGenesis(PaginaEnum pagina) {
		IntegracaoPaginaOptions options = new IntegracaoPaginaOptions();
		IntegracaoPagina integracaoPagina = paginaRep.findByPaginaEnum(pagina);
		if(integracaoPagina == null) throw new NullPointerException("Página não cadastrada.");
		options.setPagina(integracaoPagina.getPaginaEnum().getDescricao());
		if(integracaoPagina.getOrigenEnum().equals(OrigemInputEnum.GENESIS)) {
			options.setDisable(false);
			options.setVisible(true);
			
		}
		else { 
			options.setDisable(true);
			options.setVisible(false);
		}
		return options;
	}
	
	public StatusIntegracao buscarStatusIntegracaoPorPagina(PaginaEnum pagina) {
		IntegracaoPagina integracaoPagina = buscarPorPagina(PaginaEnum.ITEM_AVARIADO);		
		if(integracaoPagina != null && ERP.equals(integracaoPagina.getOrigenEnum())) {
			return StatusIntegracao.INTEGRAR;
		}		
		return StatusIntegracao.INTEGRAR;
	}

	public List<PaginaEnumDto> getPaginasEnum(){
		List<PaginaEnumDto> pagina = PaginaEnum.listarTodas();
		if(pagina.isEmpty()) throw new NullPointerException("Não existem páginas cadastradas.");
		return pagina;
	}

	public Object getTipoEnum(){
		TipoAuthenticationEnum[] tipoEnum = TipoAuthenticationEnum.values();
		return tipoEnum;
	}

	public List<FuncionalidadeEnumDto> getFuncionalidadePageEnum(PaginaEnum pagina){
		List<FuncionalidadeEnumDto> funcionalidades = FuncionalidadePaginaEnum.buscarPorPagina(pagina);
		if(funcionalidades.isEmpty()) throw new NullPointerException("Não existem funcionalidades cadastradas.");
		return funcionalidades;
	}

	public List<IntegracaoPaginaFuncionalidade> getFuncionalidadesAutomaticas(){
		return funcionalidadeRep.findByIntegracaoPaginaTipoEnum(TipoIntegracaoEnum.AUTOMATICA);
	}

	public Object getHttpRequestMethodEnum(){
		HttpRequestMethodEnum[] methodEnum = HttpRequestMethodEnum.values();
		return methodEnum;
	}

	public String deletarPagina(Long id) {
		IntegracaoPagina pagina = paginaRep.getReferenceById(id);		
		funcionalidadeRep.deleteByIntegracaoPagina(pagina);
		paginaHeaderRep.deleteByIntegracaoPagina(pagina);
		parametroRep.deleteByIntegracaoPagina(pagina);
		paginaRep.deleteById(pagina.getId());
		return "Deletado com sucesso!";
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getHttpRequestTest(IntegracaoFuncionalidadeTeste dadosTeste) throws InvalidKeyException {
		
		if(dadosTeste.getIdIntegrationAuth() == null) {
			throw new NullPointerException("Favor informar o login de acesso do ambiente selecionado.");
		}
		Optional<IntegrationAuth> auth = intRep.findById(dadosTeste.getIdIntegrationAuth());
		passwordEncode.token(token);
		
		WebClient client = WebClient.builder().baseUrl(dadosTeste.getUrlPrincipalApi())
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.filter(ExchangeFilterFunctions.basicAuthentication(auth.get().getLogin(), passwordEncode.dencode(auth.get().getSenha())))
			.build();
		
		HttpMethod httpMethod = HttpMethod.GET;	
		HttpRequestMethodEnum methodEnum = null;
		if(dadosTeste.getTipoSend() && dadosTeste.getMethodSend() != null) {
			methodEnum = dadosTeste.getMethodSend();
		}
		
		if(!dadosTeste.getTipoSend() && dadosTeste.getMethodReturn() != null){
			methodEnum = dadosTeste.getMethodReturn();
		}

		if(HttpRequestMethodEnum.POST.equals(methodEnum)) {
			httpMethod = HttpMethod.POST;
		}
		else if(HttpRequestMethodEnum.PUT.equals(methodEnum)) {
			httpMethod = HttpMethod.PUT;
		}
		else if(HttpRequestMethodEnum.DELETE.equals(methodEnum)) {
			httpMethod = HttpMethod.DELETE;
		}			
		
		LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();
		for(IntegracaoPaginaHeader header: dadosTeste.getPaginaHeader()) {
			if(!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())){
				mvmap.add(header.getChave(), header.getValor());
			}
		}		
		
		String requestBody = "";
		if(dadosTeste.getTipoSend() && dadosTeste.getPayLoadRequestSend() != null) {
			requestBody = dadosTeste.getPayLoadRequestSend();	
		}
		
		if(!dadosTeste.getTipoSend() && dadosTeste.getPayLoadRequestReturn() != null) {
			requestBody = dadosTeste.getPayLoadRequestReturn();	
		}
		
		String uri = null;
		if(dadosTeste.getTipoSend() && dadosTeste.getEndPointSend() != null) {
			uri = dadosTeste.getEndPointSend();
		}
		
		if(!dadosTeste.getTipoSend() && dadosTeste.getEndPointReturn() != null) {
			uri = dadosTeste.getEndPointReturn();
		}
		
		Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);	
		Mono<Object> objeto =  client.method(httpMethod)				
				.uri(uri)
				.headers(consumer)
				.body(BodyInserters.fromValue(requestBody))
				.retrieve()
				.bodyToMono(Object.class);
		
		Object obj = objeto.block();
		return obj;
	}
	
	public static HttpMethod getMetodo(IntegracaoPaginaFuncionalidade funcionalidade) {
		HttpRequestMethodEnum methodEnum = funcionalidade.getMethodSend();

		if(HttpRequestMethodEnum.POST.equals(methodEnum)) {
			return HttpMethod.POST;
		}
		else if(HttpRequestMethodEnum.PUT.equals(methodEnum)) {
			return HttpMethod.PUT;
		}
		else if(HttpRequestMethodEnum.DELETE.equals(methodEnum)) {
			return HttpMethod.DELETE;
		}
		
		return HttpMethod.GET;
	}
}

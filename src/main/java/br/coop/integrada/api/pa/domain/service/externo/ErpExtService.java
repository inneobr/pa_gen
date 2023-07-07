package br.coop.integrada.api.pa.domain.service.externo;

import static br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum.INT_IND_BUSCAR_NUMERO_RE;
import static br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum.INT_IND_VALIDAR_NF_ENTRADA;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.ENTRADA_PRODUCAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.modelDto.externo.NotaFiscalEntradaDto;
import br.coop.integrada.api.pa.domain.modelDto.externo.NotaFiscalEntradaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.externo.NumeroReDto;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;

@Service
public class ErpExtService {
	
	private static final Logger logger = LoggerFactory.getLogger(ErpExtService.class);
    
    @Autowired
    private IntegrationService integrationService;
    
    @Autowired
    private RestTemplate restTemplate;    

	@Value("${spring.profiles.active}")
	private String profileActive;

    public List<NumeroReDto> buscarNumeroRe(List<NumeroReDto> numeroReDtos) {
        IntegracaoPagina pagina = integrationService.buscarPorPagina(ENTRADA_PRODUCAO);
        
        if(pagina == null) {
        	throw new ObjectDefaultException("A integração da página de Entrada de Produção não foi configurada.");
        }
        
    	IntegracaoPaginaFuncionalidade funcionalidade = pagina.getFuncionalidade(INT_IND_BUSCAR_NUMERO_RE);
    	if(funcionalidade == null) {
        	throw new ObjectDefaultException("A funcionalidade(INT_IND_BUSCAR_NUMERO_RE) da página de Entrada de Produção não foi configurada.");
        }else if(funcionalidade.getSituacao() == null || funcionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) {
        	throw new ObjectDefaultException("A funcionalidade(INT_IND_BUSCAR_NUMERO_RE) da página de Entrada de Produção está inativa.");
        }
    	
    	String url = pagina.getUrlPrincipalApi(profileActive) + funcionalidade.getEndPointSend();
    	HttpMethod httpMethod = IntegrationService.getMetodo(funcionalidade);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		if(!CollectionUtils.isEmpty(pagina.getHeaders())) {
			for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
				if(pagina.isHearderProfileActive(header, profileActive)) {
					if(Strings.isNotEmpty(header.getChave()) && Strings.isNotEmpty(header.getValor())){
						httpHeaders.add(header.getChave(), header.getValor());
					}
				}
			}
		}
		
		HttpEntity<List<NumeroReDto>> httpEntity = new HttpEntity<List<NumeroReDto>>(numeroReDtos, httpHeaders);
		
		logger.info("BUSCAR NUMERO RE: (" + httpMethod.name() + ") " + url);
		
		if(pagina.getAuth(profileActive) != null) {
			try {
				IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
				restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(auth.getLogin(), auth.getSenha()));	
			}
			catch (Exception e) {
				throw new ObjectDefaultException("Falha ao recuperar usuário e senha para a consulta.");
			}
		}
    	
		try {
			ResponseEntity<List<NumeroReDto>> result = restTemplate.exchange(
					url,
	                httpMethod,
	                httpEntity,
	                new ParameterizedTypeReference<List<NumeroReDto>>() {}
	        );
			
			if(result == null) {
				logger.info("A consulta não retornou dados.");
	    		throw new ObjectDefaultException("A consulta não retornou dados.");
			}
	        
	
	        return result.getBody();
		}
	    catch (HttpClientErrorException e) {
	    	if(HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração que gera os numeros da RE, por favor entre em contato com o Administrador!");
	    	}
	    	else if(HttpStatus.BAD_GATEWAY.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("O Datasul encontra-se temporariamente indisponível. Por favor, tente novamente mais tarde.");
	    	}
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	List<NumeroReDto> response = null;
			
	    	try {
				response = mapper.readerForListOf(NumeroReDto.class).readValue(e.getResponseBodyAsString());
			}
	    	catch (JsonMappingException e1) {
				logger.error("Ocorreu uma falha não mapeada na integração que gera os numeros da RE: " + e1.getMessage());
			}
	    	catch (JsonProcessingException e1) {
				logger.error("Ocorreu uma falha não mapeada na integração que gera os numeros da RE: " + e1.getMessage());
			}
			
	    	if(response == null || CollectionUtils.isEmpty(response)) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração que gera os numeros da RE, por favor entre em contato com o Administrador!");
	    	}
	    	
	    	throw new ObjectDefaultException(response.get(0).getResults());
	    }
		catch (HttpStatusCodeException e) {
	    	if(HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração que gera os numeros da RE, por favor entre em contato com o Administrador!");
	    	}
	    	else if(HttpStatus.BAD_GATEWAY.equals(e.getStatusCode())) {
	    		throw new ObjectDefaultException("O Datasul encontra-se temporariamente indisponível. Por favor, tente novamente mais tarde.");
	    	}
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	List<NumeroReDto> response = null;
			
	    	try {
				response = mapper.readerForListOf(NumeroReDto.class).readValue(e.getResponseBodyAsString());
			}
	    	catch (JsonMappingException e1) {
				logger.error("Ocorreu uma falha não mapeada na integração que gera os numeros da RE: " + e1.getMessage());
			}
	    	catch (JsonProcessingException e1) {
				logger.error("Ocorreu uma falha não mapeada na integração que gera os numeros da RE: " + e1.getMessage());
			}
			
	    	if(response == null || CollectionUtils.isEmpty(response)) {
	    		throw new ObjectDefaultException("Ocorreu uma falha não mapeada na integração que gera os numeros da RE, por favor entre em contato com o Administrador!");
	    	}
	    	
	    	throw new ObjectDefaultException(response.get(0).getResults());
	    }
    }
    
    public NotaFiscalEntradaResponseDto validarNotaFiscalEntrada(String numeroNotaFiscal, String serie, String natureza, String codigoProdutor) {
    	List<String> mensagens = new ArrayList<String>();
    	if(numeroNotaFiscal == null || Strings.isEmpty(numeroNotaFiscal.trim())) mensagens.add("Nota fiscal");
    	if(serie == null || Strings.isEmpty(serie.trim())) mensagens.add("Serie");
    	if(natureza == null || Strings.isEmpty(natureza.trim())) mensagens.add("Natureza");
    	if(codigoProdutor == null || Strings.isEmpty(codigoProdutor.trim())) mensagens.add("Codigo Produtor");
    	
    	if(!CollectionUtils.isEmpty(mensagens)) {
    		String mensagem = String.join("\", \"", mensagens);
    		
    		if(mensagens.size() > 1) {
    			throw new ObjectDefaultException("Para validar a nota fiscal de entrada é necessário informar os campos (\"" + mensagem + "\")");
    		}
    		
    		throw new ObjectDefaultException("Para validar a nota fiscal de entrada é necessário informar o campo (\"" + mensagem + "\")");
    	}
    	
    	IntegracaoPagina pagina = integrationService.buscarPorPagina(ENTRADA_PRODUCAO);
    	IntegracaoPaginaFuncionalidade funcionalidade = pagina.getFuncionalidade(INT_IND_VALIDAR_NF_ENTRADA);
    	
    	String url = pagina.getUrlPrincipalApi(profileActive) + funcionalidade.getEndPointSend();
    	url = url.replaceAll("\\{\\{numero-nota-fiscal\\}\\}", numeroNotaFiscal.toString());
    	url = url.replaceAll("\\{\\{serie\\}\\}", serie.toString());
    	url = url.replaceAll("\\{\\{natureza\\}\\}", natureza.toString());
    	url = url.replaceAll("\\{\\{codigo-produtor\\}\\}", codigoProdutor);
    	
    	HttpMethod httpMethod = IntegrationService.getMetodo(funcionalidade);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		if(!CollectionUtils.isEmpty(pagina.getHeaders())) {
			for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
				if(Strings.isNotEmpty(header.getChave()) && Strings.isNotEmpty(header.getValor())){
					httpHeaders.add(header.getChave(), header.getValor());
				}
			}
		}
		
		logger.info("VALIDAR NOTA FISCAL ENTRADA: (" + httpMethod.name() + ") " + url);
		
		if(pagina.getAuth(profileActive) != null) {
			try {
				IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
				restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(auth.getLogin(), auth.getSenha()));	
				httpHeaders.setBasicAuth(auth.getLogin(), auth.getSenha());
			}
			catch (Exception e) {
				throw new ObjectDefaultException("Falha ao recuperar usuário e senha para a consulta.");
			}
		}
    	
		try {
			HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
			ResponseEntity<NotaFiscalEntradaDto> result = restTemplate.exchange(
					url,
	                httpMethod,
	                requestEntity,
	                NotaFiscalEntradaDto.class
	        );
			
			NotaFiscalEntradaDto notaFiscalEntradaDto = result.getBody();
			
			if(notaFiscalEntradaDto == null) {
				logger.info("A consulta não retornou dados.");
	    		throw new ObjectDefaultException("A consulta não retornou dados.");
			}
	    	
	    	return notaFiscalEntradaDto.getResponse();
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
}

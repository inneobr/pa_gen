package br.coop.integrada.api.pa.aplication.controller.integration;

import java.security.InvalidKeyException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.modelDto.enumDto.FuncionalidadeEnumDto;
import br.coop.integrada.api.pa.domain.modelDto.enumDto.PaginaEnumDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoFuncionalidadeTeste;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaDto;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegracaoPaginaOptions;
import br.coop.integrada.api.pa.domain.modelDto.integration.IntegrationAuthDto;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/integracao")
@Tag(name = "Integracao", description = "Integração Metodos.")
public class IntegrationController {
	
	@Autowired
	private IntegrationService integrationService;
	
	@GetMapping("/origem/{pagina}")
	public ResponseEntity<IntegracaoPaginaOptions> getPaginaIntegrationGenesis(@PathVariable(name="pagina") PaginaEnum pagina){
		try {
			return ResponseEntity.ok(integrationService.getPaginaGenesis(pagina));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao buscar dados de origem.", e);
		}
	}

	@PostMapping("/auth")
	public ResponseEntity<IntegrationAuth> setIntegrationAuth(@RequestBody IntegrationAuth integartion) throws InvalidKeyException {
		try {
			return ResponseEntity.ok(integrationService.cadastrarLoginExterno(integartion));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao cadastrar dados de login. ", e);
		}
	}
	
	@GetMapping("/auth/{id}")
	public ResponseEntity<IntegrationAuth> buscarLoginCadastrado(@PathVariable(name="id") Long id) throws InvalidKeyException {
		try {
			return ResponseEntity.ok(integrationService.findById(id));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao buscar login cadastrado. ", e);
		}
	}	
	
	@GetMapping("/auth/find-all")
	public ResponseEntity<Page<IntegrationAuthDto>> findAllLogins(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		try {
			return ResponseEntity.ok(integrationService.findAllLogins(pageable));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao listar logins cadastrados. ", e);
		}
	}
	
	@GetMapping("/auth/buscar/{login}")
	public ResponseEntity<List<IntegrationAuthDto>> listarLoginCadastrados(@PathVariable(name="login") String login) throws InvalidKeyException {
		try {
			return ResponseEntity.ok(integrationService.listarLoginCadastrados(login));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao listar logins cadastrados. ", e);
		}
	}	
	
	@GetMapping("/auth/buscar-por-descricao/{descricao}")
	public ResponseEntity<List<IntegrationAuthDto>> listarLoginCadastradosPorDescricao(@PathVariable(name="descricao") String descricao) throws InvalidKeyException {
		try {
			return ResponseEntity.ok(integrationService.listarLoginCadastradosPorDescricao(descricao));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao listar logins cadastrados. ", e);
		}catch(Exception e) {
			throw new ObjectNotFoundException(e.getMessage());
		}
	}
	
	@PostMapping("/pagina")
	public ResponseEntity<IntegracaoPagina> cadastrarPagina(@RequestBody IntegracaoPaginaDto paginaDto){
		try {
			return ResponseEntity.ok(integrationService.cadastrarPagina(paginaDto));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao cadastrar ou atualizar página. ", e);
		}
	}
	
	@GetMapping("/pagina/{pagina}")
	public ResponseEntity<IntegracaoPagina> buscarPaginaCadastrada(@PathVariable(name="pagina") PaginaEnum pagina ){
		try {
			
			IntegracaoPagina integracaoPagina = integrationService.buscarPaginaCadastrada(pagina);		
			return ResponseEntity.ok(integracaoPagina);
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao buscar página cadastrada. ", e);
		}
		
	}
	
	@GetMapping("/pagina/listar-paginas")
	public ResponseEntity<List<PaginaEnumDto>> getPaginasEnum(){
		try {
			return ResponseEntity.ok(integrationService.getPaginasEnum());
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao listar páginas. ", e);
		}
	}
	
	@GetMapping("/pagina/listar-tipo")
	public ResponseEntity<Object> getTipoEnumPaginas(){
		try {
			return ResponseEntity.ok(integrationService.getTipoEnum());
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao listar tipo. ", e);
		}
	}
	
	@GetMapping("/pagina/funcionalidade/{pagina}")
	public ResponseEntity<List<FuncionalidadeEnumDto>> getFuncionalidadePaginas(@PathVariable(name="pagina") PaginaEnum pagina){
		try {
			return ResponseEntity.ok(integrationService.getFuncionalidadePageEnum(pagina));
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao buscar funcionalidade. ", e);
		}
	}
	
	@GetMapping("/pagina/request-metodo")
	public ResponseEntity<Object> getHttpRequestMethodEnum(){
		return ResponseEntity.ok(integrationService.getHttpRequestMethodEnum());
	}
	
	@PostMapping("/pagina/request-teste")
	public ResponseEntity<Object> getHttpRequestTest(@RequestBody IntegracaoFuncionalidadeTeste requestSendTest){
		try {			
			return ResponseEntity.ok(integrationService.getHttpRequestTest(requestSendTest));
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Caminho ou dados de integração incorretos. ", e);
		} catch (InvalidKeyException e) {
			throw new ObjectNotFoundException("Token de decodificação de senha inválido ", e);
		}
		
	}	
	
	@GetMapping("/pagina/simplificado/{paginaEnum}")
	public ResponseEntity<IntegracaoPaginaDto> buscarPorPagina( @PathVariable(name="paginaEnum") PaginaEnum paginaEnum){
		try {
			IntegracaoPagina integracaoPagina = integrationService.buscarPorPagina(paginaEnum);		
			return ResponseEntity.ok(IntegracaoPaginaDto.construir(integracaoPagina));
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Falha ao buscar pagina de integração. ", e);
		}
	}
		
	@GetMapping("/pagina/funcionalidade-automaticas")
	public ResponseEntity<List<IntegracaoPaginaFuncionalidade>> getFuncionalidadeAutomaticas(){
		return ResponseEntity.ok(integrationService.getFuncionalidadesAutomaticas());
	}
}

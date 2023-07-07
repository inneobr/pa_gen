package br.coop.integrada.api.pa.aplication.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.utils.DefaultResponse;
import br.coop.integrada.api.pa.domain.enums.PaginaAreaEnum;
import br.coop.integrada.api.pa.domain.model.InformacoesRecursoSistema;
import br.coop.integrada.api.pa.domain.service.InformacoesRecursoSistemaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/info-recurso-sistema")
@Tag(name = "InformacoesRecursoSistema", description = "Informações Recurso Sistema")
public class InformacoesRecursoSistemaController {
	
	@Autowired
	private InformacoesRecursoSistemaService informacoesRecursoSistemaService;
	
	
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody InformacoesRecursoSistema informacoesRecursoSistema){
		
		try {		
			
			informacoesRecursoSistemaService.salvar(informacoesRecursoSistema);			
			return ResponseEntity.ok().body(
					DefaultResponse.construir(HttpStatus.ACCEPTED.value(), "Informação salva com sucesso!"));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel salvar a informação.", e), 
            		HttpStatus.BAD_REQUEST);
			
		}catch(Exception e) {
			return new ResponseEntity<>(
            		DefaultResponse.construir(HttpStatus.BAD_REQUEST.value(), "Não foi possivel salvar a informação!", e.getMessage()), 
            		HttpStatus.BAD_REQUEST);
        }     
			
	}
	
	@GetMapping("/paginas/{filtro}")
	public ResponseEntity<?> buscarTodasPaginas(@PathVariable(name = "filtro") String filtro){
		try {
			Boolean possuiInformativoSistema = filtro!=null && !filtro.toUpperCase().equals("NULL") ? Boolean.parseBoolean(filtro) : null;
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarPaginas(possuiInformativoSistema));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe pagina cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	
	
	@GetMapping("/areas/{pagina}")
	public ResponseEntity<?> buscarTodasArea(@PathVariable(name = "pagina") String pagina){
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarArea(pagina));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe área cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	
	
	//Utilizar quando for alterado as combo-box na tela de edição para trazer as informações selecionadas
	@GetMapping("/item/{paginaArea}")
	public ResponseEntity<?> buscarInformacoesModal(@PathVariable(name = "paginaArea") PaginaAreaEnum paginaArea){
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarInformacaoModal(paginaArea));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Informações do recurso do sistema não encontrado.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable(name = "id") Long id){
	
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.unico(id).get());
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Informações do recurso do sistema não encontrado.", e), 
	        		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/todos")
	public ResponseEntity<?> buscarTodos(@PageableDefault(page = 0, size = 5, sort = "paginaArea", direction = Sort.Direction.ASC) Pageable pageable){	
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarTodosList(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem Informacões de recurso do sistema cadastrados.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	@GetMapping("/todosAtivos")
	public ResponseEntity<?> buscarTodosAtivosList(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){	
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarTodosAtivoList(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem Informacões de recurso do sistema cadastrados.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	
	/*@GetMapping("/pagina/{paginaArea}")
	public ResponseEntity<?> buscarPaginaByPaginaArea(@PathVariable(name = "paginaArea") String paginaArea){
		try {
			return ResponseEntity.ok().body(informacoesRecursoSistemaService.buscarPaginaByPaginaArea(paginaArea));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem Informacões de recurso do sistema cadastrados.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}*/
	
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<?> ativarTipoGmo(@PathVariable(name="id") Long id){
		try{
			informacoesRecursoSistemaService.ativar(id);
			return ResponseEntity.ok().body("Informação recurso sistema ativado com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar o tipo Gmo.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarTipoGmo(@PathVariable(name="id") Long id){
		try{
			informacoesRecursoSistemaService.inativar(id);
			return ResponseEntity.ok().body("Informação recurso sistema ativado com sucesso!");
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultResponse.construir(
							HttpStatus.BAD_REQUEST.value(),	"Não foi possível inativar o tipo gmo!", e.getMessage()
					),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
	
	
	
	
	
}

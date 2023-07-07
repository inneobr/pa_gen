package br.coop.integrada.api.pa.aplication.controller.gmo;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoGmoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/produto/grupo-produto-gmo")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Grupo Produto Gmo", description = "Grupo de produto gmo.")
public class GrupoProdutoGmoController {
	
	@Autowired
	private GrupoProdutoGmoService grupoProdutoGmoService;
	
	@PutMapping("/desativar/{id}")
	public ResponseEntity<?> deletarGrupoProdutoGmo(@PathVariable(name="id") Long id){
		try{
			grupoProdutoGmoService.excluirGrupoProdutoGmo(id);
			return ResponseEntity.ok().body("Grupo de produtos gmo excluido com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel excluir o grupo de produto gmo.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}	
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> buscarIdGrupoProdutoGmo(@PathVariable(name="id") Long id){
		try{			
			return ResponseEntity.ok().body(grupoProdutoGmoService.buscarIdGrupoProdutoGmo(id));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel localizar o id do grupo de produto gmo.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping
	public ResponseEntity<?> buscarTodosPage(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<GrupoProdutoGmo> grupoProdutoGmo = grupoProdutoGmoService.buscarTodosPage(pageable);
		if(!grupoProdutoGmo.isEmpty()) {
			return ResponseEntity.ok().body(grupoProdutoGmo);
        }else {
        	return new ResponseEntity<>(DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem grupo de produto gmo cadastrados."), HttpStatus.BAD_REQUEST);        	
        }
	}
	
	@GetMapping("/todos")
	public ResponseEntity<?> buscarTodos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<GrupoProdutoGmo> grupoProdutoGmo = grupoProdutoGmoService.buscarTodos(pageable);
		if(!grupoProdutoGmo.isEmpty()) {
			return ResponseEntity.ok().body(grupoProdutoGmo);
        }else {
        	return new ResponseEntity<>(DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem grupo de produto gmo cadastrados."), HttpStatus.BAD_REQUEST);
        }
	}


}

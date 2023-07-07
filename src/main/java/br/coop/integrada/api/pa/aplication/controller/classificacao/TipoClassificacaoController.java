package br.coop.integrada.api.pa.aplication.controller.classificacao;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.TipoClassificacaoResponseDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.cadastro.TipoClassificacaoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/tipo-classificacao")
@Tag(name = "TipoClassificacao", description = "Tipos de Classificação")
public class TipoClassificacaoController {
	
	@Autowired
	public TipoClassificacaoService tipoClassificacaoService;

	@Autowired
	public HistoricoGenericoService historicoGenericoService;
	

	@GetMapping
	public ResponseEntity<?> listarTodos(){
		try {
			return ResponseEntity.ok().body(tipoClassificacaoService.listarTodos());
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe pagina cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}

	
	@GetMapping("/listar")
	public ResponseEntity<?> listarTodosPage(@PageableDefault(page = 0, size = 5, sort = "tipoClassificacao", direction = Sort.Direction.ASC) Pageable pageable,
			String filtro,
			Situacao situacao){
		try {
			Page<TipoClassificacao> tipoClassificacao = tipoClassificacaoService.listarPage(pageable, filtro, situacao);
			return ResponseEntity.ok().body(tipoClassificacao);
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe tipo classificação cadastrado.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/{tipoClassificacao}")
	public ResponseEntity<?> find(@PathVariable(name = "tipoClassificacao") String tipoClassificacao){
	
		try {
			TipoClassificacaoEnum classificacaoEnum = TipoClassificacaoEnum.valueOf(tipoClassificacao);
			return ResponseEntity.ok().body(tipoClassificacaoService.unico(classificacaoEnum).get());
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Informações do recurso do sistema não encontrado.", e), 
	        		HttpStatus.BAD_REQUEST);		
		}
	}
	
	
	@PostMapping
	public ResponseEntity<TipoClassificacaoResponseDto> salvar(@Valid @RequestBody TipoClassificacao tipoClassificacao){
	
		try {		
			
			tipoClassificacaoService.salvar(tipoClassificacao);			
						
			TipoClassificacaoResponseDto tipoClassificacaoResponseDto = TipoClassificacaoResponseDto.construir(
						tipoClassificacao,
						true,
						"Tipo Classificação salvo com sucesso!"
					);
			return ResponseEntity.ok(tipoClassificacaoResponseDto);
			
		}
		catch(ConstraintViolationException e) {
			/*return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel salvar a informação.", e), 
            		HttpStatus.BAD_REQUEST);*/
			
			String mensagem = "Não foi possível salvar o estabelecimento.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe o Tipo Classificação: "+ tipoClassificacao.getTipo() +" cadastrado.";
			}
			
			TipoClassificacaoResponseDto tipoClassificacaoResponseDto = TipoClassificacaoResponseDto.construir(
					tipoClassificacao,
					false,
					mensagem,
					e
			);
			return ResponseEntity.badRequest().body(tipoClassificacaoResponseDto);
			
		}
		catch(DataIntegrityViolationException e) {
			
			String mensagem = "Não foi possível salvar o estabelecimento.";

			
			mensagem += " Obs.: Já existe o Tipo Classificação: "+ tipoClassificacao.getTipo() +" cadastrado.";
			

			TipoClassificacaoResponseDto tipoClassificacaoResponseDto = TipoClassificacaoResponseDto.construir(
					tipoClassificacao,
					false,
					mensagem,
					e
			);
			return ResponseEntity.badRequest().body(tipoClassificacaoResponseDto);
		
		}
		catch(Exception e) {
			/*return new ResponseEntity<>(
            		DefaultResponse.construir(HttpStatus.BAD_REQUEST.value(), "Não foi possivel salvar a informação!", e.getMessage()), 
            		HttpStatus.BAD_REQUEST);*/
			TipoClassificacaoResponseDto tipoClassificacaoResponseDto = TipoClassificacaoResponseDto.construir(
					tipoClassificacao,
					false,
					"Não foi possível salvar o tipo de classificação. Por favor, analisar a lista de validações dos campos!"
				);
			return ResponseEntity.badRequest().body(tipoClassificacaoResponseDto);
        }     
			
	}
			
	
	@PutMapping("/inativar/{tipoClassificacao}")
	public ResponseEntity<?> inativarTipoClassificacao(@PathVariable(name="tipoClassificacao") String tipoClassificacao){
		try{
			System.out.println("TipoClassificação Parametro: "+ tipoClassificacao);
			tipoClassificacaoService.inativar(TipoClassificacaoEnum.valueOf(tipoClassificacao));
			return ResponseEntity.ok().body(
					DefaultResponse.construir(HttpStatus.ACCEPTED.value(), "Tipo de classificação foi desativado com sucesso!"));
		
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel desativar o tipo de classificação.", e), 
            		HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PutMapping("/ativar/{tipoClassificacao}")
	public ResponseEntity<?> ativarTipoClassificacao(@PathVariable(name="tipoClassificacao") String tipoClassificacao){
		try{
		
			tipoClassificacaoService.ativar(TipoClassificacaoEnum.valueOf(tipoClassificacao));
			return ResponseEntity.ok().body(
					DefaultResponse.construir(HttpStatus.ACCEPTED.value(), "Tipo de classificação foi ativado com sucesso!"));
		
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar o tipo de classificação.", e), 
            		HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/tipos")
	public ResponseEntity<?> buscarTodosTipos(){
		try {
			return ResponseEntity.ok().body(tipoClassificacaoService.buscarTipos());
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe tipos cadastrados.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	
	@GetMapping("/historico/{tipoClassificacao}/{id}")
	public ResponseEntity<?> listarHistorico(@PathVariable(name="tipoClassificacao") String tipoClassificacao, @PathVariable(name="id") Long id, @PageableDefault(page = 0, size = 3, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable){
		try {
			Page<HistoricoGenericoDto> historicoGenerico = historicoGenericoService.buscarPorRegistroEPagina(id, PaginaEnum.valueOf(tipoClassificacao), pageable);
			return ResponseEntity.ok().body(historicoGenerico);
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe pagina cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/sincronizar/{id}")
	public void sincronizar(@PathVariable(name="id") Long id) {
		tipoClassificacaoService.sincronizarTipoClassificacoes(id);
	}
	
	
	
}

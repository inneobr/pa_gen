package br.coop.integrada.api.pa.aplication.controller.produto;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.utils.DefaultResponse;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoResponseDto;
import br.coop.integrada.api.pa.domain.service.produto.TipoProdutoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/produto/tipo")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Tipo Produto", description = "Endpoints tipo de produto.")
public class TipoProdutoController {
	private static final Logger logger = LoggerFactory.getLogger(TipoProdutoController.class);
	
	@Autowired
	private TipoProdutoService tipoProdutoService;
	
		
	@PutMapping
    public ResponseEntity<TipoProdutoResponseDto> salvar(@Valid @RequestBody TipoProdutoDto tipoProdutoDto){
		TipoProdutoResponseDto responseDto = null;

        try {
        	TipoProduto tipoProduto = tipoProdutoService.cadastrarOuAtualizar(tipoProdutoDto);
        	tipoProdutoService.sincronizar(tipoProduto.getIdUnico());
        	responseDto = TipoProdutoResponseDto.construir(tipoProduto, true, "O tipo produto foi gravado com sucesso!");
            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
			responseDto = TipoProdutoResponseDto.construir(
					tipoProdutoDto,
					false,
					"Não foi possível salvar o tipo produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível salvar o tipo produto.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe tipo produto cadastrado com o nome informado.";
			}

			responseDto = TipoProdutoResponseDto.construir(
					tipoProdutoDto,
					false,
					mensagem,
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				responseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o tipo produto. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				responseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o produto!",
						e
				);
			}
		}
		catch (Exception e) {
			responseDto = TipoProdutoResponseDto.construir(
					tipoProdutoDto,
					false,
					"Não foi possível salvar o tipo produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
        
    }

	
	@PostMapping("/save-all")
	public ResponseEntity<List<TipoProdutoResponseDto>> salvar(@RequestBody List<TipoProdutoDto> objDto){
		List<TipoProdutoResponseDto> response = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(TipoProdutoDto tipoProdutoDto : objDto) {
			try {
				
				TipoProduto tipoProduto = tipoProdutoService.cadastrarOuAtualizar(tipoProdutoDto);
				tipoProdutoService.sincronizar(tipoProduto.getIdUnico());
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProduto, 
						true, 
						"Tipo produto salvo com sucesso!");
				
				
				response.add(tipoProdutoResponseDto);

			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o tipo. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(tipoProdutoResponseDto);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
							tipoProdutoDto,
							false,
							"Não foi possível salvar o tipo. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(tipoProdutoResponseDto);
				}
				else {
					TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
							tipoProdutoDto,
							false,
							"Não foi possível salvar o tipo!",
							e
					);
					response.add(tipoProdutoResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar o tipo.";

				if(e.getMessage().toUpperCase().contains("KEY_UN_NOME_TIPO_PROUTO")) {
					mensagem += " Obs.: Já existe tipo cadastrado com o nome informado.";
				}else if(e.getMessage().toUpperCase().contains("KEY_UN_ID_UNICO_TIPO_PRODUTO")) {
					mensagem += " Obs.: Já existe tipo cadastrado com o idUnico informado.";					
				}

				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						mensagem,
						e
				);
				response.add(tipoProdutoResponseDto);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o tipo de produto!",
						e
				);
				response.add(tipoProdutoResponseDto);
			}
		}

		if(falhaIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<TipoProdutoResponseDto>> integrationSaveAll(@RequestBody TipoProdutoIntegrationDto objDto){
		List<TipoProdutoResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(TipoProdutoDto tipoProdutoDto : objDto.getTipoProduto()) {
			try {
				
				TipoProduto tipoProduto = tipoProdutoService.cadastrarOuAtualizar(tipoProdutoDto);
				
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProduto, 
						true, 
						"Tipo produto salvo com sucesso!");
				
				
				response.add(tipoProdutoResponseDto);
				sucessoIntegracao = true;
			}
			catch(ConstraintViolationException e) {
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o tipo. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(tipoProdutoResponseDto);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
							tipoProdutoDto,
							false,
							"Não foi possível salvar o tipo. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(tipoProdutoResponseDto);
				}
				else {
					TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
							tipoProdutoDto,
							false,
							"Não foi possível salvar o tipo!",
							e
					);
					response.add(tipoProdutoResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível salvar o tipo.";

				if(e.getMessage().toUpperCase().contains("KEY_UN_NOME_TIPO_PROUTO")) {
					mensagem += " Obs.: Já existe tipo cadastrado com o nome informado.";
				}else if(e.getMessage().toUpperCase().contains("KEY_UN_ID_UNICO_TIPO_PRODUTO")) {
					mensagem += " Obs.: Já existe tipo cadastrado com o idUnico informado.";					
				}

				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						mensagem,
						e
				);
				response.add(tipoProdutoResponseDto);
			}
			catch (Exception e) {
				TipoProdutoResponseDto tipoProdutoResponseDto = TipoProdutoResponseDto.construir(
						tipoProdutoDto,
						false,
						"Não foi possível salvar o tipo de produto!",
						e
				);
				response.add(tipoProdutoResponseDto);
			}
		}

		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}

		
	@PutMapping("/ativar/{idUnico}")
	public ResponseEntity<?> ativarTipoProduto(@PathVariable(name="idUnico") String idUnico) throws Exception{
		try{
			TipoProduto tipoProduto = tipoProdutoService.ativarTipoProduto(idUnico);
			tipoProdutoService.sincronizar(tipoProduto.getIdUnico());
			return ResponseEntity.ok().body("Tipo de produtos ativado com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar o tipo de produto.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}

	@PutMapping("/inativar/{idUnico}")
	public ResponseEntity<?> inativarTipoProduto(@PathVariable(name="idUnico") String idUnico){
		try{
			TipoProduto tipoProduto = tipoProdutoService.inativarTipoProduto(idUnico);
			tipoProdutoService.sincronizar(tipoProduto.getIdUnico());
			return ResponseEntity.accepted().body(
					DefaultResponse.construir(
							HttpStatus.ACCEPTED.value(), "O Tipo de Produto foi inativado com sucesso!"
					)
			);
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultResponse.construir(
							HttpStatus.BAD_REQUEST.value(),	"Não foi possível inativar o tipo de produto!", e.getMessage()
					),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
	@GetMapping("/ativos")
	public ResponseEntity<?> buscarTodosProdutoAtivos(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			@RequestParam(name = "nome", defaultValue = "") String nome
	){
		try{
			return ResponseEntity.ok().body(tipoProdutoService.listarTipoProdutoAtivos(nome, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo cadastrados.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping
	public ResponseEntity<?> buscarTodosProduto(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(tipoProdutoService.listarTodosTipoProduto(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo de produto salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/nome")
	public ResponseEntity<?> buscarProdutoPorNome(@RequestParam(name = "nome", defaultValue = "") String nome){
		try{
			return ResponseEntity.ok().body(tipoProdutoService.buscaTipoProdutoPorNome(nome));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo de produto salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarTipoGmoPorId(@PathVariable(name="id") Long id){
		TipoProduto tipoProduto = tipoProdutoService.buscarTipoProdutoPorId(id);

		if(tipoProduto != null) {
			return ResponseEntity.ok().body(tipoProduto);
		}
		return new ResponseEntity<>(
				DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo de produto salvos."), 
        		HttpStatus.BAD_REQUEST);		
	}
	
	@GetMapping("/sincronizar")
	public void sincronizar() {
		tipoProdutoService.sincronizar(null);
	}
	
	@GetMapping("/sincronizar/{idUnico}")
	public void sincronizar(@PathVariable(name="idUnico") String idUnico) {
		tipoProdutoService.sincronizar(idUnico);
	}
	
	@GetMapping("/buscar-com-paginacao")
    public ResponseEntity<Page<TipoProdutoDto>> buscarComPaginacao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "nome", defaultValue = "") String nome,
            @RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao
    ) {
        Page<TipoProdutoDto> tipoProdutoDtoPage = tipoProdutoService.buscarComPaginacao(pageable, nome, situacao);
        return ResponseEntity.ok(tipoProdutoDtoPage);
    }
	
	@GetMapping("/complete/{pesquisar}")
	public ResponseEntity<?> findByAutoComplete(
		@PathVariable(name="pesquisar") String pesquisar){
		return ResponseEntity.ok().body(tipoProdutoService.findByAutoComplete(pesquisar));
	}	
}

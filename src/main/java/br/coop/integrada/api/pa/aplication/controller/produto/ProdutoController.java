package br.coop.integrada.api.pa.aplication.controller.produto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.OperacaoProdutoEnum;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoCodDescricaoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilter;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoFilterDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaValidDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoValidDto;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/produto")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Produto", description = "Endpoints de produto.")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/buscar/{codigoOuDescricao}")
	public ResponseEntity<List<ProdutoCodDescricaoDto>> buscarCodigoOuNome(@PathVariable(name="codigoOuDescricao") String codigoOuDescricao){
		return ResponseEntity.ok(produtoService.getCodigoOrDescricao(codigoOuDescricao));
	}
	
	@PostMapping
	public ResponseEntity<List<ProdutoResponseDto>> cadastrarVarios(@RequestBody List<Produto> produtos) {
		List<ProdutoResponseDto> responseDtos = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(Produto item : produtos) {
			ProdutoResponseDto responseDto = null;

			try{
				Produto produto = produtoService.cadastrar(item);
				responseDto = ProdutoResponseDto.construir(
					produto,
					true,
					"O produto foi cadastrado com sucesso!"
				);
			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				responseDto = ProdutoResponseDto.construir(
						item,
						false,
						"Não foi possível cadastrar o produto. Por favor, analisar a lista de validações dos campos!",
						e
				);
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível cadastrar o produto.";

				if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
					mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
				}

				responseDto = ProdutoResponseDto.construir(
						item,
						false,
						mensagem,
						e
				);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					responseDto = ProdutoResponseDto.construir(
							item,
							false,
							"Não foi possível cadastrar o produto. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
				}
				else {
					responseDto = ProdutoResponseDto.construir(
							item,
							false,
							"Não foi possível cadastrar o produto!",
							e
					);
				}
			}
			catch (Exception e) {
				falhaIntegracao = true;
				responseDto = ProdutoResponseDto.construir(
						item,
						false,
						"Não foi possível cadastrar o produto!",
						e
				);
			}

			responseDtos.add(responseDto);
		}

		if(falhaIntegracao) {
			return ResponseEntity.badRequest().body(responseDtos);
		}
		else {
			return ResponseEntity.ok(responseDtos);
		}
	}
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<ProdutoResponseDto> ativar(@PathVariable(name="id") Long id){
		ProdutoResponseDto responseDto = null;

		try{
			Produto produto = produtoService.ativarProduto(id);
			responseDto = ProdutoResponseDto.construir(
					produto,
					true,
					"O produto foi ativado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch (Exception e) {
			responseDto = ProdutoResponseDto.construir(
					null,
					false,
					"Não foi possível ativar o produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<ProdutoResponseDto> inativar(@PathVariable(name="id") Long id){
		ProdutoResponseDto responseDto = null;

		try{
			Produto produto = produtoService.inativarProduto(id);
			responseDto = ProdutoResponseDto.construir(
					produto,
					true,
					"O produto foi inativado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch (Exception e) {
			responseDto = ProdutoResponseDto.construir(
					null,
					false,
					"Não foi possível inativar o produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}	

	@GetMapping("/ativos")
	public ResponseEntity<Page<Produto>> listarTodosProdutoAtivo(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
	){
		Page<Produto> produtoPage = produtoService.listarProdutoAtivos(pageable);
		return ResponseEntity.ok(produtoPage);
	}
	
	@GetMapping
	public ResponseEntity<Page<Produto>> buscarTodosProduto(
			@PageableDefault(page = 0, size = 5, sort = "codItem", direction = Sort.Direction.ASC) Pageable pageable,
			String filtro,
			Situacao situacao
	){
		Page<Produto> produtoPage = produtoService.buscarTodosProduto(pageable, filtro, situacao);
		return ResponseEntity.ok(produtoPage);
	}
	
	@GetMapping("/pesquisa")
	public ResponseEntity<Page<Produto>> buscarTodosProduto2(
			@PageableDefault(page = 0, size = 5, sort = "codItem", direction = Sort.Direction.ASC) Pageable pageable,
			ProdutoFilter filtro,
			Situacao situacao
	){
		Page<Produto> produtoPage = produtoService.buscarTodosProduto(pageable, filtro, situacao);
		return ResponseEntity.ok(produtoPage);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDto> buscarProdutoPorId(@PathVariable(name="id") Long id){
		Produto produto = produtoService.buscarProdutoPorId(id);

		if(produto != null) {
			
			ProdutoDto produtoDto = ProdutoDto.convertProdutoToDto(produto);
			
			return ResponseEntity.ok().body(produtoDto);
		}
		else {
			throw new ObjectNotFoundException("Não foi encontrado produto com o ID " + id);
		}
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<ProdutoResponseDto> atualizar(@Valid @RequestBody ProdutoDto produtoDto){
		ProdutoResponseDto responseDto = null;

		try{
			Produto produto = produtoService.atualizar(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					true,
					"O produto foi atualizado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(ConstraintViolationException e) {
			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível atualizar o produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível atualizar o produto.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
			}

			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					mensagem,
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			Produto produto = ProdutoDto.convertDto(produtoDto);

			if (rootCause instanceof ConstraintViolationException) {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível atualizar o produto. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível atualizar o produto!",
						e
				);
			}
		}
		catch (Exception e) {
			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível atualizar o produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<ProdutoResponseDto> salvar(@Valid @RequestBody ProdutoDto produtoDto){
		ProdutoResponseDto responseDto = null;

		try{
			Produto produto = produtoService.salvar(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					true,
					"O produto foi salvo com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(ConstraintViolationException e) {
			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível salvar o produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível salvar o produto.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
			}

			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					mensagem,
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			Produto produto = ProdutoDto.convertDto(produtoDto);

			if (rootCause instanceof ConstraintViolationException) {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível salvar o produto. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível salvar o produto!",
						e
				);
			}
		}
		catch (Exception e) {
			Produto produto = ProdutoDto.convertDto(produtoDto);
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível salvar o produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@PutMapping("/atualizarproduto")
	public ResponseEntity<ProdutoResponseDto> atualizarproduto(@Valid @RequestBody Produto produto){
		ProdutoResponseDto responseDto = null;

		try{
			Produto produtoRetorno = produtoService.atualizar(produto);
			responseDto = ProdutoResponseDto.construir(
					produtoRetorno,
					true,
					"O produto foi atualizado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(ConstraintViolationException e) {
			
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível atualizar o produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível atualizar o produto.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
			}
			
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					mensagem,
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			

			if (rootCause instanceof ConstraintViolationException) {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível atualizar o produto. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível atualizar o produto!",
						e
				);
			}
		}
		catch (Exception e) {
			
			responseDto = ProdutoResponseDto.construir(
					produto,
					false,
					"Não foi possível atualizar o produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@GetMapping("/buscar-por/codigo-grupo-produto/{codigoGrupoProduto}")
	public ResponseEntity<List<ProdutoGetDto>> pesquisarProdutoPorCodigoOuDescricaoVinculadoAoGrupoProduto(
			@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto,
			@RequestParam(name = "filtro", defaultValue = "") String codigoOuDescricao,
			@PageableDefault(page = 0, size = 15, sort = "codItem", direction = Sort.Direction.ASC) Pageable pageable
			){
		Page<Produto> produtoPage = produtoService.pesquisarProdutoPorCodigoOuDescricaoVinculadoAoGrupoProduto(codigoOuDescricao, codigoGrupoProduto, pageable);
		List<ProdutoGetDto> produtoGetDtos = ProdutoGetDto.construir(produtoPage.getContent());
		return ResponseEntity.ok(produtoGetDtos);
	}
	

	@PostMapping("/integration/save-all")
	public ResponseEntity<List<ProdutoResponseDto>> integrationSaveAll(@RequestBody ProdutoIntegrationDto objDto){
		List<ProdutoResponseDto> responseDtos = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(ProdutoDto item : objDto.getProdutos()) {
			ProdutoResponseDto responseDto = null;

			try{
				Produto produto = produtoService.integrationSave(item);
				responseDto = ProdutoResponseDto.construir(
						produto,
						true,
						"Integração realizada com sucesso!"
				);
				
				if(responseDto != null && responseDto.getIntegrated()) {
	            	sucessoIntegracao = true;
	            }
			}
			catch(ConstraintViolationException e) {
				Produto produto = ProdutoDto.convertDto(item);
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível gravar o produto. Por favor, analisar a lista de validações dos campos!",
						e
				);
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível gravar o produto.";

				if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
					mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
				}
				

                if(e.getMessage().toUpperCase().contains("KEY_V_PRODUTO_REFERENCIA")) {
                    mensagem += " Obs.: Já existe uma referência de produto cadastrado com o código informado.";
                }

				Produto produto = ProdutoDto.convertDto(item);
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						mensagem,
						e
				);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);
				Produto produto = ProdutoDto.convertDto(item);

				if (rootCause instanceof ConstraintViolationException) {
					responseDto = ProdutoResponseDto.construir(
							produto,
							false,
							"Não foi possível gravar o produto. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
				}
				else {
					responseDto = ProdutoResponseDto.construir(
							produto,
							false,
							"Não foi possível gravar o produto!",
							e
					);
				}
			}
			catch (Exception e) {
				Produto produto = ProdutoDto.convertDto(item);
				responseDto = ProdutoResponseDto.construir(
						produto,
						false,
						"Não foi possível gravar o produto!",
						e
				);
			}

			responseDtos.add(responseDto);
		}
		
		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(responseDtos);
		}
		else {
			return ResponseEntity.ok(responseDtos);
		}
	}
	
	@GetMapping("/valida-produto/{codigoProduto}")
	public ResponseEntity<ProdutoValidDto> validarProduto(@PathVariable(name="codigoProduto") String codigoProduto){
		try{
			ProdutoValidDto produto = produtoService.validaProduto(codigoProduto);
			return ResponseEntity.ok(produto);
		}catch(DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping("/valida-produto-referencia/{codigoProduto}/{codigoReferencia}")
	public ResponseEntity<ProdutoReferenciaValidDto> validarProdutoReferencia(
			@PathVariable(name="codigoProduto") String codigoProduto,
			@PathVariable(name="codigoReferencia") String codigoReferencia){
		try{
			ProdutoReferenciaValidDto produtoReferencia = produtoService.getProdutoReferencia(codigoProduto, codigoReferencia);
			return ResponseEntity.ok(produtoReferencia);
		}catch(DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping("/buscar-data-sul/")
	public ResponseEntity<String> buscarItemDataSul(
			@RequestParam(name = "codProduto", defaultValue = "") String codProduto,
			@RequestParam(name = "operacao", defaultValue = "") OperacaoProdutoEnum operacao,
			@RequestParam(name = "codProdutor", defaultValue = "") String codProdutor
	){
		
		String codDataSul = produtoService.buscarItemDataSul(codProduto, operacao, codProdutor);
		return ResponseEntity.ok(codDataSul);
	}
	
	@GetMapping("/grupo/{idGrupo}")
	public ResponseEntity<List<ProdutoFilterDto>> buscarProdutoPorGrupo(@PathVariable(name="idGrupo") Long idGrupo){
		List<ProdutoFilterDto> produtoGetDtos = produtoService.buscarProdutoPorGrupo(idGrupo);
		return ResponseEntity.ok(produtoGetDtos);
	}
	
	@GetMapping("/grupo/filter")
	public ResponseEntity<List<ProdutoFilterDto>> findProdutoByGrupoFilter(ProdutoFilter filter){
		List<ProdutoFilterDto> produtoGetDtos = produtoService.findProdutoByGrupoFilter(filter);
		return ResponseEntity.ok(produtoGetDtos);
	}
	
}

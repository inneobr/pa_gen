package br.coop.integrada.api.pa.aplication.controller.produto;

import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ReferencaFilterDto;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoReferenciaFilter;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoReferenciaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/produto/referencia")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Produto Referência", description = "Referencia de Produtos.")
public class ProdutoReferenciaController {
	@Autowired
	private ProdutoReferenciaService produtoReferenciaService;	

	@PostMapping
	public ResponseEntity<List<ProdutoReferenciaResponseDto>> salvarVarios(@RequestBody List<ProdutoReferencia> produtoReferencias){
		List<ProdutoReferenciaResponseDto> responseDtos = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(ProdutoReferencia item : produtoReferencias) {
			ProdutoReferenciaResponseDto responseDto = null;

			try {
				ProdutoReferencia produtoReferencia = produtoReferenciaService.salvar(item);
				responseDto = ProdutoReferenciaResponseDto.construir(
						produtoReferencia,
						true,
						"A referência de produto foi salvo com sucesso!"
				);
			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				responseDto = ProdutoReferenciaResponseDto.construir(
						item,
						false,
						"Não foi possível salvar a referência de produto. Por favor, analisar a lista de validações dos campos!",
						e
				);
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar a referência de produto.";

				if(e.getMessage().toUpperCase().contains("KEY_COD_REFERENCIA")) {
					mensagem += " Obs.: Já existe referência cadastrado com o código informado.";
				}

				responseDto = ProdutoReferenciaResponseDto.construir(
						item,
						false,
						mensagem,
						e
				);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				responseDto = ProdutoReferenciaResponseDto.construir(
						item,
						false,
						"Não foi possível salvar a referência de produto!",
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
	
	@PutMapping
	public ResponseEntity<ProdutoReferenciaResponseDto> atualizar(@Valid @RequestBody ProdutoReferenciaDto produtoReferenciaDto){
		ProdutoReferenciaResponseDto responseDto = null;

		try {
			ProdutoReferencia produtoReferencia = produtoReferenciaService.atualizar(produtoReferenciaDto);
			responseDto = ProdutoReferenciaResponseDto.construir(
					produtoReferencia,
					true,
					"A referência de produto foi atualizado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(ConstraintViolationException e) {
			ProdutoReferencia produtoReferencia = ProdutoReferenciaDto.converterDto(produtoReferenciaDto);
			responseDto = ProdutoReferenciaResponseDto.construir(
					produtoReferencia,
					false,
					"Não foi possível atualizar a referência de produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível atualizar a referência de produto.";

			if(e.getMessage().toUpperCase().contains("KEY_COD_REFERENCIA")) {
				mensagem += " Obs.: Já existe referência cadastrado com o código informado.";
			}

			ProdutoReferencia produtoReferencia = ProdutoReferenciaDto.converterDto(produtoReferenciaDto);
			responseDto = ProdutoReferenciaResponseDto.construir(
					produtoReferencia,
					false,
					mensagem,
					e
			);
		}
		catch (Exception e) {
			ProdutoReferencia produtoReferencia = ProdutoReferenciaDto.converterDto(produtoReferenciaDto);
			responseDto = ProdutoReferenciaResponseDto.construir(
					produtoReferencia,
					false,
					"Não foi possível atualizar a referência de produto!",
					e
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}		
	
	
	
	@GetMapping("/busca")
	public ResponseEntity<List<ProdutoReferencia>> buscarPorCodRef(@RequestParam(required = false) String codigo){
		List<ProdutoReferencia> grupoProduto = produtoReferenciaService.buscarPorProdutoReferencia(codigo);
		return ResponseEntity.ok(grupoProduto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoReferencia>> buscarTodasReferenciaProduto(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			@RequestParam(name = "filtro", defaultValue = "") String filtro
	){
		Page<ProdutoReferencia> produtoReferenciaPage = produtoReferenciaService.buscarTodasProdutoReferencia(pageable, filtro);
		return ResponseEntity.ok(produtoReferenciaPage);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoReferencia> buscarProdutoReferencia(@PathVariable(name="id") Long id){
		ProdutoReferencia produtoReferencia = produtoReferenciaService.buscarProdutoReferenciaPorId(id);
		return ResponseEntity.ok(produtoReferencia);
	}
	
	@PutMapping("/inativar/{referencia}")
	public ResponseEntity<?> inativarRef(@PathVariable(name="referencia") String referencia){
		return ResponseEntity.ok(produtoReferenciaService.inativarProdutoReferencia(referencia));		
	}
	
	@PutMapping("/ativar/{referencia}")
	public ResponseEntity<String> ativarRef(@PathVariable(name="referencia") String referencia){		
		return ResponseEntity.ok(produtoReferenciaService.ativarProdutoReferencia(referencia));		
	}
	
	@GetMapping("/filter")
	public ResponseEntity<Page<ProdutoReferencia>> buscarReferenciaProdutoAtivas(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
			ProdutoReferenciaFilter filtro) {
		System.out.println("Filtros: " + filtro.getReferencia());
		Page<ProdutoReferencia> produtoReferenciaPage = produtoReferenciaService.getReferencias(filtro, pageable);
		return ResponseEntity.ok(produtoReferenciaPage);
	}
	
	@GetMapping("/buscar/produto/{idProduto}")
	public ResponseEntity<List<ReferencaFilterDto>> findByProduto(@PathVariable(name="idProduto") Long idProduto) {
		return ResponseEntity.ok(produtoReferenciaService.findByProduto(idProduto));
	}
}

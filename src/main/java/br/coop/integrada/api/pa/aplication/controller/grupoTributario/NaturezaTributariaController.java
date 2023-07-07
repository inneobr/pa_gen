package br.coop.integrada.api.pa.aplication.controller.grupoTributario;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import javax.validation.ConstraintViolationException;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.TransactionSystemException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;

import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturezaTributariaDto;
import br.coop.integrada.api.pa.domain.service.naturezaTributaria.NaturezaTributariaService;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturesaTributariaFilter;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturezaTributariaIntegration;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.integration.NaturezaTributariaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.integration.NaturezaTributariaResponseListDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/natureza-tributaria")
@Tag(name = "Natureza tributaria", description = "Natureza tributaria")
public class NaturezaTributariaController {
	
	@Autowired
	private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
	private NaturezaTributariaService naturezaTributariaService ;
	
	@PostMapping
	public ResponseEntity<NaturezaTributariaResponseListDto>save(@RequestBody NaturezaTributariaDto naturezaTributariaDto){
		NaturezaTributariaResponseListDto response = new NaturezaTributariaResponseListDto();
		Boolean sucessoIntegracao = false;
		
		try {
			naturezaTributariaService.cadastrar(naturezaTributariaDto);				

			NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
					naturezaTributariaDto, 
					false, 
					"Cadastrado realizada com sucesso!");
			
			response.getNaturezasTributarias().add(responseDto);
			sucessoIntegracao = true;
			
		}catch(ConstraintViolationException e) {
			NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
					naturezaTributariaDto,
					false,
					"Não foi possível salvar a Natureza Tributaria. Por favor, analisar a lista de validações dos campos!",
					e
			);
			response.getNaturezasTributarias().add(responseDto);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto,
						false,
						"Não foi possível salvar a Natureza Tributaria. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				response.getNaturezasTributarias().add(responseDto);
			}
			else {
				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto,
						false,
						"Não foi possível salvar a Natureza Tributaria!",
						e
				);
				response.getNaturezasTributarias().add(responseDto);
			}
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível salvar a Natureza Tributaria!";

			

			NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
					naturezaTributariaDto,
					false,
					mensagem,
					e
			);
			response.getNaturezasTributarias().add(responseDto);
		}
		catch (Exception e) {
			NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
					naturezaTributariaDto,
					false,
					"Não foi possível salvar a Natureza Tributaria!",
					e
			);
			response.getNaturezasTributarias().add(responseDto);
		}
		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}		
	}
	

	@PostMapping("/grupo/integration/save-all")
	public ResponseEntity<NaturezaTributariaResponseListDto> integrar(@RequestBody NaturezaTributariaIntegration  naturezaTributariaIntegration){
		NaturezaTributariaResponseListDto responseListDto = new NaturezaTributariaResponseListDto();
		Boolean sucessoIntegracao = false;
		
		for(NaturezaTributariaDto naturezaTributariaDto: naturezaTributariaIntegration.getNaturezasTributarias()){
			try {
				naturezaTributariaService.integration(naturezaTributariaDto);				

				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto, 
						true, 
						"Integração realizada com sucesso!");
				
				responseListDto.getNaturezasTributarias().add(responseDto);
				sucessoIntegracao = true;
				
			}catch(ConstraintViolationException e) {
				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto,
						false,
						"Não foi possível salvar a Natureza Tributaria. Por favor, analisar a lista de validações dos campos!",
						e
				);
				responseListDto.getNaturezasTributarias().add(responseDto);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
							naturezaTributariaDto,
							false,
							"Não foi possível salvar a Natureza Tributaria. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					responseListDto.getNaturezasTributarias().add(responseDto);
				}
				else {
					NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
							naturezaTributariaDto,
							false,
							"Não foi possível salvar a Natureza Tributaria!",
							e
					);
					responseListDto.getNaturezasTributarias().add(responseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível salvar a Natureza Tributaria!";

				

				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto,
						false,
						mensagem,
						e
				);
				responseListDto.getNaturezasTributarias().add(responseDto);
			}
			catch (Exception e) {
				NaturezaTributariaResponseDto responseDto = NaturezaTributariaResponseDto.construir(
						naturezaTributariaDto,
						false,
						"Não foi possível salvar a Natureza Tributaria!",
						e
				);
				responseListDto.getNaturezasTributarias().add(responseDto);
			}
		}
		
		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(responseListDto);
		}
		else {
			return ResponseEntity.ok(responseListDto);
		}
		
	}
	
	@GetMapping
	public ResponseEntity<?> findAllPage(
			NaturesaTributariaFilter filter,
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok().body(naturezaTributariaService.findAllPage(filter, pageable));		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(name="id") Long id) {
		return ResponseEntity.ok().body(naturezaTributariaService.findById(id));		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable(name="id") Long id) {		
		naturezaTributariaService.deleteById(id);
		return ResponseEntity.ok().body("Deletado com sucesso");					
	}
	
	@GetMapping("/gruposProduto/{IdNatureza}")
	public ResponseEntity<?> findGrupoProdutoByCodigoNatureza(
			@PathVariable(name="IdNatureza") Long IdNatureza,
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok().body(naturezaTributariaService.findGrupoProdutoByNaturezaTributaria(IdNatureza, pageable));	
	}
	
	@GetMapping("/estabelecimentos/{IdNatureza}")
	public ResponseEntity<?> findEstabelecimentosByCodigoNatureza(
			@PathVariable(name="IdNatureza") Long IdNatureza,
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok().body(naturezaTributariaService.findEstabelecimentosByNaturezaTributaria(IdNatureza, pageable));		
	}
	
	@GetMapping("/historico/{idNatureza}")
    public ResponseEntity<?> buscarNaturezaTributariaMovimentacao(
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
        @PathVariable(name="idNatureza") Long idNatureza){
  
        Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(idNatureza, PaginaEnum.NATUREZA_TRIBURARIA, pageable);
        return ResponseEntity.ok().body(historicoGenericoPage);  
        
    }
}

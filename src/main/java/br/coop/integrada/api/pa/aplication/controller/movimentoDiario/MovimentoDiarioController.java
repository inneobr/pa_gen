package br.coop.integrada.api.pa.aplication.controller.movimentoDiario;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioFiltro;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioRequest;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioResponse;
import br.coop.integrada.api.pa.domain.modelDto.movimentoDiario.MovimentoDiarioDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoDiario.MovimentoDiarioIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoDiario.MovimentoDiarioResponseDto;
import br.coop.integrada.api.pa.domain.service.movimentoDiario.MovimentoDiarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/movimento-diario")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Movimento diário", description = "Endpoints de movimentos diários.")
public class MovimentoDiarioController {
	
	@Autowired
	private MovimentoDiarioService movimentoDiarioService;	
	
	@GetMapping()
	public ResponseEntity<?> findByCodEstabelecimento(@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable,  MovimentoDiarioFiltro filter){
		Page<MovimentoDiario> movimento = movimentoDiarioService.findByEstabelecimentoAndDataMovimento(pageable, filter);	
		return ResponseEntity.ok(movimento);
	}
	
	
	@GetMapping("/do-id-unico/{IdUnico}")
	public ResponseEntity<MovimentoDiarioDto> buscarPorIdUnico(@PathVariable(name="idUnico") String idUnico) throws Exception{
		try{
			MovimentoDiario movimento = movimentoDiarioService.findByIdUnico(idUnico);
			
			if(movimento != null) {
				MovimentoDiarioDto movimentoDto = MovimentoDiarioDto.construir(movimento);				
				return ResponseEntity.ok().body(movimentoDto);
			}else {
				return new ResponseEntity<>(
						MovimentoDiarioDto.construir("Não foi possível identificar uma movimentação diária com o IdUnico informado"), 
	            		BAD_REQUEST);	
			}
		}catch(Exception e) {
			return new ResponseEntity<>(
					MovimentoDiarioDto.construir("Error:"+ e.getMessage()), 
            		BAD_REQUEST);		
		}
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<MovimentoDiarioResponseDto>> integrationSaveAll(@RequestBody MovimentoDiarioIntegrationDto objDto){
		List<MovimentoDiarioResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(MovimentoDiarioDto movimentoDiarioDto : objDto.getMovimentosDiarios()) {
			try {
				
				MovimentoDiario movimentoDiario = movimentoDiarioService.cadastrarOuAtualizar(movimentoDiarioDto);
				
				MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
						movimentoDiario, 
						true, 
						"Movimento diário salvo com sucesso!");
				
				
				response.add(movimentoDiarioResponseDto);
				sucessoIntegracao = true;

			}
			catch(ConstraintViolationException e) {
				MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
						movimentoDiarioDto,
						false,
						"Não foi possível salvar o movimento diário. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(movimentoDiarioResponseDto);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
							movimentoDiarioDto,
							false,
							"Não foi possível salvar o movimento diário. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(movimentoDiarioResponseDto);
				}
				else {
					MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
							movimentoDiarioDto,
							false,
							"Não foi possível salvar o movimento diário!",
							e
					);
					response.add(movimentoDiarioResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível salvar o tipo.";

				if(e.getMessage().toUpperCase().contains("KEY_ID_UNICO_MOVIMENTO_DIARIO")) {
					mensagem += " Obs.: Já existe movimento cadastrado com o mesmo idUnico informado.";
				}

				MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
						movimentoDiarioDto,
						false,
						mensagem,
						e
				);
				response.add(movimentoDiarioResponseDto);
			}
			catch (Exception e) {
				MovimentoDiarioResponseDto movimentoDiarioResponseDto = MovimentoDiarioResponseDto.construir(
						movimentoDiarioDto,
						false,
						"Não foi possível salvar o movimento diário!",
						e
				);
				response.add(movimentoDiarioResponseDto);
			}
		}

		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/validar-movimento")
	public ResponseEntity<MovimentoDiarioResponse> movimentoDiarioValid(@RequestBody MovimentoDiarioRequest request) {
		MovimentoDiarioResponse response = movimentoDiarioService.movimentoDiarioValid(request);
		
		if(response.isStatus()) {
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.badRequest().body(response);
	}

}

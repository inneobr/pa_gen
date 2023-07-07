package br.coop.integrada.api.pa.aplication.controller.movimentoRe;

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

import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoReFiltro;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReResponseDto;
import br.coop.integrada.api.pa.domain.service.movimentoRe.MovimentoReService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/movimento-re")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Movimento RE", description = "Endpoints de movimentos RE.")
public class MovimentoReController {
	
	@Autowired
	private MovimentoReService movimentoReService;	
	
	@GetMapping()
	public ResponseEntity<?> findByEstabelecimentoAndDataMovimento(
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable,  MovimentoReFiltro filter){
		Page<MovimentoRe> movimento = movimentoReService.findByEstabelecimentoAndDataMovimento(pageable, filter);	
		return ResponseEntity.ok(movimento);
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<MovimentoReResponseDto>> integrationSaveAll(@RequestBody MovimentoReIntegrationDto objDto){
		List<MovimentoReResponseDto> response = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(MovimentoReDto movimentoReDto : objDto.getMovimentosRe()) {
			try {
				
				MovimentoRe movimentoRe = movimentoReService.cadastrarOuAtualizar(movimentoReDto);
				
				MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
						movimentoRe, 
						true, 
						"Movimento RE salvo com sucesso!");
				
				
				response.add(movimentoReResponseDto);

			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
						movimentoReDto,
						false,
						"Não foi possível salvar o movimento RE, favor analisar a lista de validações dos campos!",
						e
				);
				response.add(movimentoReResponseDto);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
							movimentoReDto,
							false,
							"Não foi possível salvar o movimento pesagem. Favor analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(movimentoReResponseDto);
				}
				else {
					MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
							movimentoReDto,
							false,
							"Não foi possível salvar o movimento RE!",
							e
					);
					response.add(movimentoReResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar o movimnto.";

				if(e.getMessage().toUpperCase().contains("KEY_MOVIMENTO_RE")) {
					mensagem += " Obs.: Já existe movimento cadastrado com o mesmo (codEstabel + idRe + idMovRe) informado.";
				}

				MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
						movimentoReDto,
						false,
						mensagem,
						e
				);
				response.add(movimentoReResponseDto);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				MovimentoReResponseDto movimentoReResponseDto = MovimentoReResponseDto.construir(
						movimentoReDto,
						false,
						"Não foi possível salvar o movimento RE!",
						e
				);
				response.add(movimentoReResponseDto);
			}
		}

		if(falhaIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/criar-movimento-re")
	public ResponseEntity<MovimentoRe> criarMovimentoRe(@RequestBody MovimentoReDto objDto){
		
		MovimentoRe movimentoRe = movimentoReService.criarMovimentoRe(objDto);
		return ResponseEntity.ok(movimentoRe);
	}
	
	@GetMapping("/{nrRe}")
	public ResponseEntity<Page<MovimentoRe>> buscarMovimentoRe(
			@PathVariable(name="nrRe") Long nrRe,
			@PageableDefault(page = 0, size = 10, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<MovimentoRe> movimentos = movimentoReService.findByNrRe(nrRe, pageable);
		
		return ResponseEntity.ok(movimentos);
	}
	
}

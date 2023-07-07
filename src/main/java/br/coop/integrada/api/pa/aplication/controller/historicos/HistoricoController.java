package br.coop.integrada.api.pa.aplication.controller.historicos;

import java.text.ParseException;

import br.coop.integrada.api.pa.domain.model.Historico;
import br.coop.integrada.api.pa.domain.service.pesagem.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import javax.validation.ConstraintViolationException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/historico")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Historico", description = "Manter histórico de pesagem.")
public class HistoricoController {
	
	@Autowired
	private HistoricoService historicoService;
	
	@GetMapping
	public ResponseEntity<?> buscarTodos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(historicoService.buscarTodos(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe histórico de pesagem.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/{dataInicio}/{dataFim}")
	public ResponseEntity<?> buscarPorData(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
			@PathVariable(name="dataInicio") String dataInicio, 
			@PathVariable(name="dataFim") String dataFim) throws ParseException{
		try{
			return ResponseEntity.ok().body(historicoService.buscarPorData(dataInicio, dataFim, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe histórico de pesagem para esse período.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}

	@GetMapping("/{codEstabelecimento}/{safra}/{nroDocPesagem}")
	public ResponseEntity<?> buscarPorSafraENrDocPesagem(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			@PathVariable(name="codEstabelecimento") String codEstabelecimento,
			@PathVariable(name="safra") Integer safra,
			@PathVariable(name="nroDocPesagem") Integer nroDocPesagem
	) throws ParseException{
		try{
			Page<Historico> historico = historicoService.buscarPorCodEstabSafraENrDocPesagem(codEstabelecimento, safra, nroDocPesagem, pageable);
			return ResponseEntity.ok().body(historico);
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe histórico de pesagem com os parâmetros informados.", e),
					HttpStatus.BAD_REQUEST);
		}
	}
}

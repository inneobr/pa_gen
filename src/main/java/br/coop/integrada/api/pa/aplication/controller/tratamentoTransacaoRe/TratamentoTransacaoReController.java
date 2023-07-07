package br.coop.integrada.api.pa.aplication.controller.tratamentoTransacaoRe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoReMov;
import br.coop.integrada.api.pa.domain.service.tratamentoTransacaoRe.TratamentoTransacaoReService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/tratamento-transacao-re")
@Tag(name = "Tratamento de Transações de RE", description ="Endpoints Tratamento Transações RE")
public class TratamentoTransacaoReController {
	
	@Autowired
	private TratamentoTransacaoReService service;
	
	@PostMapping
	public ResponseEntity<?> registraTransacao(@RequestBody TransacaoReMov transacaoReMov) {
		
		try {
			
			service.salvarTratamentoTransacaoRe(
				transacaoReMov.getCodEstabel(), 
				transacaoReMov.getIdRe(),
				transacaoReMov.getNrRe(),
				transacaoReMov.getDataHoraMovimento(),
				transacaoReMov.getMovimento(),
				transacaoReMov.getStatusMovimento(),
				transacaoReMov.getMensagem());
		
			return ResponseEntity.ok("Ok");
			
		}
		catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}
	}
		
	@GetMapping
	public ResponseEntity<?> buscarTransacoesRe(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, 
			String filter)
	{
		try{
			Page<TransacaoRe> transacaoRe = service.buscarTransacoes(pageable, filter);
			return ResponseEntity.ok().body(transacaoRe); 			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Nenhuma transação de RE foi encontrada.");
		}
	}
	
	@GetMapping("/movimentos/{idRe}")
	public ResponseEntity<?> buscarTransacoesMovimentosRe(
			@PathVariable(name = "idRe") Long idRe,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
	{
		try{
			Page<TransacaoReMov> transacaoReMov = service.buscarTransacoesMovimento(idRe, pageable);
			return ResponseEntity.ok().body(transacaoReMov); 			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Nenhuma transação de movimento de RE foi encontrada.");
		}
	}
	
	@PostMapping("reenviar")
	public ResponseEntity<String> reenviar(@RequestParam(name="id") Long id){
		try {
			service.reenviar(id);
			return ResponseEntity.ok().body("Movimento Reenviado");
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	
}

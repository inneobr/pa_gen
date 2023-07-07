package br.coop.integrada.api.pa.aplication.controller.baixaCredito;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
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

import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoDtoList;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoRequestDto;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoResponseDto;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/baixa-credito")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Baixa crédito", description = "Endpoints de baixa de créditos.")
public class BaixaCreditoController {
	@Autowired
	private BaixaCreditoService baixaCreditoService;
	
	@PostMapping()
	public ResponseEntity<?> save(@RequestBody BaixaCredito objRequest){
		try{
			baixaCreditoService.salvalOuAtualizar(objRequest);
			return ResponseEntity.ok("Cadastrado com sucesso.");
		}catch (Exception e) {
			return new ResponseEntity<>("Error:"+ e.getMessage(), 
            		BAD_REQUEST);
		}		
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<BaixaCreditoResponseDto>> saveAll(@RequestBody BaixaCreditoDtoList baixaCreditoDtoList){
		List<BaixaCreditoResponseDto> response = new ArrayList<BaixaCreditoResponseDto>();

		for(BaixaCreditoRequestDto item:  baixaCreditoDtoList.getBaixaCredito()) {
			try{
				BaixaCredito baixaCredito = new BaixaCredito();
				BeanUtils.copyProperties(item, baixaCredito);
				baixaCreditoService.salvalOuAtualizar(baixaCredito);
				response.add(BaixaCreditoResponseDto.construir(baixaCredito, true, "Salvo com sucesso!"));
			}
	        catch(ConstraintViolationException e) {
	        	response.add(BaixaCreditoResponseDto.construir(item, false, "Falha ao salvar. Por favor, analisar a lista de validações dos campos!", e));
			}
			catch(DataIntegrityViolationException e) {
				response.add(BaixaCreditoResponseDto.construir(item, false, "Falha ao salvar!", e));
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					response.add(BaixaCreditoResponseDto.construir(item, false, "Falha ao salvar. Por favor, analisar a lista de validações dos campos!", (ConstraintViolationException) rootCause));
				}
				else {
					response.add(BaixaCreditoResponseDto.construir(item, false, "Falha ao salvar", e));
				}
			}
			catch (Exception e) {
				response.add(BaixaCreditoResponseDto.construir(item, false, "Falha ao salvar!", e));
			}
		}
		
		return ResponseEntity.ok(response);
	}	
	
	@GetMapping("codigo-estabelecimento-nr-re={codEstab}/{nrRe}")
	public ResponseEntity<BaixaCredito> findByCodEmitenteAndNrRe(
			@PathVariable(name="codEstab") String codEstab,
			@PathVariable(name="nrRe") Long nrRe){
		BaixaCredito baixaCredito = baixaCreditoService.findByCodEstabAndNrRe(codEstab, nrRe);	
		return ResponseEntity.ok(baixaCredito);
	}
	
	@GetMapping("codigo-emitente={codEmitente}")
	public ResponseEntity<Page<BaixaCredito>> findByCodEmitente(
			@PathVariable(name="codEmitente") Integer codEmitente,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<BaixaCredito> baixaCredito = baixaCreditoService.findByCodEmitente(codEmitente, pageable);	
		return ResponseEntity.ok(baixaCredito);
	}
	
	@GetMapping("/numero-re={nrRe}")
	public ResponseEntity<Page<BaixaCredito>> findByNrRe(
			@PathVariable(name="nrRe") Integer nrRe,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<BaixaCredito> baixaCredito = baixaCreditoService.findByNrRe(nrRe, pageable);	
		return ResponseEntity.ok(baixaCredito);
	}
}

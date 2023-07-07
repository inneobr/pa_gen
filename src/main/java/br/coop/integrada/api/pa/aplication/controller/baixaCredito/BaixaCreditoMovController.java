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

import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovDtoList;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovRequestDto;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovResponseDto;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoMovService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/baixa-credito-movimento")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Baixa crédito movimento", description = "Endpoints de baixa de crédito movimento.")
public class BaixaCreditoMovController {
	@Autowired
	private BaixaCreditoMovService baixaCreditoMovService;
	
	@PostMapping()
	public ResponseEntity<?> save(@RequestBody BaixaCreditoMov objRequest){
		try{
			baixaCreditoMovService.save(objRequest);
			return ResponseEntity.ok("Cadastrado com sucesso.");
		}catch (Exception e) {
			return new ResponseEntity<>("Error:"+ e.getMessage(), 
            		BAD_REQUEST);
		}		
	}
	
	@PostMapping("movimento-baixa-credito")
	public ResponseEntity<?> movimentoBaixaCredito(@RequestBody BaixaCreditoMov objRequest){
		try{
			baixaCreditoMovService.movimentoBaixaCredito(objRequest);
			return ResponseEntity.ok("Mpvimento de baixa de credito gerado com sucesso.");
		}catch (Exception e) {
			return new ResponseEntity<>("Error:"+ e.getMessage(), 
            		BAD_REQUEST);
		}		
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<?> saveAll(@RequestBody BaixaCreditoMovDtoList baixaCreditoMovDtoList){
		List<BaixaCreditoMovResponseDto> response = new ArrayList<BaixaCreditoMovResponseDto>();
		
		for(BaixaCreditoMovRequestDto item : baixaCreditoMovDtoList.getBaixaCreditoMov()) {
			try{
				BaixaCreditoMov baixaCreditoMov = new BaixaCreditoMov();
				BeanUtils.copyProperties(item, baixaCreditoMov);
				baixaCreditoMov = baixaCreditoMovService.save(baixaCreditoMov);
				response.add(BaixaCreditoMovResponseDto.construir(baixaCreditoMov, true, "Salvo com sucesso!"));
			}
	        catch(ConstraintViolationException e) {
	        	response.add(BaixaCreditoMovResponseDto.construir(item, false, "Falha ao salvar. Por favor, analisar a lista de validações dos campos!", e));
			}
			catch(DataIntegrityViolationException e) {
				response.add(BaixaCreditoMovResponseDto.construir(item, false, "Falha ao salvar!", e));
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					response.add(BaixaCreditoMovResponseDto.construir(item, false, "Falha ao salvar. Por favor, analisar a lista de validações dos campos!", (ConstraintViolationException) rootCause));
				}
				else {
					response.add(BaixaCreditoMovResponseDto.construir(item, false, "Falha ao salvar", e));
				}
			}
			catch (Exception e) {
				response.add(BaixaCreditoMovResponseDto.construir(item, false, "Falha ao salvar!", e));
			}
		}
		
		return ResponseEntity.ok(response);
	}	
	
	@GetMapping("codigo-estabelecimento-nr-re={codEstab}/{nrRe}")
	public ResponseEntity<BaixaCreditoMov> findByCodEmitenteAndNrRe(
			@PathVariable(name="codEstab") String codEstab,
			@PathVariable(name="nrRe") Long nrRe){
		BaixaCreditoMov baixaCredito = baixaCreditoMovService.findByCodEstabAndNrRe(codEstab, nrRe);	
		return ResponseEntity.ok(baixaCredito);
	}
	
	@GetMapping("codigo-emitente={codUsuario}")
	public ResponseEntity<Page<BaixaCreditoMov>> findByCodEmitente(
			@PathVariable(name="codUsuario") String codUsuario,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<BaixaCreditoMov> baixaCredito = baixaCreditoMovService.findByCodUsuario(codUsuario, pageable);	
		return ResponseEntity.ok(baixaCredito);
	}
	
	@GetMapping("/numero-re={nrRe}")
	public ResponseEntity<Page<BaixaCreditoMov>> findByNrRe(
			@PathVariable(name="nrRe") Integer nrRe,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<BaixaCreditoMov> baixaCredito = baixaCreditoMovService.findByNrRe(nrRe, pageable);	
		return ResponseEntity.ok(baixaCredito);
	}
}

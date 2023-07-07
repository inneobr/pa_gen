package br.coop.integrada.api.pa.aplication.controller.recEntrega;

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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaFilter;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaResponseDto;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/recentrega")
@Tag(name = "Rec-Entrega")
public class RecEntregaController {
	
	@Autowired
    private RecEntregaService recEntregaService;
		
	@PostMapping
    public ResponseEntity<RecEntrega> salvar(@RequestBody @Valid RecEntregaDto recEntrega){
        
		RecEntrega recEntregaOutput = recEntregaService.salvar(recEntrega);
        
        return ResponseEntity.ok(recEntregaOutput);
    }
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<RecEntregaResponseDto>> integrationSaveAll(@RequestBody RecEntregaIntegrationDto objDto){
		List<RecEntregaResponseDto> response = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(RecEntregaDto recEntregaDto : objDto.getRecEntrega() ) {
			try {
				
				RecEntrega recEntrega = recEntregaService.salvar(recEntregaDto);
				
				RecEntregaResponseDto recEntregaResponseDto = RecEntregaResponseDto.construir(
						recEntrega, 
						true, 
						"Rec Entrega salvo com sucesso!");
								
				response.add(recEntregaResponseDto);

			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				RecEntregaResponseDto recEntregaResponseDto = RecEntregaResponseDto.construir(
						recEntregaDto,
						false,
						"Não foi possível salvar o Rec Entrega. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(recEntregaResponseDto);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					RecEntregaResponseDto recEntregaResponseDto = RecEntregaResponseDto.construir(
							recEntregaDto,
							false,
							"Não foi possível salvar o Rec Entrega. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(recEntregaResponseDto);
				}
				else {
					RecEntregaResponseDto recEntregaResponseDto = RecEntregaResponseDto.construir(
							recEntregaDto,
							false,
							"Não foi possível salvar o Rec Entrega!",
							e
					);
					response.add(recEntregaResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar o Rec Entrega.";

				if(e.getMessage().toUpperCase().contains("KEY_ID_UNICO_RECENTRADA")) {
					mensagem += " Obs.: Já existe um RecEntrada cadastrado com o mesmo idUnico informado.";
				}

				RecEntregaResponseDto recEntregaResponseDto = RecEntregaResponseDto.construir(
						recEntregaDto,
						false,
						mensagem,
						e
				);
				response.add(recEntregaResponseDto);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				RecEntregaResponseDto recEntradaResponseDto = RecEntregaResponseDto.construir(
						recEntregaDto,
						false,
						"Não foi possível salvar o Rec Entrega!",
						e
				);
				response.add(recEntradaResponseDto);
			}
		}

		if(falhaIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> buscarRomaneios(@PageableDefault(page = 0, size = 10, sort = "nrRe", direction = Sort.Direction.DESC) Pageable pageable, RecEntregaFilter filter){
		try{
			Page<RecEntrega> romaneios = recEntregaService.buscarRomaneios(pageable, filter);
			return ResponseEntity.ok().body(romaneios); 			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Nenhum romaneios de entrega encontrado.");
		}
	}
	

    @GetMapping("/{id}")
    public ResponseEntity<RecEntregaDto> findById(@PathVariable(name="id") Long id){
    	RecEntrega recEntrega = recEntregaService.findById(id);
    	RecEntregaDto response = RecEntregaDto.construir(recEntrega);
        return ResponseEntity.ok(response);	
    }
	
	
}

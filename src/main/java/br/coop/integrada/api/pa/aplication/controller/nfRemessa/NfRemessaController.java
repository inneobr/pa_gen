package br.coop.integrada.api.pa.aplication.controller.nfRemessa;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessaFiltro;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.IndicadorNfRemessaDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaChaveAcessoDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaDadosFiscaisDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaResponseDto;
import br.coop.integrada.api.pa.domain.service.nfRemessa.NfRemessaService;
import br.coop.integrada.api.pa.domain.service.rependente.EntradaReService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/nf-remessa")
@Tag(name = "nf-remessa")
public class NfRemessaController {
	
	@Autowired
    private NfRemessaService nfRemessaService;
	
	@Autowired
    private EntradaReService entradaReService;
		
	@PostMapping
    public ResponseEntity<NfRemessa> salvar(@RequestBody @Valid NfRemessa nfRemessa){
        
		NfRemessa nfRemessaOutput = nfRemessaService.salvar(nfRemessa);
        
        return ResponseEntity.ok(nfRemessaOutput);
    }
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<NfRemessaResponseDto>> integrationSaveAll(@RequestBody NfRemessaIntegrationDto objDto){
		List<NfRemessaResponseDto> response = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(NfRemessa nfRemessaDto : objDto.getNfRemessa() ) {
			try {
				
				NfRemessa nfRemessa = nfRemessaService.salvar(nfRemessaDto);
				
				NfRemessaResponseDto nfRemessaResponseDto = NfRemessaResponseDto.construir(
						nfRemessa, 
						true, 
						"NfRemessa salva com sucesso!");
								
				response.add(nfRemessaResponseDto);

			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				NfRemessaResponseDto nfRemessaResponseDto = NfRemessaResponseDto.construir(
						nfRemessaDto,
						false,
						"Não foi possível salvar o a NFRemessa. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(nfRemessaResponseDto);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					NfRemessaResponseDto nfRemessaResponseDto = NfRemessaResponseDto.construir(
							nfRemessaDto,
							false,
							"Não foi possível salvar a NFRemessa. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(nfRemessaResponseDto);
				}
				else {
					NfRemessaResponseDto nfRemessaResponseDto = NfRemessaResponseDto.construir(
							nfRemessaDto,
							false,
							"Não foi possível salvar a NFRemessa!",
							e
					);
					response.add(nfRemessaResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar o tipo.";

				if(e.getMessage().toUpperCase().contains("KEY_ID_UNICO_RECENTRADA")) {
					mensagem += " Obs.: Já existe uma NFRemessa cadastrada com o mesmo idUnico informado.";
				}

				NfRemessaResponseDto nfRemessaResponseDto = NfRemessaResponseDto.construir(
						nfRemessaDto,
						false,
						mensagem,
						e
				);
				response.add(nfRemessaResponseDto);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				NfRemessaResponseDto recEntradaResponseDto = NfRemessaResponseDto.construir(
						nfRemessaDto,
						false,
						"Não foi possível salvar a NFRemessa!",
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
	
	@GetMapping("/buscar-notas-status")
	public ResponseEntity<List<NfRemessa>>buscarNotasPorStatus(
			@RequestParam(name = "status", defaultValue = "") StatusNotaFiscalEnum status){
		
		List<NfRemessa> listaNotas = this.nfRemessaService.findByStatus(status);
		
		return ResponseEntity.ok(listaNotas);
		
	}
	
	@PostMapping("/gerar-nf-remessa")
    public ResponseEntity<String> gerarNfRemessa(@RequestBody @Valid NfRemessa nfRemessa){
		try {
			nfRemessaService.gerarNfRemessa(
					nfRemessa.getCodEstabel(),
					nfRemessa.getIdRecEntrega(),
					nfRemessa.getNrRe(),
					nfRemessa.getQuantidade()
					);
	        return ResponseEntity.ok("Nota Fiscal de Remessa gerada com sucesso!");
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
		}
	}
	
	@GetMapping()
	public ResponseEntity<Page<NfRemessa>> buscarPorPaginacao(@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable,  NfRemessaFiltro filter){
		Page<NfRemessa> notas = nfRemessaService.buscarPorPaginacao(pageable, filter);	
		return ResponseEntity.ok(notas);
	}
	
	@GetMapping("/buscar-indicadores")
	public ResponseEntity<IndicadorNfRemessaDto> buscarIndicadores(NfRemessaFiltro filter){
		return ResponseEntity.ok(nfRemessaService.buscarIndicadores(filter));
	}
	
	@PutMapping("/atualizar-dados-fiscais/id-nf-remessa/{idNfRemessa}")
	public ResponseEntity<NfRemessa> atualizarDadosFiscais(
			@PathVariable(name = "idNfRemessa") Long idNfRemessa,
			@RequestBody NfRemessaDadosFiscaisDto objDto){
		NfRemessa nfRemessa = nfRemessaService.atualizarDadosFiscais(idNfRemessa, objDto);
		entradaReService.solicitarNfErp(nfRemessa.getId());
		return ResponseEntity.ok(nfRemessa);
	}
	
	@GetMapping("/buscar-dados-chave-acesso/por-id-rec-entraga/{idRecEntrega}")
	public ResponseEntity<NfRemessaChaveAcessoDto> buscarDadosChaveAcessoPorIdRecEntrega(
			@PathVariable(name = "idRecEntrega") Long idRecEntrega){
		NfRemessaChaveAcessoDto response = nfRemessaService.buscarDadosChaveAcessoPorIdRecEntrega(idRecEntrega);
		return ResponseEntity.ok(response);
	}
}

package br.coop.integrada.api.pa.aplication.controller.pesagem;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;
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
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.Historico;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.historico.HistoricoDto;
import br.coop.integrada.api.pa.domain.modelDto.historico.HistoricoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.CriarMovimentoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemFilter;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemIntegrationResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPostDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPutDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemResponseDto;
import br.coop.integrada.api.pa.domain.service.pesagem.HistoricoService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/pesagem")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Pesagem", description = "Manter pesagem.")
public class PesagemController {
	
	@Autowired
	private RecEntregaService recEntregaService;
	
	@Autowired
	private PesagemService pesagemService;

	@Autowired
	private HistoricoService historicoService;
		
    @PostMapping	
	public ResponseEntity<PesagemResponseDto> cadastrar(@Valid @RequestBody PesagemPostDto pesagemPostDto){
        try {
            Pesagem pesagem = pesagemService.cadastrar(pesagemPostDto);

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(pesagem, true, "Pesagem cadastrada com sucesso!");
            return ResponseEntity.ok(pesagemResponseDto);
        }
		catch(ConstraintViolationException e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPostDto,
					false,
					"Não foi possível salvar a pesagem. Por favor, analisar a lista de validações dos campos!",
					e
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
						pesagemPostDto,
						false,
						"Não foi possível salvar a pesagem. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				return ResponseEntity.badRequest().body(pesagemResponseDto);
			}
			else {
				PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
						pesagemPostDto,
						false,
						"Não foi possível salvar a pesagem!",
						e.getMessage()
				);
				return ResponseEntity.badRequest().body(pesagemResponseDto);
			}
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel salvar a pesagem.";

			Boolean integrated = false;
			if(e.getMessage().toUpperCase().contains("KEY_UN_PESAGEM")) {
				menssageError += " Obs.: Esta pesagem já foi integrada anteriormente no sistema.";
				integrated = true;
			}

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPostDto,
					integrated,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
        }
		catch(DataIntegrityViolationException e) {
            String menssageError = "Não foi possivel salvar a pesagem.";

            Boolean integrated = false;
			if(e.getMessage().toUpperCase().contains("KEY_UN_PESAGEM")) {
                menssageError += " Obs.: Esta pesagem já foi integrada anteriormente no sistema.";
                integrated = true;
            }

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPostDto,
					integrated,
					menssageError,
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
        }
		catch(OptimisticLockException e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPostDto,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
        }
		catch (Exception e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPostDto,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
        }
	}

	@PutMapping("/atualizar")
	public ResponseEntity<PesagemResponseDto> atualizar(@Valid @RequestBody PesagemPutDto pesagemPutDto){
		try {
			Pesagem pesagem = Pesagem.construir(pesagemPutDto);
			pesagem = pesagemService.atualizar(pesagem);

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(pesagem, true, "Pesagem atualizada com sucesso!");
			return ResponseEntity.ok(pesagemResponseDto);
		}
		catch(ConstraintViolationException e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPutDto,
					false,
					"Não foi possível salvar a pesagem. Por favor, analisar a lista de validações dos campos!",
					e
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			PesagemResponseDto pesagemResponseDto = null;

			if (rootCause instanceof ConstraintViolationException) {
				pesagemResponseDto = PesagemResponseDto.construir(
						pesagemPutDto,
						false,
						"Não foi possível salvar a pesagem. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				pesagemResponseDto = PesagemResponseDto.construir(
						pesagemPutDto,
						false,
						"Não foi possível salvar a pesagem!",
						e.getMessage()
				);
			}

			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel salvar a pesagem.";

			Boolean integrated = false;
			if(e.getMessage().toUpperCase().contains("KEY_UN_PESAGEM")) {
				menssageError += " Obs.: Esta pesagem já foi integrada anteriormente no sistema.";
				integrated = true;
			}

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPutDto,
					integrated,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
		catch(DataIntegrityViolationException e) {
			String menssageError = "Não foi possivel salvar a pesagem.";

			Boolean integrated = false;
			if(e.getMessage().toUpperCase().contains("KEY_UN_PESAGEM")) {
				menssageError += " Obs.: Esta pesagem já foi integrada anteriormente no sistema.";
				integrated = true;
			}

			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPutDto,
					integrated,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
		catch(OptimisticLockException e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPutDto,
					false,
					"Erro de integração",
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
		catch (Exception e) {
			PesagemResponseDto pesagemResponseDto = PesagemResponseDto.construir(
					pesagemPutDto,
					false,
					"Erro de integração",
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(pesagemResponseDto);
		}
	}
        
    @PostMapping("/integration/save-all")
    public ResponseEntity<PesagemIntegrationResponseDto> salvarListaPesagem(@RequestBody PesagemIntegrationSaveDto objDto){
		List<PesagemResponseDto> pesagemResponseDtos = new ArrayList<>();
		List<HistoricoResponseDto> historicoResponseDtos = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(PesagemPutDto pesagemPutDto: objDto.getPesagens()) {
			PesagemResponseDto pesagemResponseDto = null;

			try {
				if(pesagemPutDto.getId() == null) {
					PesagemPostDto pesagemPostDto = PesagemPostDto.construir(pesagemPutDto);
					pesagemResponseDto = cadastrar(pesagemPostDto).getBody();

				}
				else {
					pesagemResponseDto = atualizar(pesagemPutDto).getBody();
				}

				if(pesagemResponseDto != null && pesagemResponseDto.getIntegrated()) {
					sucessoIntegracao = true;
				}
			}
			catch (Exception e) {
				pesagemResponseDto = PesagemResponseDto.construir(
						pesagemPutDto,
						false,
						"Erro de integração",
						e.getMessage()
				);
			}

			pesagemResponseDtos.add(pesagemResponseDto);
		}

		for(HistoricoDto historicoDto : objDto.getHistoricos()) {
			HistoricoResponseDto historicoResponseDto = null;

			try {
				Historico historico = Historico.construir(historicoDto);
				historico = historicoService.salvarHistorico(historico);
				historicoResponseDto = HistoricoResponseDto.construir(
						historico,
						true,
						"Histórico de pesagem salvo com sucesso!"
				);
				sucessoIntegracao = true;
			}
			catch(ConstraintViolationException e) {
				historicoResponseDto = HistoricoResponseDto.construir(
						historicoDto,
						false,
						"Não foi possível salvar o histórico da pesagem. Por favor, analisar a lista de validações dos campos!",
						e
				);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					historicoResponseDto = HistoricoResponseDto.construir(
							historicoDto,
							false,
							"Não foi possível salvar o histórico da pesagem. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
				}
				else {
					historicoResponseDto = HistoricoResponseDto.construir(
							historicoDto,
							false,
							"Não foi possível salvar o histórico da pesagem!",
							e.getMessage()
					);
				}
			}
			catch(org.hibernate.exception.ConstraintViolationException e) {
				historicoResponseDto = HistoricoResponseDto.construir(
						historicoDto,
						false,
						"Não foi possivel salvar o histórico da pesagem!",
						e.getMessage()
				);
			}
			catch(DataIntegrityViolationException e) {
				String menssageError = "Não foi possivel salvar o histórico da pesagem.";

				Boolean integrated = false;
				if(e.getMessage().toUpperCase().contains("KEY_HISTORICO")) {
					menssageError += " Obs.: Esta histórico de pesagem já foi integrada anteriormente no sistema.";
					sucessoIntegracao = true;
					integrated =  true;
				}

				historicoResponseDto = HistoricoResponseDto.construir(
						historicoDto,
						integrated,
						menssageError,
						e.getMessage()
				);
			}
			catch (Exception e) {
				historicoResponseDto = HistoricoResponseDto.construir(
						historicoDto,
						false,
						"Não foi possivel salvar o histórico da pesagem!",
						e.getMessage()
				);
			}

			historicoResponseDtos.add(historicoResponseDto);
		}

		PesagemIntegrationResponseDto response = PesagemIntegrationResponseDto.construir(pesagemResponseDtos, historicoResponseDtos);

		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
    }
	
	@GetMapping("/buscar-pendentes")
	public ResponseEntity<?> buscarPesagensPendentes(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, PesagemFilter filter){
		try{
			Page<Pesagem> pesagens = pesagemService.buscarPesagensPendentes(pageable, filter);
			return ResponseEntity.ok().body(pesagens); 			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Não existem pesagens cadastrados.");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> buscarTodasPesagens(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, PesagemFilter filter){
		try{
			Page<Pesagem> pesagens = pesagemService.buscarTodasPesagens(pageable, filter);
			return ResponseEntity.ok().body(pesagens); 			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Não existem pesagens cadastrados.");
		}
	}
	
	@PutMapping("/desativar/{uid}")
	public ResponseEntity<?> desativarPesagem(@PathVariable(name="id") Long id){
		try{
			return ResponseEntity.ok().body(pesagemService.desativarPesagem(id));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Não foi possivel desativar o pesagem.");
		}
	}
	
	@GetMapping("/uid/{uid}")
	public ResponseEntity<?> buscarIdPesagem(@PathVariable(name="id") Long id){
		try{
			return ResponseEntity.ok().body(pesagemService.buscarIdPesagem(id));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Não foi possivel desativar o pesagem.");
		}
	}
	
	@GetMapping("/buscar-por/codigo-estabelecimento/{codigoEstabelecimento}/safra/{safra}/numero-documento-pesagem/{nroDocPesagem}")
	public ResponseEntity<Pesagem> buscarPorCodigoEstabelecimentoSafraDocPesagem(
			@PathVariable(name="safra") Integer safra,
			@PathVariable(name="nroDocPesagem") Integer nroDocPesagem,
			@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento){
		
		Pesagem pesagem = pesagemService.buscarPorCodigoEstabelecimentoSafraDocPesagem(codigoEstabelecimento, safra, nroDocPesagem);
		
		if(pesagem == null) {
			throw new ObjectNotFoundException("Não foi encontrado registro de pesagem com os parâmetros informados.");
		}
		
		return ResponseEntity.ok(pesagem);
	}
	
	@PostMapping("/criar-movimento-pesagem")
	public ResponseEntity<MovtoPesagem> criarMovimentoPesagem(@RequestBody CriarMovimentoPesagemDto input){
		RecEntrega recEntrega = recEntregaService.buscarRe(input.getCodEstabel(), input.getNrRe());
		MovtoPesagem movtoPesagem = pesagemService.criarMovimentoPesagem(input, recEntrega);		
		return ResponseEntity.ok(movtoPesagem);
	}
	
}

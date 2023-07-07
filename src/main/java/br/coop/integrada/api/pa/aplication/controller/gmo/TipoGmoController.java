package br.coop.integrada.api.pa.aplication.controller.gmo;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.DefaultResponse;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteTipoGmo;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoFilter;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoResponseDto;
import br.coop.integrada.api.pa.domain.service.produto.TipoGmoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/tipogmo")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Tipo Gmo", description = "Endpoints tipo gmo.")
public class TipoGmoController {
	
	@Autowired
	private TipoGmoService tipoGmoService;	

	@PostMapping
	public ResponseEntity<?> salvarListaTipoGmo(@Valid @RequestBody List<TipoGmo> tipoGmo){
		try{
			tipoGmoService.salvarListTipoGmo(tipoGmo);
			return ResponseEntity.ok().body("Tipo Gmo salvo com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel salvar o tipoGmo.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PostMapping("/cadastrar")
    public ResponseEntity<TipoGmoResponseDto> cadastrar(@Valid @RequestBody TipoGmo tipoGmo){
		try {
			TipoGmo tipoGmoRetorno = tipoGmoService.cadastrar(tipoGmo);
			
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(tipoGmoRetorno, true, "Tipo Gmo cadastrado com sucesso!");
			return ResponseEntity.ok(tipoGmoResponseDto);
		}
		catch(ConstraintViolationException e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(tipoGmo, false, "Não foi possível salvar o tipo GMO. Por favor, analisar a lista de validações dos campos!", e);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
						tipoGmo,
						false,
						"Não foi possível salvar o tipo GMO. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				return ResponseEntity.badRequest().body(tipoGmoResponseDto);
			}
			else {
				TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
						tipoGmo,
						false,
						"Não foi possível salvar o tipo GMO!",
						e.getMessage()
				);
				return ResponseEntity.badRequest().body(tipoGmoResponseDto);
			}
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel salvar o tipo GMO.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO_UNICO")) {
				menssageError += " Obs.: Este tipo GMO já foi integrado anteriormente no sistema.";
			}

			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch(DataIntegrityViolationException e) {
            String menssageError = "Não foi possivel salvar o tipo GMO.";
            menssageError += " Obs.: Este tipo GMO já foi integrado anteriormente no sistema.";
            
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					menssageError,
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch(OptimisticLockException e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch (Exception e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
	}
	
	@SuppressWarnings("null")
    @PostMapping("/atualizar")
	public ResponseEntity<TipoGmoResponseDto> atualizar(@Valid @RequestBody TipoGmo tipoGmo){
		try {
			TipoGmo tipoGmoRetorno = tipoGmoService.atualizar(tipoGmo);
			
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(tipoGmoRetorno, true, "Tipo Gmo atualizado com sucesso!");
			return ResponseEntity.ok(tipoGmoResponseDto);
		}
		catch(ConstraintViolationException e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(tipoGmo, false, "Não foi possível atualizar o tipo GMO. Por favor, analisar a lista de validações dos campos!", e);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
						tipoGmo,
						false,
						"Não foi possível atualizar o tipo GMO. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				return ResponseEntity.badRequest().body(tipoGmoResponseDto);
			}
			else {
				TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
						tipoGmo,
						false,
						"Não foi possível atualizar o tipo GMO!",
						e.getMessage()
				);
				return ResponseEntity.badRequest().body(tipoGmoResponseDto);
			}
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel atualizar o tipo GMO.";

			if(e.getMessage().toUpperCase().contains("KEY_UN_TIPOGMO")) {
				menssageError += " Obs.: Este tipo GMO já foi integrado anteriormente no sistema.";
			}

			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch(DataIntegrityViolationException e) {
            String menssageError = "Não foi possivel atualizar o tipo GMO.";

			if(e.getMessage().toUpperCase().contains("KEY_UN_TIPOGMO")) {
                menssageError += " Obs.: Este tipo GMO já foi integrado anteriormente no sistema.";
            }

			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					menssageError,
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch(OptimisticLockException e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		catch (Exception e) {
			TipoGmoResponseDto tipoGmoResponseDto = TipoGmoResponseDto.construir(
					tipoGmo,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(tipoGmoResponseDto);
        }
		
	}
	
    @PostMapping("/integration/save-all")
    public ResponseEntity<List<TipoGmoResponseDto>> integrationSaveAll(@RequestBody TipoGmoIntegrationSaveDto objDto){
		List<TipoGmoResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(TipoGmoDto tipoGmoDto: objDto.getTipoGmo()) {
			TipoGmoResponseDto responseDto = null;
			
			TipoGmo tipoGmo = TipoGmo.contruir(tipoGmoDto);
			tipoGmo.setDataIntegracao(new Date());
			tipoGmo.setDataCadastro(new Date());
			tipoGmo.setStatusIntegracao(StatusIntegracao.INTEGRADO);
			
			try {
				
				if(!Strings.isEmpty(tipoGmo.getIdUnico())) {
					TipoGmo tipoGmoBD = tipoGmoService.findByIdUnico(tipoGmo.getIdUnico());
										
					if(tipoGmoBD == null) {	
					    responseDto = cadastrar(tipoGmo).getBody();
					    response.add(responseDto);
					    sucessoIntegracao = true;
					}
					else {						

						if(tipoGmoDto.getOperacao() != null && tipoGmoDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
							try {
								tipoGmoService.remove(tipoGmoBD);
								responseDto = TipoGmoResponseDto.construir(tipoGmo, true, "Tipo Gmo excluído com sucesso!");
							    response.add(responseDto);
							} catch (Exception e) {
								
								if(tipoGmoBD.getDataInativacao() == null) {
									tipoGmo.setDataInativacao(new Date());
								}
								responseDto = atualizar(tipoGmo).getBody();
							    response.add(responseDto);
							}
							
						}else {
							tipoGmo.setId(tipoGmoBD.getId());
							responseDto = atualizar(tipoGmo).getBody();
						    response.add(responseDto);
						}	
						sucessoIntegracao = true;
					}
					
				}else {
					response.add(TipoGmoResponseDto.construir(
							tipoGmo,
							false,
							"Erro de integração",
		                    "Campo idUnico obrigatório."
					));
				}
				
			}
			catch (Exception e) {
			    responseDto = TipoGmoResponseDto.construir(
						tipoGmo,
						false,
						"Erro de integração",
						e.getMessage()
				);
			    response.add(responseDto);
			}
						
		}
		
		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
    }
	
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<?> ativarTipoGmo(@PathVariable(name="id") Long id){
		try{
			tipoGmoService.ativarTipoGmo(id);
			return ResponseEntity.ok().body("Tipo Gmo ativado com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar o tipo Gmo.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarTipoGmo(@PathVariable(name="id") Long id){
		try{
			tipoGmoService.inativarTipoGmo(id);
			return ResponseEntity.ok().body("Tipo gmo ativado com sucesso!");
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultResponse.construir(
							HttpStatus.BAD_REQUEST.value(),	"Não foi possível inativar o tipo gmo!", e.getMessage()
					),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
	@GetMapping("/ativo")
	public ResponseEntity<?> buscarTodosTipoGmoAtivo(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(tipoGmoService.listarTipoGmoAtivos(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo Gmo salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping
	public ResponseEntity<?> buscarTodosTipoGmo(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			TipoGmoFilter filter,
            Situacao situacao){
		try{
			return ResponseEntity.ok(tipoGmoService.buscarTodosGmo(pageable, filter, situacao));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo Gmo salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}	
	
	@GetMapping("/tipoGmo/{tipoGmo}")
	public ResponseEntity<?> buscarTipoGmoPorNome(@PathVariable(name="tipoGmo") String tipoGmo){
		try{
			return ResponseEntity.ok().body(tipoGmoService.buscaTipoGmoPorNome(tipoGmo));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo Gmo salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}	
	
	@GetMapping("/buscar-por/codigo-grupo-produto/{codigoGrupoProduto}")
	public ResponseEntity<List<TipoGmo>> buscarPorCodigoGrupoProduto(
			@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto,
			@RequestParam(name = "filtro", defaultValue = "", required = false) String filtro,
			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao,
			@PageableDefault(page = 0, size = 10, /*sort = "tipoGmo",*/ direction = Sort.Direction.ASC) Pageable pageable
			){
		Page<TipoGmo> tipoGmoPage = tipoGmoService.buscarPorCodigoGrupoProduto(codigoGrupoProduto, filtro, situacao, pageable);
		return ResponseEntity.ok(tipoGmoPage.getContent());
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> buscarTipoGmoPorId(@PathVariable(name="id") Long id){
		TipoGmo tipoGmo = tipoGmoService.buscarTipoGmoPorId(id);
		if(tipoGmo != null) {
			return ResponseEntity.ok().body(tipoGmo);
		}
		return new ResponseEntity<>(
				DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo de produto salvos."), 
        		HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping
	public ResponseEntity<?> atualizarTipoGmo(@Valid @RequestBody TipoGmoDto tipoGmoDto){
		TipoGmo tipoGmo = tipoGmoService.buscarTipoGmoPorId(tipoGmoDto.getId());
		BeanUtils.copyProperties(tipoGmoDto, tipoGmo);
		try {
			tipoGmoService.atualizarTipoGmoAtivos(tipoGmoDto);
			return ResponseEntity.accepted().body(
					DefaultResponse.construir(
							HttpStatus.ACCEPTED.value(),
							"Tipo Gmo atualizado com sucesso!"
					)
			);
		}catch(Exception e) {
			return new ResponseEntity<>(DefaultResponse.construir(HttpStatus.BAD_REQUEST.value(),"Não foi possivel atualizar o tipo!",e.getMessage()),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
	@GetMapping("/cobra-teste/{tipoGmoId}")
	public ResponseEntity<?> getCobraKit(@PathVariable(name="tipoGmoId") Long tipoGmoId){
		try{			
			return ResponseEntity.ok().body(tipoGmoService.getCobrancaKit(tipoGmoId));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Tipo Gmo não localizado.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}

	
	@GetMapping("/buscar-por/tipo-gmo/{tipoGmo}/codigo-grupo-produto/{codigoGrupoProduto}")
	public ResponseEntity<TipoGmo> buscarPorTipoGmoECodigoGrupoProduto(
			@PathVariable(name="tipoGmo") String tipoGmo,
			@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto,
			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao
			){
		TipoGmo tipoGmoObj = tipoGmoService.buscarPorTipoGmoECodigoGrupoProduto(tipoGmo, codigoGrupoProduto, situacao)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado o Tipo GMO: " + tipoGmo));
		return ResponseEntity.ok(tipoGmoObj);
	}

	
	@GetMapping("/pesquisar-por/tipo-gmo")
	public ResponseEntity<List<TipoGmoDto>> pesquisarPorTipoGmo(
			@RequestParam(name = "tipo-gmo", defaultValue = "", required = false) String tipoGmo,
			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao,
			@PageableDefault(page = 0, size = 10, /*sort = "tipoGmo",*/ direction = Sort.Direction.ASC) Pageable pageable
			){
		Page<TipoGmo> tipoGmoPage = tipoGmoService.pesquisarPorTipoGmo(tipoGmo, situacao, pageable);
		List<TipoGmoDto> tiposGmoDto = TipoGmoDto.construir(tipoGmoPage.getContent());
		return ResponseEntity.ok(tiposGmoDto);
	}
}

package br.coop.integrada.api.pa.aplication.controller.recEntrega;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
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
import br.coop.integrada.api.pa.aplication.utils.DefaultResponse;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.SituacaoReDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.SituacaoReIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.SituacaoReResponseDto;
import br.coop.integrada.api.pa.domain.service.recEntrega.SituacaoReService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/situacao-re")
@Tag(name = "Situação RE", description = "Endpoints situação re.")
public class SituacaoReController {
	
	@Autowired
	private SituacaoReService situacaoReService;	

	@PostMapping
	public ResponseEntity<?> salvarListaSituacaoRe(@Valid @RequestBody List<SituacaoRe> situacaoRe){
		try{
			situacaoReService.salvarListSituacaoRe(situacaoRe);
			return ResponseEntity.ok().body("Situação Re salva com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel salvar o situacaoRe.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	
	@PostMapping("/cadastrar")
    public ResponseEntity<SituacaoReResponseDto> cadastrar(@Valid @RequestBody SituacaoRe situacaoRe){
		try {
			SituacaoRe situacaoReRetorno = situacaoReService.cadastrar(situacaoRe);
			
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(situacaoReRetorno, true, "Situação Re cadastrada com sucesso!");
			return ResponseEntity.ok(situacaoReResponseDto);
		}
		catch(ConstraintViolationException e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(situacaoRe, false, "Não foi possível salvar a Situação Re. Por favor, analisar a lista de validações dos campos!", e);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
						situacaoRe,
						false,
						"Não foi possível salvar a Situação Re. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				return ResponseEntity.badRequest().body(situacaoReResponseDto);
			}
			else {
				SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
						situacaoRe,
						false,
						"Não foi possível salvar a Situação Re!",
						e.getMessage()
				);
				return ResponseEntity.badRequest().body(situacaoReResponseDto);
			}
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel salvar a Situação Re.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				menssageError += " Obs.: Esta Situação Re já foi integrado anteriormente no sistema.";
			}

			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch(DataIntegrityViolationException e) {
            String menssageError = "Não foi possivel salvar a Situação Re.";
            menssageError += " Obs.: Esta Situação Re já foi integrada anteriormente no sistema.";
            
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					menssageError,
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch(OptimisticLockException e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch (Exception e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
	}
	
	@SuppressWarnings("null")
    @PostMapping("/atualizar")
	public ResponseEntity<SituacaoReResponseDto> atualizar(@Valid @RequestBody SituacaoRe situacaoRe){
		try {
			SituacaoRe situacaoReRetorno = situacaoReService.atualizar(situacaoRe);
			
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(situacaoReRetorno, true, "Situação RE atualizada com sucesso!");
			return ResponseEntity.ok(situacaoReResponseDto);
		}
		catch(ConstraintViolationException e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(situacaoRe, false, "Não foi possível atualizar a Situação RE. Por favor, analisar a lista de validações dos campos!", e);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
						situacaoRe,
						false,
						"Não foi possível atualizar a Situação Re. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
				return ResponseEntity.badRequest().body(situacaoReResponseDto);
			}
			else {
				SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
						situacaoRe,
						false,
						"Não foi possível atualizar a Situação Re!",
						e.getMessage()
				);
				return ResponseEntity.badRequest().body(situacaoReResponseDto);
			}
		}
		catch(org.hibernate.exception.ConstraintViolationException e) {
			String menssageError = "Não foi possivel atualizar a Situação Re.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				menssageError += " Obs.: Esta Situação Re já foi integrado anteriormente no sistema.";
			}

			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					menssageError,
					e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch(DataIntegrityViolationException e) {
            String menssageError = "Não foi possivel atualizar a Situação Re.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
                menssageError += " Obs.: Esta Situação Re já foi integrado anteriormente no sistema.";
            }

			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					menssageError,
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch(OptimisticLockException e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		catch (Exception e) {
			SituacaoReResponseDto situacaoReResponseDto = SituacaoReResponseDto.construir(
					situacaoRe,
					false,
					"Erro de integração",
                    e.getMessage()
			);
			return ResponseEntity.badRequest().body(situacaoReResponseDto);
        }
		
	}
	
    @PostMapping("/integration/save-all")
    public ResponseEntity<List<SituacaoReResponseDto>> integrationSaveAll(@RequestBody SituacaoReIntegrationSaveDto objDto){
		List<SituacaoReResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(SituacaoReDto situacaoReDto: objDto.getSituacao() ) {
			
			try {		
				//Se Deleção
				if(situacaoReDto.getOperacao() != null && situacaoReDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
					
					SituacaoRe situacaoReBD = situacaoReService.buscarSituacaoRePorCodigo( situacaoReDto.getCodigo() );
						
					try {
													
						if(situacaoReBD != null) {
							situacaoReService.remove(situacaoReBD);
								
							response.add(SituacaoReResponseDto.construir(situacaoReBD, true, "Situacao Re excluída com sucesso!"));
							sucessoIntegracao = true;
						}
						else {
								
							response.add(SituacaoReResponseDto.construir(situacaoReBD, true, "Situacao Re excluída em outra transação!"));
							sucessoIntegracao = true;
						}
					} catch (Exception e) {		
						if(situacaoReBD != null) {	
							situacaoReBD.setDataInativacao(new Date());					
							situacaoReService.save(situacaoReBD);
							response.add(SituacaoReResponseDto.construir(situacaoReBD, true, "Situacao Re contém relacionamentos e portanto foi inativada!"));
						}
						else {
							response.add(SituacaoReResponseDto.construir(situacaoReBD, false, "Favor analizar mensagem de erro:", e.getMessage()));
						}
						
					}
					
				}
				else {
					
					SituacaoRe situacaoReBD = situacaoReService.buscarSituacaoRePorCodigo( situacaoReDto.getCodigo() );
					
					if(situacaoReBD == null) {	
					    //Se Inclusão
						SituacaoRe situacaoRe = SituacaoRe.contruir(situacaoReDto);
						situacaoRe.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						situacaoRe.setDataIntegracao(new Date());
						
					    response.add(cadastrar(situacaoRe).getBody());
					    sucessoIntegracao = true;
					}
					else {
						//Se Alteração
						
						SituacaoRe situacaoRe = SituacaoRe.contruir(situacaoReDto);
						situacaoRe.setId(situacaoReBD.getId());
						
						situacaoRe.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						situacaoRe.setDataIntegracao(new Date());
						
					    response.add(atualizar(situacaoRe).getBody());
					    sucessoIntegracao = true;
					}
				}	
			}
			catch (Exception e) {
				response.add( 
						SituacaoReResponseDto.construir(
						situacaoReDto,
						false,
						"Erro de integração",
						e.getMessage()
				));
			    
			}
		}//Fim do foreach
		
		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
		
    }
	
	
	/*@PutMapping("/ativar/{id}")
	public ResponseEntity<?> ativarSituacaoRe(@PathVariable(name="id") Long id){
		try{
			situacaoReService.ativarSituacaoRe(id);
			return ResponseEntity.ok().body("Situação RE ativada com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar a Situação RE.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}*/
	
	/*@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarSituacaoRe(@PathVariable(name="id") Long id){
		try{
			situacaoReService.inativarSituacaoRe(id);
			return ResponseEntity.ok().body("Situação RE ativada com sucesso!");
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultResponse.construir(
							HttpStatus.BAD_REQUEST.value(),	"Não foi possível inativar a Situação RE!", e.getMessage()
					),
					HttpStatus.BAD_REQUEST
			);
		}
	}*/
	
	/*@GetMapping("/ativo")
	public ResponseEntity<?> buscarTodosSituacaoReAtivo(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(situacaoReService.listarSituacaoReAtivos(pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem Situações RE salvas.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}*/
	
	@GetMapping("/buscar-com-paginacao")
    public ResponseEntity<Page<SituacaoReDto>> buscarComPaginacao(
            @PageableDefault(page = 0, size = 10, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "descricao", defaultValue = "") String descricao,
            @RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao
    ) {
        Page<SituacaoReDto> situacaoReDtoPage = situacaoReService.buscarComPaginacao(pageable, descricao, situacao);
        return ResponseEntity.ok(situacaoReDtoPage);
    }
	
	/*@GetMapping
	public ResponseEntity<?> buscarTodosSituacaoRe(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			SituacaoReFilter filter,
            Situacao situacao){
		try{
			return ResponseEntity.ok(situacaoReService.buscarTodosGmo(pageable, filter, situacao));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo Gmo salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}*/	
	
	/*@GetMapping("/situacaoRe/{situacaoRe}")
	public ResponseEntity<?> buscarSituacaoRePorNome(@PathVariable(name="situacaoRe") String situacaoRe){
		try{
			return ResponseEntity.ok().body(situacaoReService.buscaSituacaoRePorNome(situacaoRe));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo Gmo salvos.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}*/	
	
//	@GetMapping("/buscar-por/codigo-grupo-produto/{codigoGrupoProduto}")
//	public ResponseEntity<List<SituacaoRe>> buscarPorCodigoGrupoProduto(
//			@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto,
//			@RequestParam(name = "filtro", defaultValue = "", required = false) String filtro,
//			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao,
//			@PageableDefault(page = 0, size = 10, /*sort = "situacaoRe",*/ direction = Sort.Direction.ASC) Pageable pageable
//			){
//		Page<SituacaoRe> situacaoRePage = situacaoReService.buscarPorCodigoGrupoProduto(codigoGrupoProduto, filtro, situacao, pageable);
//		return ResponseEntity.ok(situacaoRePage.getContent());
//	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> buscarSituacaoRePorId(@PathVariable(name="id") Long id){
		SituacaoRe situacaoRe = situacaoReService.buscarSituacaoRePorId(id);
		if(situacaoRe != null) {
			return ResponseEntity.ok().body(situacaoRe);
		}
		return new ResponseEntity<>(
				DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem tipo de produto salvos."), 
        		HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/codigo/{codigo}")
	public ResponseEntity<?> buscarSituacaoRePorCodigo(@PathVariable(name="codigo") Long codigo){
		SituacaoRe situacaoRe = situacaoReService.buscarSituacaoRePorCodigo(codigo);
		if(situacaoRe != null) {
			return ResponseEntity.ok().body(situacaoRe);
		}
		return new ResponseEntity<>(
				DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem Situação Re cadastrada com o código informado."), 
        		HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping
	public ResponseEntity<?> atualizarSituacaoRe(@Valid @RequestBody SituacaoReDto situacaoReDto){
		SituacaoRe situacaoRe = situacaoReService.buscarSituacaoRePorId(situacaoReDto.getId());
		BeanUtils.copyProperties(situacaoReDto, situacaoRe);
		try {
			situacaoReService.atualizarSituacaoReAtivos(situacaoReDto);
			return ResponseEntity.accepted().body(
					DefaultResponse.construir(
							HttpStatus.ACCEPTED.value(),
							"Situação RE atualizada com sucesso!"
					)
			);
		}catch(Exception e) {
			return new ResponseEntity<>(DefaultResponse.construir(HttpStatus.BAD_REQUEST.value(),"Não foi possivel atualizar o tipo!",e.getMessage()),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
//	@GetMapping("/cobra-teste/{situacaoReId}")
//	public ResponseEntity<?> getCobraKit(@PathVariable(name="situacaoReId") Long situacaoReId){
//		try{			
//			return ResponseEntity.ok().body(situacaoReService.getCobrancaKit(situacaoReId));
//		}catch(ConstraintViolationException e) {
//			return new ResponseEntity<>(
//					DefaultExceptions.construir(BAD_REQUEST.value(), "Tipo Gmo não localizado.", e), 
//            		HttpStatus.BAD_REQUEST);		
//		}
//	}

	
//	@GetMapping("/buscar-por/tipo-gmo/{situacaoRe}/codigo-grupo-produto/{codigoGrupoProduto}")
//	public ResponseEntity<SituacaoRe> buscarPorSituacaoReECodigoGrupoProduto(
//			@PathVariable(name="situacaoRe") String situacaoRe,
//			@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto,
//			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao
//			){
//		SituacaoRe situacaoReObj = situacaoReService.buscarPorSituacaoReECodigoGrupoProduto(situacaoRe, codigoGrupoProduto, situacao)
//				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado o Tipo GMO: " + situacaoRe));
//		return ResponseEntity.ok(situacaoReObj);
//	}

	
//	@GetMapping("/pesquisar-por/tipo-gmo")
//	public ResponseEntity<List<SituacaoReDto>> pesquisarPorSituacaoRe(
//			@RequestParam(name = "tipo-gmo", defaultValue = "", required = false) String situacaoRe,
//			@RequestParam(name = "situacao", defaultValue = "ATIVO", required = false) Situacao situacao,
//			@PageableDefault(page = 0, size = 10, /*sort = "situacaoRe",*/ direction = Sort.Direction.ASC) Pageable pageable
//			){
//		Page<SituacaoRe> situacaoRePage = situacaoReService.pesquisarPorSituacaoRe(situacaoRe, situacao, pageable);
//		List<SituacaoReDto> tiposGmoDto = SituacaoReDto.construir(situacaoRePage.getContent());
//		return ResponseEntity.ok(tiposGmoDto);
//	}
		
}

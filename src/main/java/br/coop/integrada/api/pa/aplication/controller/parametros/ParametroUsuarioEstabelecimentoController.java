package br.coop.integrada.api.pa.aplication.controller.parametros;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.ParametrosUsuarioEstabelecimentoFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroUsuarioEstabelecimentoFiltro;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.UsuarioEstabelecimentoFuncaoDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.parametros.ParametroUsuarioEstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/parametro/estabelecimentos")
@Tag(name = "Parametro Usuario Estabelecimento", description = "Permissões dos Usuarios nos Estabelecimento.")
public class ParametroUsuarioEstabelecimentoController {
	

	@Autowired
	private ParametroUsuarioEstabelecimentoService parametroService;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
    private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
    private IntegrationService integrationService;
	
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody ParametrosUsuarioEstabelecimentoDto objDto) throws Exception{	
		try {
			parametroService.salvar(objDto);
			return ResponseEntity.ok().body("Permissao de Usuario x Estabelecimento, salva com sucesso");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel salvar a Permissao Usuario x Estabelecimento.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	@PutMapping
	public ResponseEntity<?> atualizar(@RequestBody ParametrosUsuarioEstabelecimento parametros) throws Exception{	
		try {
			parametroService.atualizar(parametros);
			return ResponseEntity.ok().body("Permissao de Usuario x Estabelecimento, atualizada com sucesso");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel atualizar a Permissao Usuario x Estabelecimento.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	

	
	@GetMapping("/parametros/{estabelecimentoCod}/{usuarioCod}/{funcao}")
	public UsuarioEstabelecimentoFuncaoDto findParametroByUsuarioEstabelecimentoFuncao(
			@PathVariable(name="estabelecimentoCod") String estabelecimentoCod, 
			@PathVariable(name="usuarioCod") String usuarioCod, 
			@PathVariable(name="funcao") FuncaoEstabelecimento funcao) {
		return parametroService.findParametroByUsuarioEstabelecimentoFuncao(estabelecimentoCod, usuarioCod, funcao);
	}
	

	
	@GetMapping("/todos/{idUsuario}")
	public ResponseEntity<?> buscarTodos(@PathVariable(name="idUsuario")Long idUsuario , @PageableDefault(page = 0, size = 10, sort = "dataCadastro", direction = Sort.Direction.ASC) Pageable pageable){	
		try {
			return ResponseEntity.ok().body(parametroService.findAll(idUsuario, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe Permissao Usuario x Estabelecimento.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	@GetMapping("/{id}/historico")
    public ResponseEntity<?> buscarHistorico(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name="id") Long id){
		
		Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(id, PaginaEnum.PARAMETRO_ESTABELECIMENTO, pageable);	     
        return ResponseEntity.ok().body(historicoGenericoPage);
    }

	@PostMapping("/integration/save-all")
	public ResponseEntity<List<ParametrosUsuarioEstabelecimentoResponseDto>> integrationSaveAll(@RequestBody ParametrosUsuarioEstabelecimentoIntegrationDto objDto){
		List<ParametrosUsuarioEstabelecimentoResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;
		

		IntegracaoPagina pagina = null; 
		try {
			pagina = integrationService.buscarPorPagina(PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO);
		}catch(Exception e) { }

		for(ParametrosUsuarioEstabelecimentoIntegrationSaveDto parametrosUsuarioEstabelecimentoDto : objDto.getParametrosUsuarios()) {
			ParametrosUsuarioEstabelecimentoResponseDto responseDto = null;

			try {
				
				if(parametrosUsuarioEstabelecimentoDto.getOperacao().equals(IntegrationOperacaoEnum.WRITE)) {
					ParametrosUsuarioEstabelecimento parametro = parametroService.cadastrarOuAtualizar(parametrosUsuarioEstabelecimentoDto, pagina);
					responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
							parametro.getCodigoEstabelecimento(),
							parametro.getCodigoUsuario(),
							true,
							"Integração realizada com sucesso!",
							parametrosUsuarioEstabelecimentoDto.getOperacao()
					);
					responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
					response.add(responseDto);
					sucessoIntegracao = true;
					
				}else {
					
					ParametrosUsuarioEstabelecimento parametrosUsuarioEstabelecimento = parametroService.findByCodigoUsuarioAndCodigoEstabelecimento(
						parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
						parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento()							
					);
					
					if(parametrosUsuarioEstabelecimento != null) {
						try {
							parametroService.remove(parametrosUsuarioEstabelecimento);
							responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
									parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
									parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
									true,
									"Parâmetro excluído com sucesso!",
									parametrosUsuarioEstabelecimentoDto.getOperacao());
							
							responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
						    response.add(responseDto);
						    sucessoIntegracao = true;
							
						} catch (Exception e) {
							parametrosUsuarioEstabelecimento.setDataIntegracao(new Date());
							parametrosUsuarioEstabelecimento.setStatusIntegracao(StatusIntegracao.INTEGRADO);
							parametrosUsuarioEstabelecimento = parametroService.atualizarDireto(parametrosUsuarioEstabelecimento);
							
							responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
									parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
									parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
									true,
									"Parâmetro inativado com sucesso!",
									parametrosUsuarioEstabelecimentoDto.getOperacao());
							
							responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
	            			response.add(responseDto);
						    sucessoIntegracao = true;
						}
						
					}else{
						
						responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
								parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
								parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
								true,
								"O parâmetro já foi excluído anteriormente.",
								parametrosUsuarioEstabelecimentoDto.getOperacao());

						responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
						response.add(responseDto);
					    sucessoIntegracao = true;
					}
					
				}
			}
			catch(ConstraintViolationException e) {
				responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
						parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
						parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
						true,
						"Não foi possível salvar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
						e,
						parametrosUsuarioEstabelecimentoDto.getOperacao()
				);

				responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
				response.add(responseDto);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
							parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
							parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
							false,
							"Não foi possível salvar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause,
							parametrosUsuarioEstabelecimentoDto.getOperacao()
					);
					responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
					response.add(responseDto);
				}
				else {
					responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
							parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
							parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
							false,
							"Não foi possível salvar o parâmetro de estabelecimento!",
							e,
							parametrosUsuarioEstabelecimentoDto.getOperacao()
					);
					responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
					response.add(responseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível salvar o parâmetro de estabelecimento.";

				if(e.getMessage().toUpperCase().contains("KEY_USUARIO_X_ESTABELECIMENTO")) {
					mensagem += " Obs.: Já existe parâmetros para o USUARIO_X_ESTABELECIMENTO informado.";
				}

				responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
						parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
						parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
						false,
						mensagem,
						e,
						parametrosUsuarioEstabelecimentoDto.getOperacao()
				);
				responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
				response.add(responseDto);
			}
			catch (Exception e) {
				responseDto = ParametrosUsuarioEstabelecimentoResponseDto.construir(
						parametrosUsuarioEstabelecimentoDto.getCodigoEstabelecimento(),
						parametrosUsuarioEstabelecimentoDto.getCodigoUsuario(),
						false,
						"Não foi possível salvar o parâmetro de estabelecimento!",
						e,
						parametrosUsuarioEstabelecimentoDto.getOperacao()
				);
				responseDto.setIdUnico(parametrosUsuarioEstabelecimentoDto.getIdUnico());
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

	@GetMapping("/{idUsuario}/estabelecimentos-disponiveis")
	public ResponseEntity<List<EstabelecimentoSimplesDto>> estabelecimentosDisponivelParaUsuario(@PathVariable(name="idUsuario") Long idUsuario){
	    List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarEstabelecimentosDisponiveisSilodoUsuario(idUsuario);
	    List<EstabelecimentoSimplesDto> estabelecimentoSimplesDtos = EstabelecimentoSimplesDto.construir(estabelecimentos);
	    return ResponseEntity.ok(estabelecimentoSimplesDtos);
	}
	
	@GetMapping("/buscar-por/usuario-autenticado")
	public ResponseEntity<Page<ParametrosUsuarioEstabelecimento>> buscarPorUsuarioAutenticado(
			ParametroUsuarioEstabelecimentoFiltro filtro,
			@RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
	    Page<ParametrosUsuarioEstabelecimento> estabelecimentoPage = parametroService.buscarPorUsuarioAutenticado(filtro, situacao, pageable);
	    return ResponseEntity.ok(estabelecimentoPage);
	}
	
	@GetMapping("/buscar-por/usuario-autenticado/codigo-estabelecimento/{codigoEstabelecimento}")
	public ResponseEntity<ParametrosUsuarioEstabelecimento> buscarPorUsuarioAutenticado(
			@PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento){
	    ParametrosUsuarioEstabelecimento response = parametroService.buscarPorUsuarioAutenticadoECodigoEstabelecimento(codigoEstabelecimento);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/filtar")
	public ResponseEntity<?> buscarTodos(			 
			@PageableDefault(page = 0, size = 10, sort = "estabelecimento.codigo", direction = Sort.Direction.ASC) Pageable pageable,
			ParametrosUsuarioEstabelecimentoFilter filter){		
		System.out.println(pageable.getSort());
			Page<ParametrosUsuarioEstabelecimentoSimplesDto> parametrosResponse = parametroService.findParamByFilter(filter, pageable);
			return ResponseEntity.ok().body(parametrosResponse);
	}
	
	@DeleteMapping("/{idParametro}")
	public ResponseEntity<?> deletar(@PathVariable(name="idParametro")Long idParametro) throws Exception{	
		try {
			parametroService.deletar(idParametro);
			return ResponseEntity.ok().body("Parametro Usuario Estabelecimento deletado com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel deletar a Permissao Usuario x Estabelecimento.", e), 
            		HttpStatus.BAD_REQUEST);		
		}	
	}
	
	@DeleteMapping("/delete-all/{username}")
	public ResponseEntity<?> deletAll(@PathVariable(name="username") String username){
		parametroService.deletAll(username);
		return ResponseEntity.ok().body("Deletado todos os parâmetro do usuario: "+username);
			
	}
	
	@GetMapping("/copiar-parametros/{idOrigem}/{idDestino}")
	public ResponseEntity<?> copiarParametros(
			@PathVariable(name="idOrigem") Long idOrigem, 
			@PathVariable(name="idDestino") Long idDestino) throws Exception{
			return ResponseEntity.ok().body(parametroService.copiarParametros(idOrigem, idDestino));	
	}
}

package br.coop.integrada.api.pa.aplication.controller.estabelecimentos;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.DefaultResponse;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.*;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroUsuarioEstabelecimentoFiltro;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/estabelecimento")
@Tag(name = "Estabelecimento", description = "Manter estabelecimentos.")
public class EstabelecimentoController {
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
		
	@GetMapping
	public ResponseEntity<?> buscarTodosAtivos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			EstabelecimentoFilter filter,
            Situacao situacao){
			
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = estabelecimentoService.findAll(pageable, filter, situacao);
		return ResponseEntity.ok(estabelecimentoAllDtoPage);
	}
	
	@GetMapping("/buscaAntiga")
	public ResponseEntity<?> buscarTodos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = estabelecimentoService.buscarTodosAtivo(pageable);
		return ResponseEntity.ok(estabelecimentoAllDtoPage);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCodigoOuNome(@RequestParam(name = "codigoOuNome", defaultValue = "") String codigoOuNome){
		List<EstabelecimentoResumidoDto> estabelecimentoResumidoDtos = 
				estabelecimentoService.buscarEstabelecimentoPorCodigoOuNomeFantasia(codigoOuNome, "codigo");
		return ResponseEntity.ok(estabelecimentoResumidoDtos);
    }

	@GetMapping("/buscar-por/codigo-ou-nome/logado")
	public ResponseEntity<?> buscarPorCodigoOuNomeDoUsuarioLogado(@RequestParam(name = "codigoOuNome", defaultValue = "") String codigoOuNome){
		List<EstabelecimentoResumidoDto> estabelecimentoResumidoDtos =
				estabelecimentoService.listarEstResumidoDtoDoUsuarioLogado(codigoOuNome);
		return ResponseEntity.ok(estabelecimentoResumidoDtos);
	}
	
	@GetMapping("/buscar-por/codigo-ou-nome-fantasia/ativo")
    public ResponseEntity<List<EstabelecimentoDto>> pesquisarPorCodigoOuNomeFantasiaAtivo(
    		@RequestParam(name = "filtro", defaultValue = "") String codigoOuNomeFantasia,
    		@PageableDefault(page = 0, size = 15, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable
    		){
		Page<Estabelecimento> estabelecimentoPage = estabelecimentoService.pesquisarPorCodigoOuNomeFantasiaAtivo(codigoOuNomeFantasia, pageable);
		List<EstabelecimentoDto> estabelecimentoDtos = EstabelecimentoDto.construir(estabelecimentoPage.getContent());
		return ResponseEntity.ok(estabelecimentoDtos);
    }
	
	@GetMapping("/todos")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> findAll(){
		return ResponseEntity.ok(estabelecimentoService.findAll());
	}
	
	@GetMapping("/usuario/{codEstabelecimentos}")
	public ResponseEntity<?> estabelecimentosPorUsuario(
			@PathVariable(name = "codEstabelecimentos") String codEstabelecimentos,
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
	){
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = estabelecimentoService.estabelecimentosDoUsuario(codEstabelecimentos, pageable);
		return ResponseEntity.ok(estabelecimentoAllDtoPage);
	}
	
	@GetMapping("/usuario/estabelecimentoDoUsuario/{codEstabelecimentos}")
	public ResponseEntity<List<EstabelecimentoAllDto>> estabelecimentosPorUsuario(@PathVariable(name = "codEstabelecimentos") String codEstabelecimentos){
		List<EstabelecimentoAllDto> estabelecimentoAllDtos = estabelecimentoService.estabelecimentosDoUsuarioBusca(codEstabelecimentos);
		return ResponseEntity.ok(estabelecimentoAllDtos);
	}
	
	@GetMapping("/buscar-por/parametro-usuario-vs-estabelecimento/usuario-autenticado")
	public ResponseEntity<List<EstabelecimentoDto>> buscarPorParametroUsuarioVsEstabelecimentoComUsuarioAutenticado(
			ParametroUsuarioEstabelecimentoFiltro filtro,
			@RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 15, sort = "estabelecimento.codigo", direction = Sort.Direction.ASC) Pageable pageable
			){
		List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorParametroUsuarioVsEstabelecimentoComUsuarioAutenticado(filtro, situacao, pageable);
		List<EstabelecimentoDto> estabelecimentoDtos = EstabelecimentoDto.construir(estabelecimentos);
		return ResponseEntity.ok(estabelecimentoDtos);
	}
	
	@GetMapping("/buscar-por/usuario-autenticado/com-hierarquia-estabelecimento/que-seja-silo")
	public ResponseEntity<List<EstabelecimentoDto>> buscarPorUsuarioAutenticadoComHierarquiaEstabelecimentoQueSejaSilo(
			@RequestParam(name = "filtro", defaultValue = "") String filtro,
    		@PageableDefault(page = 0, size = 15, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable
			){
		Page<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorUsuarioAutenticadoComHierarquiaEstabelecimentoQueSejaSilo(filtro, pageable);
		List<EstabelecimentoDto> estabelecimentoDtos = EstabelecimentoDto.construir(estabelecimentos.getContent());
		return ResponseEntity.ok(estabelecimentoDtos);
	}
	
	@GetMapping("/buscar-por/usuario-autenticado-vs-estabelecimento/permissao-re/estabelecimento-tipo-silo")
	public ResponseEntity<List<EstabelecimentoDto>> buscarPorUsuarioAutenticadoVsEstabelecimentoComPermissaReEstabelecimentoTipoSilo(
			@RequestParam(name = "filtro", defaultValue = "") String filtro,
    		@PageableDefault(page = 0, size = 15, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable
			){
		Page<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorUsuarioAutenticadoVsEstabelecimentoComPermissaReEstabelecimentoTipoSilo(filtro, pageable);
		List<EstabelecimentoDto> estabelecimentoDtos = EstabelecimentoDto.construir(estabelecimentos.getContent());
		return ResponseEntity.ok(estabelecimentoDtos);
	}
	
	
	@GetMapping("/regional")
	public ResponseEntity<List<EstabelecimentoAllDto>> listarRegional(){
		return ResponseEntity.ok(estabelecimentoService.listarRegionais());
	}
	
	@GetMapping("/regional/buscar={nomeOucodigo}")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> getRegionalNameOuCodigo(@PathVariable(name="nomeOucodigo") String request){
		return ResponseEntity.ok(estabelecimentoService.getRegionalCodigoOuNome(request));
	}
	
	@GetMapping("/regional/{codigo}")
	public ResponseEntity<Page<EstabelecimentoAllDto>> listarRegionalPorCodigo(@PathVariable(name = "codigo") String codigo, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = estabelecimentoService.listarRegionaisPorCodigo(codigo, pageable);
		return ResponseEntity.ok(estabelecimentoAllDtoPage);
	}	

	@GetMapping("/{id}")
	public ResponseEntity<EstabelecimentoAllDto> find(@PathVariable(name = "id") Long id){
		try {
			EstabelecimentoAllDto estabelecimentoAllDto = estabelecimentoService.unico(id);
			return ResponseEntity.ok(estabelecimentoAllDto);
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Estabelecimento não encontrado.", e);
		}
	}

	@GetMapping("/codigo/{codigo}")
	public  ResponseEntity<EstabelecimentoAllDto> findByCodigo(@PathVariable(name = "codigo") String codigo){
		try {
			EstabelecimentoAllDto estabelecimentoAllDto = estabelecimentoService.findByCodigo(codigo);
			return ResponseEntity.ok(estabelecimentoAllDto);
		}
		catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Estabelecimento não encontrado.", e);
		}
	}

	@PostMapping()
	public ResponseEntity<?> cadastrarEstabelecimento(@RequestBody Estabelecimento estabelecimento){
		try {
			return ResponseEntity.ok(estabelecimentoService.cadastrar(estabelecimento));
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Estabelecimento ja cadastrado.", e);
		}
	}
	
	@PostMapping("/save-all")
	public ResponseEntity<List<EstabelecimentoResponseDto>> saveAll(@RequestBody List<EstabelecimentoDto> objDtos){
		List<EstabelecimentoResponseDto> response = new ArrayList<>();
		Boolean falhaIntegracao = false;

		for(EstabelecimentoDto estabelecimentoDto : objDtos) {
			try {
				Estabelecimento estabelecimento = estabelecimentoService.cadastrarOuAtualizar(estabelecimentoDto);

				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimento,
						true,
						"Estabelecimento salvo com sucesso!"
				);
				response.add(estabelecimentoResponseDto);
			}
			catch(ConstraintViolationException e) {
				falhaIntegracao = true;
				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(estabelecimentoResponseDto);
			}
			catch (TransactionSystemException e) {
				falhaIntegracao = true;
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
							estabelecimentoDto,
							false,
							"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(estabelecimentoResponseDto);
				}
				else {
					EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
							estabelecimentoDto,
							false,
							"Não foi possível salvar o estabelecimento!",
							e
					);
					response.add(estabelecimentoResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				falhaIntegracao = true;
				String mensagem = "Não foi possível salvar o estabelecimento.";

				if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
					mensagem += " Obs.: Já existe estabelecimento cadastrado com o código informado.";
				}

				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						mensagem,
						e
				);
				response.add(estabelecimentoResponseDto);
			}
			catch (Exception e) {
				falhaIntegracao = true;
				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento!",
						e
				);
				response.add(estabelecimentoResponseDto);
			}
		}

		if(falhaIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
	}
	
	@PutMapping
	public ResponseEntity<EstabelecimentoResponseDto> update(@RequestBody EstabelecimentoDto estabelecimentoDto){
		EstabelecimentoResponseDto response = null;

		try {
			Estabelecimento estabelecimento = estabelecimentoService.update(estabelecimentoDto);

			response = EstabelecimentoResponseDto.construir(
					estabelecimento,
					true,
					"Estabelecimento atualizado com sucesso!"
			);

			return ResponseEntity.ok(response);
		}
		catch(ConstraintViolationException e) {
			response = EstabelecimentoResponseDto.construir(
					estabelecimentoDto,
					false,
					"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				response = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				response = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento!",
						e
				);
			}
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível salvar o estabelecimento.";

			if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
				mensagem += " Obs.: Já existe estabelecimento cadastrado com o código informado.";
			}

			response = EstabelecimentoResponseDto.construir(
					estabelecimentoDto,
					false,
					mensagem,
					e
			);
		}
		catch (Exception e) {
			response = EstabelecimentoResponseDto.construir(
					estabelecimentoDto,
					false,
					"Não foi possível salvar o estabelecimento!",
					e
			);
		}

		return ResponseEntity.badRequest().body(response);
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<EstabelecimentoResponseDto>> integrationSaveAll(@RequestBody EstabelecimentoIntegrationDto objDto){
		List<EstabelecimentoResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for(EstabelecimentoDto estabelecimentoDto : objDto.getEstabelecimentos()) {
			try {
				
				Estabelecimento estabelecimento = estabelecimentoService.cadastrarOuAtualizarIntegration(estabelecimentoDto);

				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimento,
						true,
						"Estabelecimento salvo com sucesso!"
				);

				response.add(estabelecimentoResponseDto);
				sucessoIntegracao = true;
			}
			catch(ConstraintViolationException e) {
				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
						e
				);
				response.add(estabelecimentoResponseDto);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
							estabelecimentoDto,
							false,
							"Não foi possível salvar o estabelecimento. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
					response.add(estabelecimentoResponseDto);
				}
				else {
					EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
							estabelecimentoDto,
							false,
							"Não foi possível salvar o estabelecimento!",
							e
					);
					response.add(estabelecimentoResponseDto);
				}
			}
			catch(DataIntegrityViolationException e) {
				String mensagem = "Não foi possível salvar o estabelecimento.";

				if(e.getMessage().toUpperCase().contains("KEY_CODIGO")) {
					mensagem += " Obs.: Já existe estabelecimento cadastrado com o código informado.";
				}

				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						mensagem,
						e
				);
				response.add(estabelecimentoResponseDto);
			}
			catch (Exception e) {
				EstabelecimentoResponseDto estabelecimentoResponseDto = EstabelecimentoResponseDto.construir(
						estabelecimentoDto,
						false,
						"Não foi possível salvar o estabelecimento!",
						e
				);
				response.add(estabelecimentoResponseDto);
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
	public ResponseEntity<?> ativarEstabelecimento(@PathVariable(name="id") Long id){
		try{
			estabelecimentoService.ativarEstabelecimento(id);
			return ResponseEntity.ok().body("Estabelecimento ativado com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel ativar o estabelecimento.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarEstabelecimento(@PathVariable(name="id") Long id){
		try{
			estabelecimentoService.inativarEstabelecimento(id);
			return ResponseEntity.ok().body("Estabelecimento inativado com sucesso!");
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultResponse.construir(
							HttpStatus.BAD_REQUEST.value(),	"Não foi possível inativar o estabelecimento!", e.getMessage()
					),
					HttpStatus.BAD_REQUEST
			);
		}
	}
	
	@GetMapping("/validar-entrada-producao-por/codigo-estabelecimento/{codigoEstabelecimento}/entrada-manual/{entradaManual}")
	public  ResponseEntity<EstabelecimentoDto> validarEntradaProducaoPorCodigoEstabelecimentoEntradaManual(
			@PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento,
			@PathVariable(name = "entradaManual") Boolean entradaManual) {
		Estabelecimento estabelecimento = estabelecimentoService.validarEntradaProducaoPorUsuarioVsEstabelecimento(codigoEstabelecimento, entradaManual);
		EstabelecimentoDto response = EstabelecimentoDto.construir(estabelecimento);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/estabelecimentos-parametro-estabelecimento-natureza-silo")
	public ResponseEntity<List<EstabelecimentoAllDto>> buscarEstabelecimentoPorParametroEstabelecimentoAndNaturezaOperacaoAndSilo(){
		List<EstabelecimentoAllDto> estabelecimentoAllDtos = estabelecimentoService.buscarEstabelecimentoPorParametroEstabelecimentoAndNaturezaOperacaoAndSilo();
		return ResponseEntity.ok(estabelecimentoAllDtos);
	}
		
	
	@GetMapping("/estabelecimentos-parametro-estabelecimento-silo")
	public ResponseEntity<List<EstabelecimentoAllDto>> buscarEstabelecimentoPorParametroEstabelecimentoAndSilo(){
		List<EstabelecimentoAllDto> estabelecimentoAllDtos = estabelecimentoService.buscarEstabelecimentoPorParametroEstabelecimentoAndSilo();
		return ResponseEntity.ok(estabelecimentoAllDtos);
	}
	
	@GetMapping("/buscar-por/filtrar-estabelecimento-silo")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> filtrarEstabelecimentoSilo(
			@RequestParam(name = "filtro", defaultValue = "") String filtro){
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.filtrarEstabelecimentoSilo(filtro);
		return ResponseEntity.ok(estabelecimentos);
	}
	
	@GetMapping("/estabelecimento-silo-natureza-operacao")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> filtrarEstabelecimentoSiloNaturezaOperacao(){
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.findBySiloNotNaturezaOperacao();
		return ResponseEntity.ok(estabelecimentos);
	}
	
	@GetMapping("/natureza-tributaria/estabelecimento-silo")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> naturezaTributariaSilo(){
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.findByNaturezaTributariaSilo();
		return ResponseEntity.ok(estabelecimentos);
	}
	
	@GetMapping("/filtro-pesquisar")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> estabelecimentoUbsFilter(EstabelecimentoFilter filter){		
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.estabelecimentoUbsFilter(filter);
		return ResponseEntity.ok(estabelecimentos);
	}
	
	@GetMapping("/estabelecimento-silo-item-avariado")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> filtrarEstabelecimentoSiloItemAvariado(String tipoValidacao, Long idGrupo){
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.findBySiloNotItemAvariado(tipoValidacao, idGrupo);
		return ResponseEntity.ok(estabelecimentos);
	}
	
	@GetMapping("/silo-filtro-pesquisar")
	public ResponseEntity<List<EstabelecimentoNomeCodigoDto>> estabelecimentoSiloFilter(EstabelecimentoFilter filter){		
		List<EstabelecimentoNomeCodigoDto> estabelecimentos = estabelecimentoService.estabelecimentoSiloFilter(filter);
		return ResponseEntity.ok(estabelecimentos);
	}
}

package br.coop.integrada.api.pa.aplication.controller.produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoFilter;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoGetDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration.GrupoProdutoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration.GrupoProdutoIntegrationSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ValidarSubProdutoDto;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoGmoService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/produto/grupo-produto")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Grupo Produto", description = "Grupo de produtos.")
public class GrupoProdutoController {
	private static final Logger logger = LoggerFactory.getLogger( GrupoProdutoController.class);
	
	@Autowired
	private GrupoProdutoService grupoProdutoService;
	
	@Autowired
	private GrupoProdutoGmoService grupoProdutoGmoService;
		
	@PostMapping
	public ResponseEntity<GrupoProdutoResponseDto> salvarGrupoProduto(@Valid @RequestBody GrupoProdutoDto objDto){
		GrupoProdutoResponseDto responseDto = null;

		try{						
			GrupoProduto grupoProduto = grupoProdutoService.salvar(objDto);
			responseDto = GrupoProdutoResponseDto.construir(
					grupoProduto,
					true,
					"O grupo de produto foi gravado com sucesso!"
			);
			return ResponseEntity.ok(responseDto);
		}
		catch(ConstraintViolationException e) {
			responseDto = GrupoProdutoResponseDto.construir(
					objDto,
					false,
					"Não foi possível gravar o grupo de produto. Por favor, analisar a lista de validações dos campos!",
					e
			);
		}
		catch(DataIntegrityViolationException e) {
			String mensagem = "Não foi possível gravar o grupo de produto.";

			if(e.getMessage().toUpperCase().contains("KEY_FM_CODIGO")) {
				mensagem += " Obs.: Já existe grupo de produto cadastrado com o código informado.";
			}

			responseDto = GrupoProdutoResponseDto.construir(
					objDto,
					false,
					mensagem,
					e
			);
		}
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				responseDto = GrupoProdutoResponseDto.construir(
						objDto,
						false,
						"Não foi possível gravar o grupo de produto. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				responseDto = GrupoProdutoResponseDto.construir(
						objDto,
						false,
						"Não foi possível gravar o grupo de produto!",
						e
				);
			}
		}
		catch (Exception e) {
			responseDto = GrupoProdutoResponseDto.construir(
					objDto,
					false,
					e.getMessage()
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}

	@PostMapping("/integration/save-all")
	public ResponseEntity<List<GrupoProdutoResponseDto>> integrationSaveAll(@RequestBody GrupoProdutoIntegrationDto objDtos) {
		List<GrupoProdutoResponseDto> responseDtos = new ArrayList<>();
		Boolean sucessoIntegracao = false;

		for (GrupoProdutoIntegrationSimplesDto item : objDtos.getGrupoProdutos()) {
			GrupoProdutoResponseDto responseDto = null;

			try {
				
				if(item.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
					GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(item.getFmCodigo());
					if(grupoProduto != null) {
						try {
							grupoProdutoService.delete(grupoProduto);
							responseDto = GrupoProdutoResponseDto.construir(grupoProduto, true, "Grupo de produto excluído com sucesso!");					
							sucessoIntegracao = true;
							
						} catch (Exception e) {
							grupoProduto.setDataInativacao(new Date());
							grupoProduto.setStatusIntegracao(StatusIntegracao.INTEGRADO);
							grupoProdutoService.save(grupoProduto);
							
							responseDto = GrupoProdutoResponseDto.construir(grupoProduto, true, "Grupo de produto excluído com sucesso!");					
							sucessoIntegracao = true;
						}
					}else {
						responseDto = GrupoProdutoResponseDto.construir(grupoProduto, true, "Grupo de produto excluído com sucesso!");					
						sucessoIntegracao = true;
					}
					
				} else {				
					GrupoProduto grupoProduto = grupoProdutoService.integrationSave(item);
					responseDto = GrupoProdutoResponseDto.construir(grupoProduto, true, "Integração realizada com sucesso!");					
					sucessoIntegracao = true;
				}
			}
			catch (ConstraintViolationException e) {
				responseDto = GrupoProdutoResponseDto.construir(
						item,
						false,
						"Não foi possível gravar o grupo de produto. Por favor, analisar a lista de validações dos campos!",
						e
				);
			}
			catch (DataIntegrityViolationException e) {
				String mensagem = "Não foi possível gravar o grupo de produto.";

				if (e.getMessage().toUpperCase().contains("KEY_FM_CODIGO")) {
					mensagem += " Obs.: Já existe grupo de produto cadastrado com o código informado.";
				}

				responseDto = GrupoProdutoResponseDto.construir(
						item,
						false,
						mensagem,
						e
				);
			}
			catch (TransactionSystemException e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);

				if (rootCause instanceof ConstraintViolationException) {
					responseDto = GrupoProdutoResponseDto.construir(
							item,
							false,
							"Não foi possível gravar o grupo de produto. Por favor, analisar a lista de validações dos campos!",
							(ConstraintViolationException) rootCause
					);
				} else {
					responseDto = GrupoProdutoResponseDto.construir(
							item,
							false,
							"Não foi possível gravar o grupo de produto!",
							e
					);
				}
			}
			catch (Exception e) {
				responseDto = GrupoProdutoResponseDto.construir(
						item,
						false,
						"Não foi possível gravar o grupo de produto!",
						e
				);
			}

			responseDtos.add(responseDto);
		}

		if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(responseDtos);
		}
		else {
			return ResponseEntity.ok(responseDtos);
		}
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<GrupoProduto>> listarAtivos(){
		List<GrupoProduto> grupoProduto = grupoProdutoService.listarGrupoProdutoAtivo();
		return ResponseEntity.ok(grupoProduto);
	}
	
	@GetMapping("/listar/resumido")
	public ResponseEntity<List<GrupoProdutoResumidoDto>> listarAtivosResumido(){
		return ResponseEntity.ok(grupoProdutoService.listarGrupoProdutoAtivoResumido());
	}
	
	
	
	@GetMapping("/grupo/buscar/{codigoOuDescricao}")
    public ResponseEntity<?> buscarProdutoPorCodigoDescricao(@PathVariable(name="codigoOuDescricao") String codigoOuDescricao){
        try {
            return ResponseEntity.ok().body(grupoProdutoService.autoCompleteCodigoDescricao(codigoOuDescricao));
        }catch(ConstraintViolationException e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(HttpStatus.BAD_REQUEST.value(), "Não existem grupos cadastrados.", e), 
                    HttpStatus.BAD_REQUEST);        
        }
    }
	
	@GetMapping("/grupo-produto-referencia")
    public ResponseEntity<List<GrupoProdutoGetDto>> buscarProdutoPorGrupo(){
		List<GrupoProdutoGetDto> grupoProdutoGetDtos = grupoProdutoService.buscarGrupoProdutoReferecia();
		return ResponseEntity.ok(grupoProdutoGetDtos);
    }
	
	@GetMapping("/ativos")
	public ResponseEntity<Page<GrupoProduto>> buscarTodosAtivos(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
	){
		Page<GrupoProduto> grupoProduto = grupoProdutoService.buscarGrupoProdutoAtivo(pageable);
		return ResponseEntity.ok(grupoProduto);
	}
	
	/*@GetMapping
	public ResponseEntity<Page<GrupoProduto>> buscarTodos(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
	){
		Page<GrupoProduto> grupoProdutoPage = grupoProdutoService.buscarTodos(pageable);
		return ResponseEntity.ok(grupoProdutoPage);
	}*/
	
	/*@GetMapping
	public ResponseEntity<Page<GrupoProduto>> buscarTodos(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
			Situacao situacao
	){
		Page<GrupoProduto> grupoProdutoPage = grupoProdutoService.buscarTodos(pageable, situacao);
		return ResponseEntity.ok(grupoProdutoPage);
	}*/
	
	@GetMapping
	public ResponseEntity<Page<GrupoProduto>> buscarTodos(
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
			String filtro,
			Situacao situacao
	){
		Page<GrupoProduto> grupoProdutoPage = grupoProdutoService.buscarTodos(pageable, filtro, situacao);
		return ResponseEntity.ok(grupoProdutoPage);
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarGrupoProduto(@PathVariable(name="id") Long id){
		GrupoProdutoResponseDto responseDto = null;

		try{
			GrupoProduto grupoProduto = grupoProdutoService.inativarGrupoProduto(id);
			responseDto = GrupoProdutoResponseDto.construir(
					grupoProduto,
					true,
					"O grupo de produto foi inativado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(Exception e) {
			GrupoProduto grupoProduto = new GrupoProduto();
			grupoProduto.setId(id);

			responseDto = GrupoProdutoResponseDto.construir(
					grupoProduto,
					false,
					"Não foi possível inativar o grupo de produto!"
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<?> ativarGrupoProduto(@PathVariable(name="id") Long id){
		GrupoProdutoResponseDto responseDto = null;

		try{
			GrupoProduto grupoProduto = grupoProdutoService.ativarGrupoProduto(id);
			responseDto = GrupoProdutoResponseDto.construir(
					grupoProduto,
					true,
					"O grupo de produto foi ativado com sucesso!"
			);

			return ResponseEntity.ok(responseDto);
		}
		catch(Exception e) {
			GrupoProduto grupoProduto = new GrupoProduto();
			grupoProduto.setId(id);

			responseDto = GrupoProdutoResponseDto.construir(
					grupoProduto,
					false,
					"Não foi possível ativar o grupo de produto!"
			);
		}

		return ResponseEntity.badRequest().body(responseDto);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<GrupoProdutoDto> buscarIdGrupoProduto(@PathVariable(name="id") Long id){
		GrupoProdutoDto grupoProdutoDto = grupoProdutoService.buscarGrupoProdutoDto(id);
		return ResponseEntity.ok(grupoProdutoDto);
	}
	
	@GetMapping("/codigo-grupo-produto/{codigoGrupoProduto}")
	public ResponseEntity<GrupoProdutoDto> buscarPorCodigo(@PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto){
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoGrupoProduto);
		List<GrupoProdutoGmo> grupoProdutoGmoList = grupoProdutoGmoService.buscarPorGrupoProduto(grupoProduto);
		GrupoProdutoDto grupoProdutoDto = GrupoProdutoDto.construir(grupoProduto, grupoProdutoGmoList);
		return ResponseEntity.ok(grupoProdutoDto);
		
	}
	
	@GetMapping("/validar-produto-e-subproduto/{codigoProduto}")
	public ResponseEntity<ValidarSubProdutoDto> validarProdutoESubProduto(@PathVariable(name="codigoProduto") String codigoProduto){
		try {
			Boolean isSubProduto = grupoProdutoService.validarProdutoeSubProduto(codigoProduto);
			return ResponseEntity.ok().body(new ValidarSubProdutoDto(isSubProduto, "Busca realizada com sucesso."));
		}
		catch(ConstraintViolationException e) {
			return ResponseEntity.badRequest().body(new ValidarSubProdutoDto("Ocorreu um erro na consulta."));	
		}
		
	}
	
	@GetMapping("/buscar/{codigoOuDescricao}")
    public ResponseEntity<?> getCodigoOrDescricao(@PathVariable(name="codigoOuDescricao") String filter){
		return ResponseEntity.ok().body(grupoProdutoService.getCodigoDescricao(filter));
    }

	@GetMapping("/buscar-entrada-re")
	public ResponseEntity<?> getBuscarEntradaRe()
	{
		return ResponseEntity.ok().body(grupoProdutoService.buscarEntradaEnum());
	}

	@GetMapping("/semente/filtrar")
    public ResponseEntity<?> getGrupoProdutoProdutoSemente(GrupoProdutoFilter filter){		
		return ResponseEntity.ok().body(grupoProdutoService.getGrupoProdutoProdutoSemente(filter));
    }
	
	@GetMapping("/buscar-por/codigo-ou-descricao")
    public ResponseEntity<List<GrupoProdutoSimplesDto>> buscarPorCodigoOuDescricao(
    		@RequestParam(name = "filtro", required = false, defaultValue = "") String filtro,
    		@RequestParam(name = "situacao", required = false, defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 10, sort = "fmCodigo", direction = Sort.Direction.ASC) Pageable pageable){
		Page<GrupoProduto> grupoProdutoPage = grupoProdutoService.buscarPorCodigoOuDescricaoPaginado(filtro, situacao, pageable);
		List<GrupoProdutoSimplesDto> gruposProdutos = GrupoProdutoSimplesDto.construir(grupoProdutoPage.getContent());
		return ResponseEntity.ok(gruposProdutos);
    }
	
	@GetMapping("/buscar-por/codigo-ou-descricao/parametro-entrada-re-diferente-nao-permite")
    public ResponseEntity<List<GrupoProdutoSimplesDto>> buscarPorCodigoOuDescricaoComParametroEntradaReDiferenteNaoPermite(
    		@RequestParam(name = "filtro", required = false, defaultValue = "") String filtro,
    		@RequestParam(name = "situacao", required = false, defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 10, sort = "fmCodigo", direction = Sort.Direction.ASC) Pageable pageable){
		Page<GrupoProduto> grupoProdutoPage = grupoProdutoService.buscarPorCodigoOuDescricaoComParametroEntradaReDiferenteNaoPermitePaginado(filtro, situacao, pageable);
		List<GrupoProdutoSimplesDto> gruposProdutos = GrupoProdutoSimplesDto.construir(grupoProdutoPage.getContent());
		return ResponseEntity.ok(gruposProdutos);
    }

	@GetMapping("/grupo-produto/filter")
    public ResponseEntity<?> findByGrupoProdutoFilter(GrupoProdutoFilter filter){
		return ResponseEntity.ok().body(grupoProdutoService.findByGrupoProdutoFilter(filter));
    }
	
	@GetMapping("/buscar/filtrar")
    public ResponseEntity<?> getGrupoProdutoBuscarFiltro(GrupoProdutoFilter filter){		
		return ResponseEntity.ok().body(grupoProdutoService.getGrupoProdutoProdutoSemente(filter));
    }
	
}

package br.coop.integrada.api.pa.aplication.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
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

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoBuscaDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoFilter;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoSaveDto;
import br.coop.integrada.api.pa.domain.service.PrecoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/preco")
@Tag(name = "Preco", description = "Precos cadastrados.")
public class PrecoController {
	
	@Autowired
	private PrecoService precoService;
	
	@PutMapping
    public ResponseEntity<PrecoResponseDto> salvar(@RequestBody PrecoSaveDto objDto) {
        PrecoResponseDto responseDto = null;
        try {
        	Preco preco = precoService.salvar(objDto);
            responseDto = PrecoResponseDto.construir(
                    preco,
                    true,
                    "Integração realizada com sucesso!",
                    objDto.getOperacao()
            );

            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
            responseDto = PrecoResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar o preço. Por favor, analisar a lista de validações dos campos!",
                    e,
                    objDto.getOperacao()
            );
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível realizar a integração do preço.";

            if(e.getMessage().toUpperCase().contains("KEY_ID_UNICO")) {
                mensagem += " Obs.: Já existe produto cadastrado com o código informado.";
            }

            responseDto = PrecoResponseDto.construir(
                    objDto,
                    false,
                    mensagem,
                    e,
                    objDto.getOperacao()
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                responseDto = PrecoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível realizar a integração do preço!",
                        (ConstraintViolationException) rootCause,
                        objDto.getOperacao()
                );
            }
            else {
                responseDto = PrecoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível realizar a integração do preço!",
                        e,
                        objDto.getOperacao()
                );
            }
        }
        catch (Exception e) {
            responseDto = PrecoResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível realizar a integração do preço!",
                    e,
                    objDto.getOperacao()
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }

	@GetMapping("/{idEstabelecimento}")
    public ResponseEntity<Page<PrecoDto>> buscarPrecos(
    		@PathVariable(name="idEstabelecimento") Long idEstabelecimento,
    		@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
    		PrecoFilter filter
    ){
        Page<PrecoDto> precoDtoPage = precoService.buscar(idEstabelecimento, filter, pageable);
        return ResponseEntity.ok(precoDtoPage);
    }
	
	@PostMapping("/integration/salve-all")
	public ResponseEntity<List<PrecoResponseDto>> integrationSaveAll(@RequestBody PrecoIntegrationSaveDto objDto){
		List<PrecoResponseDto> response = new ArrayList<>();
		Boolean sucessoIntegracao = false;
		
		for(PrecoSaveDto precoDto : objDto.getPrecos()) {
            PrecoResponseDto responseDto = null;
                        
            try {
            	
            	if(!Strings.isEmpty(precoDto.getIdUnico())) {            		
            		if(precoDto.getOperacao().equals(IntegrationOperacaoEnum.WRITE)) {
            			
            			responseDto = salvar(precoDto).getBody();
            			response.add(responseDto);
					    sucessoIntegracao = true;
					}else {
						
						Preco preco = precoService.findByIdUnico(precoDto.getIdUnico());
						if(preco != null) {
							
							try {
								precoService.remove(preco);
								responseDto = PrecoResponseDto.construir(precoDto, true, "Preço excluído com sucesso!", precoDto.getOperacao());
							    response.add(responseDto);
							    sucessoIntegracao = true;
								
							} catch (Exception e) {
								preco.setDataIntegracao(new Date());
								preco.setStatusIntegracao(StatusIntegracao.INTEGRADO);
								preco = precoService.atualizar(preco);
								
								responseDto = PrecoResponseDto.construir(precoDto, true, "Preço inativado com sucesso!", precoDto.getOperacao());
		            			response.add(responseDto);
							    sucessoIntegracao = true;
							}
							
						}else{
							responseDto = PrecoResponseDto.construir(precoDto, true, "O preço já foi excluído anteriormente.", precoDto.getOperacao());
	            			response.add(responseDto);
						    sucessoIntegracao = true;
						}
					}
            		
            	}else {
            		responseDto = PrecoResponseDto.construir(
                    		precoDto,
                            false,
                            "Erro de integração: Campo idUnico obrigatório.",
                            precoDto.getOperacao()
                    );
            		response.add(responseDto);
            	}
            	
            }catch (Exception e) {
                responseDto = PrecoResponseDto.construir(
                		precoDto,
                        false,
                        "Erro de integração",
                        e,
                        precoDto.getOperacao()
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
	
	@GetMapping("/regional")
	public ResponseEntity<Page<PrecoDto>> buscarPrecosRegional(
    		@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
    		PrecoFilter filter
    ){
        Page<PrecoDto> precoDtoPage = precoService.buscarRegional(filter, pageable);
        return ResponseEntity.ok(precoDtoPage);
    }

	@GetMapping("/estabelecimento-grupo-produto")
	public ResponseEntity<Page<PrecoDto>> buscarPrecosPorEstabelecimentoEOuGrupoEOuProduto(
			@PageableDefault(page = 0, size = 5, sort = "dataValidade desc, codigoProduto asc", direction = Sort.Direction.ASC) Pageable pageable,
			PrecoFilter filter){		
		Page<PrecoDto> precoDtoPage = precoService.buscarPrecoPorEstabelecimentoEOuGrupoEOuProduto(filter, pageable);
		return ResponseEntity.ok(precoDtoPage);
	}
	
	@GetMapping("/buscar-por/{codigoEstabelecimento}/{codigoProduto}/{codigoReferencia}/{precoCoco}")
	public ResponseEntity<PrecoBuscaDto> buscarPor(
			@PathVariable(name="precoCoco") Boolean precoCoco,
			@PathVariable(name="codigoProduto") String codigoProduto,
			@PathVariable(name="codigoReferencia") String codigoReferencia,
			@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento
    ){
        PrecoBuscaDto preco = precoService.buscarPor(codigoEstabelecimento, codigoProduto, codigoReferencia, precoCoco);
        return ResponseEntity.ok(preco);
    }
	
	/*
	@GetMapping("/corrigirHoraValidade")
	public ResponseEntity<?> corrigirhoraValidade() {
		precoService.horaValidadeCorrecao();
		return ResponseEntity.ok("Ajustado");
	}*/
}

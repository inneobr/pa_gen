package br.coop.integrada.api.pa.aplication.controller.parametros;


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
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroEstabelecimentoDataMovimentoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroEstabelecimentoIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroEstabelecimentoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaResponseDto;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/parametros-estabelecimento")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Parametro Estabelecimento", description = "Parametros de estabelecimento.")
public class ParametroEstabelecimentoController {
    
    @Autowired
    private ParametroEstabelecimentoService parametroEstabelecimentoService;
    
    @GetMapping("/movimentacao/estabelecimento={codigo}")
    public ResponseEntity<?> getDataMovimentoaberto(@PathVariable(name="codigo") String codigo){
    	return ResponseEntity.ok(parametroEstabelecimentoService.dataMovimentacaoAberto(codigo));   	
    }
    
    @PostMapping
    public ResponseEntity<ParametroEstabelecimentoResponseDto> cadastrar(@RequestBody ParametroEstabelecimentoDto objDto){
        ParametroEstabelecimentoResponseDto responseDto = null;

        try{
            ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.cadastrar(objDto);
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "O parâmetro de estabelecimento foi cadastrado com sucesso!"
            );

            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível cadastrar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                    e
            );

            ResponseEntity.ok(responseDto);
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                responseDto = ParametroEstabelecimentoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível cadastrar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                responseDto = ParametroEstabelecimentoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível cadastrar o parâmetro de estabelecimento!",
                        e
                );
            }
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível cadastrar o parâmetro de estabelecimento.";

            if(e.getMessage().toUpperCase().contains("KEY_ID_ESTABELECIMENTO")) {
                mensagem += " Obs.: Já existe parâmetros para o estabelecimetno informado.";
            }

            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    true,
                    mensagem,
                    e
            );
        }
        catch (Exception e) {
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível cadastrar o parâmetro de estabelecimento!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping
    public ResponseEntity<ParametroEstabelecimentoResponseDto> atualizar(@RequestBody ParametroEstabelecimentoDto objDto){
        ParametroEstabelecimentoResponseDto responseDto = null;

        try{
            ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.atualizar(objDto);
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "O parâmetro de estabelecimento foi atualizado com sucesso!"
            );

            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    true,
                    "Não foi possível atualizar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                responseDto = ParametroEstabelecimentoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível atualizar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                responseDto = ParametroEstabelecimentoResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível atualizar o parâmetro de estabelecimento!",
                        e
                );
            }
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível atualização o parâmetro de estabelecimento.";

            if(e.getMessage().toUpperCase().contains("KEY_ID_ESTABELECIMENTO")) {
                mensagem += " Obs.: Já existe parâmetros para o estabelecimetno informado.";
            }

            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    true,
                    mensagem,
                    e
            );
        }
        catch (Exception e) {
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível atualizar o parâmetro de estabelecimento!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @GetMapping
    public ResponseEntity<Page<ParametroEstabelecimentoDto>> buscarParametroEstabelecimentoPaginacao(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
    		String filter,
            Situacao situacao){
        Page<ParametroEstabelecimentoDto> objDtos = parametroEstabelecimentoService.buscarParametroEstabelecimentoPaginacao(pageable, filter, situacao);
    	//Page<ParametroEstabelecimentoDto> objDtos = parametroEstabelecimentoService.buscarParametroEstabelecimentoPaginacao(pageable);
        return ResponseEntity.ok().body(objDtos);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<List<ParametroEstabelecimentoDto>> listar(){
        List<ParametroEstabelecimentoDto> objDtos = parametroEstabelecimentoService.listar();
        return ResponseEntity.ok().body(objDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParametroEstabelecimentoDto> buscarPorId(@PathVariable(name="id") Long id) {
        ParametroEstabelecimentoDto objDto = parametroEstabelecimentoService.buscarPorId(id);
        return ResponseEntity.ok().body(objDto);
    }
    
    @GetMapping("/buscar-por/codigo-estabelecimento/{codigoEstabelecimento}")
    public ResponseEntity<ParametroEstabelecimentoDto> buscarPorCodigoEstabelecimento(@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento) {
    	ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(codigoEstabelecimento);
    	
    	if(parametroEstabelecimento == null) {
    		throw new ObjectNotFoundException("O estabelecimento \"" + codigoEstabelecimento + "\" não está parametrizado.");
    	}
    	
        ParametroEstabelecimentoDto parametroEstabelecimentoDto = ParametroEstabelecimentoDto.construir(parametroEstabelecimento);
        return ResponseEntity.ok(parametroEstabelecimentoDto);
    }
    
    @PutMapping("/inativar/{id}")
    public ResponseEntity<ParametroEstabelecimentoResponseDto> inativar(@PathVariable(name="id") Long id){
        ParametroEstabelecimentoResponseDto responseDto = null;

        try{
            ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.inativar(id);
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "O parâmetro de estabelecimento foi inativado com sucesso!"
            );
            return ResponseEntity.ok(responseDto);
        }
        catch(Exception e) {
            var parametroEstabelecimento = new ParametroEstabelecimento();
            parametroEstabelecimento.setId(id);

            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "Falha ao inativar o parâmetro de estabelecimento!"
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping("/ativar/{id}")
    public ResponseEntity<ParametroEstabelecimentoResponseDto> ativar(@PathVariable(name="id") Long id) {
        ParametroEstabelecimentoResponseDto responseDto = null;

        try{
            ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.ativar(id);
            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "O parâmetro de estabelecimento foi ativado com sucesso!"
            );
            return ResponseEntity.ok(responseDto);
        }
        catch(Exception e) {
            var parametroEstabelecimento = new ParametroEstabelecimento();
            parametroEstabelecimento.setId(id);

            responseDto = ParametroEstabelecimentoResponseDto.construir(
                    parametroEstabelecimento,
                    true,
                    "Falha oa ativar o parâmetro de estabelecimento!"
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }

    @PostMapping("/integration/save-all")
    public ResponseEntity<List<ParametroEstabelecimentoResponseDto>> integrationSaveAll(@RequestBody ParametroEstabelecimentoIntegrationSaveDto objDto){
        List<ParametroEstabelecimentoResponseDto> response = new ArrayList<>();
        Boolean sucessoIntegracao = false;

        for(ParametroEstabelecimentoDto parametroEstabelecimentoDto : objDto.getParametros()) {

            try {
            	
            	if(parametroEstabelecimentoDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
            		
            		ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(parametroEstabelecimentoDto.getCodigo());
            		if(parametroEstabelecimento != null) {
            			
            			try {
            				parametroEstabelecimentoService.delete(parametroEstabelecimento);
            				response.add(ParametroEstabelecimentoResponseDto.construir(parametroEstabelecimento, true, "Parâmetro excluído com sucesso "));
            				sucessoIntegracao = true;
							
						} catch (Exception e) {
							parametroEstabelecimento.setDataInativacao(new Date());
							parametroEstabelecimento.setDataIntegracao(new Date());
							parametroEstabelecimento.setStatusIntegracao(StatusIntegracao.INTEGRADO);
							parametroEstabelecimentoService.save(parametroEstabelecimento);
							
							response.add(ParametroEstabelecimentoResponseDto.construir(parametroEstabelecimentoDto, false, "Falha ao tentar excluir o parâmetro ", e));
						}
            			
            		}else {
            			response.add(ParametroEstabelecimentoResponseDto.construir(parametroEstabelecimentoDto, true, "Parâmetro excluído com sucesso "));
        				sucessoIntegracao = true;
            		}
            		
            	}else {  
            		
	                ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.cadastrarOuAtualizar(parametroEstabelecimentoDto);
	                response.add(ParametroEstabelecimentoResponseDto.construir(parametroEstabelecimento, true, "O parâmetro de estabelecimento salvo com sucesso!" ));
	                sucessoIntegracao = true;
            	}
            }
            catch(ConstraintViolationException e) {
            	response.add(ParametroEstabelecimentoResponseDto.construir(
                        parametroEstabelecimentoDto,
                        true,
                        "Não foi possível salvar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                        e
                ));
            }
            catch (TransactionSystemException e) {
                Throwable rootCause = ExceptionUtils.getRootCause(e);

                if (rootCause instanceof ConstraintViolationException) {
                	response.add(ParametroEstabelecimentoResponseDto.construir(
                            parametroEstabelecimentoDto,
                            false,
                            "Não foi possível salvar o parâmetro de estabelecimento. Por favor, analisar a lista de validações dos campos!",
                            (ConstraintViolationException) rootCause
                    ) );
                }
                else {
                	response.add(ParametroEstabelecimentoResponseDto.construir(
                            parametroEstabelecimentoDto,
                            false,
                            "Não foi possível salvar o parâmetro de estabelecimento!",
                            e
                    ));
                }
            }
            catch(DataIntegrityViolationException e) {
                String mensagem = "Não foi possível salvar o parâmetro de estabelecimento.";

                if(e.getMessage().toUpperCase().contains("KEY_ID_ESTABELECIMENTO")) {
                    mensagem += " Obs.: Já existe parâmetros para o estabelecimetno informado.";
                }

                response.add(ParametroEstabelecimentoResponseDto.construir(
                        parametroEstabelecimentoDto,
                        true,
                        mensagem,
                        e
                ));
                sucessoIntegracao = true;
            }
            catch (Exception e) {
            	response.add(ParametroEstabelecimentoResponseDto.construir(
                        parametroEstabelecimentoDto,
                        false,
                        "Não foi possível salvar o parâmetro de estabelecimento!",
                        e
                ));
            }

        }

        if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
    }
    
    @PostMapping("/validar-entrada-nome-cooperativa")
    public ResponseEntity<ValidaEntradaCooperativaResponseDto> validarEntradaNomeCooperativa(@RequestBody ValidaEntradaCooperativaDto input){
    	
    	try {
    		ValidaEntradaCooperativaResponseDto output = parametroEstabelecimentoService.validarEntradaNomeCooperativa(input);
        	output.setMensagem("Validação Realizada com sucesso");
        	
        	return ResponseEntity.ok().body(output);
    	}
    	catch(Exception e) {
    		return ResponseEntity.badRequest().body(new ValidaEntradaCooperativaResponseDto(e.getMessage()));
    	}
    }
    
    @GetMapping("/data-movimento-aberto/buscar-por/codigo-estabelecimento/{codigoEstabelecimento}")
    public ResponseEntity<ParametroEstabelecimentoDataMovimentoResponseDto> buscarDataMovimentoAberto(
    		@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento) {
    	ParametroEstabelecimentoDataMovimentoResponseDto response = null;
    	
    	try {
    		Date dataMovimentoAberto = parametroEstabelecimentoService.buscarDataMovimento(codigoEstabelecimento);
    		response = ParametroEstabelecimentoDataMovimentoResponseDto.construir(dataMovimentoAberto);
    		return ResponseEntity.ok(response);
    	}
    	catch (Exception e) {
    		response = ParametroEstabelecimentoDataMovimentoResponseDto.construir(e.getMessage());
    		return ResponseEntity.badRequest().body(response);
		}
    }
}
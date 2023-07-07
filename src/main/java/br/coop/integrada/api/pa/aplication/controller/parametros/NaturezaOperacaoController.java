package br.coop.integrada.api.pa.aplication.controller.parametros;

import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.GENESIS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.Operacao;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoMovimentacao;
import br.coop.integrada.api.pa.domain.modelDto.natureza.NaturezaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.NaturezaOperacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoEstabelecimentoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoEstabelecimentoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoIntegrationDataDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoMovimentoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoMovimentoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoResponseDataDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration.NaturezaOperacaoResponseDto;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoRep;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.natureza.NaturezaService;
import br.coop.integrada.api.pa.domain.service.naturezaOperacao.NaturezaMovimentacaoService;
import br.coop.integrada.api.pa.domain.service.naturezaOperacao.NaturezaOperacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/natureza-operacao")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Natureza Operacao", description = "Natureza Operação.")
public class NaturezaOperacaoController {
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
	@Autowired
	private NaturezaMovimentacaoService naturezaMovimentacaoService;

	@Autowired
	private NaturezaService naturezaService;
	
	@Autowired
	private NaturezaOperacaoRep naturezaOperacaoRep;
	
	@Autowired
    private IntegrationService integrationService;
		
	@PostMapping("/genesis/cadastrar")
	public ResponseEntity<NaturezaOperacao> cadastrar(@Valid @RequestBody NaturezaOperacaoDto naturezaOperacaoDto) throws Exception{
		NaturezaOperacao cadastrada = naturezaOperacaoRep.findByCodGrupoOrDescricao(naturezaOperacaoDto.getCodGrupo(), naturezaOperacaoDto.getDescricao());
		NaturezaOperacao nova = new NaturezaOperacao();
		BeanUtils.copyProperties(naturezaOperacaoDto, nova);		
		naturezaMovimentacaoService.salvarHistoricoNatureza("Cadastrou", naturezaOperacaoDto);
		
		if(cadastrada != null) {
			throw new NullPointerException("Não foi possivel cadastrar a Natureza de Operação: "+  cadastrada.getCodGrupoDescricao() +" Codigo ou Descrição já cadastrados em outra Natureza de Operação.");
		}
		NaturezaOperacao naturezaOperacao = new NaturezaOperacao();
	    BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao);
		naturezaOperacaoService.cadastrar(naturezaOperacao, naturezaOperacaoDto.getListEstabelecimentoDto());	
        return ResponseEntity.ok(naturezaOperacao); 
	}
	
	@PutMapping("/genesis/atualizar")
	public ResponseEntity<NaturezaOperacao> atualizar(@Valid @RequestBody NaturezaOperacaoDto naturezaOperacaoDto) throws Exception {
		naturezaMovimentacaoService.salvarHistoricoNatureza("Atualizou", naturezaOperacaoDto);
		if(naturezaOperacaoDto.getCodGrupo() == null) {
			throw new NullPointerException("Código do Grupo obrigatorio para atualização.");
		}
		NaturezaOperacao naturezaOperacao = naturezaOperacaoService.findByCodGrupo(naturezaOperacaoDto.getCodGrupo());
		if(naturezaOperacao == null){
			throw new NullPointerException("Não foi possivel atualizar o grupo: "+ naturezaOperacaoDto.getCodGrupo()+" Natureza Operação não encontrada.");
		}
		
		NaturezaOperacao naturezaOperacaoHistorico = new NaturezaOperacao();
		BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoHistorico);
	    BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao); 
		naturezaOperacaoService.salvar(naturezaOperacao, naturezaOperacaoDto.getListEstabelecimentoDto());
        return ResponseEntity.ok(naturezaOperacao); 
	}
	
	@PostMapping("/integration/save-all")
    public ResponseEntity<NaturezaOperacaoResponseDataDto> integrationSaveAll(@RequestBody NaturezaOperacaoIntegrationDataDto data) {        
        
        NaturezaOperacaoResponseDataDto response = new NaturezaOperacaoResponseDataDto();
        response.setNaturezas(new ArrayList<>());
        response.setEstabelecimentos(new ArrayList<>());
        response.setMovimentos(new ArrayList<>());
        
        Boolean sucessoIntegracao = false;
        
        IntegracaoPagina pagina = null; 
	    
		try {
			pagina = integrationService.buscarPorPagina(PaginaEnum.NATUREZA_OPERACAO);
		}catch(Exception e) { }
        
        if(data.getNaturezas() != null) {
	        for (NaturezaOperacaoIntegrationDto dto : data.getNaturezas()) {	        	
	        	
	            NaturezaOperacaoResponseDto responseDto = cadastrarOuAtualizar(dto, pagina).getBody();
	            response.getNaturezas().add(responseDto);
	            
	            if(responseDto != null && responseDto.getIntegrated()) {
	            	sucessoIntegracao = true;
	            }
	        	
	        }
        }

    	Map<Integer, NaturezaOperacao> mapNaturezas = new HashMap<Integer, NaturezaOperacao>();
        if(data.getEstabelecimentos() != null) {
        	for(NaturezaOperacaoEstabelecimentoIntegrationDto dto : data.getEstabelecimentos()) {
        		
        		
    			if(dto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
    				
    				NaturezaOperacaoEstabelecimento estabelecimentoNatOp = naturezaOperacaoService.buscarNaturezaOperacaoEstabelecimento(dto.getIdUnico());
    				if(estabelecimentoNatOp != null) {
    					
    					try {
							naturezaOperacaoService.deletarEstabelecimento(estabelecimentoNatOp.getId());
							response.getEstabelecimentos().add(NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, true, "Estabelecimento excluído com sucesso."));
							sucessoIntegracao = true;
							
						} catch (Exception e) {
							response.getEstabelecimentos().add(NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, false, "Falha ao tentar excluir o estabelecimento ", e.getMessage()));
						}
    					
    				}else {
    					response.getEstabelecimentos().add(NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, true, "Estabelecimento excluído com sucesso."));
    					sucessoIntegracao = true;
    				}
    			
    			}else {        		
        		
	        		NaturezaOperacao naturezaOperacao = mapNaturezas.get(dto.getCodGrupo());
	        		if(naturezaOperacao == null) {
	        			naturezaOperacao = naturezaOperacaoService.findByCodGrupo(dto.getCodGrupo());
	        		}
	        		
	        		if(naturezaOperacao == null) {
	        			response.getEstabelecimentos().add(NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, false, "Grupo de natureza não encontrado."));
	        			
	        		}else {
	        		
		        		NaturezaOperacaoEstabelecimentoResponseDto responseDto = cadastrarOuAtualizar(dto, naturezaOperacao, pagina).getBody();
			            response.getEstabelecimentos().add(responseDto);
			            
			            if(responseDto != null && responseDto.getIntegrated()) {
			            	sucessoIntegracao = true;
			            }
			            mapNaturezas.put(naturezaOperacao.getCodGrupo(), naturezaOperacao);
	        		}
    			}
    			
        	}
        }
        
        if(data.getMovimentos() != null) {
        	
        	for(NaturezaOperacaoMovimentoIntegrationDto dto : data.getMovimentos()) {
        		
        		if(dto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
    				
        			NaturezaOperacaoMovimentacao movimentoNatOp = naturezaOperacaoService.buscarNaturezaOperacaoMovimento(dto.getIdUnico());
    				if(movimentoNatOp != null) {
    					try {
    						
    						if(movimentoNatOp != null) {
    							naturezaOperacaoService.deletarMovimento(movimentoNatOp.getId());
    						}
    						response.getMovimentos().add(NaturezaOperacaoMovimentoResponseDto.construir(dto, true, "Movimentação excluída com sucesso.")); 
    						sucessoIntegracao = true;
    						
						} catch (Exception e) {
							response.getMovimentos().add(NaturezaOperacaoMovimentoResponseDto.construir(dto, false, "Falha ao tentar excluir o estabelecimento ", e.getMessage()));
						}
    					
    				}else {
    					response.getMovimentos().add(NaturezaOperacaoMovimentoResponseDto.construir(dto, true, "Movimentação excluída com sucesso."));
    					sucessoIntegracao = true;
    				}
    			
    			}else { 
        		
	        		NaturezaOperacao naturezaOperacao = mapNaturezas.get(dto.getCodGrupo());
	        		if(naturezaOperacao == null) {
	        			naturezaOperacao = naturezaOperacaoService.findByCodGrupo(dto.getCodGrupo());
	        		}
	        		
	        		if(naturezaOperacao == null) {
	        			response.getMovimentos().add(NaturezaOperacaoMovimentoResponseDto.construir(dto, false, "Grupo de natureza não encontrado."));
	        			
	        		}else {
	        		
	        			NaturezaOperacaoMovimentoResponseDto responseDto = cadastrarOuAtualizar(dto, naturezaOperacao, pagina).getBody();
			            response.getMovimentos().add(responseDto);
			            
			            if(responseDto != null && responseDto.getIntegrated()) {
			            	sucessoIntegracao = true;
			            }
			            mapNaturezas.put(naturezaOperacao.getCodGrupo(), naturezaOperacao);
	        		}
    			}
        		
        	}
        }
                
        if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
    }

	public ResponseEntity<NaturezaOperacaoResponseDto> cadastrarOuAtualizar(
			@Valid @RequestBody NaturezaOperacaoIntegrationDto naturezaOperacaoDto, IntegracaoPagina pagina){	
		NaturezaOperacaoResponseDto response = null;
        
		try {	
			
			if(naturezaOperacaoDto.getCodGrupo() == null) {
				throw new ObjectNotFoundException("Campo {codGrupo} obrigatório");
			}
			
			NaturezaOperacao naturezaOperacao = naturezaOperacaoService.findByCodGrupo(naturezaOperacaoDto.getCodGrupo());
			
			if(naturezaOperacaoDto.getOperacao() == null) {
				throw new ObjectNotFoundException("Campo {operacao} obrigatório");
			}else {
				if(naturezaOperacaoDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {					
					if(naturezaOperacao != null) {
						naturezaOperacaoService.deletar(naturezaOperacao.getId());
					}
					response = NaturezaOperacaoResponseDto.construir(naturezaOperacaoDto, true, "Natureza excluída com sucesso."); 
					return ResponseEntity.ok(response);   
				}
			}
			
			if(naturezaOperacao == null) {
				naturezaOperacao = new NaturezaOperacao();
			}
			
		    BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao); 
			
	    	if(pagina != null && GENESIS.equals(pagina.getOrigenEnum())) {
	    		naturezaOperacao.setStatusIntegracao(StatusIntegracao.INTEGRAR);
	    	}else{
	    		naturezaOperacao.setStatusIntegracao(StatusIntegracao.INTEGRADO);
	    		naturezaOperacao.setDataIntegracao(new Date());
	    	}		    
	    	
	    	naturezaOperacaoService.save(naturezaOperacao);
            response = NaturezaOperacaoResponseDto.construir(naturezaOperacaoDto, true, "Integração realizada com sucesso.");            
            return ResponseEntity.ok(response);              
		}
		catch(ConstraintViolationException e) {
            response = NaturezaOperacaoResponseDto.construir(
                    naturezaOperacaoDto, false, "Falha ao salvar, violação de restrição!");
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				response = NaturezaOperacaoResponseDto.construir(
						naturezaOperacaoDto,
						false,
						"Não foi possível gravar a natureza de operação. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				response = NaturezaOperacaoResponseDto.construir(
						naturezaOperacaoDto,
						false,
						"Não foi possível gravar a natureza de operação!",
						e.getMessage()
				);
			}
		}
        catch(DataIntegrityViolationException e) {
            response = NaturezaOperacaoResponseDto.construir(naturezaOperacaoDto, false, "Natureza de operação já cadastrada: "+ e.getMessage());
        }
        catch(Exception e) {
            response = NaturezaOperacaoResponseDto.construir(naturezaOperacaoDto, false, "Falha ao salvar: ", e.getMessage());
        }
        
        return ResponseEntity.badRequest().body(response); 
	}
	
	public ResponseEntity<NaturezaOperacaoEstabelecimentoResponseDto> cadastrarOuAtualizar(
			@Valid @RequestBody NaturezaOperacaoEstabelecimentoIntegrationDto dto, NaturezaOperacao naturezaOperacao, IntegracaoPagina pagina ){	
		
		NaturezaOperacaoEstabelecimentoResponseDto response = null;
        
		try {	
			
			if(dto.getIdUnico() == null) {
				throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
			}
			
			if(dto.getCodGrupo() == null) {
				throw new ObjectNotFoundException("Campo {codGrupo} obrigatório");
			}
			
			if(Strings.isEmpty(dto.getCodigo() ) ) {
				throw new ObjectNotFoundException("Campo {codigo} obrigatório");
			}
			
			if( dto.getOperacao() == null ) {
				throw new ObjectNotFoundException("Campo {operacao} obrigatório");
			}
			
			NaturezaOperacaoEstabelecimento estabelecimentoNatOp = naturezaOperacaoService.buscarNaturezaOperacaoEstabelecimento(dto.getIdUnico());
						
			if(estabelecimentoNatOp == null) {
				estabelecimentoNatOp = new NaturezaOperacaoEstabelecimento();
			}
			
		    BeanUtils.copyProperties(dto, estabelecimentoNatOp); 
		    estabelecimentoNatOp.setNaturezaOperacao(naturezaOperacao);
		    estabelecimentoNatOp.setEstabelecimento(naturezaOperacaoService.localizarEstabelecimento(dto.getCodigo()));
		    
	    	if(pagina != null && GENESIS.equals(pagina.getOrigenEnum())) {
	    		estabelecimentoNatOp.setStatusIntegracao(StatusIntegracao.INTEGRAR);
	    	}else{
	    		estabelecimentoNatOp.setStatusIntegracao(StatusIntegracao.INTEGRADO);
	    		estabelecimentoNatOp.setDataIntegracao(new Date());
	    	}		    
	    	
	    	naturezaOperacaoService.saveEstabelecimento(estabelecimentoNatOp);
            response = NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, true, "Integração realizada com sucesso.");            
            return ResponseEntity.ok(response);              
		}
		catch(ConstraintViolationException e) {
            response = NaturezaOperacaoEstabelecimentoResponseDto.construir(
            		dto, false, "Falha ao salvar, violação de restrição!");
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				response = NaturezaOperacaoEstabelecimentoResponseDto.construir(
						dto,
						false,
						"Não foi possível gravar o estabelecimento na natureza de operação. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				response = NaturezaOperacaoEstabelecimentoResponseDto.construir(
						dto,
						false,
						"Não foi possível gravar o estabeleciento na natureza de operação!",
						e.getMessage()
				);
			}
		}
        catch(DataIntegrityViolationException e) {
        	String mensagem = "O estabelecimento já estava vinculado a natureza.";
        	Boolean sucesso = true;
        	
        	if(e.getMessage().toUpperCase().contains("KEY_NATUREZA_OPERACAO_ESTABELECIMENTO")) {
                mensagem = "Obs.: O estabelecimento já está vinculado a outra natureza de operação.";
                sucesso = false;
            }
        	
            response = NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, sucesso, mensagem);
        }
        catch(Exception e) {
            response = NaturezaOperacaoEstabelecimentoResponseDto.construir(dto, false, "Falha ao salvar: ", e.getMessage());
        }
        
        return ResponseEntity.badRequest().body(response); 
	}
	
	public ResponseEntity<NaturezaOperacaoMovimentoResponseDto> cadastrarOuAtualizar(
			@Valid @RequestBody NaturezaOperacaoMovimentoIntegrationDto dto, NaturezaOperacao naturezaOperacao, IntegracaoPagina pagina ){	
		
		NaturezaOperacaoMovimentoResponseDto response = null;
        
		try {	
			
			if(dto.getIdUnico() == null) {
				throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
			}
			
			if(dto.getCodGrupo() == null) {
				throw new ObjectNotFoundException("Campo {codGrupo} obrigatório");
			}
			
			if( dto.getOperacao() == null ) {
				throw new ObjectNotFoundException("Campo {operacao} obrigatório");
			}
			
			NaturezaOperacaoMovimentacao movimentoNatOp = naturezaOperacaoService.buscarNaturezaOperacaoMovimento(dto.getIdUnico());
							
			if(movimentoNatOp == null) {
				movimentoNatOp = new NaturezaOperacaoMovimentacao();
			}
			
		    BeanUtils.copyProperties(dto, movimentoNatOp); 
		    movimentoNatOp.setCodGrupo(naturezaOperacao.getCodGrupo());
		    			
	    	if(pagina != null && GENESIS.equals(pagina.getOrigenEnum())) {
	    		movimentoNatOp.setStatusIntegracao(StatusIntegracao.INTEGRAR);
	    	}else{
	    		movimentoNatOp.setStatusIntegracao(StatusIntegracao.INTEGRADO);
	    		movimentoNatOp.setDataIntegracao(new Date());
	    	}		    
	    	
	    	naturezaOperacaoService.saveMovimentacao(movimentoNatOp);
            response = NaturezaOperacaoMovimentoResponseDto.construir(dto, true, "Integração realizada com sucesso.");            
            return ResponseEntity.ok(response);              
		}
		catch(ConstraintViolationException e) {
            response = NaturezaOperacaoMovimentoResponseDto.construir(
            		dto, false, "Falha ao salvar, violação de restrição!");
        }
		catch (TransactionSystemException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof ConstraintViolationException) {
				response = NaturezaOperacaoMovimentoResponseDto.construir(
						dto,
						false,
						"Não foi possível gravar a movimentação. Por favor, analisar a lista de validações dos campos!",
						(ConstraintViolationException) rootCause
				);
			}
			else {
				response = NaturezaOperacaoMovimentoResponseDto.construir(
						dto,
						false,
						"Não foi possível gravar a movimentação!",
						e.getMessage()
				);
			}
		}
        catch(DataIntegrityViolationException e) {
            response = NaturezaOperacaoMovimentoResponseDto.construir(dto, true, "A movimentação já estava cadastrada: ");
        }
        catch(Exception e) {
            response = NaturezaOperacaoMovimentoResponseDto.construir(dto, false, "Falha ao salvar: ", e.getMessage());
        }
        
        return ResponseEntity.badRequest().body(response); 
	}
	
	@GetMapping("/filtrar")
	public ResponseEntity<?> buscarTodos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			String filter){
		try{
			return ResponseEntity.ok().body(naturezaOperacaoService.buscarTodos(filter, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe natureza operação cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarIdNaturezaOperacao(@PathVariable(name="id") Long id){
		try{
			
			return ResponseEntity.ok().body(naturezaOperacaoService.buscarIdNaturezaOperacao(id));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel inativar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/{id}/estabelecimentos")
	public ResponseEntity<?> buscarIdNaturezaOperacaoEstabelecimentos(
			@PathVariable(name="id") Long id, 
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			
			return ResponseEntity.ok().body(naturezaOperacaoService.buscarIdNaturezaOperacaoEstabelecimentos(id, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel inativar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativarNaturezaOperacao(@PathVariable(name="id") Long id) throws Exception{
		try{
			naturezaOperacaoService.inativarNaturezaOperacao(id);
			return ResponseEntity.ok().body("Natureza operacao inativada com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel inativar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<?> ativarNaturezaOperacao(@PathVariable(name="id") Long id) throws Exception{
		try{
			naturezaOperacaoService.ativarNaturezaOperacao(id);
			return ResponseEntity.ok().body("Natureza operacao inativada com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel inativar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> deletarNaturezaOperacao(@PathVariable(name="id") Long id) throws Exception{
		try{
			naturezaOperacaoService.deletar(id);
			return ResponseEntity.ok().body("Natureza operacao deletada com sucesso!");
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel deletar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
	
	@GetMapping("/historico-natureza/{codGrupo}")
	public ResponseEntity<?> buscarTodoHistorico(
			@PathVariable(name="codGrupo") Integer codGrupo, 
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(naturezaMovimentacaoService.buscarHistoricoNatureza(codGrupo, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe historico natureza operação cadastrada.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
    
    @GetMapping("/buscar-por/{codigoEstabelecimento}/{operacao}/{codigoProdutor}")
    public ResponseEntity<NaturezaResponseDto> buscarPor(
            @PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name = "operacao") Operacao operacao,
            @PathVariable(name = "codigoProdutor") String codigoProdutor,
			@RequestParam(name = "codigoGrupoProduto", defaultValue = "", required = false) String codigoGrupoProduto,
            @RequestParam(name = "naturezaOrigem", defaultValue = "", required = false) String naturezaOrigem
    ){
		NaturezaResponseDto naturezaResponseDto = naturezaService.buscarNaturezaPor(
				codigoEstabelecimento,
				operacao,
				codigoProdutor,
				codigoGrupoProduto,
				naturezaOrigem
		);

		if(naturezaResponseDto.getIntegrated()) {
			return ResponseEntity.ok(naturezaResponseDto);
		}

		return ResponseEntity.badRequest().body(naturezaResponseDto);
    }
}

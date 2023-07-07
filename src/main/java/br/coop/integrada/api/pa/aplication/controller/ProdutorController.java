package br.coop.integrada.api.pa.aplication.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.RepresentanteResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.VerificaProdutorResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtorFilter.ProdutorFilter;
import br.coop.integrada.api.pa.domain.modelDto.produtorFilter.ProdutorFilterResponse;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/produtor")
@Tag(name = "Produtor", description = "Produtores cadastrados.")
public class ProdutorController {
    
    @Autowired
    private ProdutorService produtorService;
    
    @Autowired
    private ImovelService imovelService;
    
    @PostMapping
    public ResponseEntity<ProdutorResponseDto> salvar(@Valid @RequestBody ProdutorDto produtorDto) {        
        ProdutorResponseDto response = null; 
        
        try {
            
             boolean isNovoRegistro = false;   
             Produtor produtor = null;
             if(produtorDto.getCodProdutor() != null) { 
                 produtor = produtorService.findByCodProdutor(produtorDto.getCodProdutor());
             }
             
             if(produtor == null) { 
                 produtor = new Produtor();
                 isNovoRegistro = true;
             }
             
             BeanUtils.copyProperties(produtorDto, produtor);
             
             produtor.setDataIntegracao(new Date());
             produtor.setStatusIntegracao(StatusIntegracao.INTEGRAR);
             
             if (produtor.getAtivo() == null || produtor.getAtivo()) {
            	 produtor.setDataInativacao(null);
			 } else {
				 produtor.setDataInativacao(new Date());
			 }
             
             produtorService.salvar(produtor);
             
             if(!isNovoRegistro) {
                 response = ProdutorResponseDto.construir(produtorDto, true, "Produtor atualizado com sucesso.");
             }else {
                 response = ProdutorResponseDto.construir(produtorDto, true, "Produtor cadastrado com sucesso.");
             }
             return ResponseEntity.ok(response);  
        }  
        catch(ConstraintViolationException e) {
            response = ProdutorResponseDto.construir(
                                                produtorDto, 
                                                false, 
                                                "Falha ao salvar, violação de restrição!", 
                                                e);
        }
        catch(DataIntegrityViolationException e) {
            response = ProdutorResponseDto.construir(produtorDto, false, "Falha ao salvar, violação de integridade de dados!", e.getMessage());
        }
        catch(Exception e) {
            response = ProdutorResponseDto.construir(produtorDto, false, "Falha ao salvar", e.getMessage());
        }
        
        return ResponseEntity.badRequest().body(response);           
    }
    
    @PostMapping("/save-integration")
    public ResponseEntity<ProdutorResponseDto> salveIntegration(@Valid @RequestBody ProdutorDto produtorDto) {        
        ProdutorResponseDto response = null; 
        
        try {
            
             boolean isNovoRegistro = false;   
             Produtor produtor = null;
             if(produtorDto.getCodProdutor() != null && !produtorDto.getCodProdutor().trim().isEmpty()) { 
                 produtor = produtorService.findByCodProdutor(produtorDto.getCodProdutor());
                 
                 if(produtorDto.getOperacao() == null ) {
                	 response = ProdutorResponseDto.construir(produtorDto, false, "Campo {operacao} obrigatório.");
                	 return ResponseEntity.badRequest().body(response);
                 }
                 
                 if(produtorDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
                	 
                	 if(produtor == null) {
                		 return ResponseEntity.ok(ProdutorResponseDto.construir(produtorDto, true, "Produtor informado já foi excluído."));
                	 }else {
                	 
	                	 try {
	                		 produtorService.remove(produtor);
							return ResponseEntity.ok(ProdutorResponseDto.construir(produtorDto, true, "Produtor excluído com sucesso!"));
						} catch (Exception e) {
							
							if(produtor.getDataInativacao() == null) {
								produtor.setDataInativacao(new Date());
							}
							produtor.setDataIntegracao(new Date());
							produtor.setStatusIntegracao(StatusIntegracao.INTEGRADO);
							return ResponseEntity.ok(ProdutorResponseDto.construir(produtorDto, true, "Produtor inativado com sucesso!"));
						}
                	 }
                 }
                 
                 
             }else {
            	 response = ProdutorResponseDto.construir(produtorDto, false, "Campo {codProdutor} obrigatório.");
            	 return ResponseEntity.badRequest().body(response);
             }
             
             if(produtor == null) { 
                 produtor = new Produtor();
                 isNovoRegistro = true;
             }
             
             BeanUtils.copyProperties(produtorDto, produtor);
             produtor.setDataIntegracao(new Date());
             produtor.setStatusIntegracao(StatusIntegracao.INTEGRADO);
             
             if (produtor.getAtivo() == null || produtor.getAtivo()) {
            	 produtor.setDataInativacao(null);
			 } else {
				 produtor.setDataInativacao(new Date());
			 }
             
             produtorService.salvar(produtor);
             
             if(!isNovoRegistro) {
                 response = ProdutorResponseDto.construir(produtorDto, true, "Produtor atualizado com sucesso.");
             }else {
                 response = ProdutorResponseDto.construir(produtorDto, true, "Produtor cadastrado com sucesso.");
             }
             return ResponseEntity.ok(response);  
        }  
        catch(ConstraintViolationException e) {
            response = ProdutorResponseDto.construir(
                                                produtorDto, 
                                                false, 
                                                "Falha ao salvar, violação de restrição!", 
                                                e);
        }
        catch(DataIntegrityViolationException e) {
            response = ProdutorResponseDto.construir(produtorDto, false, "Falha ao salvar, violação de integridade de dados!", e.getMessage());
        }
        catch(Exception e) {
            response = ProdutorResponseDto.construir(produtorDto, false, "Falha ao salvar", e.getMessage());
        }
        
        return ResponseEntity.badRequest().body(response);           
    }
    
    @PostMapping("salvar-lista")
    public ResponseEntity<List<ProdutorResponseDto>> cadastrarLista(@RequestBody List<ProdutorDto> produtores) {        
        
        List<ProdutorResponseDto> response = new ArrayList<>();
        for (ProdutorDto produtorDto : produtores) {
            response.add(salvar(produtorDto).getBody());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/integration/save-all")
    public ResponseEntity<List<ProdutorResponseDto>> integrationSaveAll(@RequestBody ProdutorIntegrationDto objDto) {        
        
        List<ProdutorResponseDto> response = new ArrayList<>();
        Boolean sucessoIntegracao = false;
        
        
        for (ProdutorDto produtorDto : objDto.getProdutores()) {
            ProdutorResponseDto responseDto = salveIntegration(produtorDto).getBody();
            response.add(responseDto);
            
            if(responseDto != null && responseDto.getIntegrated()) {
            	sucessoIntegracao = true;
            }
        }
        
        if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(response);
		}
		else {
			return ResponseEntity.ok(response);
		}
    }
    
    @GetMapping
    public ResponseEntity<?> buscarTodos(
    		@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
    		@RequestParam(name = "nome", defaultValue = "") String nome){
        try {
            return ResponseEntity.ok().body(produtorService.buscarTodosPage(pageable, nome));
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/buscarCadpro/produtor-imovel/{codigoProdutor}/{matriculaImovel}")
    public ResponseEntity<?> getCadProProdutorCodigoAndImovelMatricula(
    		@PathVariable(name= "codigoProdutor")  String codigoProdutor,
    		@PathVariable(name= "matriculaImovel") Long matriculaImovel){
        try {
            return ResponseEntity.ok().body(imovelService.getCadProProdutorAndImovel(codigoProdutor, matriculaImovel));
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Imóvel não encontrado."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/buscar-por/codigo-produtor/{codigoProdutor}")
    public ResponseEntity<ProdutorSimplesDto> buscarPorCodigoProdutor(@PathVariable(name= "codigoProdutor")  String codigoProdutor) {
    	Produtor produtor = produtorService.findByCodProdutor(codigoProdutor);
    	
    	if(produtor == null) {
    		throw new ObjectNotFoundException("Não foi encontrado produtor com o código: " + codigoProdutor);
    	}
    	
    	ProdutorSimplesDto produtorDto = ProdutorSimplesDto.construir(produtor);
    	return  ResponseEntity.ok(produtorDto);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> Listar(){
        try {
            return ResponseEntity.ok().body(produtorService.buscarTodosLista());
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/buscar/{dados}")
    public ResponseEntity<?> buscarPorCodigoouNome(@PathVariable(name="dados") String dados){
        try {
            return ResponseEntity.ok().body(produtorService.buscarProdutor(dados));
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativar(@PathVariable(name="id") Long id){
        try {
            produtorService.inativar(id);
            return ResponseEntity.ok().body("Produtor inativado com sucesso!");
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/ativar/{id}")
    public ResponseEntity<?> ativar(@PathVariable(name="id") Long id){
        try {
            produtorService.ativar(id);
            return ResponseEntity.ok().body("Produtor ativado com sucesso!");
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable(name="id") Long id){
        try {
            produtorService.deletar(id);
            return ResponseEntity.ok().body("Produtor deletado com sucesso!");
        }catch(Exception e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(NOT_FOUND.value(), "Não existem Produtores cadastrados."+ e),
                    HttpStatus.NOT_FOUND);
        }
    }   
    
    @GetMapping("/buscar-codigo-representante/{codProdutor}")
    public ResponseEntity<RepresentanteResponseDto> buscarPorCodigoOuNome(@PathVariable(name="codProdutor") String codProdutor){
        try {
            Produtor produtor =  produtorService.buscarPorCodigoProdutor(codProdutor);
            if(produtor != null && produtor.getCodRepres() != null &&  !produtor.getCodRepres().trim().isEmpty()) {
                return ResponseEntity.ok().body(RepresentanteResponseDto.construir(produtor.getCodRepres()));
            }
            return ResponseEntity.badRequest().body(RepresentanteResponseDto.construir(null));
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(RepresentanteResponseDto.construir(null));
        }
    }
    
    @PutMapping("/validar-parceiro-bayer/{cnpj}/{parceiro}/{movimento}")
    public void validarParceiroBayer(
            @PathVariable(name="cnpj") String cnpj,
            @PathVariable(name="parceiro") Boolean parceiro,
            @PathVariable(name="movimento") String movimento
       ) {
        produtorService.setParticipanteBayer(cnpj, parceiro, movimento);
    }
    
    @GetMapping("/verificar-produtor/{codProdutor}")
    public ResponseEntity<VerificaProdutorResponseDto> verificarProdutor(@PathVariable(name="codProdutor") String codProdutor){
    	try {
    		
    		VerificaProdutorResponseDto verificaProdutorResponseDto = produtorService.verificarProdutor(codProdutor);
			
			return ResponseEntity.ok().body(verificaProdutorResponseDto);
		}
    	catch(ObjectNotFoundException e) {
    		return ResponseEntity.badRequest().body(new VerificaProdutorResponseDto(e.getMessage()));
    	}
		catch(ConstraintViolationException e) {
			return ResponseEntity.badRequest().body(new VerificaProdutorResponseDto("Ocorreu um erro na consulta."));	
		}
    	catch(Exception e) {
    		return ResponseEntity.badRequest().body(new VerificaProdutorResponseDto("Ocorreu um erro na consulta."));
    	}
    }
    
    @GetMapping("/pesquisar-por/codigo-ou-nome")
    public ResponseEntity<List<ProdutorSimplesDto>> pesquisarPorCodigoOuNome(
    		@RequestParam(name = "filtro", required = false) String filtro,
    		@RequestParam(name = "situacao", required = false, defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 10, sort = "codProdutor", direction = Sort.Direction.ASC) Pageable pageable
    		) {
    	List<Produtor> produtores = produtorService.pesquisarPorCodigoOuNomeOuCpfCnpj(filtro, situacao, pageable);
    	List<ProdutorSimplesDto> response = ProdutorSimplesDto.construir(produtores);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/buscar-por/codigo-produtor/{codProdutor}/ativo/nao-emite-nota-fiscal")
    public ResponseEntity<ProdutorSimplesDto> buscarPorCodigoQueNaoEmiteNf(
    		@PathVariable(name="codProdutor") String codProdutor) {
    	Produtor produtor = produtorService.buscarPorCodigoQueNaoEmiteNf(codProdutor, Situacao.ATIVO)
    			.orElseThrow(() -> new ObjectDefaultException("Não foi identificado nenhum produtor ativo com o código \"" + codProdutor + "\" que não emita nota fiscal."));
    	ProdutorSimplesDto response = ProdutorSimplesDto.construir(produtor);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<ProdutorFilterResponse>> getProdutoresPorFiltro(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, ProdutorFilter filter) {
		Page<Produtor> produtorPage = produtorService.listarProdutoresComFiltros(pageable, filter);
		List<ProdutorFilterResponse> produtorFilterResponse = ProdutorFilterResponse.construir(produtorPage.getContent());
		Page<ProdutorFilterResponse> response = new PageImpl<ProdutorFilterResponse>(produtorFilterResponse, pageable, produtorPage.getTotalElements());
    	return ResponseEntity.ok(response);
    }
    
}

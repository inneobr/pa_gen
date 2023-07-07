package br.coop.integrada.api.pa.aplication.controller.imovel;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImoveisProdutorFilter;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelIntegratioinDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelIntegratioinResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelProdutorResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelZoomDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorImovelDto;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/imovel")
@Tag(name = "Imovel", description = "Imóveis cadastrados.")
public class ImovelController {
    
    @Autowired
    private ImovelService imovelService;
    
    @PostMapping
    public ResponseEntity<ImovelResponseDto> cadastrarNovo(@RequestBody ImovelDto imovelDto){
        ImovelResponseDto imovelResponseDto = null;

        try {
            Imovel imovel = imovelService.salvarImovel(imovelDto);
            imovelResponseDto = ImovelResponseDto.construir(
                    imovel,
                    true,
                    "O imóvel foi gravado com sucesso!"
            );

            return ResponseEntity.ok(imovelResponseDto);
        }
        catch(ConstraintViolationException e) {
            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    "Não foi possível gravar o imóvel. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                imovelResponseDto = ImovelResponseDto.construir(
                        imovelDto,
                        false,
                        "Não foi possível gravar o imóvel. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                imovelResponseDto = ImovelResponseDto.construir(
                        imovelDto,
                        false,
                        "Não foi possível gravar o imóvel!",
                        e
                );
            }
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível gravar o imóvel.";

            if(e.getMessage().toUpperCase().contains("KEY_MATRICULA")) {
                mensagem += " Obs.: Já existe imóvel cadastrado com a matrícula informada.";
            }

            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    mensagem,
                    e
            );
        }
        catch (Exception e) {
            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    "Não foi possível gravar o imóvel!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(imovelResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imovel> buscarUnico(@PathVariable(name="id") Long id){
        Imovel imovel = imovelService.buscarImovelUnicoId(id);
        return ResponseEntity.ok(imovel);
    }

    @GetMapping("/buscar-por/matricula/{matricula}")
    public ResponseEntity<ImovelSimplesDto> buscarPorMatricula(@PathVariable(name="matricula") Long matricula){
        Imovel imovel = imovelService.buscarPorMatricula(matricula);
        
        if(imovel == null) {
        	throw new ObjectNotFoundException("Não foi encontrado imovel com a matricula: " + matricula);
        }
        
        ImovelSimplesDto imovelDto = ImovelSimplesDto.construir(imovel);
        return ResponseEntity.ok(imovelDto);
    }
    
    @GetMapping("/produtor/{codProdutor}")
    public ResponseEntity<Page<ImovelProdutor>> buscarPorCodigoOuDescricao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, 
            @PathVariable(name="codProdutor") String codProdutor)
    {
        return ResponseEntity.ok(imovelService.buscarImovelPorProdutor(codProdutor, pageable));
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<Page<ImovelProdutor>> buscarTodosAtivos(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ImovelProdutor> imovelProdutorPage = imovelService.buscarTodosAtivos(pageable);
        return ResponseEntity.ok(imovelProdutorPage);
    }
    
    @PutMapping("/ativar/{id}")
    public ResponseEntity<ImovelResponseDto> ativarImovel(@PathVariable(name="id") Long id){
        ImovelResponseDto responseDto = null;

        try {
            Imovel imove = imovelService.ativarImovelPorId(id);
            responseDto = ImovelResponseDto.construir(
                    imove,
                    true,
                    "O imóvel foi ativado com sucesso!"
            );

            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e) {
            responseDto = ImovelResponseDto.construir(
                    null,
                    false,
                    "Não foi possível ativar o imóvel!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping("/inativar/{id}")
    public ResponseEntity<ImovelResponseDto> inativarImovel(@PathVariable(name="id") Long id){
        ImovelResponseDto responseDto = null;

        try {
            Imovel imove = imovelService.inativarImovelPorId(id);
            responseDto = ImovelResponseDto.construir(
                    imove,
                    true,
                    "O imóvel foi inativado com sucesso!"
            );

            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e) {
            responseDto = ImovelResponseDto.construir(
                    null,
                    false,
                    "Não foi possível inativar o imóvel!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }

    @PostMapping("/integration/save-all")
    public ResponseEntity<ImovelIntegratioinResponseDto> integrationSaveAll(@RequestBody ImovelIntegratioinDto objDto){
    	ImovelIntegratioinResponseDto response = ImovelIntegratioinResponseDto.construir();
        Boolean sucessoIntegracao = false;

        if(objDto.getData() != null) { 
        	
        	if(objDto.getData().getImoveis() != null) {
        
		        for(ImovelDto imovelDto : objDto.getData().getImoveis()) {	
		        	ImovelResponseDto imovelResponseDto = integrationSaveAllImovel(imovelDto);	
		        	response.getData().getImoveis().add(imovelResponseDto);
		            
		            if(imovelResponseDto.getIntegrated()) {
		            	sucessoIntegracao = true;
		            }
		        }
	        }
        	
        	if(objDto.getData().getImoveisProdutor() != null) {
        		for(ImovelProdutorDto imovelProdutorDto : objDto.getData().getImoveisProdutor()) {	
        			ImovelProdutorResponseDto imovelProdutorResponseDto = integrationSaveAllImovelProdutor(imovelProdutorDto);	
        			response.getData().getImoveisProdutor().add(imovelProdutorResponseDto);
		            
		            if(imovelProdutorResponseDto.getIntegrated()) {
		            	sucessoIntegracao = true;
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
    
    private ImovelProdutorResponseDto integrationSaveAllImovelProdutor(ImovelProdutorDto imovelProdutorDto) {

        ImovelProdutorResponseDto imovelProdutorResponseDto = null;
    	try {
            ImovelProdutor imovelProdutor = imovelService.integrationSaveAll(imovelProdutorDto);
            imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                    imovelProdutor,
                    true,
                    "O imóvel produtor integrado com sucesso!"
            );
            
        }
        catch(ConstraintViolationException e) {
            imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                    imovelProdutorDto,
                    false,
                    "Não foi possível integrar o imóvel produtor. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                        imovelProdutorDto,
                        false,
                        "Não foi possível integrar o imóvel produtor. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                        imovelProdutorDto,
                        false,
                        "Não foi possível integrar o imóvel produtor!",
                        e
                );
            }
        }
        catch(DataIntegrityViolationException e) {

            String mensagem = "Não foi possível gravar o imóvel produtor.";

            if(e.getMessage().toUpperCase().contains("KEY_MATRICULA")) {
                mensagem += " Obs.: Já existe imóvel cadastrado com a matrícula informada.";
            }

            imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                    imovelProdutorDto,
                    false,
                    mensagem,
                    e
            );
        }
        catch (Exception e) {
            imovelProdutorResponseDto = ImovelProdutorResponseDto.construir(
                    imovelProdutorDto,
                    false,
                    "Não foi possível integrar o imóvel!",
                    e
            );
        }
    	return imovelProdutorResponseDto;
    }
    
    private ImovelResponseDto integrationSaveAllImovel(ImovelDto imovelDto) {

        ImovelResponseDto imovelResponseDto = null;
    	try {
            Imovel imovel = imovelService.integrationSaveAll(imovelDto);
            imovelResponseDto = ImovelResponseDto.construir(
                    imovel,
                    true,
                    "O imóvel foi integrado com sucesso!"
            );
            
        }
        catch(ConstraintViolationException e) {
            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    "Não foi possível integrar o imóvel. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                imovelResponseDto = ImovelResponseDto.construir(
                        imovelDto,
                        false,
                        "Não foi possível integrar o imóvel. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                imovelResponseDto = ImovelResponseDto.construir(
                        imovelDto,
                        false,
                        "Não foi possível integrar o imóvel!",
                        e
                );
            }
        }
        catch(DataIntegrityViolationException e) {

            String mensagem = "Não foi possível integrar o imóvel.";

            if(e.getMessage().toUpperCase().contains("KEY_MATRICULA")) {
                mensagem += " Obs.: Já existe imóvel cadastrado com a matrícula informada.";
            }

            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    mensagem,
                    e
            );
        }
        catch (Exception e) {
            imovelResponseDto = ImovelResponseDto.construir(
                    imovelDto,
                    false,
                    "Não foi possível integrar o imóvel!",
                    e
            );
        }
    	return imovelResponseDto;
    }

    @GetMapping("/buscar-por-produtor/{codigoProdutor}")
    public ResponseEntity<List<ImovelZoomDto>> buscarPorCodigoProdutor(@PathVariable(name="codigoProdutor") String codigoProdutor){
        List<ImovelProdutor> imovelProdutores = imovelService.buscarPorCodigoProdutor(codigoProdutor);
        List<ImovelZoomDto> imovelZoomDtos = ImovelZoomDto.construir(imovelProdutores);
        return ResponseEntity.ok(imovelZoomDtos);
    }

    @GetMapping("/validar-imovel-entrada/{matriculaImovel}/{codigoProdutor}/{codigoEstabelecimento}")
    public ResponseEntity<?> validarImovelEntrada(
            @PathVariable(name="matriculaImovel") Long matriculaImovel,
            @PathVariable(name="codigoProdutor") String codigoProdutor,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento){
    	imovelService.validarImovelEntrada(matriculaImovel, codigoProdutor, codigoEstabelecimento);
    	return ResponseEntity.ok(imovelService.buscarCadProPor(codigoProdutor, matriculaImovel));
    }
    
    @GetMapping("/pesquisar-por/produtor/{codigoProdutor}/matricula-ou-descricao/sem-ser-transferencia")
    public ResponseEntity<List<ImovelProdutorSimplesDto>> pesquisarPorCodigoProdutorEMatriculaOuDescricaoSemSerTransferencia(
    		@PathVariable(name="codigoProdutor") String codigoProdutor,
    		@RequestParam(name = "filtro", required = false) String filtro,
    		@RequestParam(name = "situacao", required = false, defaultValue = "ATIVO") Situacao situacao,
    		@PageableDefault(page = 0, size = 10, sort = "imovel.descricao", direction = Sort.Direction.ASC) Pageable pageable
    		) {
    	Page<ImovelProdutor> imovelProdutorPage = imovelService.pesquisarPorCodigoProdutorEMatriculaOuDescricaoSemSerTransferencia(codigoProdutor, filtro, situacao, pageable);
    	List<ImovelProdutorSimplesDto> response = ImovelProdutorSimplesDto.construir(imovelProdutorPage.getContent());
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/pesquisar-imovel-filter")
    public ResponseEntity<Page<Imovel>> getImovelFilter( 
    		Pageable pageable, 
    		ImoveisProdutorFilter filter){    	
    	return ResponseEntity.ok(imovelService.getImovelFilter(filter, pageable));
    }
    
    @GetMapping("/pesquisar-produtores-imovel/{matricula}")
    public ResponseEntity<List<ProdutorImovelDto>> getProdutorByImovel(@PathVariable(name="matricula") Long matricula){
    	return ResponseEntity.ok(imovelService.getProdutorByImovel(matricula));
    }
}

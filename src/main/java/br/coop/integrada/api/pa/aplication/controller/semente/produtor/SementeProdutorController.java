package br.coop.integrada.api.pa.aplication.controller.semente.produtor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.modelDto.semente.LaudoInspecaoItemDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.ZoomCampoSementeDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.GrupoSementeProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.GrupoSementeProdutorIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.GrupoSementeProdutorIntegrationResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.GrupoSementeProdutorResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeCampoDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeCampoProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeCampoProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeCampoProdutorSimplesResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeLaudoInspecaoDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeLaudoInspecaoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeLaudoInspecaoSimplesResponseDto;
import br.coop.integrada.api.pa.domain.service.semente.produtor.SementeProdutorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/semente/produtor")
@Tag(name = "Semente Produtor", description = "Campo semente produtor")
public class SementeProdutorController {

    @Autowired
    private SementeProdutorService sementeProdutorService;

    @PutMapping("/save-campo")
    public ResponseEntity<GrupoSementeProdutorResponseDto> salvar(@RequestBody GrupoSementeProdutorDto objDto){
        GrupoSementeProdutorResponseDto responseDto = null;

        try {
        	
        	if(objDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {	
        		
        		SementeCampo sementeCampo = sementeProdutorService.findSementeCampoByIdUnico(objDto.getIdUnico());
        		if(sementeCampo != null) {
					try {
						sementeProdutorService.delete(sementeCampo);
						responseDto = GrupoSementeProdutorResponseDto.construir(objDto, true, "Item excluído com sucesso!");					
												
					} catch (Exception e) {
						sementeCampo.setDataInativacao(new Date());
						sementeCampo.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						sementeProdutorService.save(sementeCampo);
						
						responseDto = GrupoSementeProdutorResponseDto.construir(objDto, true, "Item inativado com sucesso!");					
					
					}
				}else {
					responseDto = GrupoSementeProdutorResponseDto.construir(objDto, true, "Obs: O grupo de produto não foi encontrado durante a exclusão");					
					
				}
        		
        	}else {
        		
	            sementeProdutorService.integrationSave(objDto);
	            responseDto = GrupoSementeProdutorResponseDto.construir(
	            		objDto,
	                    true,
	                    "Integração realizada com sucesso!"
	            );
	            return ResponseEntity.ok(responseDto);
        	}
        }
        catch(ConstraintViolationException e) {
        	responseDto = GrupoSementeProdutorResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de campo. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível gravar a estrutura de campo.";

            if(e.getMessage().toUpperCase().contains("KEY_SEMENTE_CAMPO")) {
                mensagem += " Obs.: Já existe estrutura de campo cadastrada com o {safra}, {ordemCampo}, {codigoEstabelecimetno}, {codigoFamilia} e {classeCodigo} informado.";
            }

            responseDto = GrupoSementeProdutorResponseDto.construir(
                    objDto,
                    false,
                    mensagem,
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
            	responseDto = GrupoSementeProdutorResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de campo. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
            	responseDto = GrupoSementeProdutorResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de campo!",
                        e
                );
            }
        }
        catch (Exception e) {
        	responseDto = GrupoSementeProdutorResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de campo!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping("/save-produtor")
    public ResponseEntity<SementeCampoProdutorSimplesResponseDto> salvar(@RequestBody SementeCampoProdutorSimplesDto objDto){
    	SementeCampoProdutorSimplesResponseDto responseDto = null;

        try {
        	
        	if(objDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {	
        		
        		SementeCampoProdutor sementeCampoProdutor = sementeProdutorService.findSementeCampoProdutorByIdUnico(objDto.getIdUnico());
        		if(sementeCampoProdutor != null) {
					try {
						sementeProdutorService.delete(sementeCampoProdutor);
						responseDto = SementeCampoProdutorSimplesResponseDto.construir(objDto, true, "Item excluído com sucesso!");					
												
					} catch (Exception e) {
						sementeCampoProdutor.setDataInativacao(new Date());
						sementeCampoProdutor.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						sementeProdutorService.save(sementeCampoProdutor);
						
						responseDto = SementeCampoProdutorSimplesResponseDto.construir(objDto, true, "Item inativado com sucesso!");					
					
					}
				}else {
					responseDto = SementeCampoProdutorSimplesResponseDto.construir(objDto, true, "Obs: O campo do produtor não foi encontrado durante a exclusão");				
					
				}
        		
        	}else {        		
	            sementeProdutorService.integrationSave(objDto);
	            responseDto = SementeCampoProdutorSimplesResponseDto.construir(objDto,true,"Integração realizada com sucesso!");
	            return ResponseEntity.ok(responseDto);
        	}
        }
        catch(ConstraintViolationException e) {
        	responseDto = SementeCampoProdutorSimplesResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de campo. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível gravar a estrutura de produtor.";

            if(e.getMessage().toUpperCase().contains("KEY_SEMENTE_CAMPO_PRODUTOR")) {
                mensagem += " Obs.: Já existe produtor cadastrado com o {safra}, {ordemCampo}, {codigoProdutor}, {codigoEstabelecimetno}, {codigoFamilia} e {classeCodigo} informado.";
            }
            responseDto = SementeCampoProdutorSimplesResponseDto.construir( objDto, false,  mensagem, e );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
            	responseDto = SementeCampoProdutorSimplesResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de produtor. Por favor, analisar a lista de validações do produtor!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
            	responseDto = SementeCampoProdutorSimplesResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de produtor!",
                        e
                );
            }
        }
        catch (Exception e) {
        	responseDto = SementeCampoProdutorSimplesResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de campo!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping("/save-laudo-inspecao")
    public ResponseEntity<SementeLaudoInspecaoSimplesResponseDto> salvar(@RequestBody SementeLaudoInspecaoSimplesDto objDto){
    	SementeLaudoInspecaoSimplesResponseDto responseDto = null;

        try {
        	
        	if(objDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {	
        		
        		SementeLaudoInspecao sementeLaudoInspecao = sementeProdutorService.findSementeLaudoInspecaoByIdUnico(objDto.getIdUnico());
        		if(sementeLaudoInspecao != null) {
					try {
						sementeProdutorService.delete(sementeLaudoInspecao);
						responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(objDto, true, "Item excluído com sucesso!");					
												
					} catch (Exception e) {
						sementeLaudoInspecao.setDataInativacao(new Date());
						sementeLaudoInspecao.setStatusIntegracao(StatusIntegracao.INTEGRADO);
						sementeProdutorService.save(sementeLaudoInspecao);
						
						responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(objDto, true, "Item inativado com sucesso!");					
					
					}
				}else {
					responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(objDto, true, "Obs: O laudo foi encontrado durante a exclusão");				
					
				}
        		
        	}else {
        		
	            sementeProdutorService.integrationSave(objDto);
	            responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
	            		objDto,
	                    true,
	                    "Integração realizada com sucesso!"
	            );
	            return ResponseEntity.ok(responseDto);
        	}
        }
        catch(ConstraintViolationException e) {
        	responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de campo. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível gravar a estrutura de laudo.";

            if(e.getMessage().toUpperCase().contains("KEY_SEMENTE_LAUDO_INSPECAO")) {
                mensagem += " Obs.: Já existe laudo cadastrado com o {safra}, {codigoEstabelecimetno}, {numeroLaudo}, {ordemCampo} e {codigoFamilia} informado.";
            }

            responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
                    objDto,
                    false,
                    mensagem,
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
            	responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de laudo. Por favor, analisar a lista de validações do laudo!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
            	responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
                        objDto,
                        false,
                        "Não foi possível gravar a estrutura de laudo!",
                        e
                );
            }
        }
        catch (Exception e) {
        	responseDto = SementeLaudoInspecaoSimplesResponseDto.construir(
                    objDto,
                    false,
                    "Não foi possível gravar a estrutura de laudo!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }

    @PutMapping("/save-all")
    public ResponseEntity<List<GrupoSementeProdutorResponseDto>> salvarVarios(@RequestBody List<GrupoSementeProdutorDto> objDtos){
        List<GrupoSementeProdutorResponseDto> response = new ArrayList<>();
        Boolean falhaIntegracao = false;

        for(GrupoSementeProdutorDto objDto : objDtos) {
            GrupoSementeProdutorResponseDto grupoSementeProdutorResponseDto = null;

            try {
                grupoSementeProdutorResponseDto = salvar(objDto).getBody();

                if(grupoSementeProdutorResponseDto != null && !grupoSementeProdutorResponseDto.getIntegrated()) {
                    falhaIntegracao = true;
                }
            }
            catch (Exception e) {
                falhaIntegracao = true;
                grupoSementeProdutorResponseDto = GrupoSementeProdutorResponseDto.construir(
                        objDto,
                        false,
                        "A estrutura de campo não foi gravada!",
                        e
                );
            }

            response.add(grupoSementeProdutorResponseDto);
        }

        if(falhaIntegracao) {
            return ResponseEntity.badRequest().body(response);
        }
        else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/buscar-semente-campo-por/{safra}/{ordemCampo}/{codigoEstabelecimento}/{codigoFamilia}/{codigoClasse}")
    public ResponseEntity<SementeCampoDto> buscarSementeCampoPor(
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="ordemCampo") Integer ordemCampo,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="codigoFamilia") String codigoFamilia,
            @PathVariable(name="codigoClasse") Long codigoClasse
    ){
        SementeCampo sementeCampo = sementeProdutorService.buscarSementeCampoPor(safra, ordemCampo, codigoEstabelecimento, codigoFamilia, codigoClasse);
        SementeCampoDto sementeCampoDto = SementeCampoDto.construir(sementeCampo);
        return ResponseEntity.ok(sementeCampoDto);
    }

    @GetMapping("/buscar-semente-por/{safra}/{codigoEstabelecimento}/{codigoGrupoProduto}")
    public ResponseEntity<ZoomCampoSementeDto> buscarSementeLaudoInspecaoPor(
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="codigoGrupoProduto") String codigoGrupoProduto
    ){
        ZoomCampoSementeDto zoomCampoSementeDto = sementeProdutorService.buscarSementeCampoPor(safra, codigoEstabelecimento, codigoGrupoProduto);
        return ResponseEntity.ok(zoomCampoSementeDto);
    }

    @GetMapping("/buscar-semente-campo-produtor-por/{safra}/{ordemCampo}/{codigoProdutor}/{codigoEstabelecimento}/{codigoFamilia}/{codigoClasse}")
    public ResponseEntity<SementeCampoProdutorDto> buscarSementeCampoProdutorPor(
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="ordemCampo") Integer ordemCampo,
            @PathVariable(name="codigoProdutor") String codigoProdutor,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="codigoFamilia") String codigoFamilia,
            @PathVariable(name="codigoClasse") Long codigoClasse
    ){
        SementeCampoProdutor sementeCampoProdutor = sementeProdutorService.buscarSementeCampoProdutorPor(safra, ordemCampo, codigoProdutor, codigoEstabelecimento, codigoFamilia, codigoClasse);
        SementeCampoProdutorDto sementeCampoProdutorDto = SementeCampoProdutorDto.construir(sementeCampoProdutor);
        return ResponseEntity.ok(sementeCampoProdutorDto);
    }

    @GetMapping("/buscar-semente-laudo-inspecao-por/{safra}/{ordemCampo}/{numeroLaudo}/{codigoEstabelecimento}/{codigoFamilia}")
    public ResponseEntity<SementeLaudoInspecaoDto> buscarSementeLaudoInspecaoPor(
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="ordemCampo") Integer ordemCampo,
            @PathVariable(name="numeroLaudo") Long numeroLaudo,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="codigoFamilia") String codigoFamilia
    ){
        SementeLaudoInspecao sementeLaudoInspecao = sementeProdutorService.buscarSementeLaudoInspecaoPor(safra, codigoEstabelecimento, numeroLaudo, ordemCampo, codigoFamilia);
        SementeLaudoInspecaoDto sementeLaudoInspecaoDto = SementeLaudoInspecaoDto.construir(sementeLaudoInspecao);
        return ResponseEntity.ok(sementeLaudoInspecaoDto);
    }
    
    @GetMapping("/buscar-laudo-inspecao-por/codigo-estabelecimento/{codigoEstabelecimento}/safra/{safra}/codigo-grupo-produto/{codigoGrupoProduto}/ordem-campo/{ordemCampo}/numero-laudo/{nroLaudo}")
    public ResponseEntity<SementeLaudoInspecaoDto> buscarLaudoInspecaoPor(
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="nroLaudo") Long numeroLaudo,
            @PathVariable(name="ordemCampo") Integer ordemCampo,
            @PathVariable(name="codigoGrupoProduto") String codigoFamilia,
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento){
        
    	SementeLaudoInspecao sementeLaudoInspecao = sementeProdutorService.buscarSementeLaudoInspecaoPor(safra, codigoEstabelecimento, numeroLaudo, ordemCampo, codigoFamilia);
    	SementeLaudoInspecaoDto laudoInspecaoDto = SementeLaudoInspecaoDto.construir(sementeLaudoInspecao);
        return ResponseEntity.ok(laudoInspecaoDto);
    }

    @GetMapping("/buscar-laudo-inspecao-item-classe/{codigoEstabelecimento}/{safra}/{codigoFamilia}/{codigoClasse}/{numeroLaudo}")
    public ResponseEntity<List<LaudoInspecaoItemDto>> buscarLaudoInspecaoClasse(
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="safra") Integer safra,
            @PathVariable(name="codigoFamilia") String codigoFamilia,
            @PathVariable(name="codigoClasse") Long codigoClasse,
            @PathVariable(name="numeroLaudo") Long numeroLaudo){      
        return ResponseEntity.ok(sementeProdutorService.buscarLaudoCampoSementePorClasse(codigoEstabelecimento, safra, codigoFamilia, codigoClasse, numeroLaudo));
    }

    @PostMapping("/integration/save-all")
    public ResponseEntity<GrupoSementeProdutorIntegrationResponseDto> integrationSaveAll(@RequestBody GrupoSementeProdutorIntegrationDto objDto){
    	GrupoSementeProdutorIntegrationResponseDto responseDto = GrupoSementeProdutorIntegrationResponseDto.construir();
        Boolean sucessoIntegracao = false;

        if(objDto.getGrupoSementesProdutores() != null) {
	        for(GrupoSementeProdutorDto item : objDto.getGrupoSementesProdutores()) {
	            GrupoSementeProdutorResponseDto dto = salvar(item).getBody();
	            responseDto.getGrupoSementesProdutores().add(dto);
	            if(dto != null && dto.getIntegrated()) {
	            	sucessoIntegracao = true;
	            }
	        }
        }
        
        if(objDto.getProdutores() != null) {
	        for(SementeCampoProdutorSimplesDto item : objDto.getProdutores()) {
	        	SementeCampoProdutorSimplesResponseDto dto = salvar(item).getBody();
	        	responseDto.getProdutores().add(dto);
	            if(dto != null && dto.getIntegrated()) {
	            	sucessoIntegracao = true;
	            }
	        }
        }
        
        if(objDto.getLaudos() != null) {
	        for(SementeLaudoInspecaoSimplesDto item : objDto.getLaudos()) {
	        	SementeLaudoInspecaoSimplesResponseDto dto = salvar(item).getBody();
	        	responseDto.getLaudos().add(dto);
	            if(dto != null && dto.getIntegrated()) {
	            	sucessoIntegracao = true;
	            }
	        }
        }

        if(!sucessoIntegracao) {
			return ResponseEntity.badRequest().body(responseDto);
		}
		else {
			return ResponseEntity.ok(responseDto);
		}
    }
}

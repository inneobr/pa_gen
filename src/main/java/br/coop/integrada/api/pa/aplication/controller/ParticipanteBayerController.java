package br.coop.integrada.api.pa.aplication.controller;

import static br.coop.integrada.api.pa.domain.enums.OrigemEnum.IMPORTACAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.participante.bayer.ParticipanteBayerDto;
import br.coop.integrada.api.pa.domain.modelDto.participante.bayer.ParticipanteBayerResponseDto;
import br.coop.integrada.api.pa.domain.service.ParticipanteBayerService;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/participante/bayer")
@Tag(name = "Participantes Bayer", description = "Participantes de Royalties da Bayer")
public class ParticipanteBayerController {

    @Autowired
    private ParticipanteBayerService participanteBayerService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/salvar/manual")
    public ResponseEntity<ParticipanteBayerResponseDto> salvar(@RequestBody ParticipanteBayerDto objDto) {
        ParticipanteBayerResponseDto responseDto = null;

        try {
            participanteBayerService.salvarManual(objDto);
            responseDto = ParticipanteBayerResponseDto.construir(objDto.getCnpj(), true, "O participante bayer foi gravado com sucesso!");
            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
            responseDto = ParticipanteBayerResponseDto.construir(
                    objDto.getCnpj(),
                    false,
                    "Não foi possível gravar o imóvel. Por favor, analisar a lista de validações dos campos!",
                    e
                    );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                responseDto = ParticipanteBayerResponseDto.construir(
                        objDto.getCnpj(),
                        false,
                        "Não foi possível gravar o imóvel. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                        );
            }
            else {
                responseDto = ParticipanteBayerResponseDto.construir(
                        objDto.getCnpj(),
                        false,
                        "Não foi possível gravar o participante bayer!",
                        e
                        );
            }
        }
        catch(DataIntegrityViolationException e) {
            String mensagem = "Não foi possível gravar o participante bayer.";

            if(e.getMessage().toUpperCase().contains("KEY_MATRICULA")) {
                mensagem += " Obs.: Já existe imóvel cadastrado com a matrícula informada.";
            }

            responseDto = ParticipanteBayerResponseDto.construir(
                    objDto.getCnpj(),
                    false,
                    mensagem,
                    e
                    );
        }
        catch (Exception e) {
            responseDto = ParticipanteBayerResponseDto.construir(
                    objDto.getCnpj(),
                    false,
                    "Não foi possível gravar o participante bayer!",
                    e
                    );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }

    @PostMapping("/salvar/importacao")
    public ResponseEntity<String> salvarVarios(@RequestBody List<ParticipanteBayerDto> objDtos) {
        
    	try {
	        Usuario usuario = usuarioService.getUsuarioLogado();
	        List<ParticipanteBayer> participantesBayer = participanteBayerService.converterDto(objDtos, IMPORTACAO, usuario.getUsername());
	        
	        //Realiza a exclusão da carga Anterior
	        participanteBayerService.excluirPorOrigem(IMPORTACAO);
	        participanteBayerService.excluirOrigemManualQueEstaNaListaDeImportacao(participantesBayer);
	        
	        //Aqui realiza a salva da lista de participantes, atualiza o status do produtor e o histórico do produto
	        participanteBayerService.salvarLista(participantesBayer);        
	        
	        return ResponseEntity.ok("A lista de participantes bayer foi gravada com sucesso!");
	        
    	}
    	catch (Exception e) {
    		return ResponseEntity.badRequest().body("Não foi possível carregar a lista de participantes bayer." + e.getMessage());
		}
        
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<ParticipanteBayerResponseDto> excluir(@PathVariable(name="cnpj") String cnpj) {
        ParticipanteBayerResponseDto responseDto = null;

        try {
            participanteBayerService.excluirPorCnpj(cnpj);
            responseDto = ParticipanteBayerResponseDto.construir(cnpj, true, "Participante Bayer excluído com sucesso!");
            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e) {
            responseDto = ParticipanteBayerResponseDto.construir(cnpj, false, "Não foi possível excluir o Participante Bayer!", e);
            ResponseEntity.ok(responseDto);
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @DeleteMapping("/excluir-todos")
    public ResponseEntity<ParticipanteBayerResponseDto> excluirTodos() {
        ParticipanteBayerResponseDto responseDto = null;

        try {
            participanteBayerService.excluirTodos();
            responseDto = ParticipanteBayerResponseDto.construir("", true, "Todos os participantes Bayer foram excluídos com sucesso!");
            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e) {
            responseDto = ParticipanteBayerResponseDto.construir("", false, "Não foi possível excluir todos os Participantes Bayer!", e);
            //ResponseEntity.ok(responseDto);
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @GetMapping
    public ResponseEntity<Page<ParticipanteBayerDto>> listar(
            @PageableDefault (page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "filtro", defaultValue = "") String filtro ) {
        Page<ParticipanteBayerDto> responseDto = participanteBayerService.listar(pageable, filtro);
        return ResponseEntity.ok(responseDto);
    }
    
    @GetMapping("/validar-produtor-bayer")
    public ResponseEntity<String> validarProdutorBayer() {
        ParticipanteBayerResponseDto responseDto = null;

        try {
            List<String> mensagens = participanteBayerService.validarProdutorBayer();
            
            String mensagem=null;
            if(!mensagens.isEmpty()) {
            	mensagem = String.join("\n", mensagens);
            }

            return ResponseEntity.ok(mensagem);
        }
        catch (Exception e) {
        	return ResponseEntity.badRequest().body(e.getMessage());
        }

        
    }

}

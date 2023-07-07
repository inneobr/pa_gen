package br.coop.integrada.api.pa.aplication.controller.historicos;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/historico/parametros-estabelecimento")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Historico", description = "histórico de parametros de estabelecimento.")
public class HistoricoParametroEstabelecimentoController {
    
    @Autowired
    private ParametroEstabelecimentoService parametroEstabelecimentoService;
    
    @GetMapping("/{codEstabelecimento}")
    public ResponseEntity<?> buscarTodosPage(
            @PathVariable(name="codEstabelecimento") String codEstabelecimento,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        try{
            return ResponseEntity.ok().body(parametroEstabelecimentoService.BuscarHistoricoParametroEstabelecimentoPage(codEstabelecimento, pageable));
        }catch(ConstraintViolationException e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe histórico de pesagem.", e), 
                    HttpStatus.BAD_REQUEST);        
        }
    }
    
    @GetMapping("/lista/{idEstabelecimento}")
    public ResponseEntity<?> buscarTodos(@PathVariable(name="idEstabelecimento") Long idEstabelecimento){
        try{
            return ResponseEntity.ok().body(parametroEstabelecimentoService.BuscarHistoricoParametroEstabelecimentoList(idEstabelecimento));
        }catch(ConstraintViolationException e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(BAD_REQUEST.value(), "Não existe histórico de pesagem.", e), 
                    HttpStatus.BAD_REQUEST);        
        }
    }

}

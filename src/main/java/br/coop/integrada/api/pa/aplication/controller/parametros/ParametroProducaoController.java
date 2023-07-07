package br.coop.integrada.api.pa.aplication.controller.parametros;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroProducao;
import br.coop.integrada.api.pa.domain.modelDto.producao.ParametroProducaoDto;
import br.coop.integrada.api.pa.domain.modelDto.producao.ParametroProducaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.producao.ParametroProducaoResponseDto;
import br.coop.integrada.api.pa.domain.service.pesagem.ParametroProducaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/parametros/producao")
@Tag(name = "Parâmetros de Produção", description = "Manutenção dos parâmetros de produção")
public class ParametroProducaoController {

    @Autowired
    private ParametroProducaoService parametroProducaoService;

    @GetMapping
    public ResponseEntity<ParametroProducao> buscar(){
        ParametroProducao parametroProducao = parametroProducaoService.buscar();
        return ResponseEntity.ok().body(parametroProducao);
    }

    @PutMapping
    public ResponseEntity<ParametroProducaoResponseDto> salvar(@RequestBody ParametroProducaoDto parametroProducaoDto){
        ParametroProducaoResponseDto responseDto = null;

        try {
            parametroProducaoService.salvar(parametroProducaoDto);
            responseDto = ParametroProducaoResponseDto.construir(true, "Parâmetros de produção atualizado com sucesso!");
            return ResponseEntity.ok(responseDto);
        }
        catch(ConstraintViolationException e) {
            responseDto = ParametroProducaoResponseDto.construir(
                    true,
                    "Não foi possível atualizar os parâmetros de produção. Por favor, analisar a lista de validações dos campos!",
                    e
            );
        }
        catch (TransactionSystemException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);

            if (rootCause instanceof ConstraintViolationException) {
                responseDto = ParametroProducaoResponseDto.construir(
                        false,
                        "Não foi possível atualizar os parâmetros de produção. Por favor, analisar a lista de validações dos campos!",
                        (ConstraintViolationException) rootCause
                );
            }
            else {
                responseDto = ParametroProducaoResponseDto.construir(
                        false,
                        "Não foi possivel atualizar os parâmetros de produção!",
                        e
                );
            }
        }
        catch (Exception e) {
            responseDto = ParametroProducaoResponseDto.construir(
                    false,
                    "Não foi possivel atualizar os parâmetros de produção!",
                    e
            );
        }

        return ResponseEntity.badRequest().body(responseDto);
    }
    
    @PutMapping("/integration/save-all")
    public ResponseEntity<ParametroProducaoResponseDto> integrationSave(@RequestBody ParametroProducaoIntegrationDto objDto) {
    	ParametroProducaoDto parametroProducaoDto = objDto.getParametroProducao();
    	return salvar(parametroProducaoDto);
    }
}

package br.coop.integrada.api.pa.aplication.controller.semente;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeClasseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeClasseResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoResponseDto;
import br.coop.integrada.api.pa.domain.service.semente.SementeClasseService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/semente/classe")
@Tag(name = "Classe semente")
public class SementeClasseController {

    @Autowired
    private SementeClasseService sementeClasseService;

    @PutMapping
    public ResponseEntity<SementeClasseResponseDto> salvar(@Valid @RequestBody SementeClasseDto objDto){
        SementeClasseResponseDto response = null;

        try {
            SementeClasse sementeClasse = sementeClasseService.salvar(objDto);
            response = SementeClasseResponseDto.construir(sementeClasse, true, "A classe foi gravada com sucesso!");
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            response = SementeClasseResponseDto.construir(objDto, false, "A classe não foi gravada!", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<SementeClasseResponseDto> inativar(@PathVariable(name="id") Long id){
        try {
            SementeClasse sementeClasse = sementeClasseService.inativar(id);
            SementeClasseResponseDto sementeClasseResponseDto = SementeClasseResponseDto.construir(
                    sementeClasse,
                    true,
                    "A classe foi inativada com sucesso!"
            );

            return ResponseEntity.ok(sementeClasseResponseDto);
        }
        catch (Exception e) {
            SementeClasseDto sementeClasseDto = new SementeClasseDto();
            sementeClasseDto.setId(id);
            SementeClasseResponseDto sementeClasseResponseDto = SementeClasseResponseDto.construir(
                    sementeClasseDto,
                    false,
                    "Não foi possível inativar a classe!",
                    e.getMessage()
            );

            return ResponseEntity.badRequest().body(sementeClasseResponseDto);
        }
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<SementeClasseResponseDto> ativar(@PathVariable(name="id") Long id){
        try {
            SementeClasse sementeClasse = sementeClasseService.ativar(id);
            SementeClasseResponseDto sementeClasseResponseDto = SementeClasseResponseDto.construir(
                    sementeClasse,
                    true,
                    "A classe foi ativada com sucesso!"
            );

            return ResponseEntity.ok(sementeClasseResponseDto);
        }
        catch (Exception e) {
            SementeClasseDto sementeClasseDto = new SementeClasseDto();
            sementeClasseDto.setId(id);
            SementeClasseResponseDto sementeClasseResponseDto = SementeClasseResponseDto.construir(
                    sementeClasseDto,
                    false,
                    "Não foi possível ativar a classe!",
                    e.getMessage()
            );

            return ResponseEntity.badRequest().body(sementeClasseResponseDto);
        }
    }

    @GetMapping
    public ResponseEntity<List<SementeClasseDto>> listarTodasClasses() {
        List<SementeClasse> sementeClasses = sementeClasseService.listarTodasClasses();
        List<SementeClasseDto> sementeClasseDtos = SementeClasseDto.construir(sementeClasses);
        return ResponseEntity.ok(sementeClasseDtos);
    }

    @GetMapping("/buscar-com-paginacao")
    public ResponseEntity<Page<SementeClasseDto>> buscarComPaginacao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "filtro", defaultValue = "") String filtro,
            @RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao
    ) {
        Page<SementeClasseDto> sementeClasseDtoPage = sementeClasseService.buscarComPaginacao(pageable, filtro, situacao);
        return ResponseEntity.ok(sementeClasseDtoPage);
    }

    @GetMapping("/buscar-por/codigo-ou-descricao")
    public ResponseEntity<List<SementeClasseDto>> buscarPorCodigoOuDescricao(
            @RequestParam(name = "filtro", defaultValue = "") String filtro,
            @RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao,
            @PageableDefault(page = 0, size = 15, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<SementeClasse> sementeClassePage = sementeClasseService.buscarPorCodigoOuDescricao(filtro, situacao, pageable);
        List<SementeClasseDto> sementeClasseDtos = SementeClasseDto.construir(sementeClassePage.getContent());
        return ResponseEntity.ok(sementeClasseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SementeClasseDto> buscarPorCodigo(@PathVariable(name="id") Long id) {
        SementeClasse sementeClasse = sementeClasseService.buscarPorId(id);

        if(sementeClasse == null) {
            throw new NullPointerException("Não foi encontrado classe com o código " + id);
        }

        SementeClasseDto sementeClasseDto = SementeClasseDto.construir(sementeClasse);
        return ResponseEntity.ok(sementeClasseDto);
    }
    
    @PostMapping("/verificarToleranciaRecebimentoSemente")
    public ResponseEntity<VerificaTotleranciaRecebimentoResponseDto> verificarToleranciaRecebimentoSemente(@RequestBody VerificaTotleranciaRecebimentoDto input){
    	try {
    		VerificaTotleranciaRecebimentoResponseDto output = sementeClasseService.verificarToleranciaRecebimentoSemente(input);
    		return ResponseEntity.ok(output);
    	}
    	catch (Exception e) {
    		return ResponseEntity.badRequest().body(new VerificaTotleranciaRecebimentoResponseDto( e.getMessage() ));
		}
	}
}

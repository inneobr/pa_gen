package br.coop.integrada.api.pa.aplication.controller;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.modelDto.unidade.federacao.UnidadeFederacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.unidade.federacao.UnidadeFederacaoResponseDto;
import br.coop.integrada.api.pa.domain.service.UnidadeFederacaoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/unidade/federacao")
@Tag(name = "Unidade federação", description = "Cadastrar os estados com seus respectivos códigos do IBGE")
public class UnidadeFederacaoController {

    @Autowired
    private UnidadeFederacaoService unidadeFederacaoService;

    @PutMapping
    public ResponseEntity<UnidadeFederacaoResponseDto> salvar(@Valid @RequestBody UnidadeFederacaoDto objDto){
        UnidadeFederacaoResponseDto response = null;

        try {
            UnidadeFederacao unidadeFederacao = unidadeFederacaoService.salvar(objDto);
            response = UnidadeFederacaoResponseDto.construir(unidadeFederacao, true, "A unidade federação foi gravada com sucesso!");
            return ResponseEntity.ok(response);
        }
        catch(ConstraintViolationException e) {
            response = UnidadeFederacaoResponseDto.construir(objDto, false, "A unidade federação não foi gravada!", e);
            return ResponseEntity.badRequest().body(response);
        }
        catch (Exception e) {
            response = UnidadeFederacaoResponseDto.construir(objDto, false, "A unidade federação não foi gravada!", e);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/save-all")
    public ResponseEntity<List<UnidadeFederacaoResponseDto>> saveAll(@RequestBody List<UnidadeFederacaoDto> objDtos){
        List<UnidadeFederacaoResponseDto> response = new ArrayList<>();

        for(UnidadeFederacaoDto unidadeFederacaoDto : objDtos) {
            try {
                UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = salvar(unidadeFederacaoDto).getBody();
                response.add(unidadeFederacaoResponseDto);
            }
            catch (Exception e) {
                UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = UnidadeFederacaoResponseDto.construir(
                        unidadeFederacaoDto,
                        false,
                        "A unidade federação não foi gravada!",
                        e
                );
                response.add(unidadeFederacaoResponseDto);
            }
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<UnidadeFederacaoResponseDto> inativar(@PathVariable(name="id") Long id){
        try {
            UnidadeFederacao unidadeFederacao = unidadeFederacaoService.inativar(id);
            UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = UnidadeFederacaoResponseDto.construir(
                    unidadeFederacao,
                    true,
                    "A unidade de federação foi inativada com sucesso!"
            );

            return ResponseEntity.ok(unidadeFederacaoResponseDto);
        }
        catch (Exception e) {
            UnidadeFederacaoDto unidadeFederacaoDto = new UnidadeFederacaoDto();
            unidadeFederacaoDto.setId(id);
            UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = UnidadeFederacaoResponseDto.construir(
                    unidadeFederacaoDto,
                    false,
                    "Não foi possível inativar a unidade de federação!",
                    e
            );

            return ResponseEntity.badRequest().body(unidadeFederacaoResponseDto);
        }
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<UnidadeFederacaoResponseDto> ativar(@PathVariable(name="id") Long id){
        try {
            UnidadeFederacao unidadeFederacao = unidadeFederacaoService.ativar(id);
            UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = UnidadeFederacaoResponseDto.construir(
                    unidadeFederacao,
                    true,
                    "A unidade de federação foi ativada com sucesso!"
            );

            return ResponseEntity.ok(unidadeFederacaoResponseDto);
        }
        catch (Exception e) {
            UnidadeFederacaoDto unidadeFederacaoDto = new UnidadeFederacaoDto();
            unidadeFederacaoDto.setId(id);
            UnidadeFederacaoResponseDto unidadeFederacaoResponseDto = UnidadeFederacaoResponseDto.construir(
                    unidadeFederacaoDto,
                    false,
                    "Não foi possível ativar a unidade de federação!",
                    e
            );

            return ResponseEntity.badRequest().body(unidadeFederacaoResponseDto);
        }
    }

    @GetMapping("/buscar-por-codigo-ibge/{codigoIbge}")
    public ResponseEntity<UnidadeFederacaoDto> buscarPorCodigoIbbge(
            @PathVariable(name="codigoIbge") String codigoIbge
    ) {
        UnidadeFederacao unidadeFederacao = unidadeFederacaoService.buscarPorCodigoIbge(codigoIbge);

        if(unidadeFederacao == null) {
            throw new NullPointerException("Não foi encontrado unidade de federação com o código " + codigoIbge);
        }

        UnidadeFederacaoDto unidadeFederacaoDto = UnidadeFederacaoDto.construir(unidadeFederacao);
        return ResponseEntity.ok(unidadeFederacaoDto);
    }

    @GetMapping("/buscar-por-sigla-estado/{estado}")
    public ResponseEntity<UnidadeFederacaoDto> buscarPorEstado(
            @PathVariable(name="estado") String estado
    ) {
        UnidadeFederacao unidadeFederacao = unidadeFederacaoService.buscarPorEstado(estado);

        if(unidadeFederacao == null) {
            throw new NullPointerException("Não foi encontrado unidade de federação com a sigla " + estado);
        }

        UnidadeFederacaoDto unidadeFederacaoDto = UnidadeFederacaoDto.construir(unidadeFederacao);
        return ResponseEntity.ok(unidadeFederacaoDto);
    }

    @GetMapping()
    public ResponseEntity<Page<UnidadeFederacaoDto>> buscarComPaginacao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "filtro", defaultValue = "") String filter,
            @RequestParam(name = "situacao", defaultValue = "ATIVO") Situacao situacao

    ) {
        Page<UnidadeFederacaoDto> unidadeFederacaoDtoPage = unidadeFederacaoService.buscarComPaginacao(pageable, filter, situacao);
        return ResponseEntity.ok(unidadeFederacaoDtoPage);
    }
}

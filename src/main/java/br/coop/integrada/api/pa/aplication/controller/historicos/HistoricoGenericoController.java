package br.coop.integrada.api.pa.aplication.controller.historicos;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
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

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/historico/generico")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Histórico Genérico", description = "Consulta dos históricos genéricos")
public class HistoricoGenericoController {

    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @GetMapping("/{pagina}/{id}")
    public ResponseEntity<?> buscarHistorico(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "pagina") PaginaEnum pagina
    ){
        Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(id, pagina, pageable);
        return ResponseEntity.ok().body(historicoGenericoPage);
    }
}

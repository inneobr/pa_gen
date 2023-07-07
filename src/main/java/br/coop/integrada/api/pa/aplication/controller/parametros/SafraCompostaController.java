package br.coop.integrada.api.pa.aplication.controller.parametros;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.domain.enums.TipoSafraEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaComGrupoProdutoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaSimplesDto;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.parametros.SafraCompostaService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/parametros/safra/composta")
@Tag(name = "Safra Composta", description = "Parâmetro de safra composta")
public class SafraCompostaController {

    @Autowired
    private SafraCompostaService safraCompostaService;

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody SafraCompostaDto objDto){
    	SafraComposta safraComposta =safraCompostaService.cadastrar(objDto);
    	SafraCompostaDto safraCompostaDto = SafraCompostaDto.construir(safraComposta);      
    	
        if(safraComposta != null) {
        	try {
        		safraCompostaService.sincronizar(safraComposta.getId());
        		return ResponseEntity.ok(safraCompostaDto);
        	}catch (Exception e) {
        		return ResponseEntity.ok(safraCompostaDto);
			}        	
        }
		return ResponseEntity.badRequest().body("Não foi possível cadastrar a safra composta: " + objDto.getNomeSafra());
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody SafraCompostaDto objDto){
        SafraComposta safraComposta = safraCompostaService.atualizar(objDto);
        SafraCompostaDto safraCompostaDto = SafraCompostaDto.construir(safraComposta);
        safraCompostaService.sincronizar(safraComposta.getId());
        return ResponseEntity.ok(safraCompostaDto);
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativar(@PathVariable(name="id") Long id){
        safraCompostaService.inativar(id);
        safraCompostaService.sincronizar(id);
        return ResponseEntity.ok("Safra composta inativada com sucesso!");
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<?> ativar(@PathVariable(name="id") Long id){
        safraCompostaService.ativar(id);
        safraCompostaService.sincronizar(id);
        return ResponseEntity.ok("Safra composta ativada com sucesso!");
    }

    @GetMapping("/com-grupo-produto/{anoBase}")
    public ResponseEntity<?> buscarSafrasCompostasComGruposProdutos(
            @PathVariable(name = "anoBase") Integer anoBase
    ) {
        List<SafraCompostaComGrupoProdutoSimplesDto> objDtos = safraCompostaService.buscarSafrasCompostasComGruposProdutos(anoBase);
        return ResponseEntity.ok(objDtos);
    }

    @GetMapping("/estabelecimentos-ativos-sem-vinculos/{tipoSafra}/{idTipoProduto}")
    public ResponseEntity<?> buscarEstabelecimentosSemVinculos(
            @PathVariable(name = "tipoSafra") TipoSafraEnum tipoSafra,
            @PathVariable(name = "idTipoProduto") Long idTipoProduto
    ) {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarAtivoSemVinculoComSafraComposta(tipoSafra, idTipoProduto);
        List<EstabelecimentoSimplesDto> estabelecimentoSimplesDtos = EstabelecimentoSimplesDto.construir(estabelecimentos);
        return ResponseEntity.ok(estabelecimentoSimplesDtos);
    }

    @GetMapping("/com-paginacao/{situacao}/{anoBase}")
    public ResponseEntity<?> buscarComPaginacao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "anoBase") Integer anoBase,
            @PathVariable(name = "situacao") Situacao situacao,
            SafraCompostaFilter filter
    ) {
        Page<SafraCompostaSimplesDto> safraCompostaSimplesDtos = safraCompostaService.buscarComPaginacao(pageable, filter, situacao, anoBase);
        return ResponseEntity.ok(safraCompostaSimplesDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable(name = "id") Long id
    ) {
        SafraCompostaDto safraCompostaDto = SafraCompostaDto.construir(safraCompostaService.buscarPorId(id));
        return ResponseEntity.ok(safraCompostaDto);
    }

    @GetMapping("/{id}/estabelecimentos")
    public ResponseEntity<?> buscarEstabelecimentosPorIdSafraCompostaComPaginacao(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "id") Long id
    ) {
        Page<EstabelecimentoSimplesDto> estabelecimentoSimplesDtoPage = estabelecimentoService.buscarEstabelecimentosPorIdSafraCompostaComPaginacao(id, pageable);
        return ResponseEntity.ok(estabelecimentoSimplesDtoPage);
    }

    @GetMapping("/{data}/{codigoEstabelecimento}/{idTipoProduto}")
    public ResponseEntity<?> buscarEstabelecimentosSemVinculos(
            @PathVariable(name = "data") String data,
            @PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name = "idTipoProduto") Long idTipoProduto
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SafraCompostaSimplesDto objDto = safraCompostaService.buscarPorDataEstabelecimentoTipoProduto(sdf.parse(data), codigoEstabelecimento, idTipoProduto);
            return ResponseEntity.ok(objDto);
        }
        catch (ParseException e) {
            DefaultExceptions defaultExceptions = DefaultExceptions.construir(
                    BAD_REQUEST.value(),
                    "A data informada está incorreta (" + data + "), formato correto (YYYY-MM-DD).",
                    e.getMessage()
            );
            return new ResponseEntity<>(defaultExceptions, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/por-data/{data}/grupo-produto/{codigoGrupoProduto}/estabelecimento/{codigoEstabelecimento}")
    public ResponseEntity<?> buscarPorDataGrupoProdutoEstabelecimento(
            @PathVariable(name = "data") String data,
            @PathVariable(name = "codigoGrupoProduto") String codigoGrupoProduto,
            @PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SafraCompostaSimplesDto objDto = safraCompostaService.buscarPorDataGrupoProdutoEstabelecimento(sdf.parse(data), codigoGrupoProduto, codigoEstabelecimento);
            return ResponseEntity.ok(objDto);
        }
        catch (ParseException e) {
            DefaultExceptions defaultExceptions = DefaultExceptions.construir(
                    BAD_REQUEST.value(),
                    "A data informada está incorreta (" + data + "), formato correto (YYYY-MM-DD).",
                    e.getMessage()
            );
            return new ResponseEntity<>(defaultExceptions, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/sincronizar")
    public void sincronizar() {
    	safraCompostaService.sincronizar(null);
    }
    
    @GetMapping("/sincronizar/{id}")
    public void sincronizar(@PathVariable(name="id") Long id) {
    	safraCompostaService.sincronizar(id);
    }
}

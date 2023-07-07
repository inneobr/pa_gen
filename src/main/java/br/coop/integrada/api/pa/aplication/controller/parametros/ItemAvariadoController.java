package br.coop.integrada.api.pa.aplication.controller.parametros;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoBuscaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoDetalheDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoFilter;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.parametros.ItemAvariadoDetalheService;
import br.coop.integrada.api.pa.domain.service.parametros.ItemAvariadoService;
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

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/item-avariado")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Item Avariado", description = "Item Avariado")
public class ItemAvariadoController {

    @Autowired
    private ItemAvariadoService itemAvariadoService;
    
    @Autowired
    private ItemAvariadoDetalheService itemAvariadoDetalheService;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ItemAvariadoDto objDto){
        itemAvariadoService.cadastrar(objDto);
        return ResponseEntity.ok().body("Item avariado cadastrado com sucesso!");
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody ItemAvariadoDto objDto){
        itemAvariadoService.atualizar(objDto);
        return ResponseEntity.ok().body("Item avariado atualizado com sucesso!");
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativar(@PathVariable(name="id") Long id){
        itemAvariadoService.inativar(id);
        return ResponseEntity.ok( "Item Avariado inativado com sucesso!");
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<?> ativar(@PathVariable(name="id") Long id){
        itemAvariadoService.ativar(id);
        return ResponseEntity.ok("Item Avariado ativado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<?> getItemAvariadoFilter(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.ASC) Pageable pageable,
            ItemAvariadoFilter filter){
        return ResponseEntity.ok().body(itemAvariadoService.getItemAvariadoFilter(pageable, filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable(name="id") Long id){
        ItemAvariado itemAvariado = itemAvariadoService.buscarPorId(id);
        ItemAvariadoDto itemAvariadoDto = ItemAvariadoDto.construir(itemAvariado);
        return ResponseEntity.ok().body(itemAvariadoDto);
    }

    @GetMapping("/{id}/detalhes")
    public ResponseEntity<?> buscarDetalhesPorItemAvariado(@PathVariable(name="id") Long id){
        List<ItemAvariadoDetalhe> itemAvariadoDetalhes = itemAvariadoService.buscarDetalhesPorItemAvariado(id);
        List<ItemAvariadoDetalheDto> itemAvariadoDetalheDtos = ItemAvariadoDetalheDto.construir(itemAvariadoDetalhes);
        return ResponseEntity.ok().body(itemAvariadoDetalheDtos);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<?> buscarHistoricoPorItemAvariado(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name="id") Long idItemAvariado
    ){
        Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(idItemAvariado, PaginaEnum.ITEM_AVARIADO, pageable);
        return ResponseEntity.ok().body(historicoGenericoPage);
    }
    
    @GetMapping("/grupo/{codigoOuDescricao}")
    public ResponseEntity<?> buscarItemAvariadoProduto(@PathVariable(name="codigoOuDescricao") String codigoOuDescricao){
        try {
            return ResponseEntity.ok().body(itemAvariadoService.buscarGrupoProdutoCodigoDescricao(codigoOuDescricao));
        }catch(ConstraintViolationException e) {
            return new ResponseEntity<>(
                    DefaultExceptions.construir(BAD_REQUEST.value(), "Não existem grupos cadastrados.", e), 
                    HttpStatus.BAD_REQUEST);        
        }
    }
    
    @GetMapping("/sincronizar/{id}")
    public void sincronizar(@PathVariable(name="id") Long id){
    	itemAvariadoService.sincronizarItensAvariados(id);
    }
    
    @GetMapping("/sincronizar")
    public void sincronizar(){
    	itemAvariadoService.sincronizarItensAvariados(null);
    }
    
    @GetMapping("/buscar-por/grupo-produto/{codigoGrupoProduto}/estabelecimento/{codigoEstabelecimento}")
    public ResponseEntity<ItemAvariadoBuscaDto> buscarPor(
    		@PathVariable(name = "codigoGrupoProduto") String codigoGrupoProduto,
    		@PathVariable(name = "codigoEstabelecimento") String codigoEstabelecimento,
    		@RequestParam(name = "percentual", defaultValue = "0", required = false) BigDecimal percentual,
    		@RequestParam(name = "ph", defaultValue = "0", required = false) BigInteger ph,
    		@RequestParam(name = "fnt", defaultValue = "0", required = false) BigDecimal fnt
    		){
    	
    	ItemAvariadoDetalhe itemAvariadoDetalhe = itemAvariadoService.buscarPor(codigoGrupoProduto, codigoEstabelecimento, percentual, ph, fnt);
    	
    	if(itemAvariadoDetalhe == null) {
    		throw new ObjectDefaultException("Não foi encontrado \"Item Avariado\" com os parâmetros informados!");
    	}
    	
    	ItemAvariadoBuscaDto itemAvariadoDto = ItemAvariadoBuscaDto.construir(itemAvariadoDetalhe);
        return ResponseEntity.ok(itemAvariadoDto);
    }
    
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable(name="id") Long id){
    	itemAvariadoService.deletar(id);
    	return ResponseEntity.ok("Deletado com sucesso");
    }
    
    @GetMapping("/estabelecimentos/{idItemAvariado}")
    public ResponseEntity<Page<EstabelecimentoNomeCodigoDto>> estabelecimentosVinculados(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name="idItemAvariado") Long idItemAvariado){
        Page<EstabelecimentoNomeCodigoDto> estabelecimentos = itemAvariadoService.findByItemAvariado(idItemAvariado, pageable);
        return ResponseEntity.ok().body(estabelecimentos);
    }
}

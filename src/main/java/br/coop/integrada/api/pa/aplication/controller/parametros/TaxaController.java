package br.coop.integrada.api.pa.aplication.controller.parametros;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaRetornoCadastroDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaSimplesDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.parametros.TaxaService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/parametros/taxa")
@Tag(name = "Taxa", description = "Taxas de Produção")
public class TaxaController {

    @Autowired
    private TaxaService taxaService;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody TaxaDto objDto){
    	System.out.println(objDto.getValorTaxaSaca60Recepcao());
    	
    	
        Taxa obj = taxaService.cadastrar(objDto);
        return ResponseEntity.ok(
                "Taxa cadastrada com sucesso! ID: " + obj.getId()
        );
    }

    @PostMapping("/cadastrar-ou-atualizar-varias-taxas")
    public ResponseEntity<?> cadastrarOuAtualizarVariasTaxas(@RequestBody List<@Valid TaxaDto> objDtos){
        List<TaxaRetornoCadastroDto> taxaRetornoCadastroDtos = taxaService.savarVarias(objDtos);
        return ResponseEntity.ok(taxaRetornoCadastroDtos);
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody TaxaDto objDto){    	
    	if(objDto.getValorTaxaSaca60Recepcao().compareTo(new BigDecimal(999.999999999)) == 1) {
    		return ResponseEntity.badRequest().body("Taxa saca 60kg recepção com valor acima do limite permitido.");
    	}
    	
    	if(objDto.getValorTaxaSaca60().compareTo(new BigDecimal(999.999999999)) == 1) {
    		return ResponseEntity.badRequest().body("Taxa saca 60kg com valor acima do limite permitido.");
    	}
    	
        taxaService.atualizar(objDto);
        return ResponseEntity.ok(
                "Taxa atualizada com sucesso!"
        );
    }

    @Deprecated
    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativar(@PathVariable(name="id") Long id){
        taxaService.inativar(id);
        return ResponseEntity.ok(
                "Taxa inativada com sucesso!"
        );
    }

    @Deprecated
    @PutMapping("/ativar/{id}")
    public ResponseEntity<?> ativar(@PathVariable(name="id") Long id){
        taxaService.ativar(id);
        return ResponseEntity.ok(
                "Taxa ativada com sucesso!"
        );
    }

    @GetMapping
    public ResponseEntity<?> buscarPorPagina(
            @PageableDefault(page = 0, size = 5, sort = "safra", direction = Sort.Direction.ASC) Pageable pageable,
            TaxaFilter filter,
            Situacao situacao
    ) {
        Page<TaxaSimplesDto> objDtos = taxaService.buscarPorPagina(pageable, filter, situacao);
        return ResponseEntity.ok(objDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable(name="id") Long id) {
        Taxa obj = taxaService.buscarPorId(id);
        TaxaDto objDto = TaxaDto.construir(obj);
        return ResponseEntity.ok(objDto);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<?> buscarHistoricoPorItemAvariado(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name="id") Long id
    ){
        Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(id, PaginaEnum.TAXA, pageable);
        return ResponseEntity.ok().body(historicoGenericoPage);
    }
    
//    @GetMapping("/sincronizar")
//    public ResponseEntity<String> sincronizar(){
//    	try {
//    		taxaService.sincronizarTaxas(null);
//            return ResponseEntity.ok().body("");
//    	}
//    	catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//		}
//
//    }
	
	@GetMapping("/sincronizar/{id}")
	public void sincronizar(@PathVariable(name="id") Long id) {
   		taxaService.sincronizarTaxas(id);
	}
    
    @GetMapping("/buscar-taxa-producao-estabelecimento")
    public ResponseEntity<?> buscarTaxaProducaoEstabelecimento(int safra, Long codEstabelecimento, Long familia ){
    	Taxa taxa = taxaService.buscarTaxaProducaoEstabelecimento(safra, codEstabelecimento, familia);
    	return ResponseEntity.ok().body(taxa);
    }
    
    @GetMapping("/{id}/estabelecimentos")
	public ResponseEntity<?> buscarIdTaxaEstabelecimentos(@PathVariable(name="id") Long id, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(taxaService.buscarIdTaxaEstabelecimentos(id, pageable));
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel listar os estabelecimentos.", e.getMessage()), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
    
    @GetMapping("/{id}/grupo-produto")
	public ResponseEntity<?> buscarIdTaxaGrupoProdutos(@PathVariable(name="id") Long id, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(taxaService.buscarIdTaxaGrupoProdutos(id, pageable));
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel listar os estabelecimentos.", e.getMessage()), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
    
    @GetMapping("/{idTaxa}/periodo-carencia")
	public ResponseEntity<?> buscarIdTaxaPeriodoCarencia(@PathVariable(name="idTaxa") Long idTaxa, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(taxaService.buscarPeridoCarencia(idTaxa, pageable));
		}catch(Exception e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel listar o período de carência.", e.getMessage()), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
    
    @DeleteMapping("/excluir/{idTaxa}")
    public ResponseEntity<?> excluir(@PathVariable(name="idTaxa") Long idTaxa){
    	try {
    		taxaService.excluir(idTaxa);
    		return ResponseEntity.ok("Taxa de produção excluída com sucesso!");
    	}
    	catch (Exception e) {
    		return ResponseEntity.badRequest().body("A Taxa de produção não pode ser excluída. " + e.getMessage() );
		}
    }
    
}

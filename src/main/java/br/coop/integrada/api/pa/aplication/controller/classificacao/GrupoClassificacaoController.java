package br.coop.integrada.api.pa.aplication.controller.classificacao;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.DefaultExceptions;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectFieldErrorsException;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.BuscaTabelaClassificacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoFilter;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.TipoClassificacaoDetalheDto;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.classificacao.ClassificacaoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/classificacao")
@Tag(name = "Grupo Classificacao", description = "Grupo classificação")
public class GrupoClassificacaoController {

    @Autowired
    private ClassificacaoService classificacaoService;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;
       

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ClassificacaoDto objDto){
        try {
        	Classificacao classificacao = classificacaoService.cadastrar(objDto);
        	return ResponseEntity.ok("Grupo de Classificação cadastrado com sucesso! ID: " + classificacao.getId());
        }
        catch (ObjectFieldErrorsException e) {
        	
        	StringBuilder mensagem = new StringBuilder();
        	mensagem.append(e.getMessage() + "\n");
        	
        	for(FieldErrorItem item : e.getFieldErrors()){
        		mensagem.append("\n" + item.getMessage());
        	}
        	
        	//return ResponseEntity.badRequest().body( DefaultExceptions.construir(0, e.getMessage(), e.getFieldErrors().toString() ));
        	return ResponseEntity.badRequest().body( mensagem.toString() );
		}
        catch(ConstraintViolationException e) {
        	
        	StringBuilder st = new StringBuilder();
        	
        	if (e.getConstraintViolations().size() > 0 ) { 
    	           	       
    	       for (ConstraintViolation<?> contraints : e.getConstraintViolations()) {
    	    	   String nomeCampo = "";
    	    	   
    	    	   switch (contraints.getPropertyPath().toString()) {
    	    	   	case "taxaSecagemQuilo": 
    	    	   		nomeCampo = "Taxa Secagem Kg";
    	    	   		break;
    	    	   	case "taxaSecagemValor": 
    	    	   		nomeCampo = "Taxa Secagem Valor";
    	    	   		break;
				
    	    	   	default:
    	    	   		nomeCampo = contraints.getPropertyPath().toString();
					
    	    	   }
    	    	   
    	    	   
    	            st.append("O campo: " + nomeCampo + " está com seguinte problema: " + contraints.getMessage().replace("esperar", "") + " o valor recebido foi: " + contraints.getInvalidValue() + " e precisa ser corrigido.");
    	            
    	        }        
        	                      
    	    }
        	
        	
        	return ResponseEntity.badRequest().body(st.toString());
        }
        catch (Exception ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }

    @SuppressWarnings("unused")
	@PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody ClassificacaoDto objDto){
        try {
        	Classificacao classificacao = classificacaoService.atualizar(objDto);
        	return ResponseEntity.ok("Grupo de Classificação atualizado com sucesso!");
        }
        catch (ObjectFieldErrorsException e) {
	    	
	    	StringBuilder mensagem = new StringBuilder();
	    	mensagem.append(e.getMessage() + "\n");
	    	
	    	for(FieldErrorItem item : e.getFieldErrors()){
	    		mensagem.append("\n" + item.getMessage());
	    	}
	    	
	    	
	    	return ResponseEntity.badRequest().body( mensagem.toString() );
		}
	    catch (Exception e) {
	    	return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @GetMapping
    public ResponseEntity<?> buscarPorPagina(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            ClassificacaoFilter filter,
            Situacao situacao
    ) {
        Page<ClassificacaoSimplesDto> objDtos = classificacaoService.buscarPorPagina(pageable, filter, situacao);
        return ResponseEntity.ok(objDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable(name="id") Long idClassificacao) {
        Classificacao obj = classificacaoService.buscarPorId(idClassificacao);
        ClassificacaoDto objDto = ClassificacaoDto.construirDetalheAtivos(obj);
        
        return ResponseEntity.ok(objDto);
    }

    @Deprecated
    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativar(@PathVariable(name="id") Long idClassificacao){
        classificacaoService.inativar(idClassificacao);
        return ResponseEntity.ok(
                "Classificação inativada com sucesso!"
        );
    }

    @Deprecated
    @PutMapping("/ativar/{id}")
    public ResponseEntity<?> ativar(@PathVariable(name="id") Long idClassificacao){
        classificacaoService.ativar(idClassificacao);
        return ResponseEntity.ok(
                "Classificação ativada com sucesso!"
        );
    }
    
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable(name="id") Long idClassificacao){
    	try {
    		Classificacao obj = classificacaoService.buscarPorId(idClassificacao);
        	obj.setDataInativacao(new Date() );
        	classificacaoService.salvar(obj);
        	
        	classificacaoService.inativar(idClassificacao);
        	this.sincronizar(idClassificacao);
        	
            classificacaoService.excluir(idClassificacao);
            return ResponseEntity.ok(
                    "Grupo Classificação excluído com sucesso!"
            );
    	}
    	catch (Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		}
    	
    }

    @GetMapping("/{id}/tipo-classificacao-detalhes")
    public ResponseEntity<?> buscarDedalhesPorClassificacao(@PathVariable(name="id") Long idClassificacao) {
        Classificacao obj = classificacaoService.buscarPorId(idClassificacao);
        TipoClassificacaoDetalheDto objDto = TipoClassificacaoDetalheDto.construir(obj);
        return ResponseEntity.ok(objDto);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<?> buscarHistoricoPorClassificacao(
            @PageableDefault(page = 0, size = 5, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name="id") Long idClassificacao
    ){
        Page<HistoricoGenericoDto> historicoGenericoPage = historicoGenericoService.buscarPorRegistroEPagina(idClassificacao, PaginaEnum.GRUPO_CLASSIFICACAO, pageable);
        return ResponseEntity.ok().body(historicoGenericoPage);
    }

    @GetMapping("/safras")
    public ResponseEntity<?> buscarSafras() {
        List<Integer> safras = classificacaoService.buscarSafras();
        return ResponseEntity.ok(safras);
    }
    
    @GetMapping("/safras-1996")
    public ResponseEntity<?> buscarSafras1996() {
        List<Integer> safras = classificacaoService.buscarSafras1996();
        return ResponseEntity.ok(safras);
    }
       
	
	@GetMapping("/sincronizar/{id}")
	public void sincronizar(@PathVariable(name="id") Long id) {
		classificacaoService.sincronizarClassificacoes(id);
	}
	
    
    
    @GetMapping("/buscar-tabela-classificacao-detalhe")
    public ResponseEntity<BuscaTabelaClassificacaoDto> BuscarTabelaClassificacaoDetalhe(
    		@RequestParam(name = "tipoClassificacao", required = true) TipoClassificacaoEnum tipoClassificacao,
    		@RequestParam(name = "codigoEstabelecimento", required = true) String codigoEstabelecimento,
    		@RequestParam(name = "idGrupoProduto", required = true) Long idGrupoProduto,
    		@RequestParam(name = "safra", required = false) Integer safra,
    		@RequestParam(name = "phEntrada", required = false) BigInteger phEntrada,
    		@RequestParam(name = "teorClassificacao", required = true) BigDecimal teorClassificacao
    		){
    	BuscaTabelaClassificacaoDto objDto = classificacaoService.buscarTabelaClassificacaoDetalhe(
    			tipoClassificacao, codigoEstabelecimento, idGrupoProduto, safra,
    			phEntrada, teorClassificacao); 
    	
    	if(objDto.getCadastrado()) {
    		return ResponseEntity.ok(objDto);
    	}
    	
    	return ResponseEntity.badRequest().body(objDto);
    }
    
    @GetMapping("/{id}/buscar-grupo-produtos")
    public ResponseEntity<?> buscarListGrupoProdutoPorIdClassificacao(@PathVariable(name="id") Long idClassificacao) {
        Classificacao obj = classificacaoService.buscarPorId(idClassificacao);
        
        List<GrupoProduto> listaGrupoProdutos = new ArrayList<>();
        
        for(ClassificacaoGrupoProduto classGp : obj.getGrupoProdutos()){
        	listaGrupoProdutos.add( classGp.getGrupoProduto() );
        }
        
        return ResponseEntity.ok(listaGrupoProdutos);
    }
    
    @GetMapping("/{idClassificacao}/buscar-estabelecimentos")
    public ResponseEntity<?> buscarListEstabelecimentosPorIdGrupoProduto(@PathVariable(name="idClassificacao") Long idClassificacao) {
        
    	Classificacao classificacao = classificacaoService.buscarPorId(idClassificacao);
    	
    	List<Estabelecimento> listaEstabelecimento = new ArrayList<>();
    	
    	for(ClassificacaoEstabelecimento e : classificacao.getEstabelecimentos()) {
    		listaEstabelecimento.add(e.getEstabelecimento());
    	}
    	
        return ResponseEntity.ok(listaEstabelecimento);
    }
    
    
    @GetMapping("/{id}/estabelecimentos")
	public ResponseEntity<?> buscarIdGrupoClassificacaoEstabelecimentos(@PathVariable(name="id") Long id, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		try{
			return ResponseEntity.ok().body(classificacaoService.buscarIdGrupoClassificacaoEstabelecimentos(id, pageable));
		}catch(ConstraintViolationException e) {
			return new ResponseEntity<>(
					DefaultExceptions.construir(BAD_REQUEST.value(), "Não foi possivel inativar natureza operacao.", e), 
            		HttpStatus.BAD_REQUEST);		
		}
	}
    
}

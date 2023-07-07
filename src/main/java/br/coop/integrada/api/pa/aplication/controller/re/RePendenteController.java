package br.coop.integrada.api.pa.aplication.controller.re;

import static br.coop.integrada.api.pa.domain.enums.StatusPesagem.AGUARDANDO_RE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.utils.DataUtil;
import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteJavaDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteParametrosEstabelecimento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteTipoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RependentePesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.externo.NumeroReDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemModalFilter;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoPesoLiquidoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoPesoLiquidoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRecepcaoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRecepcaoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaCafeDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaCafeResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoValorSecagemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoValorSecagemResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.ProdutoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteCompletoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteResponseValitationDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.TipoDesmembramentoEnumDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RepPendenteItemResponse;
import br.coop.integrada.api.pa.domain.service.rependente.RePendenteItemService;
import br.coop.integrada.api.pa.domain.service.rependente.RePendenteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/rependente")
@Tag(name = "Re-Pendente")
public class RePendenteController {

    @Autowired
    private RePendenteService rePendenteService;
    
    @Autowired
    private  RePendenteItemService rePendenteItemService;

    @PostMapping
    public ResponseEntity<RePendenteCompletoDto> salvar(@RequestBody @Valid RePendenteDto rePendenteDto){
        RePendente rePendente = rePendenteService.salvar(rePendenteDto);
        RePendenteCompletoDto response = RePendenteCompletoDto.construir(rePendente);
        return ResponseEntity.ok(response);
    }	

    @GetMapping("/{id}")
    public ResponseEntity<RePendenteCompletoDto> findById(@PathVariable(name="id") Long id){
    	RePendente rePendente = rePendenteService.findById(id);
    	RePendenteCompletoDto response = RePendenteCompletoDto.construir(rePendente);
        return ResponseEntity.ok(response);	
    }

    @GetMapping("/buscar-por/codigo-estabelecimento/{codigoEstabelecimento}")
    public ResponseEntity<Page<RePendenteSimplesDto>> fildByEstabelecimento(
    		@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento, 
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
    	Page<RePendenteSimplesDto> response = rePendenteService.listarPorCodigoEstabelecimento(codigoEstabelecimento, pageable); 
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name="id") Long id){
        rePendenteService.deletar(id);
        return ResponseEntity.ok("Deletado com sucesso!");		
    }

    @GetMapping("/estabelecimentos/{dados}")
    public ResponseEntity<List<RePendenteEstabelecimentoDto>> getEstabelecimentos(@PathVariable(name = "dados") String dados){
        if(dados == null || dados == "undefined") dados = "0";
        return ResponseEntity.ok(rePendenteService.getEstabelecimentos(dados));
    }

    @GetMapping("/grupo-produto/{codigoOuDescricao}")
    public ResponseEntity<List<RePendenteGrupoProdutoDto>> getGrupoProduto(@PathVariable(name="codigoOuDescricao") String codigoOuDescricao){
        if(codigoOuDescricao == null || codigoOuDescricao == "undefined") codigoOuDescricao = "0";
        return ResponseEntity.ok().body(rePendenteService.getGruposProdutos(codigoOuDescricao));        
    }

    @GetMapping("/produtor/{dados}")
    public ResponseEntity<List<ProdutorSimplesDto>> getProdutor(@PathVariable(name="dados") String dados){
        if(dados == null || dados == "undefined") dados = "0";
        return ResponseEntity.ok().body(rePendenteService.getProdutor(dados));             
    }

    @GetMapping("/imovel/{id}")
    public ResponseEntity<List<ImovelSimplesDto>> getImoveis(@PathVariable(name="id") Long id){
        return ResponseEntity.ok(rePendenteService.getImovel(id));
    }

    @GetMapping("/parametros-estabelecimento/{id}")
    public ResponseEntity<RePendenteParametrosEstabelecimento> getParametros(@PathVariable(name="id") Long id){
        return ResponseEntity.ok(rePendenteService.getParametros(id));
    }
    
    @GetMapping("/buscar-produto-pesagem-por/codigo-estabelecimento/{codigoEstabelecimento}/safra/{safra}/numero-documento-pesagem/{nroDocPesagem}")
    public ResponseEntity<ProdutoPesagemDto> buscarProdutoPesagemPorCodigoEstabelecimentoSafraNroDocPesagem(
    		@PathVariable(name="safra") Integer safra,
    		@PathVariable(name="nroDocPesagem") Integer nroDocPesagem,
    		@PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento){
    	ProdutoPesagemDto produtoPesagemDto = rePendenteService.buscarProdutoPesagemPorCodigoEstabelecimentoSafraNroDocPesagem(codigoEstabelecimento, safra, nroDocPesagem);
        return ResponseEntity.ok(produtoPesagemDto);
    }

    @GetMapping("/validar-re-pendente/{codigoEstabelecimento}/{placa}/{codigoGrupoProdutor}/{codigoProdutor}")
    public ResponseEntity<RePendenteResponseValitationDto> validarRePendente(
            @PathVariable(name="codigoEstabelecimento") String codigoEstabelecimento,
            @PathVariable(name="placa") String placa,
            @PathVariable(name="codigoGrupoProdutor") String codigoGrupoProdutor,
            @PathVariable(name="codigoProdutor") String codigoProdutor
            ){
        List<String> mensagens  = rePendenteService.validarRePendente(codigoEstabelecimento, placa, codigoGrupoProdutor, codigoProdutor);
        RePendenteResponseValitationDto responseValitationDto = RePendenteResponseValitationDto.construir(mensagens);
        return ResponseEntity.ok(responseValitationDto);
    }

    @GetMapping("/ticket-pesagem/{codigo_estabelecimento}")
    public ResponseEntity<Page<RependentePesagemDto>> getTicketPesagem(
            @PathVariable(name="codigo_estabelecimento") String codigo,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(rePendenteService.getTicketPesagens(codigo, pageable));
    }
    
    @GetMapping("/ticket-pesagem/filtro")
    public ResponseEntity<Page<RependentePesagemDto>> getTicketPesagemFiltro(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, PesagemModalFilter filter){ 
        return ResponseEntity.ok(rePendenteService.getModalTicketPesagens(pageable, filter));
    }
	
	@GetMapping("/ticket-pesagem/documento/{numero}")
    public ResponseEntity<RePendenteJavaDto> getTicketPesagemNrDocumento(
    		@PathVariable(name="numero") Integer numero){
        return ResponseEntity.ok(rePendenteService.getPesagensNrDocumento(numero));
    }
	
	@PostMapping("/calcular-renda-cafe")
	public ResponseEntity<CalculoRendaCafeResponseDto> calcularRendaCafe(@RequestBody CalculoRendaCafeDto amostra){
		
		try {
			
			if(amostra.getQtdCafeCoco() == null || amostra.getQtdCafeBeneficiado() == null) {
				return ResponseEntity.badRequest().body(new CalculoRendaCafeResponseDto("Os parâmetros informados não podem ser nulos."));
			}
			
			if(amostra.getQtdCafeCoco().equals(BigDecimal.ZERO) || amostra.getQtdCafeBeneficiado().equals(BigDecimal.ZERO)) {
				return ResponseEntity.badRequest().body(new CalculoRendaCafeResponseDto("Os parâmetros não podem ter valor zero."));
			}
			
			if(amostra.getQtdCafeBeneficiado().doubleValue() > amostra.getQtdCafeCoco().doubleValue()) {
				return ResponseEntity.badRequest().body(new CalculoRendaCafeResponseDto("A quantidade de café beneficiado não pode ser maior que a quantidade de café em coco."));
			}
			
			BigDecimal divisor = (amostra.getQtdCafeCoco().divide(new BigDecimal(40), 9, RoundingMode.HALF_EVEN));
			BigDecimal resultado = (amostra.getQtdCafeBeneficiado().divide(divisor, 2, RoundingMode.HALF_EVEN)); 
			return ResponseEntity.ok().body(new CalculoRendaCafeResponseDto(resultado, "Cálculo realizado com sucesso."));
			
		}catch(ConstraintViolationException e) {
			return ResponseEntity.badRequest().body(new CalculoRendaCafeResponseDto("O cálculo não pode ser realizado."));	
			
		}
		
	}
	
	@GetMapping("/listar-tipo-desmembramento")
    public ResponseEntity<List<TipoDesmembramentoEnumDto>> listarTipoDesmembramento () {
		List<TipoDesmembramentoEnumDto> tipoDesmembramentoEnumDtos = TipoDesmembramentoEnum.listar();
        return ResponseEntity.ok(tipoDesmembramentoEnumDtos);
    }
    
	@PostMapping("/calcular-quantidade-secagem")
	public ResponseEntity<CalculoValorSecagemResponseDto> calcularQtdSecagem(@RequestBody CalculoValorSecagemDto input){
		try {
			CalculoValorSecagemResponseDto response = rePendenteService.calcularQtdSecagem(input.getPesoLiquido(),
					input.getPercentualDescontoImpureza(), input.getValorTaxaLimpeza(), input.getPrecoPonto());		
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(new CalculoValorSecagemResponseDto(e.getMessage()));	
			
		}
	}
	
	@PostMapping("/calcular-recepcao")
	public ResponseEntity<CalculoRecepcaoResponseDto> calcularRecepcao(@RequestBody CalculoRecepcaoDto input){
		CalculoRecepcaoResponseDto response = null;
		
		try {
			response = rePendenteService.calcularRecepcao(input.getQuantidade(), input.getValorPonto(), input.getValorRecepcao());
			return ResponseEntity.ok(response);
		}
		catch (Exception e) {
			response = new CalculoRecepcaoResponseDto(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/calcular-renda-liquida")
	public ResponseEntity<CalculoRendaLiquidaResponseDto> calcularRendaLiquida(@RequestBody CalculoRendaLiquidaDto input){
		CalculoRendaLiquidaResponseDto response = null;
		
		try {
			response = rePendenteService.calcularRendaLiquida(input);
			return ResponseEntity.ok(response);
		}
		catch (Exception e) {
			response = new CalculoRendaLiquidaResponseDto(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PostMapping("/verificar-taxas-re")
	public ResponseEntity<VerificarTaxasReResponseDto> verificarTaxasRe(@RequestBody VerificarTaxasReDto input){
		try {
			
			VerificarTaxasReResponseDto responseDto = rePendenteService.verificarTaxasRe(input);
						
			return ResponseEntity.ok().body(responseDto);
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(new VerificarTaxasReResponseDto("O cálculo não pode ser realizado. " + e.getMessage()));
		}
	}
	
//	@PostMapping("/validacao-desmenbramento-pendente")
//	public ResponseEntity<String> validacaoDesmembramentoPendente(@RequestBody validacaoDesmembramentoPendente input){
//		try {
//			
//			RePendenteDto rePendenteDto=null;
//						
//			
//			boolean responseDto = rePendenteService.validacaoDesmembramentoPendente(input, rePendenteDto);
//			
//			if(responseDto)
//				return ResponseEntity.ok().body("O cálculo realizado com sucesso. ");
//			else
//				return ResponseEntity.ok().body("O cálculo não pode ser realizado. ");
//				
//		}
//		catch (Exception e) {
//			return ResponseEntity.badRequest().body("O cálculo não pode ser realizado. " + e.getMessage());
//		}
//	}
	
	@PostMapping("/calcular-peso-liquido-atual")
	public ResponseEntity<CalculoPesoLiquidoResponseDto> calcularPesoLiquidoAtual(@RequestBody CalculoPesoLiquidoDto input){
		try {
			
			BigDecimal baseCalculo = rePendenteService.calcularPesoLiquidoAtual(input);
			CalculoPesoLiquidoResponseDto responseDto = new CalculoPesoLiquidoResponseDto("O cálculo foi realizado com sucesso.");
			responseDto.setPesoLiquido(baseCalculo);
			
			return ResponseEntity.ok().body(responseDto);

		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(new CalculoPesoLiquidoResponseDto("O cálculo não pode ser realizado. " + e.getMessage()));
		}
	}
	
	@GetMapping("/atualizar-re/id-re-pendente/{id}")
    public ResponseEntity<AbstractResponseDto> atualizarRe(
    		@PathVariable(name = "id") Long id,
    		@RequestParam(name = "permitirDataMovimentoAbertoMenorQueDataAtual", defaultValue = "false", required = false) boolean permitirDataMovimentoAbertoMenorQueDataAtual){
        AbstractResponseDto response = new AbstractResponseDto();
        
        try {
        	List<NumeroReDto> numerosReDto = rePendenteService.atualizarRe(id, permitirDataMovimentoAbertoMenorQueDataAtual);
        	List<String> numerosRe = numerosReDto.stream()
        			.filter(numeroReDto -> { return numeroReDto != null; })
        			.map(numeroReDto -> { return numeroReDto.getNroDocto().toString(); })
        			.toList();
        	
        	String mensagem = String.join(", ", numerosRe);
        	if(numerosRe.size() > 1) {
        		Integer posicaoUltimaVirgula = mensagem.lastIndexOf(",");
        		if(posicaoUltimaVirgula > 0) {
        			mensagem = mensagem.substring(0, posicaoUltimaVirgula) + " e" + mensagem.substring(posicaoUltimaVirgula + 1);
        		}
        		mensagem = " e gerou os números " + mensagem;
        	}
        	else {
        		mensagem = " e gerou o número " + mensagem;
        	}
        	
        	response.setIntegrated(true);
            response.setMessage("A atualização da RE foi concluída com sucesso" + mensagem + ".");
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
        	response.setIntegrated(false);
            response.setMessage(e.getMessage());
            response.setException(e.toString());
            return ResponseEntity.badRequest().body(response);
		}
    }
	
	@GetMapping("/estabelecimento-produto-lote/{codEstab}/{codItem}/{lote}")
	public ResponseEntity<RepPendenteItemResponse> getLoteUtilizado(
			@PathVariable(name="codEstab") String codEstab, 
			@PathVariable(name="codItem") String codItem, 
			@PathVariable(name="lote") String lote){
		return ResponseEntity.ok(rePendenteItemService.getLoteUtilizado(codEstab, codItem, lote));
	}
}

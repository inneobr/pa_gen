package br.coop.integrada.api.pa.domain.service.recEntrega;


import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;
import static br.coop.integrada.api.pa.domain.spec.RecEntregaSpecs.codigoEstabelecimentoEquals;
import static br.coop.integrada.api.pa.domain.spec.RecEntregaSpecs.nrDocumentoPesagemEquals;
import static br.coop.integrada.api.pa.domain.spec.RecEntregaSpecs.rendaLiquidaAtualDiferenteDeZero;
import static br.coop.integrada.api.pa.domain.spec.RecEntregaSpecs.safraEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.webjars.NotFoundException;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoDto;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovDto;
import br.coop.integrada.api.pa.domain.modelDto.externo.NumeroReDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoBuscaDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaFilter;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaItemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRecepcaoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoValorSecagemResponseDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRep;
import br.coop.integrada.api.pa.domain.service.PrecoService;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoMovService;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoService;
import br.coop.integrada.api.pa.domain.service.externo.ErpExtService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.movimentoRe.MovimentoReService;
import br.coop.integrada.api.pa.domain.service.nfRemessa.NfRemessaService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.service.rependente.RePendenteService;

@Service
public class RecEntregaService {
	private static final Logger log = LoggerFactory.getLogger(RecEntregaService.class);
	
	@Autowired
	private RecEntregaRep recEntregaRep;

	
	@Autowired
	private ProdutoService produtoService;
    
    @Autowired
    private RecEntregaItemService recEntregaItemService;
    
    @Autowired
    private EstabelecimentoRep estabelecimentoRep;	
    
	@Autowired
    private ParametroEstabelecimentoRep parametroEstabelecimentoRep; 

	@Autowired
    private IntegrationService integrationService;
    
    @Autowired
    private PrecoService precoService;
	
    @Autowired
    private RePendenteService rePendenteService;
    
    @Autowired
    private ErpExtService erpExtService;
    
    @Autowired
    private MovimentoReService movimentoReService;
    
    @Autowired
    private BaixaCreditoService baixaCreditoService;
    
    @Autowired
    private BaixaCreditoMovService baixaCreditoMovService;
    
    @Autowired
    private NfRemessaService nfRemessaService; 
    
    @Autowired
    private ProdutorService produtorService;

	@Value("${spring.profiles.active}")
	private String profileActive;
		
	public RecEntrega findById(Long id) {
		return recEntregaRep.findById(id).orElse(null);
	}
	
	public void deletar(Long id) {
		recEntregaRep.deleteById(id);
    }
	
	public void validaSeExisteDocumentoPesagemNaRecEntrega(String codigoEstabelecimento, Integer safra, Long nroDocPesagem) {
		List<RecEntrega> recEntregas = recEntregaRep.findAll(codigoEstabelecimentoEquals(codigoEstabelecimento)
				.and(safraEquals(safra))
				.and(nrDocumentoPesagemEquals(nroDocPesagem))
				.and(rendaLiquidaAtualDiferenteDeZero()));
		
		if(CollectionUtils.isEmpty(recEntregas) == false) {
			throw new ObjectDefaultException("Já possui um RE cadastrado com este número de documento de pesagem.");
		}
	}
	
	public List<RecEntrega> salvar(List<RecEntrega> objs) {
		return (List<RecEntrega>) recEntregaRep.saveAll(objs);
	}
	
	@Transactional
	public RecEntrega atualizar(RecEntrega recEntrega) {
		recEntrega = recEntregaRep.save(recEntrega);
		return recEntrega;
	}
	
	@Transactional
	public RecEntrega salvar(RecEntregaDto obj) {
    	
		RecEntrega recEntrega = new RecEntrega();
		
		Estabelecimento estabelecimento = null;
		
		if(obj.getCodEstabel() != null)
		{
			estabelecimento = estabelecimentoRep.findByCodigo(obj.getCodEstabel());
		}
		
		
		if(estabelecimento == null) {
			throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código \"" + obj.getCodEstabel() + "\"");
		}
		
		RecEntrega recEntregaAtual = recEntregaRep.findByCodEstabelAndNrRe(estabelecimento.getCodigo(), obj.getNrRe()).orElse(null);
		
		if(recEntregaAtual != null) {
			BeanUtils.copyProperties(recEntregaAtual, recEntrega);
		}
		
		BeanUtils.copyProperties(obj, recEntrega);
		
		// VERIFICAR SE OS CÓDIGOS INFORMADOS EXISTEM E VINCULA NA REC ENTREGA
		
		recEntrega = recEntregaRep.save(recEntrega);
		
		List<RecEntregaItemDto> itemDtos = obj.getItemRecEntrega();
		List<RecEntregaItem> itens = recEntregaItemService.converterDto(recEntrega, itemDtos);
		
		for(RecEntregaItem i : itens) {
			i.setRecEntrega(recEntrega);
		}
		
		recEntrega.getItens().clear();
		recEntrega.getItens().addAll(itens);

		return recEntrega;
	}
	
	//Gera entradas normais
	public List<RecEntrega> gerarEntradasNormais(List<RecEntrega> recEntregaLista, RePendenteItem item) throws CloneNotSupportedException{
		Long nrPriEnt = 0L;
		
		for(RecEntrega recEntrega: recEntregaLista) {
			nrPriEnt += 10L;
			
			recEntrega.setCodSit(30);
			recEntrega.setLogReDpi(false);
			recEntrega.setLogBloqDpi(false);
			recEntrega.setNrReDpi(0L);
			recEntrega.setPesoLiqSemDescDpi(BigDecimal.ZERO);
			recEntrega.setReBloqueado(false);
			recEntrega.setReDisponivel(true);
			recEntrega.setNrReOrig(0L);
			recEntrega.setNrRePai(0L);
			recEntrega.setSequencia(nrPriEnt);
			recEntrega.setNrPriEnt(recEntrega.getSequencia());
			
			for(RecEntregaItem recEntregaItem: recEntrega.getItens()) {					
				if(item.getIncideTaxaRecepcao()){	
					//Calcula recepção
					CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
							recEntrega.getPesoLiquido(), 
							recEntregaItem.getVlPonto(), 
							recEntregaItem.getTabVlRecepcao());
										
					recEntregaItem.setVlRecepcao(calculoRecepcao.getValorRecepcao());		
					recEntregaItem.setQtRecepcao(calculoRecepcao.getQuantidadeRecepcao());

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}
				
				if(item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("renda")) {
					//Calcula secagem
					CalculoValorSecagemResponseDto calculoValorSecagem = rePendenteService.calcularQtdSecagem(
							recEntrega.getPesoLiquido(), 
							recEntregaItem.getPerDescImpur(), 
							recEntregaItem.getTabTxSecVl(), 
							recEntregaItem.getVlPonto());
					
					recEntregaItem.setVlSecagem(calculoValorSecagem.getValorSecagem());
					recEntregaItem.setQtSecagem(calculoValorSecagem.getQuantidadeSecagem());

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}
				
				CalculoRendaLiquidaDto rendaLiquida = new CalculoRendaLiquidaDto();		
				rendaLiquida.setCalculaSecagem(false);
				rendaLiquida.setPesoLiquido(recEntregaItem.getPesoLiquido());
				rendaLiquida.setPercDescImpureza(recEntregaItem.getPerDescImpur());
				rendaLiquida.setPercDescUmidade(recEntregaItem.getPerDescUmid());
				rendaLiquida.setPercDescChuvadoAvariado(recEntregaItem.getPerDescChuv());
				rendaLiquida.setPercTBM(recEntregaItem.getTbm());
				rendaLiquida.setQuantidadeRecepcao(recEntregaItem.getQtRecepcao());
				rendaLiquida.setQuantidadeSecagem(recEntregaItem.getQtSecagem());
				rendaLiquida.setTaxaSecagem(BigDecimal.ZERO);
				
				//Calcula renda liquida
				CalculoRendaLiquidaResponseDto rendaLiquidaCalculada = rePendenteService.calcularRendaLiquida(rendaLiquida);
				
				recEntregaItem.setPesoLiquidoAtu(recEntregaItem.getPesoLiquido());
				recEntregaItem.setQtDescImpur(rendaLiquidaCalculada.getQtdImpureza());
				recEntregaItem.setQtDescUmid(rendaLiquidaCalculada.getQtdUmidade());
				recEntregaItem.setQtDescChuv(rendaLiquidaCalculada.getQtdChuvaAvariado());
				recEntregaItem.setQtTbm(rendaLiquidaCalculada.getQtdTBM());
				recEntregaItem.setRendaLiquida(rendaLiquidaCalculada.getQtdRendaLiquida());
				recEntregaItem.setRendaLiquidaAtu(recEntregaItem.getRendaLiquida());
				recEntregaItem.setSeqItemDocum(10);
			}			
		}
		return recEntregaLista;
	}
	
	//GERAR ENTRADA DPI
	public List<RecEntrega> gerarEntradaDpi(List<RecEntrega> recEntregaLista, RePendenteItem item) throws CloneNotSupportedException{
		Long nrPriEnt = 0L;
		List<RecEntrega> recEntregaResponse = new ArrayList<>();
		
		for(RecEntrega recEntrega: recEntregaLista) {
			nrPriEnt += 10L;
			
			// GUARDAR O PESO LIQUIDO SEM DESCONTAR O DPI E RENDA LIQUIDA
			recEntrega.setPesoLiqSemDescDpi(recEntrega.getPesoLiquido().setScale(2, RoundingMode.HALF_EVEN));
			
			// CALCULO RENDA LIQUIDA SEM DESCONTO DE DPI
			if(CollectionUtils.isEmpty(recEntrega.getItens()) == false) {
				RecEntregaItem recEntregaItem = recEntrega.getItens().get(0);
				
				BigDecimal QTDRecepcao = BigDecimal.ZERO;
				if(item.getIncideTaxaRecepcao()){
					CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
							recEntrega.getPesoLiquido(), 
							recEntregaItem.getVlPonto(), 
							recEntregaItem.getTabVlRecepcao());
					
					QTDRecepcao = calculoRecepcao.getQuantidadeRecepcao();						
				}
				
				BigDecimal QTDSecagem  = BigDecimal.ZERO;
				if(item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("renda")) {
					CalculoValorSecagemResponseDto calculoValorSecagem = rePendenteService.calcularQtdSecagem(
							recEntrega.getPesoLiquido(), 
							recEntregaItem.getPerDescImpur(), 
							recEntregaItem.getTabTxSecVl(), 
							recEntregaItem.getVlPonto());
					
					QTDSecagem = calculoValorSecagem.getQuantidadeSecagem();
				}
				
				CalculoRendaLiquidaDto rendaLiquida = new CalculoRendaLiquidaDto();		
				rendaLiquida.setCalculaSecagem(false);
				rendaLiquida.setPercDescImpureza(recEntregaItem.getPerDescImpur());
				rendaLiquida.setPercDescUmidade(recEntregaItem.getPerDescUmid());
				rendaLiquida.setPercDescChuvadoAvariado(recEntregaItem.getPerDescChuv());
				rendaLiquida.setPercTBM(recEntregaItem.getQtTbm());
				rendaLiquida.setPesoLiquido(recEntrega.getPesoLiquido());
				rendaLiquida.setQuantidadeRecepcao(QTDRecepcao);
				rendaLiquida.setQuantidadeSecagem(QTDSecagem);
				
				//CALCULA RENDA LIQUIDA
				CalculoRendaLiquidaResponseDto rendaLiquidaCalculada = rePendenteService.calcularRendaLiquida(rendaLiquida);
				recEntregaItem.setRendaSemDescDpi(rendaLiquidaCalculada.getQtdRendaLiquida());
			}
			
			recEntrega.setCodSit(30);
			recEntrega.setPerDpi(item.getPercentualDpi());
			
			BigDecimal QTDDpi = recEntrega.getPesoLiquido().multiply(item.getPercentualDpi());
			QTDDpi = QTDDpi.divide(new BigDecimal(100), 0, RoundingMode.HALF_EVEN);
			recEntrega.setQtdDpi(QTDDpi);
			
			BigDecimal pesoBruto = recEntrega.getPesoBruto().subtract(recEntrega.getQtdDpi());
			pesoBruto = pesoBruto.setScale(0, RoundingMode.HALF_EVEN);
			recEntrega.setPesoBruto(pesoBruto);
			
			BigDecimal pesoLiquido = recEntrega.getPesoBruto();
			pesoLiquido = pesoLiquido.subtract(recEntrega.getTaraSacaria());
			pesoLiquido = pesoLiquido.subtract(recEntrega.getTaraVeiculo());
			pesoLiquido = pesoLiquido.setScale(0, RoundingMode.HALF_EVEN);
			recEntrega.setPesoLiquido(pesoLiquido);
			
			recEntrega.setReBloqueado(false);
			recEntrega.setReDisponivel(true);
			recEntrega.setSequencia(nrPriEnt);
			recEntrega.setNrPriEnt(recEntrega.getSequencia()); 
			recEntrega.setNrReDpi(recEntrega.getSequencia() + 1L);
			recEntrega.setNrRePai(0L);
			recEntrega.setLogReDpi(false);
			recEntrega.setLogBloqDpi(false);
			
			for(RecEntregaItem recEntregaItem: recEntrega.getItens()) {
				recEntregaItem.setPesoLiquido(recEntrega.getPesoLiquido());
				recEntregaItem.setPesoLiquidoAtu(recEntrega.getPesoLiquido());
				recEntregaItem.setSeqItemDocum(10);
				
				if(item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("RENDA")) {						
					CalculoValorSecagemResponseDto calculoValorSecagem = rePendenteService.calcularQtdSecagem(
							recEntregaItem.getPesoLiquido(), 
							recEntregaItem.getPerDescImpur(), 
							recEntregaItem.getTabTxSecVl(), 
							recEntregaItem.getVlPonto());						
						
					recEntregaItem.setVlSecagem(calculoValorSecagem.getValorSecagem());
					recEntregaItem.setQtSecagem(calculoValorSecagem.getQuantidadeSecagem());

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}

				if(item.getIncideTaxaRecepcao()) {
					CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
							recEntregaItem.getPesoLiquido(), 
							recEntregaItem.getVlPonto(), 
							recEntregaItem.getTabVlRecepcao());						
										
					recEntregaItem.setQtRecepcao(calculoRecepcao.getQuantidadeRecepcao());
					recEntregaItem.setVlRecepcao(calculoRecepcao.getValorRecepcao());

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}				
				}
				
				CalculoRendaLiquidaDto rendaLiquidaItem = new CalculoRendaLiquidaDto();		
				rendaLiquidaItem.setCalculaSecagem(false);
				rendaLiquidaItem.setPercDescImpureza(recEntregaItem.getPerDescImpur());
				rendaLiquidaItem.setPercDescUmidade(recEntregaItem.getPerDescUmid());
				rendaLiquidaItem.setPercDescChuvadoAvariado(recEntregaItem.getPerDescChuv());
				rendaLiquidaItem.setPercTBM(recEntregaItem.getTbm());
				rendaLiquidaItem.setPesoLiquido(recEntrega.getPesoLiquido());
				rendaLiquidaItem.setQuantidadeRecepcao(recEntregaItem.getQtRecepcao());
				rendaLiquidaItem.setQuantidadeSecagem(recEntregaItem.getQtSecagem());					

				CalculoRendaLiquidaResponseDto rendaLiquidaCalculadaItem = rePendenteService.calcularRendaLiquida(rendaLiquidaItem);

				recEntregaItem.setQtDescImpur(rendaLiquidaCalculadaItem.getQtdImpureza());
				recEntregaItem.setQtDescUmid(rendaLiquidaCalculadaItem.getQtdUmidade());
				recEntregaItem.setQtDescChuv(rendaLiquidaCalculadaItem.getQtdChuvaAvariado());
				recEntregaItem.setQtTbm(rendaLiquidaCalculadaItem.getQtdTBM());
				recEntregaItem.setRendaLiquida(rendaLiquidaCalculadaItem.getQtdRendaLiquida());
				recEntregaItem.setRendaLiquidaAtu(recEntregaItem.getRendaLiquida());
								
			}//FINAL FOR REC_ENTREGA_ITEM
			
			//ADICIONAMOS A PRIMEIRA RE NA LISTA DE RETORNO COM AS ALTERAÇÕES;
			recEntregaResponse.add(recEntrega);
			
			//REALIZA UMA COPIA DA REC_ENTREGA
			RecEntrega recEntregaDpi = (RecEntrega) recEntrega.clone();	
			recEntregaDpi.setCodSit(30);
			recEntregaDpi.setPlaca(recEntregaDpi.getPlaca() + "DPI");
			recEntregaDpi.setSequencia(recEntrega.getSequencia() + 1);
			recEntregaDpi.setPesoBruto(recEntrega.getQtdDpi().setScale(0, RoundingMode.HALF_EVEN));
			recEntregaDpi.setTaraSacaria(BigDecimal.ZERO);
			recEntregaDpi.setTaraVeiculo(BigDecimal.ZERO);
			recEntregaDpi.setPesoLiquido(recEntrega.getQtdDpi().setScale(0, RoundingMode.HALF_EVEN));
			recEntregaDpi.setNrRePai(recEntrega.getSequencia());
			recEntregaDpi.setReBloqueado(true);
			recEntregaDpi.setReDisponivel(false);
			recEntregaDpi.setNrPriEnt(recEntregaDpi.getSequencia());
			recEntregaDpi.setLogReDpi(true);
			recEntregaDpi.setLogBloqDpi(true);
			recEntregaDpi.setNrReDpi(0L);
			recEntregaDpi.setObservacoes("RE Royalties " + recEntrega.getObservacoes());
			recEntregaDpi.setPesoLiqSemDescDpi(recEntrega.getPesoLiqSemDescDpi().setScale(2, RoundingMode.HALF_EVEN));
			recEntregaDpi.setItens(new ArrayList<>());
			
			for(RecEntregaItem recEntregaItem:  recEntrega.getItens()) {	
				RecEntregaItem recEntregaItemDpi = new RecEntregaItem();
				BeanUtils.copyProperties(recEntregaItem, recEntregaItemDpi);
				recEntregaItemDpi.setRecEntrega(recEntregaDpi);
				
				//NOVO ITENS COPIA REC_ENTREGA_ITEM				
				recEntregaItemDpi.setPesoLiquido(recEntregaDpi.getPesoLiquido());
				recEntregaItemDpi.setPesoLiquidoAtu(recEntregaDpi.getPesoLiquido());
				
				Integer seqItemDocum =  recEntregaDpi.getNatureza().equals(NaturezaEnum.PJ_NF) ? 20 : 10;
				recEntregaItemDpi.setSeqItemDocum(seqItemDocum);
				
				if(item.getIncideTaxaRecepcao() || (item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("renda"))) {
					//CALCULA SECAGEM 3
					if(item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("renda")) {							
						CalculoValorSecagemResponseDto calculoValorSecagem = rePendenteService.calcularQtdSecagem(
								recEntregaItemDpi.getPesoLiquido(), 
								recEntregaItemDpi.getPerDescImpur(), 
								recEntregaItemDpi.getTabTxSecVl(), 
								recEntregaItemDpi.getVlPonto());

						recEntregaItemDpi.setVlSecagem(calculoValorSecagem.getValorSecagem());
						recEntregaItemDpi.setQtSecagem(calculoValorSecagem.getQuantidadeSecagem());

						String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
						if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
							recEntregaDpi.setLogTxSpRetida(true);
							recEntregaDpi.setNrReTxSpRetida(recEntregaDpi.getSequencia());
						}
						
					}//FIM CALCULA SECAGEM 3
					
					//CALCULO RECEPÇÃO 3
					if(item.getIncideTaxaRecepcao()) {
						CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
								recEntregaItemDpi.getPesoLiquido(), 
								recEntregaItemDpi.getVlPonto(), 
								recEntregaItemDpi.getTabVlRecepcao());							
				
						recEntregaItemDpi.setQtRecepcao(calculoRecepcao.getQuantidadeRecepcao());
						recEntregaItemDpi.setVlRecepcao(calculoRecepcao.getValorRecepcao());

						String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
						if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
							recEntregaDpi.setLogTxSpRetida(true);
							recEntregaDpi.setNrReTxSpRetida(recEntregaDpi.getSequencia());
						}
					}//FIM CALCULO RECEPÇÃO 3
				}	
				
				//CALCULA RENDA LIQUIDA					 
				CalculoRendaLiquidaDto rendaLiquidaDpi = new CalculoRendaLiquidaDto();		
				rendaLiquidaDpi.setCalculaSecagem(false);
				rendaLiquidaDpi.setPercDescImpureza(recEntregaItemDpi.getPerDescImpur());
				rendaLiquidaDpi.setPercDescUmidade(recEntregaItemDpi.getPerDescUmid());
				rendaLiquidaDpi.setPercDescChuvadoAvariado(recEntregaItemDpi.getPerDescChuv());
				rendaLiquidaDpi.setPesoLiquido(recEntregaDpi.getPesoLiquido());
				rendaLiquidaDpi.setPercTBM(recEntregaItemDpi.getTbm());
				rendaLiquidaDpi.setQuantidadeRecepcao(recEntregaItemDpi.getQtRecepcao());
				rendaLiquidaDpi.setQuantidadeSecagem(recEntregaItemDpi.getQtSecagem());
				
				CalculoRendaLiquidaResponseDto rendaLiquidaCalculadaReturn = rePendenteService.calcularRendaLiquida(rendaLiquidaDpi);
				recEntregaItemDpi.setQtDescImpur(rendaLiquidaCalculadaReturn.getQtdImpureza());
				recEntregaItemDpi.setQtDescUmid(rendaLiquidaCalculadaReturn.getQtdUmidade());
				recEntregaItemDpi.setQtDescChuv(rendaLiquidaCalculadaReturn.getQtdChuvaAvariado());
				recEntregaItemDpi.setQtTbm(rendaLiquidaCalculadaReturn.getQtdTBM());
				recEntregaItemDpi.setRendaLiquida(rendaLiquidaCalculadaReturn.getQtdRendaLiquida());
				recEntregaItemDpi.setRendaLiquidaAtu(recEntregaItemDpi.getRendaLiquida());
				
				recEntregaDpi.getItens().add(recEntregaItemDpi);
			}
			recEntregaResponse.add(recEntregaDpi);
			
		}//FINAL FOR REC_ENTREGA
		return recEntregaResponse;
	}
	
	

	//PAP-1124 - Gera as cobranças de taxa
	public List<RecEntrega> gerarReCobrancaTaxa(List<RecEntrega> recEntregaList, RePendenteItem item) throws CloneNotSupportedException {
		Long nrPriEnt = 0L;
		BigDecimal qtdReTaxa = BigDecimal.ZERO;
		
		List<RecEntrega> response = new ArrayList<RecEntrega>();
		for(RecEntrega recEntrega: recEntregaList) {
			nrPriEnt += 10L;
			
			recEntrega.setCodSit(30);
			recEntrega.setLogReDpi(false);
			recEntrega.setLogBloqDpi(false);
			recEntrega.setNrReDpi(0L);
			recEntrega.setPesoLiqSemDescDpi(BigDecimal.ZERO);
			recEntrega.setReBloqueado(false); 
			recEntrega.setReDisponivel(true);
			recEntrega.setNrReOrig(0L);
			recEntrega.setNrRePai(0L);
			recEntrega.setSequencia(nrPriEnt);
			recEntrega.setNrPriEnt(recEntrega.getSequencia());
			
			for(RecEntregaItem recEntregaItem: recEntrega.getItens()) {	
				if(item.getIncideTaxaRecepcao()) {
					CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
							recEntregaItem.getPesoLiquido(), 
							item.getValorRecepcao(),
							recEntregaItem.getVlPonto());
					
					recEntregaItem.setQtRecepcao(calculoRecepcao.getQuantidadeRecepcao());
					recEntregaItem.setVlRecepcao(calculoRecepcao.getValorRecepcao());
					
					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}
				
				//Inicio calculo renda liquida
				CalculoRendaLiquidaDto rendaLiquida = new CalculoRendaLiquidaDto();		
				rendaLiquida.setCalculaSecagem(true);
				rendaLiquida.setPesoLiquido(recEntregaItem.getPesoLiquido());
				rendaLiquida.setPercDescImpureza(recEntregaItem.getPerDescImpur());
				rendaLiquida.setPercDescUmidade(recEntregaItem.getPerDescUmid());
				rendaLiquida.setPercDescChuvadoAvariado(recEntregaItem.getPerDescChuv());
				rendaLiquida.setPercTBM(recEntregaItem.getTbm());
				rendaLiquida.setQuantidadeRecepcao(recEntregaItem.getQtRecepcao());
				rendaLiquida.setQuantidadeSecagem(BigDecimal.ZERO);
				rendaLiquida.setTaxaSecagem(recEntregaItem.getTabTxSecKg());
				
				//Insere os valores no novo item da lista de retorno
				CalculoRendaLiquidaResponseDto rendaLiquidaCalculada = rePendenteService.calcularRendaLiquida(rendaLiquida);
				recEntregaItem.setQtDescImpur(rendaLiquidaCalculada.getQtdImpureza());
				recEntregaItem.setQtDescUmid(rendaLiquidaCalculada.getQtdUmidade());
				recEntregaItem.setQtDescChuv(rendaLiquidaCalculada.getQtdChuvaAvariado());
				recEntregaItem.setQtSecagem(rendaLiquidaCalculada.getQtdSecagem());
				qtdReTaxa = rendaLiquidaCalculada.getQtdSecagem();
				recEntregaItem.setQtTbm(rendaLiquidaCalculada.getQtdTBM());
				recEntregaItem.setRendaLiquida(rendaLiquidaCalculada.getQtdRendaLiquida());
				recEntregaItem.setRendaLiquidaAtu(rendaLiquidaCalculada.getQtdRendaLiquida());
				recEntregaItem.setPesoLiquidoAtu(recEntregaItem.getPesoLiquido());
				
				recEntregaItem.setSeqItemDocum(10);
			}
			
			response.add(recEntrega);
			
			//Nova RecEntrega
			RecEntrega recEntregaCoop = new RecEntrega();
			recEntregaCoop.setLogTemSaldo(true);
			recEntregaCoop.setLogNfe(false);
			recEntregaCoop.setNatEntArmaz(null);
			recEntregaCoop.setSerie(null);
			recEntregaCoop.setNrNotaFis(null);
			recEntregaCoop.setNfPj(false);
			recEntregaCoop.setNrReOpcao(0);
			recEntregaCoop.setCodUnidParc(0);
			recEntregaCoop.setCodEstOffLine(null);
			recEntregaCoop.setImpresso(false);
			recEntregaCoop.setCodSit(30);
			recEntregaCoop.setNrAutTransf(0L);
			recEntregaCoop.setUbsLogLiberado(false);
			recEntregaCoop.setUbsLogRequisitado(false);
			recEntregaCoop.setUbsDataRequisicao(null);
			recEntregaCoop.setUbsHoraRequisicao(null);
			recEntregaCoop.setNrBcqs(0L);
			recEntregaCoop.setReDevolvido(false);
			recEntregaCoop.setNrOrdProd(0L);
			recEntregaCoop.setNrReOrigDfx(0);
			recEntregaCoop.setMotivoBloqueio(null);
			recEntregaCoop.setBeneficiado(false);
			recEntregaCoop.setNotifPortal(true);
			recEntregaCoop.setNrReParcialDpi(0L);
			recEntregaCoop.setSafra(recEntrega.getSafra());
			recEntregaCoop.setDtEmissao(recEntrega.getDtEmissao());
			recEntregaCoop.setPlaca(recEntrega.getPlaca() + "TXA");
			recEntregaCoop.setDtEntrada(recEntrega.getDtEntrada());
			recEntregaCoop.setHrEntrada(recEntrega.getHrEntrada());
			recEntregaCoop.setHrSaida(recEntrega.getHrSaida());
			recEntregaCoop.setNrDocPes(recEntrega.getNrDocPes());
			recEntregaCoop.setMotorista(recEntrega.getMotorista());
			recEntregaCoop.setTulha(recEntrega.getTulha());
			recEntregaCoop.setNrOrdCampo(recEntrega.getNrOrdCampo());
			recEntregaCoop.setNrLaudo(recEntrega.getNrLaudo());
			recEntregaCoop.setNrContSem(recEntrega.getNrContSem());
			recEntregaCoop.setClasse(recEntrega.getClasse());
			recEntregaCoop.setTipoRr(recEntrega.getTipoRr());
			recEntregaCoop.setTipoGmo(recEntrega.getTipoGmo());
			recEntregaCoop.setProdPadr(recEntrega.getProdPadr());
			recEntregaCoop.setDescarUnid(recEntrega.getDescarUnid());
			recEntregaCoop.setSafraCompos(recEntrega.getSafraCompos());
			recEntregaCoop.setNomeSafraCompos(recEntrega.getNomeSafraCompos());
			recEntregaCoop.setCodEstabel(recEntrega.getCodEstabel());
			recEntregaCoop.setTipoRendaCafe(recEntrega.getTipoRendaCafe());
			recEntregaCoop.setEntradaManual(recEntrega.getEntradaManual());
			recEntregaCoop.setLogTxSpRetida(false);
			recEntregaCoop.setNrReTxSpRetida(0L);
			recEntregaCoop.setQtdDpi(BigDecimal.ZERO);
			recEntregaCoop.setPerDpi(BigDecimal.ZERO);
			recEntregaCoop.setPjNroNota(null);
			recEntregaCoop.setPjSerie(null);
			recEntregaCoop.setPjDtEmissao(null);
			recEntregaCoop.setPjVlTotNota(BigDecimal.ZERO);
			recEntregaCoop.setPjQtTotNota(BigDecimal.ZERO);
			recEntregaCoop.setChaveAcessoNfe(null);
			recEntregaCoop.setPjLogNotaPropria(false);
			recEntregaCoop.setPjNatOper(null);
			recEntregaCoop.setSequencia(recEntrega.getSequencia() + 1);
			recEntregaCoop.setLogReDpi(false);
			recEntregaCoop.setLogBloqDpi(false);
			recEntregaCoop.setNrReDpi(0L);
			recEntregaCoop.setPesoLiqSemDescDpi(BigDecimal.ZERO);
			recEntregaCoop.setReBloqueado(false);
			recEntregaCoop.setReDisponivel(true);
			recEntregaCoop.setNrReOrig(0L);
			recEntregaCoop.setEntradaRr("ORI");
			recEntregaCoop.setNrRePai(recEntrega.getSequencia());
			recEntregaCoop.setNrPriEnt(recEntrega.getNrPriEnt());
			recEntregaCoop.setFmCodigo(recEntrega.getFmCodigo());
			
			//Busca o estabelecimento e retorna codigo;
			ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findByEstabelecimentoCodigo(recEntrega.getCodEstabel());
			recEntregaCoop.setCodEmitente(parametroEstabelecimento.getCodEmitente());
			recEntregaCoop.setMatricula(parametroEstabelecimento.getCodImovel());
			
			recEntregaCoop.setNomeProd(null);
			recEntregaCoop.setPesoBruto(qtdReTaxa);
			recEntregaCoop.setTaraVeiculo(BigDecimal.ZERO);
			recEntregaCoop.setTaraSacaria(BigDecimal.ZERO);
			recEntregaCoop.setPesoLiquido(qtdReTaxa);
			recEntregaCoop.setObservacoes(recEntrega.getObservacoes());
			recEntregaCoop.setCodRegional(recEntrega.getCodRegional());
			recEntregaCoop.setCdnRepres(recEntrega.getCdnRepres());
			
			List<RecEntregaItem> recEntregaItemCoopList = new ArrayList<>();
			for(RecEntregaItem recEntregaItem: recEntrega.getItens()) {
				RecEntregaItem recEntregaItemCoop = new RecEntregaItem();
				recEntregaItemCoop.setRecEntrega(recEntregaCoop);
				
				recEntregaItemCoop.setCodEstabel(recEntregaItem.getCodEstabel());
				recEntregaItemCoop.setCodRefer(null);
				recEntregaItemCoop.setPhEntrada(BigInteger.ZERO);
				recEntregaItemCoop.setPhCorrigido(BigInteger.ZERO);
				recEntregaItemCoop.setImpureza(BigDecimal.ZERO);
				recEntregaItemCoop.setPerDescImpur(BigDecimal.ZERO);
				recEntregaItemCoop.setUmidade(BigDecimal.ZERO);
				recEntregaItemCoop.setPerDescUmid(BigDecimal.ZERO);
				recEntregaItemCoop.setChuvAvar(BigDecimal.ZERO);
				recEntregaItemCoop.setPerDescChuv(BigDecimal.ZERO);
				recEntregaItemCoop.setTbm(BigDecimal.ZERO);
				recEntregaItemCoop.setTipo(0);
				recEntregaItemCoop.setBebida(null);
				recEntregaItemCoop.setCafeEscolha(BigDecimal.ZERO);
				recEntregaItemCoop.setDefeitos(0);
				recEntregaItemCoop.setNormal(recEntregaItem.getNormal());
				recEntregaItemCoop.setSementeira(recEntregaItem.getSementeira());
				recEntregaItemCoop.setTerra(recEntregaItem.getTerra());
				recEntregaItemCoop.setVagem(recEntregaItem.getVagem());
				recEntregaItemCoop.setLote(null);
				recEntregaItemCoop.setTabTxSecKg(BigDecimal.ZERO);
				recEntregaItemCoop.setTabTxSecVl(BigDecimal.ZERO);
				recEntregaItemCoop.setCafermTipo(null);
				recEntregaItemCoop.setCafermBebida(null);
				recEntregaItemCoop.setCafermCafeEscolha(BigDecimal.ZERO);
				recEntregaItemCoop.setCafermRendaLiquida(BigDecimal.ZERO);
				recEntregaItemCoop.setCafermDefeitos(0);
				recEntregaItemCoop.setCafermCodRefer(null);
				recEntregaItemCoop.setCafermRendascLiq(BigDecimal.ZERO);
				recEntregaItemCoop.setCafermRendascEsc(BigDecimal.ZERO);
				recEntregaItemCoop.setCafermRendascTot(BigDecimal.ZERO);
				recEntregaItemCoop.setPerPen14Acima(BigDecimal.ZERO);
				recEntregaItemCoop.setPercBandinha(BigDecimal.ZERO);
				recEntregaItemCoop.setTabVlRecepcao(BigDecimal.ZERO);
				
				recEntregaItemCoop.setCodProduto(item.getItemParaSecagem());
				recEntregaItemCoop.setPesoLiquido(qtdReTaxa);
				recEntregaItemCoop.setPesoLiquidoAtu(qtdReTaxa);
				recEntregaItemCoop.setRendaLiquida(qtdReTaxa);
				recEntregaItemCoop.setRendaLiquidaAtu(qtdReTaxa);
				recEntregaItemCoop.setQtDescImpur(BigDecimal.ZERO);
				recEntregaItemCoop.setQtDescUmid(BigDecimal.ZERO);
				recEntregaItemCoop.setQtDescChuv(BigDecimal.ZERO);
				recEntregaItemCoop.setRendaSemDescDpi(BigDecimal.ZERO);
				recEntregaItemCoop.setQtAlocada(BigDecimal.ZERO);
				recEntregaItemCoop.setVlRecepcao(BigDecimal.ZERO);
				recEntregaItemCoop.setQtRecepcao(BigDecimal.ZERO);
				recEntregaItemCoop.setVlSecagem(BigDecimal.ZERO);
				recEntregaItemCoop.setQtSecagem(BigDecimal.ZERO);				
				
				PrecoBuscaDto buscarPreco = precoService.buscarPor(
						recEntregaItem.getCodEstabel(), 
						recEntregaItemCoop.getCodProduto(), 
						recEntregaItemCoop.getCodRefer(), 
						false);
				
				recEntregaItemCoop.setVlFiscal(buscarPreco.getPrecoFiscal());
				recEntregaItemCoop.setVlPonto(buscarPreco.getPrecoFechamento());
				recEntregaItemCoop.setSeqItemDocum(10);
				recEntregaItemCoopList.add(recEntregaItemCoop);
			}
			
			recEntregaCoop.setItens(recEntregaItemCoopList);
			response.add(recEntregaCoop);
		}		
		return response;	
	}
	
	//GERAÇÃO DE SUBPRODUTO
	public List<RecEntrega> gerarEntradaSubProduto(List<RecEntrega> recEntregaLista, RePendenteItem item){
		Long nrPriEnt = 0L;
		List<RecEntrega> response = new ArrayList<>();
		
		for(RecEntrega recEntrega: recEntregaLista) {
			BigDecimal QTGPRe = BigDecimal.ZERO;
			nrPriEnt += 10L;
			
			recEntrega.setCodSit(30);
			recEntrega.setLogReDpi(false);
			recEntrega.setLogBloqDpi(false);
			recEntrega.setNrReDpi(0L);
			recEntrega.setPesoLiqSemDescDpi(BigDecimal.ZERO);
			recEntrega.setReBloqueado(false);
			recEntrega.setReDisponivel(true);
			recEntrega.setNrReOrig(0L);
			recEntrega.setNrRePai(0L);
			recEntrega.setSequencia(nrPriEnt);
			recEntrega.setNrPriEnt(recEntrega.getSequencia());
			
			for(RecEntregaItem recEntregaItem : recEntrega.getItens()) {
				recEntregaItem.setSeqItemDocum(10);
				
				//CALCULA RECEPÇÃO
				if(item.getIncideTaxaRecepcao()) {
					CalculoRecepcaoResponseDto calculoRecepcao = rePendenteService.calcularRecepcao(
							recEntrega.getPesoLiquido(), 
							recEntregaItem.getVlPonto(), 
							recEntregaItem.getTabVlRecepcao());
					
					recEntregaItem.setVlRecepcao( calculoRecepcao.getValorRecepcao() );
					recEntregaItem.setQtRecepcao( calculoRecepcao.getQuantidadeRecepcao() );

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}
				
				if(item.getIndiceSecagemLimpeza() && item.getTipoCobrancaSecagem().equalsIgnoreCase("RENDA")) {
					//CALCULA SECAGEM
					CalculoValorSecagemResponseDto calculoSecagem = rePendenteService.calcularQtdSecagem(
							recEntregaItem.getPesoLiquido(), 
							recEntregaItem.getPerDescImpur(), 
							recEntregaItem.getTabTxSecVl(), 
							recEntregaItem.getVlPonto());
					
					recEntregaItem.setVlSecagem(calculoSecagem.getValorSecagem());
					recEntregaItem.setQtSecagem(calculoSecagem.getQuantidadeSecagem());

					String estadoEstabelecimento = item.getRePendente().getEstabelecimento().getEstado();
					if(Strings.isNotEmpty(estadoEstabelecimento) && estadoEstabelecimento.equalsIgnoreCase("SP")) {
						recEntrega.setLogTxSpRetida(true);
						recEntrega.setNrReTxSpRetida(recEntrega.getSequencia());
					}
				}//FIM CALCULO RECEPÇÃO				
				
				//CALCULA RENDA LIQUIDA
				CalculoRendaLiquidaDto rendaLiquida = new CalculoRendaLiquidaDto();		
				rendaLiquida.setCalculaSecagem(false);
				rendaLiquida.setPesoLiquido(recEntregaItem.getPesoLiquido());
				rendaLiquida.setPercDescImpureza(recEntregaItem.getPerDescImpur());
				rendaLiquida.setPercDescUmidade(recEntregaItem.getPerDescUmid());
				rendaLiquida.setPercDescChuvadoAvariado(recEntregaItem.getPerDescChuv());
				rendaLiquida.setPercTBM(recEntregaItem.getTbm());
				rendaLiquida.setQuantidadeRecepcao(recEntregaItem.getQtRecepcao());
				rendaLiquida.setQuantidadeSecagem(recEntregaItem.getQtSecagem());
				
				CalculoRendaLiquidaResponseDto rendaLiquidaCalculada = rePendenteService.calcularRendaLiquida(rendaLiquida);
				recEntregaItem.setQtDescImpur( rendaLiquidaCalculada.getQtdImpureza() );
				recEntregaItem.setQtDescUmid( rendaLiquidaCalculada.getQtdUmidade() );
				recEntregaItem.setQtDescChuv( rendaLiquidaCalculada.getQtdChuvaAvariado() );
				recEntregaItem.setQtSecagem( rendaLiquidaCalculada.getQtdSecagem() );
				recEntregaItem.setQtTbm( rendaLiquidaCalculada.getQtdTBM() );				
				recEntregaItem.setRendaLiquida( rendaLiquidaCalculada.getQtdRendaLiquida() );
				recEntregaItem.setRendaLiquidaAtu( rendaLiquidaCalculada.getQtdRendaLiquida() );
				recEntregaItem.setPesoLiquidoAtu( recEntregaItem.getPesoLiquido() );
				recEntregaItem.setRendaSemDescDpi( BigDecimal.ZERO );
				QTGPRe = rendaLiquidaCalculada.getQtdTBM();
				//FIM CALCULA RENDA LIQUIDA
			}
			
			response.add(recEntrega);
			
			//GERAR NOVA RE_SUBPRODUTO
			if(QTGPRe.compareTo(new BigDecimal("2")) >= 0) {
				RecEntrega subProduto = new RecEntrega();
				subProduto.setCodSit(30);
				subProduto.setLogTemSaldo(true);
				subProduto.setLogNfe(false);
				subProduto.setNatEntArmaz("");
				subProduto.setSerie("");
				subProduto.setNrNotaFis("");
				subProduto.setNfPj(false);
				subProduto.setNrReOpcao(0);
				subProduto.setCodUnidParc(0);
				subProduto.setCodEstOffLine("");
				subProduto.setImpresso(false);
				subProduto.setCodSit(30);
				subProduto.setNrAutTransf(0L);
				subProduto.setUbsLogLiberado(false);
				subProduto.setUbsLogRequisitado(false);
				subProduto.setUbsDataRequisicao(null);
				subProduto.setUbsHoraRequisicao("");
				subProduto.setNrBcqs(0L);
				subProduto.setReDevolvido(false);
				subProduto.setNrOrdProd(0L);
				subProduto.setNrReOrigDfx(0);
				subProduto.setMotivoBloqueio("");
				subProduto.setBeneficiado(false);
				subProduto.setNotifPortal(true);
				subProduto.setNrReParcialDpi(0L);
				subProduto.setSafra(recEntrega.getSafra());
				subProduto.setDtEmissao(recEntrega.getDtEmissao());
				subProduto.setPlaca(recEntrega.getPlaca()+"SUB");
				subProduto.setDtEntrada(recEntrega.getDtEntrada());
				subProduto.setHrEntrada(recEntrega.getHrEntrada());
				subProduto.setNrDocPes(recEntrega.getNrDocPes());
				subProduto.setMotorista(recEntrega.getMotorista());
				subProduto.setTulha(recEntrega.getTulha());
				subProduto.setNrOrdCampo(recEntrega.getNrOrdCampo());
				subProduto.setNrLaudo(recEntrega.getNrLaudo());
				subProduto.setNrContSem(recEntrega.getNrContSem());
				subProduto.setClasse(recEntrega.getClasse());
				subProduto.setTipoRr(recEntrega.getTipoRr());
				subProduto.setTipoGmo(recEntrega.getTipoGmo());
				subProduto.setProdPadr(recEntrega.getProdPadr());
				subProduto.setDescarUnid(recEntrega.getDescarUnid());
				subProduto.setSafraCompos(recEntrega.getSafraCompos());
				subProduto.setNomeSafraCompos(recEntrega.getNomeSafraCompos());
				subProduto.setCodEstabel(recEntrega.getCodEstabel());
				subProduto.setTipoRendaCafe(recEntrega.getTipoRendaCafe());
				subProduto.setEntradaManual(recEntrega.getEntradaManual());
				subProduto.setLogTxSpRetida(recEntrega.getLogTxSpRetida());
				subProduto.setNrReTxSpRetida(recEntrega.getNrReTxSpRetida());
				subProduto.setQtdDpi(recEntrega.getQtdDpi());
				subProduto.setPerDpi(recEntrega.getPerDpi());
				subProduto.setPjNroNota("");
				subProduto.setPjSerie("");
				subProduto.setPjDtEmissao(null);
				subProduto.setPjVlTotNota(BigDecimal.ZERO);
				subProduto.setPjQtTotNota(BigDecimal.ZERO);
				subProduto.setPjChaveAcesso("");
				subProduto.setPjLogNotaPropria(false);
				subProduto.setPjNatOper("");
				subProduto.setLogReDpi(false);
				subProduto.setLogBloqDpi(false);
				subProduto.setNrReDpi(0L);
				subProduto.setPesoLiqSemDescDpi(BigDecimal.ZERO);
				subProduto.setReBloqueado(false);
				subProduto.setReDisponivel(true);
				subProduto.setNrReOrig(0L);
				subProduto.setSequencia(recEntrega.getSequencia() + 1);
				subProduto.setNrRePai(recEntrega.getSequencia());
				subProduto.setNrPriEnt(subProduto.getSequencia());
				subProduto.setFmCodigo(item.getFamiliaSubProduto());
				
				subProduto.setCodEmitente(recEntrega.getCodEmitente());
				subProduto.setMatricula(recEntrega.getMatricula());
				subProduto.setNrCadPro(recEntrega.getNrCadPro());
				subProduto.setNrNfProd(recEntrega.getNrNfProd());
				subProduto.setSerNfProd(recEntrega.getSerNfProd());
				subProduto.setDtNfProd(recEntrega.getDtNfProd());
				subProduto.setCodRegional(recEntrega.getCodRegional());
				subProduto.setPesoBruto(QTGPRe);
				subProduto.setTaraVeiculo(BigDecimal.ZERO); 
				subProduto.setTaraSacaria(BigDecimal.ZERO);
				subProduto.setPesoLiquido(QTGPRe);
				subProduto.setObservacoes(recEntrega.getObservacoes());
				subProduto.setCdnRepres(recEntrega.getCdnRepres());
				
				Produtor produtor = produtorService.findByCodProdutor(subProduto.getCodEmitente());
				Boolean produtorEhPj = produtor.getNatureza() != null && produtor.getNatureza().equalsIgnoreCase("PJ");
				Boolean produtorEmiteNf = produtor.getEmiteNota();
				if(produtor != null &&  produtorEhPj && produtorEmiteNf) {
					subProduto.setPendenciasFiscais(true);
				}
				
				List<RecEntregaItem> subProdutoItemList = new ArrayList<>();
				for(RecEntregaItem recEntregaItem : recEntrega.getItens()) {
					var subProdutoItem = new RecEntregaItem();
					subProdutoItem.setRecEntrega(subProduto);
					
					recEntregaItem.setSeqItemDocum(10);
					subProdutoItem.setCodEstabel(recEntregaItem.getCodEstabel());
					subProdutoItem.setCodRefer(null);
					subProdutoItem.setPhEntrada(BigInteger.ZERO);
					subProdutoItem.setPhCorrigido(BigInteger.ZERO);
					subProdutoItem.setImpureza(BigDecimal.ZERO);
					subProdutoItem.setPerDescImpur(BigDecimal.ZERO);
					subProdutoItem.setUmidade(BigDecimal.ZERO);
					subProdutoItem.setPerDescImpur(BigDecimal.ZERO);
					subProdutoItem.setUmidade(BigDecimal.ZERO);
					subProdutoItem.setPerDescUmid(BigDecimal.ZERO);
					subProdutoItem.setChuvAvar(BigDecimal.ZERO);
					subProdutoItem.setPerDescChuv(BigDecimal.ZERO);
					subProdutoItem.setTbm(BigDecimal.ZERO);
					subProdutoItem.setTipo(0);
					subProdutoItem.setBebida(null);
					subProdutoItem.setCafeEscolha(BigDecimal.ZERO);
					subProdutoItem.setDefeitos(0);
					subProdutoItem.setNormal(recEntregaItem.getNormal());
					subProdutoItem.setSementeira(recEntregaItem.getSementeira());
					subProdutoItem.setTerra(recEntregaItem.getTerra());
					subProdutoItem.setVagem(recEntregaItem.getVagem());
					subProdutoItem.setLote(recEntregaItem.getLote());
					subProdutoItem.setTabTxSecKg(BigDecimal.ZERO);
					subProdutoItem.setTabTxSecVl(BigDecimal.ZERO);
					subProdutoItem.setCafermTipo(null);
					subProdutoItem.setCafermBebida(null);
					subProdutoItem.setCafermCafeEscolha(BigDecimal.ZERO);
					subProdutoItem.setCafermRendaLiquida(BigDecimal.ZERO);
					subProdutoItem.setCafermDefeitos(0);
					subProdutoItem.setCafermCodRefer(null);
					subProdutoItem.setCafermRendascLiq(BigDecimal.ZERO);
					subProdutoItem.setCafermRendascEsc(BigDecimal.ZERO);
					subProdutoItem.setCafermRendascTot(BigDecimal.ZERO);
					subProdutoItem.setPerPen14Acima(BigDecimal.ZERO);
					subProdutoItem.setPercBandinha(BigDecimal.ZERO);
					subProdutoItem.setTabVlRecepcao(BigDecimal.ZERO);
					subProdutoItem.setCodProduto(item.getItemSubProduto()); 
					subProdutoItem.setPesoLiquido(QTGPRe);
					subProdutoItem.setPesoLiquidoAtu(QTGPRe);
					subProdutoItem.setRendaLiquida(QTGPRe);
					subProdutoItem.setRendaLiquidaAtu(QTGPRe);
					subProdutoItem.setQtSecagem(BigDecimal.ZERO);
					subProdutoItem.setQtDescImpur(BigDecimal.ZERO);
					subProdutoItem.setQtDescUmid(BigDecimal.ZERO);
					subProdutoItem.setQtDescChuv(BigDecimal.ZERO);
					subProdutoItem.setRendaSemDescDpi(BigDecimal.ZERO);
					subProdutoItem.setQtAlocada(BigDecimal.ZERO);
					subProdutoItem.setVlRecepcao(BigDecimal.ZERO);
					subProdutoItem.setQtRecepcao(BigDecimal.ZERO);
					subProdutoItem.setVlSecagem(BigDecimal.ZERO);
					subProdutoItem.setQtSecagem(BigDecimal.ZERO);
					
					PrecoBuscaDto buscarPreco = precoService.buscarPor(
							subProdutoItem.getCodEstabel(), 
							subProdutoItem.getCodProduto(), 
							subProdutoItem.getCodRefer(), 
							false);
					
					subProdutoItem.setVlFiscal(buscarPreco.getPrecoFiscal());
					subProdutoItem.setVlPonto(buscarPreco.getPrecoFechamento());
					
					subProdutoItemList.add(subProdutoItem);
				}
				
				subProduto.setItens(subProdutoItemList);
				response.add(subProduto);
			}
		}
		
		return response;
	}

	public RecEntrega buscarRe(String codEstabel, Long nrRe) {
		
		RecEntrega recEntrega = recEntregaRep.findByCodEstabelAndNrRe(codEstabel, nrRe).orElse(null);
		
		if(recEntrega== null) {
			throw new NotFoundException("RecEntrega não encontrado com os seguinte parâmetros: cod. estabelecimento: " + codEstabel + " número RE: " + nrRe);
		}
		
		return recEntrega;
	}
	
	//API Integração da RE
	public List<NumeroReDto> gerarPayloadIntegracaoReComErp(List<NumeroReDto> numerosReDto) {
		if(numerosReDto != null) {
			List<NumeroReDto> response = erpExtService.buscarNumeroRe(numerosReDto);
			
			if(response != null) {
				
				RecEntregaIntegrationDto integrationDto = new RecEntregaIntegrationDto();
				integrationDto.setRecEntrega(new ArrayList<>());
				
				for (NumeroReDto numeroReDto : response) {
					if(numeroReDto.getNroDocto() != null) {

						Long nrRe = numeroReDto.getNroDocto();
						log.info("Atualizando a RecEntrega: "+ numeroReDto.getIdGenesis() + " com o Nr RE: "+ nrRe);
						
						try {
							Optional<RecEntrega> result = recEntregaRep.findByCodEstabelAndId(numeroReDto.getCodEstabel(), numeroReDto.getIdGenesis());
							RecEntrega recEntrega = result.get();
							if(recEntrega != null) {
								
								recEntrega.setNrRe(nrRe);
								recEntrega.setDataIntegracao(new Date());
								recEntrega.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
								
								Long numeroReDpi = pesquisarNumeroReNaLista(response, recEntrega.getNrReDpi(), recEntrega.getCodEstabel());
								if(numeroReDpi != null) recEntrega.setNrReDpi(numeroReDpi);
								
								Long numeroReTxSpRetida = pesquisarNumeroReNaLista(response, recEntrega.getNrReTxSpRetida(), recEntrega.getCodEstabel());
								if(numeroReTxSpRetida != null) recEntrega.setNrReTxSpRetida(numeroReTxSpRetida);
								
								Long numeroRePriEnt = pesquisarNumeroReNaLista(response, recEntrega.getNrPriEnt(), recEntrega.getCodEstabel());
								if(numeroRePriEnt != null) recEntrega.setNrPriEnt(numeroRePriEnt);
								
								Long numeroReOrig = pesquisarNumeroReNaLista(response, recEntrega.getNrReOrig(), recEntrega.getCodEstabel());
								if(numeroReOrig != null) recEntrega.setNrReOrig(numeroReOrig);
								
								Long numeroRePai = pesquisarNumeroReNaLista(response, recEntrega.getNrRePai(), recEntrega.getCodEstabel());
								if(numeroRePai != null) recEntrega.setNrRePai(numeroRePai);
								
								for(RecEntregaItem recEntregaItem : recEntrega.getItens()) {
									recEntregaItem.setNrRe(nrRe);
									recEntregaItem.setDataIntegracao(new Date());
									recEntregaItem.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
								}
								
//								recEntrega = recEntregaRep.save(recEntrega);
								
								List<MovimentoRe> listaMovimentoRe = new ArrayList<MovimentoRe>();
								MovimentoRe movimentoRe = movimentoReService.findByCodEstabelAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
								if(movimentoRe != null) {
									movimentoRe.setNrRe(nrRe);
									movimentoRe.setDataIntegracao(new Date());
									movimentoRe.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
									movimentoRe = movimentoReService.salvar(movimentoRe);
									listaMovimentoRe.add(movimentoRe);
								}
								
								BaixaCredito baixaCredito = null;
								try {
									baixaCredito = baixaCreditoService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
									baixaCredito.setNrRe(nrRe);
									baixaCredito.setDataIntegracao(new Date());
									baixaCredito.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
									baixaCredito = baixaCreditoService.salvar(baixaCredito);
								}
								catch (Exception e) {
									log.info(e.getMessage());
								}
								
								BaixaCreditoMov baixaCreditoMov = null;
								try {
									baixaCreditoMov = baixaCreditoMovService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
									baixaCreditoMov.setNrRe(nrRe);
									baixaCreditoMov.setDataIntegracao(new Date());
									baixaCreditoMov.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
									baixaCreditoMov = baixaCreditoMovService.salvar(baixaCreditoMov);
								}
								catch (Exception e) {
									log.info(e.getMessage());
								}
								
								integrationDto.getRecEntrega().add(
										RecEntregaDto.construir(recEntrega, listaMovimentoRe, baixaCredito, baixaCreditoMov));
								
							}
						} catch (Exception e) {
							log.error("Gerar Payload Integracao Re Com Erp: " + e.getMessage());
						}
						
					}
				}
				
				if(!integrationDto.getRecEntrega().isEmpty()) {
					integrarReComErp(integrationDto);
				}
				
			}
			
			return response;
		}
		
		return Collections.emptyList();
	}
	
	//API para REENVIO da RE ao DataSul
	public void gerarPayloadReenvioIntegracaoReComErp(String codEstabel, Long idGenesis) {
		
		RecEntregaIntegrationDto integrationDto = new RecEntregaIntegrationDto();
		integrationDto.setRecEntrega(new ArrayList<>());
			
		try {
			Optional<RecEntrega> result = recEntregaRep.findByCodEstabelAndId(codEstabel, idGenesis);
			RecEntrega recEntrega = result.get();
			
			if(recEntrega != null) {
				
				recEntrega.setDataIntegracao(new Date());
				recEntrega.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
				
				for(RecEntregaItem recEntregaItem : recEntrega.getItens()) {
					//recEntregaItem.setNrRe(nrRe);
					recEntregaItem.setDataIntegracao(new Date());
					recEntregaItem.setStatusIntegracao(StatusIntegracao.PROCESSANDO);
				}
				
								
				List<MovimentoRe> listaMovimentoRe = new ArrayList<MovimentoRe>();
				MovimentoRe movimentoRe = movimentoReService.findByCodEstabelAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
				
				if(movimentoRe != null) {
					listaMovimentoRe.add(movimentoRe);
				}
								
				BaixaCredito baixaCredito = null;
				try {
					baixaCredito = baixaCreditoService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
				}
				catch (Exception e) {
					log.info(e.getMessage());
				}
								
				BaixaCreditoMov baixaCreditoMov = null;
				try {
					baixaCreditoMov = baixaCreditoMovService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
					
				}
				catch (Exception e) {
					log.info(e.getMessage());
				}
								
				integrationDto.getRecEntrega().add(
					RecEntregaDto.construir(recEntrega, listaMovimentoRe, baixaCredito, baixaCreditoMov));
				}					
							
		}
		catch (Exception e) {
			log.error("Gerar Payload Integracao Re Com Erp: " + e.getMessage());
		}
							
		if(!integrationDto.getRecEntrega().isEmpty()) {
			integrarReComErp(integrationDto);
		}
		
	}	
	
	private Long pesquisarNumeroReNaLista(List<NumeroReDto> numeroReLista, Long numeroRe, String codigoEstabelecimento) {
		NumeroReDto numeroReDto = numeroReLista.stream().filter(item -> {
			Boolean osEstabelecimentosSaoIguais = item.getCodEstabel().equalsIgnoreCase(codigoEstabelecimento); 
			Boolean osIdsSaoIguais = item.getIdGenesis().equals(numeroRe);
			return  osEstabelecimentosSaoIguais && osIdsSaoIguais;
		}).findFirst().orElse(null);
		
		if(numeroReDto == null) return null;
		return numeroReDto.getNroDocto();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void integrarReComErp(RecEntregaIntegrationDto integrationDto) {
		IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.ENTRADA_PRODUCAO, FuncionalidadePaginaEnum.INT_IND_ENVIO_RE);
    	if(pagina == null || ERP.equals(pagina.getOrigenEnum())) return; 
    	

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_IND_ENVIO_RE);
		if(paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null || paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) return;
		
		try {
			
			IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
			
			LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();    		
    		if(pagina.getHeaders() != null) {
	    		for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
	    			if(pagina.isHearderProfileActive(header, profileActive)) {
		    			if(!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())){
		    				mvmap.add(header.getChave(), header.getValor());
		    			}
	    			}
	    		}	
    		}
    		
    		String url = pagina.getUrlPrincipalApi(profileActive);
    		if(url == null) throw new NullPointerException("A URL da funcionalidade(INT_IND_ENVIO_RE) não foi encontada." );;
    		
    		WebClient client = WebClient.builder().baseUrl(url)
    				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    				.filter(ExchangeFilterFunctions.basicAuthentication(auth.getLogin(), auth.getSenha()))
    				.build();
    		    		
    		HttpMethod httpMethod = IntegrationService.getMetodo(paginaFuncionalidade);

    		Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);
    		client.method(httpMethod)
    		.uri(paginaFuncionalidade.getEndPointSend())
			.headers(consumer)
    		.body(BodyInserters.fromValue(integrationDto))
    		.retrieve()
    		.bodyToFlux(RecEntregaIntegrationDto.class)
    		.subscribe(result -> {
    			if(!CollectionUtils.isEmpty(result.getRecEntrega())) {
    				for(RecEntregaDto recEntregaDto : result.getRecEntrega()) {
    					Long idRecEntrega = Long.parseLong(recEntregaDto.getIdRe());
    					
    					StatusIntegracao statusRecEntrega = recEntregaDto.getIntegrated() ? StatusIntegracao.INTEGRADO : StatusIntegracao.INTEGRAR;
						Boolean gerarNfRemessa = recEntregaDto.getIntegrated();
						
						for(RecEntregaItemDto recEntregaItemDto : recEntregaDto.getItemRecEntrega()) {
							StatusIntegracao status = recEntregaItemDto.getIntegrated() ? StatusIntegracao.INTEGRADO : StatusIntegracao.INTEGRAR;
							recEntregaRep.updateIntegracaoRecEntregaItem(new Date(), recEntregaItemDto.getIntegrated(), 
									status, Long.parseLong(recEntregaItemDto.getIdItRe()));
							
							if(status.equals(StatusIntegracao.INTEGRAR)) {
								statusRecEntrega = StatusIntegracao.INTEGRAR;
								gerarNfRemessa = false;
							}
						}							
						
						if(recEntregaDto.getMovRe() != null) {
							for(MovimentoReDto movimentoReDto :  recEntregaDto.getMovRe() ) {
								if(movimentoReDto.getIdMovRe() != null) {
									MovimentoRe movimentoRe = movimentoReService.findByIdMovRe(movimentoReDto.getIdMovRe());
									if(movimentoRe != null) {
										movimentoRe.setStatusIntegracao(movimentoReDto.getIntegrated() ? StatusIntegracao.INTEGRADO : StatusIntegracao.INTEGRAR);
										movimentoRe.setLogIntegrado(movimentoReDto.getIntegrated());
										movimentoRe.setDataIntegracao(new Date());
										
										if(movimentoRe.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR)) {
											statusRecEntrega = StatusIntegracao.INTEGRAR;
											gerarNfRemessa = false;
										}
										
										movimentoReService.salvar(movimentoRe);
									}
								}
							}
						}
						
						if(recEntregaDto.getBxaCredito() != null && !recEntregaDto.getBxaCredito().isEmpty()) {
							BaixaCredito baixaCredito = baixaCreditoService.findByCodEstabAndIdRe(recEntregaDto.getCodEstabel(), idRecEntrega);
							BaixaCreditoDto baixaCreditoDto = recEntregaDto.getBxaCredito().get(0);
							if(baixaCredito != null) {
								Boolean integrado = baixaCreditoDto.getIntegrated();
								baixaCredito.setStatusIntegracao(integrado ? StatusIntegracao.INTEGRADO : StatusIntegracao.INTEGRAR);
								baixaCredito.setDataIntegracao(new Date());
								baixaCredito.setLogIntegrado(integrado);
								
								if(baixaCredito.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR)) {
									statusRecEntrega = StatusIntegracao.INTEGRAR;
									gerarNfRemessa = false;
								}
								
								baixaCreditoService.salvar(baixaCredito);
							}
							
							if(baixaCreditoDto != null && baixaCreditoDto.getBxaCreditoMov() != null) {
								for(BaixaCreditoMovDto baixaCreditoMovDto : baixaCreditoDto.getBxaCreditoMov()) {
									BaixaCreditoMov baixaCreditoMov = baixaCreditoMovService.findByIdMovtoBxaCredAndCodEstabel(
											baixaCreditoMovDto.getIdMovtoBxaCred(), recEntregaDto.getCodEstabel());
									if(baixaCreditoMov != null) {
										baixaCreditoMov.setStatusIntegracao(baixaCreditoMovDto.getIntegrated() ? StatusIntegracao.INTEGRADO : StatusIntegracao.INTEGRAR);
										baixaCreditoMov.setDataIntegracao(new Date());
										
										if(baixaCreditoMov.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR)) {
											statusRecEntrega = StatusIntegracao.INTEGRAR;
											gerarNfRemessa = false;
										}
										
										baixaCreditoMovService.salvar(baixaCreditoMov);
									}
								}
							}
						}						
						
						recEntregaRep.updateIntegracaoRecEntrega(new Date(), recEntregaDto.getLogIntegrado(), 
								statusRecEntrega, idRecEntrega);
						
						if(gerarNfRemessa) {
							atualizarCampoCodSit(MovimentoReEnum.ENVIO_RE_DATASUL, idRecEntrega);
							BigDecimal quantidadeTotal = recEntregaRep.quantidadeTotalRendaLiquida(recEntregaDto.getCodEstabel(), idRecEntrega);
							nfRemessaService.gerarNfRemessa(
									recEntregaDto.getCodEstabel(), 
									idRecEntrega,
									recEntregaDto.getNrRe(), 
									quantidadeTotal);
						} 
    				}
    			}
    		});			

		}catch (Exception e) {
			System.out.println("Erro de integração da RE: "+ e.getMessage());
			updateTabelasIntegracaoReParaStatusIntegrar(integrationDto);
		}
	}
	
	private void updateTabelasIntegracaoReParaStatusIntegrar(RecEntregaIntegrationDto integrationDto) {
		if(integrationDto.getRecEntrega() != null) {
			for(RecEntregaDto recEntregaDto : integrationDto.getRecEntrega()) {
				
				RecEntrega recEntrega = findById(Long.parseLong(recEntregaDto.getIdRe()));
				if(recEntrega != null) {
					recEntrega.setStatusIntegracao(StatusIntegracao.INTEGRAR);
					
					for(RecEntregaItem recEntregaItem : recEntrega.getItens()) {
						recEntregaItem.setStatusIntegracao(StatusIntegracao.INTEGRAR);
					}
					recEntregaRep.save(recEntrega);
					
					MovimentoRe movimentoRe = movimentoReService.findByCodEstabelAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
					if(movimentoRe != null) {
						movimentoRe.setStatusIntegracao(StatusIntegracao.INTEGRAR);
						movimentoRe = movimentoReService.salvar(movimentoRe);
					}
					
					BaixaCredito baixaCredito = baixaCreditoService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
					if(baixaCredito != null) {
						baixaCredito.setStatusIntegracao(StatusIntegracao.INTEGRAR);
						baixaCredito = baixaCreditoService.salvar(baixaCredito);
					}
					
					BaixaCreditoMov baixaCreditoMov = baixaCreditoMovService.findByCodEstabAndIdRe(recEntrega.getCodEstabel(), recEntrega.getId());
					if(baixaCreditoMov != null) {
						baixaCreditoMov.setStatusIntegracao(StatusIntegracao.INTEGRAR);
						baixaCreditoMov = baixaCreditoMovService.salvar(baixaCreditoMov);
					}
				}
				
			}
		}
	}
	
	public Page<RecEntrega> buscarRomaneios(Pageable pageable, RecEntregaFilter filter) {
		log.info("Listando os romaneios...");
		return recEntregaRep.buscarRomaneios(pageable, filter);
	}

	public void atualizarCampoCodSit(MovimentoReEnum movimentoRe, Long id) {
		recEntregaRep.updateCodSitRecEntrega(movimentoRe.getCodSit(), id);
	}
	
	public void atualizarCampoCodSit(MovimentoReEnum movimentoRe, List<Long> ids) {
		recEntregaRep.updateCodSitRecEntrega(movimentoRe.getCodSit(), ids);
	}
}














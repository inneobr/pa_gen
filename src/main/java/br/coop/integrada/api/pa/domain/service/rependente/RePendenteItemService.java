package br.coop.integrada.api.pa.domain.service.rependente;

import static br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum.CHUVA_AVARIADO;
import static br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum.IMPUREZA;
import static br.coop.integrada.api.pa.domain.enums.grupo.produto.ReferenciaEnum.TIPO;
import static br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum.ENTRADA;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.BuscaTabelaClassificacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoBuscaDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaValidDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRecepcaoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoValorSecagemResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RepPendenteItemResponse;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaItemRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRep;
import br.coop.integrada.api.pa.domain.repository.rependente.RePendenteItemRep;
import br.coop.integrada.api.pa.domain.service.PrecoService;
import br.coop.integrada.api.pa.domain.service.classificacao.ClassificacaoService;
import br.coop.integrada.api.pa.domain.service.parametros.ItemAvariadoService;
import br.coop.integrada.api.pa.domain.service.parametros.TaxaService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.service.semente.produtor.SementeProdutorService;

@Service
public class RePendenteItemService {
	private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
	
	@Autowired
	private RePendenteItemRep rePendenteItemRep;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemAvariadoService itemAvariadoService;
	
	@Autowired
	private GrupoProdutoService grupoProdutoService;
	
	@Autowired
	private ClassificacaoService classificacaoService;
	
	@Autowired
	private SementeProdutorService sementeProdutorService;
	
	@Autowired
	private PrecoService precoService;
	
	@Autowired
	private TaxaService taxaService;
	
	@Autowired
	private RecEntregaItemRep recEntregaItemRep;
	
	@Autowired
	private RecEntregaRep recEntregaRep;
	
	@Lazy
	@Autowired
	private RePendenteService rePendenteService;

	public RePendenteItem buscarPorRePendenteECodigoProdutoEReferencia(
			RePendente rePendente, String codigoProduto, String referencia) {
		return rePendenteItemRep.findByRePendenteAndProdutoCodItemAndReferencia(rePendente, codigoProduto, referencia).orElse(null);
	}
	
	public RePendenteItem converterDto(RePendente rePendente, RePendenteItemDto rePendenteItemDto) {
		RePendenteItem obj = null;
		
		if(rePendente.getId() != null) {
			buscarPorRePendenteECodigoProdutoEReferencia(rePendente, rePendenteItemDto.getProdutoCodigo(), rePendenteItemDto.getReferencia());
		}
		
		if(obj == null) {
			obj = new RePendenteItem();
		}
		
		BeanUtils.copyProperties(rePendenteItemDto, obj);
		
		Produto produto = produtoService.buscarProdutoAtivoPorCodItem(rePendenteItemDto.getProdutoCodigo());
		obj.setProduto(produto);
		obj.setRePendente(rePendente);
		
		return obj;
	}
	
	public List<RePendenteItem> converterDto(RePendente rePendente, List<RePendenteItemDto> rePendenteItemDtos) {
		if(CollectionUtils.isEmpty(rePendenteItemDtos)) return Collections.emptyList();
		
		return rePendenteItemDtos.stream().map(rePendenteItemDto -> {
			return converterDto(rePendente, rePendenteItemDto);
		}).toList();
	}
	
	public void validarPercentualChuvadoAvariadoComGrupoProduto(GrupoProduto grupoProduto, BigDecimal percentualChuvadoAvariado) {
		if(grupoProduto.getEntradaRe() != null) {
			if(grupoProduto.getEntradaRe().equals(EntradaReEnum.NAO_PERMITE)) {
				throw new ObjectDefaultException("Esse grupo de produto \"" + grupoProduto.getFmCodigo() + "\" não permite entrada de RE.");
			}
			else if(grupoProduto.getEntradaRe().equals(EntradaReEnum.CONDICAO)){
				String condicao = grupoProduto.getCondChuvAvarSinal();
				BigDecimal valor = grupoProduto.getCondChuvAvarValor() == null ? BigDecimal.ZERO : grupoProduto.getCondChuvAvarValor();
				
				if((condicao.equalsIgnoreCase(">")  && percentualChuvadoAvariado.compareTo(valor) > 0)
				|| (condicao.equalsIgnoreCase(">=") && percentualChuvadoAvariado.compareTo(valor) >= 0)
				|| (condicao.equalsIgnoreCase("<")  && percentualChuvadoAvariado.compareTo(valor) < 0)
				|| (condicao.equalsIgnoreCase("<=") && percentualChuvadoAvariado.compareTo(valor) <= 0)) {
					throw new ObjectDefaultException("A classificação dessa família esta fora das condições parametrizadas para o grupo de produto " + grupoProduto.getFmCodigo() + ", não é possível prosseguir");
				}
			}
			
		}
	}

	public void validarEIncluirDadosConformeParametrizacao(RePendente rePendente, RePendenteItemDto rePendenteItemDto) {
		if(!rePendenteItemDto.getQualidadeProdutoNormal() && !rePendenteItemDto.getQualidadeProdutoTerra() 
				&& !rePendenteItemDto.getQualidadeProdutoSementeira() && !rePendenteItemDto.getQualidadeProdutoVagem()) {
			throw new ObjectDefaultException("É obrigatório informar a(s) qualidade(s) do produto.");
		}
		
		if(rePendente.getProdPadr() && rePendenteItemDto.getDesmembramento()) {
			throw new ObjectDefaultException("Não pode lançar desmembramento quando a opção \"Produto Padronizado\" estiver ativo.");
		}
		
		ItemAvariadoDetalhe itemAvariadoDetalhe = itemAvariadoService.buscarPor(
				rePendente.getGrupoProduto().getFmCodigo(), rePendente.getEstabelecimento().getCodigo(),
				rePendenteItemDto.getChuvadoAvariado(), rePendenteItemDto.getPhEntrada(), rePendenteItemDto.getFnt());
		
		if(itemAvariadoDetalhe != null) {
			rePendenteItemDto.setProdutoCodigo(itemAvariadoDetalhe.getProduto().getCodItem());
			
			if(itemAvariadoDetalhe.getProdutoReferencia() != null) { 
				rePendenteItemDto.setReferencia(itemAvariadoDetalhe.getProdutoReferencia().getCodRef());
			}
			// Produto corrigido automaticamente, de acordo com a parametrização
			// Para esse % de chuvado avariado, deverá ser utilizado outro código de item, o código será substituído automaticamente
		}
		
		// VALIDAR SE O PRODUTO EXISTE E SE O GRUPO DE PRODUTO DA RE ESTÁ VINCULADO AO PRODUTO
		produtoService.validarProdutoEGrupoProduto(rePendenteItemDto.getProdutoCodigo(), rePendente.getGrupoProduto().getFmCodigo());
		
		// VERIFICAR SE A REFERENCIA EH OBRIGATORIA CONFORME A PARAMETRIZACAO DO GRUPO DE PRODUTO
		if(TIPO.equals(rePendente.getGrupoProduto().getReferFixado()) && Strings.isEmpty(rePendenteItemDto.getReferencia())) {
			throw new ObjectDefaultException("Produto necessita de uma referência. Favor Validar");
		}
		
		// VALIDAR SE A REFERENCIA ESTA VINCULADA AO PRODUTO
//		if(Strings.isNotEmpty(rePendenteItemDto.getReferencia())) {
//			ProdutoReferenciaValidDto produtoReferenciaValidDto = produtoService.getProdutoReferencia(rePendenteItemDto.getProdutoCodigo(), rePendenteItemDto.getReferencia());
//			if(!produtoReferenciaValidDto.isCadastrado()) {
//				throw new ObjectDefaultException("Referência não utilizada para o item");
//			}
//		}
		
		// VERIFICAR SE O CAMPO LOTE EH OBRIGATORIO CONFORME A PARAMETRIZACAO DO GRUPO DE PRODUTO
		if(rePendente.getGrupoProduto().isLote() && Strings.isEmpty(rePendenteItemDto.getLote())) {
			throw new ObjectDefaultException("Produto controlado por lote, favor informar o lote.");
		}
		
		// VERIFICAR LOTE CADASTRADO PAP-992			 
		RepPendenteItemResponse repPendenteItemResponse = getLoteUtilizado(rePendente.getEstabelecimento().getCodigo(), rePendenteItemDto.getProdutoCodigo(), rePendenteItemDto.getLote());
		if(repPendenteItemResponse.getStatus()) {
			throw new ObjectDefaultException(repPendenteItemResponse.getMessage());
		}
		
		// VERIFICAR SE O TBM EH MAIOR QUE 100%
		BigDecimal tbmMaximo = new BigDecimal("100");
		if(rePendenteItemDto.getTbm() != null && rePendenteItemDto.getTbm().compareTo(tbmMaximo) > 0) {
			throw new ObjectDefaultException("Percentual de Trigo/Bandinha/Milho quebrado deve ser menor ou igual 100%");
		}
		
		// VERIFICAR SE O BANDINHA EH MAIOR QUE 100%
		BigDecimal bandinhaMaximo = new BigDecimal("100");
		if(rePendenteItemDto.getPercentualBandinha() != null && rePendenteItemDto.getPercentualBandinha().compareTo(bandinhaMaximo) > 0) {
			throw new ObjectDefaultException("Percentual de Bandinha deve ser menor ou igual 100%");
		}
		
		// NAO PERMITIR TBM QUANDO O PRODUTO FOR UM SUBPRODUTO
		if(rePendenteItemDto.getTbm() != null && rePendenteItemDto.getTbm().compareTo(BigDecimal.ZERO) > 0) {
			Boolean isSubProduto = grupoProdutoService.validarProdutoeSubProduto(rePendenteItemDto.getProdutoCodigo());
			
			if(isSubProduto) {
				throw new ObjectDefaultException("Esse produto é um sub produto, não é possível ter um tbm maior que 0.");
			}
		}
		
		GrupoProduto grupoProduto = rePendente.getGrupoProduto();
		Calendar dataEntrada = Calendar.getInstance();
		dataEntrada.setTime(rePendente.getDtEntrada());
		Integer safra = dataEntrada.get(Calendar.YEAR);
		
		if(grupoProduto.isFnt() && rePendenteItemDto.getFnt().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ObjectDefaultException("Necessário informar a classificação de FNT.");
		}
		
		validarPercentualChuvadoAvariadoComGrupoProduto(grupoProduto, rePendenteItemDto.getChuvadoAvariado());
		
		// VERIFICAR SE O GRUPO DE PRODUTO ESTA PARAMETRIZADO PARA TIPO CHUVADO/AVARIADO
		if(grupoProduto.isChuvAvar()) {
			BuscaTabelaClassificacaoDto tabelaDto = classificacaoService.buscarTabelaClassificacaoDetalhe(
					CHUVA_AVARIADO, rePendente.getEstabelecimento().getCodigo(), grupoProduto.getId(),
					safra, BigInteger.ZERO, rePendenteItemDto.getChuvadoAvariado());
			
			if(!tabelaDto.getCadastrado()) {
				throw new ObjectDefaultException("Tabela de Chuvado e Avariado não cadastrada para este percentual");
			}
			
			rePendenteItemDto.setPercentualDescontoChuvadoAvariado(tabelaDto.getPercentualDesconto());
		}
		
		// VERIFICAR SE O GRUPO DE PRODUTO ESTA PARAMETRIZADO PARA TIPO IMPUREZA
		if(grupoProduto.isImpureza()) {
			BuscaTabelaClassificacaoDto tabelaDto = classificacaoService.buscarTabelaClassificacaoDetalhe(
					IMPUREZA, rePendente.getEstabelecimento().getCodigo(), grupoProduto.getId(),
					safra, BigInteger.ZERO, rePendenteItemDto.getImpureza());
			
			if(!tabelaDto.getCadastrado()) {
				throw new ObjectDefaultException("Tabela de Impureza não cadastrada para este percentual");
			}
			
			rePendenteItemDto.setPercentualDescontoImpureza(tabelaDto.getPercentualDesconto());
		}
		
		// VERIFICAR SE O GRUPO DE PRODUTO ESTA PARAMETRIZADO PARA TIPO UMIDADE
		if(grupoProduto.isUmidade()) {
			BuscaTabelaClassificacaoDto tabelaDto = classificacaoService.buscarTabelaClassificacaoDetalhe(
					TipoClassificacaoEnum.UMIDADE, rePendente.getEstabelecimento().getCodigo(), grupoProduto.getId(),
					safra, BigInteger.ZERO, rePendenteItemDto.getUmidade());
			
			if(!tabelaDto.getCadastrado()) {
				throw new ObjectDefaultException("Tabela de Umidade não cadastrada para este percentual");
			}
			
			rePendenteItemDto.setPercentualDescontoUmidade(tabelaDto.getPercentualDesconto());
			rePendenteItemDto.setTabelaUmidadeTaxaSecagemKg(tabelaDto.getTaxaSecagemQuilo());
			rePendenteItemDto.setTabelaUmidadeTaxaSecagemValor(tabelaDto.getTaxaSecagemValor());
		}
		
		// VERIFICAR SE O GRUPO DE PRODUTO ESTA PARAMETRIZADO PARA PH ENTRADA
		if(grupoProduto.isPhEntrada()) {
			BuscaTabelaClassificacaoDto tabelaDto = classificacaoService.buscarTabelaClassificacaoDetalhe(
					TipoClassificacaoEnum.PH, rePendente.getEstabelecimento().getCodigo(), grupoProduto.getId(),
					0, rePendenteItemDto.getPhEntrada(), rePendenteItemDto.getUmidade());
			
			if(!tabelaDto.getCadastrado()) {
				throw new ObjectDefaultException("PH Informado não cadastrado em Tabela de Melhoria de PH");
			}
			
			BigInteger phCorrigido = tabelaDto.getPhCorrigido();
			rePendenteItemDto.setPhCorrigido(phCorrigido);
			
			String codigoReferencia = tabelaDto.getCodigoReferencia();
			BigInteger phMinimo = BigInteger.valueOf(grupoProduto.getPhMinimo());
			Boolean isPhCorrigidoEhMenorQuePhMinimo = phCorrigido.compareTo(phMinimo) < 0; 
			
			if(isPhCorrigidoEhMenorQuePhMinimo) {
				String codigoGrupoProdutoSub = grupoProduto.getFmCodigoSub();
				GrupoProduto subGrupoProduto =  grupoProdutoService.buscarGrupoFmCodigo(codigoGrupoProdutoSub);
				rePendente.setGrupoProduto(subGrupoProduto);
				
				String codigoProdutoSub = grupoProduto.getItSubCoop();
				rePendenteItemDto.setProdutoCodigo(codigoProdutoSub);
				
				rePendenteItemDto.setReferencia("");
				BigDecimal calculoImpureza = rePendenteItemDto.getImpureza().add(subGrupoProduto.getPercImpureza());
				rePendenteItemDto.setImpureza(calculoImpureza);
				rePendenteItemDto.setTbm(BigDecimal.ZERO);
			}
			
			rePendenteItemDto.setReferencia(codigoReferencia);
		}
		
		// VERIFICAR SE O GRUPO DE PRODUTO ESTA PARAMETRIZADO PARA TIPO SEMENTE
		if(grupoProduto.isSemente()) {
			
			if(rePendente.getClasse() == null) {
				throw new ObjectDefaultException("O campo classe não foi informado.");
			}
			
			SementeCampo sementeCampo = sementeProdutorService.buscarSementeCampoPor(
					safra, rePendente.getNrOrdCampo(), rePendente.getEstabelecimento().getCodigo(),
					grupoProduto.getFmCodigo(), rePendente.getClasse().getCodigo());
			
			String produtoVinculadoAoSementeCampo = sementeCampo.getProduto().getCodItem();
			Boolean isProdutoReEhIgualAoProdutoVinculadoAoSementeCampo = rePendenteItemDto.getProdutoCodigo().equals(produtoVinculadoAoSementeCampo);
			if(!isProdutoReEhIgualAoProdutoVinculadoAoSementeCampo) {
				// Item informado diferente do item do Laudo, será substituído automaticamente
				rePendenteItemDto.setProdutoCodigo(produtoVinculadoAoSementeCampo);
			}
			
			SementeLaudoInspecao laudoInspecao = sementeProdutorService.buscarSementeLaudoInspecaoPor(
					safra, rePendente.getEstabelecimento().getCodigo(), rePendente.getNrLaudo(),
					rePendente.getNrOrdCampo(), grupoProduto.getFmCodigo());
			
			String produtoVinculadoAoLaudo = laudoInspecao.getProduto().getCodItem();
			Boolean isProdutoReEhIgualAoProdutoVinculadoAoLaudo = rePendenteItemDto.getProdutoCodigo().equals(produtoVinculadoAoLaudo);
			if(!isProdutoReEhIgualAoProdutoVinculadoAoLaudo) {
				// Item informado diferente do item do Laudo, será substituído automaticamente
				rePendenteItemDto.setProdutoCodigo(produtoVinculadoAoLaudo);
			}
		}

		PrecoBuscaDto precoDto = null;
		
		try {
			precoDto = precoService.buscarPor(rePendente.getEstabelecimento().getCodigo(),
					rePendenteItemDto.getProdutoCodigo(), rePendenteItemDto.getReferencia(), rePendente.getGrupoProduto().isCafeCoco());
		}
		catch (Exception e) {
			StringBuilder produto = new StringBuilder();
			produto.append(rePendenteItemDto.getProdutoCodigo());
			
			if(Strings.isNotBlank(rePendenteItemDto.getProdutoDescricao())) {
				produto.append(" ");
				produto.append(rePendenteItemDto.getProdutoDescricao());
			}
			
			if(Strings.isNotBlank(rePendenteItemDto.getReferencia())) {
				produto.append(" - ");
				produto.append(rePendenteItemDto.getReferencia());
			}
			
			throw new ObjectDefaultException("Não foi encontrado preço valido para o produto " + produto.toString() + ", favor verificar com SCO.");
		}
		
		if(precoDto.getPrecoFiscal() == null || precoDto.getPrecoFiscal().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ObjectDefaultException("Preço fiscal está zerado, favor verificar com SCO.");
		}
		
		if(precoDto.getPrecoFechamento() == null || precoDto.getPrecoFechamento().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ObjectDefaultException("Preço de fechamento está zerado, favor verificar com SCO.");
		}
		
		rePendenteItemDto.setValorFiscal(precoDto.getPrecoFiscal());
		rePendenteItemDto.setValorPonto(precoDto.getPrecoFechamento());
		
		
		// -----------------------------------
		// VERIFICAR TAXAS RE
		// -----------------------------------
		VerificarTaxasReDto verificarTaxasReDto = new VerificarTaxasReDto();
		verificarTaxasReDto.setFmCodigo(rePendente.getGrupoProduto().getFmCodigo());		
		verificarTaxasReDto.setGmo(rePendente.getTipoGmo() == null ? null : rePendente.getTipoGmo().getId());
		verificarTaxasReDto.setDeclaradaTestada(rePendente.getTipoRr());
		verificarTaxasReDto.setCodProdutor(rePendente.getProdutor().getCodProdutor());
		verificarTaxasReDto.setTbm(rePendenteItemDto.getTbm());
		verificarTaxasReDto.setCodEstabelecimento(rePendente.getEstabelecimento().getId());
		verificarTaxasReDto.setSafra(safra);
		verificarTaxasReDto.setProdutoPadronizado(rePendente.getProdPadr());
		verificarTaxasReDto.setDescargaUnidade(rePendente.getDescarUnid());
		
		VerificarTaxasReResponseDto taxaRe = rePendenteService.verificarTaxasRe(verificarTaxasReDto);
		rePendenteItemDto.setTabelaValorRecepcao(taxaRe.getValorRecepcao());
		rePendenteItemDto.setIndiceDpi(taxaRe.getRetemDPI());
		rePendenteItemDto.setPercentualDpi(taxaRe.getPercentualDpi());
		rePendenteItemDto.setObservacaoDpi(taxaRe.getObservacaoRE());
		rePendenteItemDto.setTeraSubProduto(taxaRe.getSubProduto());
		rePendenteItemDto.setFamiliaSubProduto(taxaRe.getGrupoProdutoSub());
		rePendenteItemDto.setItemSubProduto(taxaRe.getProdutoSub());
		rePendenteItemDto.setIndiceSecagemLimpeza(taxaRe.getSecagem());
		rePendenteItemDto.setTipoCobrancaSecagem(taxaRe.getCobranca());
		rePendenteItemDto.setItemParaSecagem(taxaRe.getProduto());
		rePendenteItemDto.setIncideTaxaRecepcao(taxaRe.getRecepcao());
		rePendenteItemDto.setValorRecepcao(taxaRe.getValorRecepcao());
		
		if(taxaRe.getSecagem()) {
			if(taxaRe.getCobranca().equals("Renda")) {
				BigDecimal valorTaxaLimpeza = rePendenteItemDto.getTabelaUmidadeTaxaSecagemValor();
				CalculoValorSecagemResponseDto calculoValorSecagemResponseDto =  rePendenteService.calcularQtdSecagem(rePendente.getPesoLiquido(),
						rePendenteItemDto.getPercentualDescontoImpureza(),  valorTaxaLimpeza,  rePendenteItemDto.getValorPonto());
				
				rePendenteItemDto.setQtdSecagem(calculoValorSecagemResponseDto.getQuantidadeSecagem());
				rePendenteItemDto.setValorCalculadoSecagem(calculoValorSecagemResponseDto.getValorSecagem());
			}
		}
		
		if(taxaRe.getRecepcao() != null && taxaRe.getRecepcao()) {
			CalculoRecepcaoResponseDto calculoRecepcaoResponseDto = rePendenteService.calcularRecepcao(
					rePendente.getPesoLiquido(), rePendenteItemDto.getValorPonto(), taxaRe.getValorRecepcao());
			
			rePendenteItemDto.setQtdRecepcao(calculoRecepcaoResponseDto.getQuantidadeRecepcao());
			rePendenteItemDto.setValorRecepcao(calculoRecepcaoResponseDto.getValorRecepcao());
		}
		
		CalculoRendaLiquidaDto paramRendaLiquida = new CalculoRendaLiquidaDto();
		paramRendaLiquida.setCalculaSecagem(taxaRe.getSecagem() && taxaRe.getCobranca().equals("RE"));
		paramRendaLiquida.setPesoLiquido(rePendente.getPesoLiquido());
		paramRendaLiquida.setPercDescImpureza(rePendenteItemDto.getPercentualDescontoImpureza());
		paramRendaLiquida.setPercDescUmidade(rePendenteItemDto.getPercentualDescontoUmidade());
		paramRendaLiquida.setPercDescChuvadoAvariado(rePendenteItemDto.getPercentualDescontoChuvadoAvariado());
		paramRendaLiquida.setPercTBM(rePendenteItemDto.getTbm());
		
		if(rePendenteItemDto.getTipoDesmembramento() == null || rePendenteItemDto.getTipoDesmembramento().equals(TipoDesmembramentoEnum.PESO_LIQUIDO)) {
			paramRendaLiquida.setQuantidadeRecepcao(rePendenteItemDto.getQtdRecepcao());
			paramRendaLiquida.setQuantidadeSecagem(rePendenteItemDto.getQtdSecagem());
			paramRendaLiquida.setTaxaSecagem(rePendenteItemDto.getTabelaUmidadeTaxaSecagemKg());
		}
		
		CalculoRendaLiquidaResponseDto calculoRendaLiquidaDto = rePendenteService.calcularRendaLiquida(paramRendaLiquida);
		rePendenteItemDto.setQtdDescontoImpureza(calculoRendaLiquidaDto.getQtdImpureza());
		rePendenteItemDto.setQtdDescontoUmidade(calculoRendaLiquidaDto.getQtdUmidade());
		rePendenteItemDto.setQtdDescontoChuvadoAvariado(calculoRendaLiquidaDto.getQtdChuvaAvariado());
		rePendenteItemDto.setQtdSecagem(calculoRendaLiquidaDto.getQtdSecagem());
		rePendenteItemDto.setQtdTbm(calculoRendaLiquidaDto.getQtdTBM());
		rePendenteItemDto.setRendaLiquida(calculoRendaLiquidaDto.getQtdRendaLiquida());
		
		if(rePendenteItemDto.getDesmembramento() != null && rePendenteItemDto.getDesmembramento()) {
			Taxa taxa = taxaService.buscarTaxaProducaoEstabelecimento(safra, rePendente.getEstabelecimento().getId(), rePendente.getGrupoProduto().getId());
			
			if(rePendente.getProdutor().getCooperativa() && (taxa.getCobraSecagem() || taxa.getCobraRecepcao())) {
				throw new ObjectDefaultException("Para RE em nome da cooperativa o desmebramento pode ser feito apenas "
						+ "com base no peso líquido, pois não é descontado as taxas de recepção e secagem nesse momento.");
			}
		}
	}
	
	public void validarEIncluirDadosConformeParametrizacao(RePendente rePendente, List<RePendenteItemDto> rePendenteItemDtos) {
		if(CollectionUtils.isEmpty(rePendenteItemDtos)) return;
		
		for(RePendenteItemDto itemDto : rePendenteItemDtos) {
			validarEIncluirDadosConformeParametrizacao(rePendente, itemDto);
		}
	}
	
	public RepPendenteItemResponse getLoteUtilizado(String codEstab, String codItem, String lote) {
		List<RecEntregaItem> recEntregaItem = recEntregaItemRep.findByCodEstabelAndCodProdutoAndLoteAndLoteIsNotNull(codEstab, codItem, lote);
		RepPendenteItemResponse repPendenteItemResponse = new RepPendenteItemResponse();
		Boolean loteCadastrado = false;
		logger.info("Buscando re por estabelecimento: {}, produto: {}, lote: {}", codEstab, codItem, lote);
		
		if(!recEntregaItem.isEmpty()) {			
			StringBuilder re = new StringBuilder();
			for(RecEntregaItem item: recEntregaItem) {
				List<RecEntrega> recEntrega = recEntregaRep.findByCodEstabelAndNrReAndReDevolvidoFalse(codEstab, item.getNrRe());
				if(!recEntrega.isEmpty()) {
					for(RecEntrega recItem: recEntrega) {
						re.append(" Re Nº:" + recItem.getNrRe() + " ");
						loteCadastrado = true;
					}
				}
			}			
			
			if(loteCadastrado) {
				logger.info("Lote encontrado: {}.", re);
				repPendenteItemResponse.setStatus(true);
				repPendenteItemResponse.setMessage("Lote encontrado: [ "+ re +" ]");
				return repPendenteItemResponse;
			}
		}
		
		logger.info("Lote não cadastrado.");
		repPendenteItemResponse.setStatus(false);
		repPendenteItemResponse.setMessage("Lote não cadastrado.");
		return repPendenteItemResponse;
	}
}

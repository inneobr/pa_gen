package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum.INT_LOTE_SAFRA_COMPOSTA;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.SAFRA_COMPOSTA;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRAR;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.TipoSafraOperacaoEnum.SOMAR;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoSafraEnum;
import br.coop.integrada.api.pa.domain.enums.TipoSafraOperacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraCompostaEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaComGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaComGrupoProdutoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration.SafraCompostaEstabelecimentoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration.SafraCompostaIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.integration.SafraCompostaIntegrationListDto;
import br.coop.integrada.api.pa.domain.repository.parametros.SafraCompostaRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegracaoLogService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.naturezaTributaria.NaturezaTributariaService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.service.produto.TipoProdutoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SafraCompostaService {

	private static final Logger logger = LoggerFactory.getLogger(SafraCompostaService.class);

	@Autowired
	private SafraCompostaRep safraCompostaRep;

	@Autowired
	private TipoProdutoService tipoProdutoService;

	@Autowired
	private HistoricoGenericoService historicoGenericoService;

	@Autowired
	private GrupoProdutoService grupoProdutoService;

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	private IntegracaoLogService integracaoLogService;

	@Autowired
	private SafraCompostaEstabelecimentoService safraCompostaEstabelecimentoService;

	@Value("${spring.profiles.active}")
	private String profileActive;

	public SafraComposta cadastrar(SafraCompostaDto objDto) {		
		SafraComposta obj = new SafraComposta();		
		BeanUtils.copyProperties(objDto, obj);

		List<SafraCompostaEstabelecimento> estabelecimentos = safraCompostaEstabelecimentoService.converterDto(obj, objDto.getEstabelecimentos());
		TipoProduto tipoProduto = tipoProdutoService.buscarPorId(objDto.getTipoProdutoId());
		validarSeSafrasCompostasPossuemEstabelecimentosVinculados(objDto.getTipoSafra(), tipoProduto.getId(), objDto.getEstabelecimentos());

		obj.setEstabelecimentos(estabelecimentos);
		obj.setTipoProduto(tipoProduto);

		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(SAFRA_COMPOSTA);
		obj.setStatusIntegracao(statusIntegracao);


		if(obj.getMesInicial() != null && obj.getMesInicial() == 2) {
			if(obj.getDiaInicial() >= 29) {
				obj.setDiaInicial(28);
			}
		}
		
		if(obj.getMesFinal() != null && obj.getMesFinal() == 2) {
			if(obj.getDiaFinal() >= 29) {
				obj.setDiaFinal(28);
			}		
		}	
		
		obj = safraCompostaRep.save(obj);
		safraCompostaEstabelecimentoService.salvar(obj, objDto.getEstabelecimentos());

		historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Nova safra cadastrada: ", obj.getNomeSafra());

		return obj;
	}

	public SafraComposta atualizar(SafraCompostaDto objDto) {
		if (objDto.getId() == null) {
			throw new NullPointerException("É necessário informar o ID para atualizar o cadastro!");
		}

		SafraComposta safraCompostaAtual = buscarPorId(objDto.getId());
		objDto.setTipoSafra(safraCompostaAtual.getTipoSafra());

		if (safraCompostaAtual.getTipoProduto() != null) {
			objDto.setTipoProdutoId(safraCompostaAtual.getTipoProduto().getId());
		}

		SafraComposta obj = new SafraComposta();
		BeanUtils.copyProperties(safraCompostaAtual, obj);
		BeanUtils.copyProperties(objDto, obj);

		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(SAFRA_COMPOSTA);
		obj.setStatusIntegracao(statusIntegracao);

		validarSeSafrasCompostasPossuemEstabelecimentosVinculados(objDto.getTipoSafra(), objDto.getTipoProdutoId(),
				objDto.getEstabelecimentos(), objDto.getId());
		List<SafraCompostaEstabelecimento> estabelecimentosParaInativar = safraCompostaEstabelecimentoService
				.getEstabelecimentoParaInativar(obj.getId(), objDto.getEstabelecimentos());

		String alteracoes = getAlteracoes(objDto);
		String estabelecimentosRemovidos = getEstabelecimentosRemovidos(objDto.getId(), estabelecimentosParaInativar);
		String estabelecimentosAdicionados = getEstabelecimentosAdicionados(objDto.getId(),
				objDto.getEstabelecimentos());

		
		if(obj.getMesInicial() != null && obj.getMesInicial() == 2) {
			if(obj.getDiaInicial() >= 29) {
				obj.setDiaInicial(28);
			}
		}
		
		if(obj.getMesFinal() != null && obj.getMesFinal() == 2) {
			if(obj.getDiaFinal() >= 29) {
				obj.setDiaFinal(28);
			}		
		}
		
		obj = safraCompostaRep.save(obj);
		safraCompostaEstabelecimentoService.salvar(obj, objDto.getEstabelecimentos());
		safraCompostaEstabelecimentoService.inativar(estabelecimentosParaInativar);

		if (!alteracoes.isEmpty()) {
			historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Alteração", alteracoes);
		}

		if (!estabelecimentosRemovidos.isEmpty()) {
			historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Estabelecimento(s) removido(s)",
					estabelecimentosRemovidos);
		}

		if (!estabelecimentosAdicionados.isEmpty()) {
			historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Estabelecimento(s) adicionados(s)",
					estabelecimentosAdicionados);
		}

		return obj;
	}

	public SafraComposta buscarPorId(Long id) {
		SafraComposta objAtual = safraCompostaRep.findById(id).orElse(null);

		if (objAtual == null) {
			throw new NullPointerException("A safra composta não foi encontrada! ID: " + id);
		}

		return objAtual;
	}

	public void inativar(Long id) {
		SafraComposta obj = buscarPorId(id);
		obj.setDataInativacao(new Date());
		safraCompostaRep.save(obj);
		historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Inativar", obj.getNomeSafra());
	}

	public void ativar(Long id) {
		SafraComposta obj = buscarPorId(id);
		obj.setDataInativacao(null);
		SafraCompostaDto objDto = SafraCompostaDto.construir(obj);
		atualizar(objDto);
		historicoGenericoService.salvar(obj.getId(), PaginaEnum.SAFRA_COMPOSTA, "Ativar", obj.getNomeSafra());
	}

	public List<SafraCompostaComGrupoProdutoSimplesDto> buscarSafrasCompostasComGruposProdutos(Integer anoBase) {
		List<SafraCompostaComGrupoProdutoDto> safras = safraCompostaRep.safrasCompostasComGrupoProduto();
		return converterParaSafraCompostaComGrupoProdutoSimplesDto(safras, anoBase);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<SafraCompostaSimplesDto> buscarComPaginacao(Pageable pageable, SafraCompostaFilter filter,
			Situacao situacao, Integer anoBase) {
		Page<SafraComposta> safraCompostaPage = safraCompostaRep.findAll(pageable, filter, situacao);
		List<SafraCompostaSimplesDto> safraCompostaSimplesDtos = converterParaSafraCompostaSimplesDto(
				safraCompostaPage.getContent(), anoBase);

		return new PageImpl(safraCompostaSimplesDtos, pageable, safraCompostaPage.getTotalElements());
	}

	public List<Estabelecimento> buscarEstabelecimentosPorSafraComposta(Long idSafraComposta) {
		SafraComposta safraComposta = buscarPorId(idSafraComposta);
		return safraComposta.getEstabelecimentos().stream().map(safraCompostaEstabelecimento -> {
			return safraCompostaEstabelecimento.getEstabelecimento();
		}).toList();
	}

	public SafraCompostaSimplesDto buscarPorDataEstabelecimentoTipoProduto(Date data, String codigoEstabelecimento,
			Long tipoProduto) {
		List<SafraComposta> safraCompostas = safraCompostaRep
				.buscarPorEstabelecimentoETipoProduto(codigoEstabelecimento, tipoProduto);

		if (safraCompostas == null || safraCompostas.isEmpty()) {
			throw new ObjectNotFoundException(
					"Não existe parametrização de safra composta para o estabelecimento e tipo de produto informado!");
		}

		LocalDate dataAux = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		for (SafraComposta safraComposta : safraCompostas) {
			Integer anoInicial = getAno(safraComposta.getAnoInicialOperacao(), safraComposta.getAnoInicialQuantidade(),
					dataAux.getYear());
			LocalDate dataInicial = LocalDate.of(anoInicial, safraComposta.getMesInicial(),
					safraComposta.getDiaInicial());

			Integer anoFinal = getAno(safraComposta.getAnoFinalOperacao(), safraComposta.getAnoFinalQuantidade(),
					dataAux.getYear());
			LocalDate dataFinal = LocalDate.of(anoFinal, safraComposta.getMesFinal(), safraComposta.getDiaFinal());

			Boolean dataAuxMaiorOuIgualDataInicial = (dataAux.equals(dataInicial) || dataAux.isAfter(dataInicial));
			Boolean dataAuxMenorOuIgualDataFinal = (dataAux.equals(dataFinal) || dataAux.isBefore(dataFinal));

			if (dataAuxMaiorOuIgualDataInicial && dataAuxMenorOuIgualDataFinal) {
				return converterParaSafraCompostaSimplesDto(safraComposta, dataAux.getYear());
			}
		}

		throw new ObjectNotFoundException(
				"Não existe parametrização de safra composta para a data, estabelecimento e tipo de produto informado!");
	}

	public SafraCompostaSimplesDto buscarPorDataGrupoProdutoEstabelecimento(Date data, String codigoGrupoProduto,
			String codigoEstabelecimento) {
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoGrupoProduto);
		TipoProduto tipoProduto = grupoProduto.getTipoProduto();

		return buscarPorDataEstabelecimentoTipoProduto(data, codigoEstabelecimento, tipoProduto.getId());
	}

	private void validarSeSafrasCompostasPossuemEstabelecimentosVinculados(TipoSafraEnum tipoSafra, Long idTipoProduto,
			List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		validarSeSafrasCompostasPossuemEstabelecimentosVinculados(tipoSafra, idTipoProduto, estabelecimentoDtos, null);
	}

	private void validarSeSafrasCompostasPossuemEstabelecimentosVinculados(TipoSafraEnum tipoSafra, Long idTipoProduto,
			List<EstabelecimentoSimplesDto> estabelecimentoDtos, Long idSafraComposta) {
		List<String> codigoEstabelecimentos = estabelecimentoDtos.stream().map(estabelecimentoDto -> {
			return estabelecimentoDto.getCodigo();
		}).toList();
		List<String> mensagens = safraCompostaRep.getSafrasCompostasComMesmoEstabelecimentos(tipoSafra.name(),
				idTipoProduto, codigoEstabelecimentos, idSafraComposta == null ? 0 : idSafraComposta);

		if (mensagens == null || mensagens.isEmpty())
			return;

		String mensagem = String.join("\n", mensagens);
		throw new IllegalArgumentException(
				"Já existe safra composta com Tipo Safra X Ano Base X Estabelecimento cadastradas: \n" + mensagem);
	}

	private Date getData(Integer dia, Integer mes, Integer ano) {
		LocalDate dataInicial = LocalDate.of(ano, mes, dia);
		return Date.from(dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private Integer getAno(TipoSafraOperacaoEnum tipoSafraOperacao, Integer anoQuantidade, Integer anoBase) {
		if (tipoSafraOperacao.equals(SOMAR)) {
			return anoBase + anoQuantidade;
		}

		return anoBase - anoQuantidade;
	}

	private List<SafraCompostaSimplesDto> converterParaSafraCompostaSimplesDto(List<SafraComposta> obj, Integer anoBase) {
		if (obj == null)
			return new ArrayList<>();
		return obj.stream().map(safraComposta -> {
			return converterParaSafraCompostaSimplesDto(safraComposta, anoBase);
		}).toList();
	}

	private SafraCompostaSimplesDto converterParaSafraCompostaSimplesDto(SafraComposta obj, Integer anoBase) {
		var objDto = new SafraCompostaSimplesDto();
		BeanUtils.copyProperties(obj, objDto);

		if (obj.getTipoProduto() != null) {
			objDto.setTipoProdutoId(obj.getTipoProduto().getId());
			objDto.setTipoProdutoNome(obj.getTipoProduto().getNome());
		}		
		objDto.setDataIntegracao(obj.getDataIntegracao());

		Integer anoInicial = getAno(obj.getAnoInicialOperacao(), obj.getAnoInicialQuantidade(), anoBase);
		Date dataInicial = getData(obj.getDiaInicial(), obj.getMesInicial(), anoInicial);
		objDto.setDataInicio(dataInicial);
		
		Integer anoFinal = getAno(obj.getAnoFinalOperacao(), obj.getAnoFinalQuantidade(), anoBase);
		Date dataFinal = getData(obj.getDiaFinal(), obj.getMesFinal(), anoFinal);
		objDto.setDataFinal(dataFinal);
				
		Integer anoPlantio = getAno(obj.getSafraPlantioOperacao(), obj.getSafraPlantioQuantidade(), anoBase);
		Integer anoColheita = getAno(obj.getSafraColheitaOperacao(), obj.getSafraColheitaQuantidade(), anoBase);
		objDto.setSafraComposta(anoPlantio + "/" + anoColheita);
		
		if(obj.getStatusIntegracao() != null) {
			objDto.setDescricaoStatusIntegracao(obj.getStatusIntegracao().getDescricao());
    	}

		return objDto;
	}

	public List<SafraCompostaComGrupoProdutoSimplesDto> converterParaSafraCompostaComGrupoProdutoSimplesDto(
			List<SafraCompostaComGrupoProdutoDto> objs, Integer anoBase) {
		if (objs == null)
			return new ArrayList<>();

		return objs.stream().map(safraCompostaComGrupoProdutoDto -> {
			return converterParaSafraCompostaComGrupoProdutoSimplesDto(safraCompostaComGrupoProdutoDto, anoBase);
		}).toList();
	}

	private SafraCompostaComGrupoProdutoSimplesDto converterParaSafraCompostaComGrupoProdutoSimplesDto(
			SafraCompostaComGrupoProdutoDto obj, Integer anoBase) {
		var objDto = new SafraCompostaComGrupoProdutoSimplesDto();
		BeanUtils.copyProperties(obj, objDto);

		Integer anoInicial = getAno(obj.getAnoInicialOperacao(), obj.getAnoInicialQuantidade(), anoBase);
		Date dataInicial = getData(obj.getDiaInicial(), obj.getMesInicial(), anoInicial);
		objDto.setDataInicio(dataInicial);

		Integer anoFinal = getAno(obj.getAnoFinalOperacao(), obj.getAnoFinalQuantidade(), anoBase);
		Date dataFinal = getData(obj.getDiaFinal(), obj.getMesFinal(), anoFinal);
		objDto.setDataFinal(dataFinal);

		Integer anoPlantio = getAno(obj.getSafraPlantioOperacao(), obj.getSafraPlantioQuantidade(), anoBase);
		Integer anoColheita = getAno(obj.getSafraColheitaOperacao(), obj.getSafraColheitaQuantidade(), anoBase);
		objDto.setSafraComposta(anoPlantio + "/" + anoColheita);

		return objDto;
	}

	private String getAlteracoes(SafraCompostaDto objDto) {
		SafraComposta safraComposta = buscarPorId(objDto.getId());
		SafraCompostaDto safraCompostaDto = SafraCompostaDto.construir(safraComposta);

		try {
			return CompararObjetos.comparar(safraCompostaDto, objDto);
		} catch (Exception e) {
			logger.error("Falha ao obter alterações da safra composta: " + e.getMessage());
		}

		return "";
	}

	private String getEstabelecimentosRemovidos(Long idSafraComposta,
			List<SafraCompostaEstabelecimento> safraCompostaEstabelecimento) {
		StringBuilder removidos = new StringBuilder();

		for (SafraCompostaEstabelecimento item : safraCompostaEstabelecimento) {
			Estabelecimento estabelecimento = item.getEstabelecimento();

			removidos.append("\nCódigo: \"");
			removidos.append(estabelecimento.getCodigo());
			removidos.append("\", Código regional: \"");
			removidos.append(estabelecimento.getCodigoRegional());
			removidos.append("\", Nome fantasia: \"");
			removidos.append(estabelecimento.getNomeFantasia());
			removidos.append("\"");
		}

		return removidos.toString();
	}

	private String getEstabelecimentosAdicionados(Long idSafraComposta,
			List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		List<Estabelecimento> estabAtuais = buscarEstabelecimentosPorSafraComposta(idSafraComposta);
		StringBuilder adicionados = new StringBuilder();

		for (EstabelecimentoSimplesDto estabelecimentoDto : estabelecimentoDtos) {
			Estabelecimento estabAtual = estabAtuais.stream().filter(item -> {
				return item.getCodigo().equals(estabelecimentoDto.getCodigo());
			}).findFirst().orElse(null);

			if (estabAtual == null) {
				adicionados.append("\nCódigo: \"");
				adicionados.append(estabelecimentoDto.getCodigo());
				adicionados.append("\", Código regional: \"");
				adicionados.append(estabelecimentoDto.getCodigoRegional());
				adicionados.append("\", Nome fantasia: \"");
				adicionados.append(estabelecimentoDto.getNomeFantasia());
				adicionados.append("\"");
			}
		}

		return adicionados.toString();
	}

	public List<SafraComposta> buscarPorStatus(StatusIntegracao status) {
		return safraCompostaRep.findByStatusIntegracao(status);
	}

	public List<SafraComposta> buscarComStatus(StatusIntegracao status) {
		List<SafraComposta> safraCompostas = buscarPorStatus(status);
		if (CollectionUtils.isEmpty(safraCompostas))
			return Collections.emptyList();
		return safraCompostas.stream().map(safraComposta -> {
			var obj = new SafraComposta();
			BeanUtils.copyProperties(safraComposta, obj);
			List<SafraCompostaEstabelecimento> estabelecimentos = safraCompostaEstabelecimentoService
					.buscarPorIdSafraCompostaComStatus(obj.getId(), status);
			obj.setEstabelecimentos(estabelecimentos);
			return obj;
		}).toList();
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<SafraComposta> objs) {
		if (CollectionUtils.isEmpty(objs))
			return;
		List<SafraComposta> safraCompostas = objs.stream().map(safraComposta -> {
			safraCompostaEstabelecimentoService.alterarStatusIntegracao(status, safraComposta.getEstabelecimentos());
			safraComposta.setDataIntegracao(null);
			safraComposta.setStatusIntegracao(status);
			return safraComposta;
		}).toList();
		safraCompostaRep.saveAll(safraCompostas);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sincronizar(Long id) {
		IntegracaoPagina pagina = integrationService.buscarPorPagina(SAFRA_COMPOSTA, INT_LOTE_SAFRA_COMPOSTA);
		if (pagina == null || ERP.equals(pagina.getOrigenEnum()))
			return;

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(INT_LOTE_SAFRA_COMPOSTA);
		if (paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null
				|| paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO))
			return;

		List<SafraComposta> safraCompostas = null;
		SafraCompostaIntegrationListDto objDto = null;
		IntegracaoLog integracaoLogRequest = IntegracaoLog.construirLogRequest(SAFRA_COMPOSTA, INT_LOTE_SAFRA_COMPOSTA);

		try {

			if (id == null) {
				safraCompostas = buscarComStatus(INTEGRAR);
			} else {
				SafraComposta safraComposta = buscarPorId(id);
				if (safraComposta != null) {
					safraCompostas = new ArrayList<>();
					safraCompostas.add(safraComposta);
				}
			}

			integracaoLogRequest.setTotalRegistros(safraCompostas != null ? safraCompostas.size() : 0);
			alterarStatusIntegracao(PROCESSANDO, safraCompostas);

			if (!CollectionUtils.isEmpty(safraCompostas)) {

				objDto = SafraCompostaIntegrationListDto.construir(safraCompostas);

				IntegrationAuth auth = integrationService
						.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());

				LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();
				if (pagina.getHeaders() != null) {
					for (IntegracaoPaginaHeader header : pagina.getHeaders()) {
						if (pagina.isHearderProfileActive(header, profileActive)) {
							if (!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())) {
								mvmap.add(header.getChave(), header.getValor());
							}
						}
					}
				}

				String url = pagina.getUrlPrincipalApi(profileActive);
				if (url == null)
					return;

				WebClient client = WebClient.builder().baseUrl(url)
						.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.filter(ExchangeFilterFunctions.basicAuthentication(auth.getLogin(), auth.getSenha())).build();

				HttpMethod httpMethod = IntegrationService.getMetodo(paginaFuncionalidade);

				Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);
				client.method(httpMethod).uri(paginaFuncionalidade.getEndPointSend()).headers(consumer)
						.body(BodyInserters.fromValue(objDto)).retrieve()
						.bodyToFlux(SafraCompostaIntegrationListDto.class)
						.subscribe(safraCompostaIntegrationListDto -> {

							Integer totalRegistros = 0;
							Integer sucessNotFound = 0;
							Integer sucess = 0;
							Integer error = 0;

							if (safraCompostaIntegrationListDto != null
									&& !CollectionUtils.isEmpty(safraCompostaIntegrationListDto.getSafrasCompostas())) {

								List<SafraCompostaIntegrationDto> itens = safraCompostaIntegrationListDto.getSafrasCompostas();

								for (SafraCompostaIntegrationDto item : itens) {
									
									try {
										
										StatusIntegracao statusIntegracaoCabecalho = item.getIntegrated() ? INTEGRADO : INTEGRAR;

										SafraComposta safraComposta = safraCompostaRep.findById(item.getId()).orElse(null);
										if (safraComposta != null) {
											safraComposta.setDataIntegracao(statusIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null);
											safraComposta.setStatusIntegracao(statusIntegracaoCabecalho);
											safraCompostaRep.save(safraComposta);

											if (item.getIntegrated()) {
												sucess++;
											}
										} else if (item.getIntegrated()) {
											sucessNotFound++;
										} else {
											error++;
										}

										totalRegistros++;
									} catch (Exception e) {
										logger.error("Retorno da sincronização de SAFRA COMPOSTA");
										logger.error("SAFRA COMPOSTAO: " + item.toString());

										totalRegistros++;
										error++;
									}
								}
							}

							StringBuilder obs = new StringBuilder();
							obs.append("[Resumo Integração ERP] \n\r");
							obs.append("Sucesso: ").append(sucess).append(", Sucesso ERP/NotFound Gênesis: ")
									.append(sucessNotFound).append(", Falha: ").append(error);

							SituacaoIntegracaoLogEnum situacao = (error > 0 || sucess == 0)
									? SituacaoIntegracaoLogEnum.FALHA
									: SituacaoIntegracaoLogEnum.SUCESSO;

							IntegracaoLog integracaoLogResponse = IntegracaoLog.construirLogResponse(SAFRA_COMPOSTA,
									INT_LOTE_SAFRA_COMPOSTA, situacao, totalRegistros, obs.toString());
							integracaoLogService.salvar(integracaoLogResponse);

						});
			}

		} catch (Exception e) {
			System.out.println("Falha ao sincronizar safra composta: " + e.getMessage());
			alterarStatusIntegracao(StatusIntegracao.INTEGRAR, safraCompostas);
			integracaoLogRequest.registrarFalha("Error: " + e.getMessage());
			e.printStackTrace();

		} finally {
			integracaoLogService.salvar(integracaoLogRequest);
		}

	}
}

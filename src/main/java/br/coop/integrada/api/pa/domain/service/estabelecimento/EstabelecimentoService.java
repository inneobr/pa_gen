package br.coop.integrada.api.pa.domain.service.estabelecimento;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoSafraEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.*;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroUsuarioEstabelecimentoFiltro;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.naturezaTributaria.NaturezaTributariaService;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.parametros.ParametroUsuarioEstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.EstabelecimentoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.coop.integrada.api.pa.domain.spec.enums.Situacao.ATIVO;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class EstabelecimentoService {
	private static final Logger logger = LoggerFactory.getLogger(NaturezaTributariaService.class);	
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRepository;
	
	@Autowired
	private ParametroUsuarioEstabelecimentoService parametroUsuarioEstabelecimentoService;
	
	@Autowired
	private ParametroEstabelecimentoService parametroEstabelecimentoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public EstabelecimentoAllDto unico(Long id) {
		Estabelecimento estabelecimento = estabelecimentoRepository.findByIdOrderByCodigoAsc(id);
		EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
		BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
		return estabelecimentoAllDto;
	}
	
	public Page<EstabelecimentoAllDto> estabelecimentosDoUsuario(String codigo, Pageable pageable) {
		Estabelecimento estabelecimento = estabelecimentoRepository.findByCodigo(codigo);		
		Page<Estabelecimento> estabelecimentoPage = null;		
		if(estabelecimento.getMatriz()) {
			estabelecimentoPage = estabelecimentoRepository.findByDataInativacaoNull(pageable);
		}else {
			estabelecimentoPage = estabelecimentoRepository.findByCodigoRegional(estabelecimento.getCodigoRegional(), pageable);
		}		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		
		for(Estabelecimento estabelecimentoItem: estabelecimentoPage.getContent()) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimentoItem, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}
		
		Page<EstabelecimentoAllDto> estabelecimentoPageReturn = new PageImpl<>(
				estabelecimentosList, pageable, estabelecimentoPage.getTotalElements()
		);
		return estabelecimentoPageReturn;
	}

	public List<Estabelecimento> buscarPorParametroUsuarioVsEstabelecimentoComUsuarioAutenticado(
			ParametroUsuarioEstabelecimentoFiltro filtro, Situacao situacao, Pageable pageable) {
		Page<ParametrosUsuarioEstabelecimento> parametrosUsuarioVsEstabelecimento = parametroUsuarioEstabelecimentoService.buscarPorUsuarioAutenticado(filtro, situacao, pageable);
		return parametrosUsuarioVsEstabelecimento.getContent().stream().map(parametroUsuarioVsEstabelecimento -> {
			return parametroUsuarioVsEstabelecimento.getEstabelecimento();
		}).toList();
	}
	
	public List<EstabelecimentoAllDto> estabelecimentosDoUsuarioBusca(String codigo) {
		Estabelecimento estabelecimento = estabelecimentoRepository.findByCodigo(codigo);		
		List<Estabelecimento> estabelecimentos = null;		
		if(estabelecimento.getMatriz()) {
			estabelecimentos = estabelecimentoRepository.findByDataInativacaoNullOrderByCodigoAsc();
		}else {
			estabelecimentos = estabelecimentoRepository.findByCodigoRegionalOrderByCodigoAsc(estabelecimento.getCodigoRegional());
		}		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		
		for(Estabelecimento estabelecimentoItem: estabelecimentos) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimentoItem, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}
		return estabelecimentosList;
	}
	
	public Page<EstabelecimentoAllDto> buscarTodosAtivo(Pageable pageable) {
		Page<Estabelecimento> estabelecimentos = estabelecimentoRepository.findByDataInativacaoNull(pageable);
		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		for(Estabelecimento estabelecimento: estabelecimentos.getContent()) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}		
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = new PageImpl<>(
				estabelecimentosList, pageable, estabelecimentos.getTotalElements()
		);
		return estabelecimentoAllDtoPage;
	}
	
	public List<EstabelecimentoNomeCodigoDto> findAll() {
		logger.info("Listando todos os estabelecimentos resumidos");
		List<Estabelecimento> lista = estabelecimentoRepository.findByDataInativacaoNullOrderByCodigoAsc();
		
		List<EstabelecimentoNomeCodigoDto> response = new ArrayList<>();
		for(Estabelecimento estabelecimento: lista) {
			EstabelecimentoNomeCodigoDto estabelecimentoDto = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(estabelecimento,  estabelecimentoDto);
			response.add(estabelecimentoDto);
		}		
		return response;
	}

	public List<EstabelecimentoAllDto> listarRegionais() {
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.listarRegionaisAndDataInativacaoNull();
		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		for(Estabelecimento estabelecimento: estabelecimentos) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}
		return estabelecimentosList;
	}
	
	public List<EstabelecimentoNomeCodigoDto> getRegionalCodigoOuNome(String request){		
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.findAll(
				EstabelecimentoSpecs.codigoOuNomeFantasiaLike(request)
				.and(EstabelecimentoSpecs.isRegional()));
		
		List<EstabelecimentoNomeCodigoDto> response = new ArrayList<>();
		for(Estabelecimento item: estabelecimentos) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			response.add(estabelecimento);
		}
		return response;
	}	

	public Page<EstabelecimentoAllDto> listarRegionaisPorCodigo(String codigoRegional, Pageable pageable) {
		Page<Estabelecimento> estabelecimentos = estabelecimentoRepository.findByCodigoRegional(codigoRegional, pageable);
		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		for(Estabelecimento estabelecimento: estabelecimentos.getContent()) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}
		
		Page<EstabelecimentoAllDto> estabelecimentoAllDtoPage = new PageImpl<>(
				estabelecimentosList, pageable, estabelecimentos.getTotalElements()
		);		
		return estabelecimentoAllDtoPage;
	}

	public EstabelecimentoAllDto findByCodigo(String codigo) {
		Estabelecimento estabelecimento = buscarPorCodigo(codigo);
		EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
		BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
		return estabelecimentoAllDto;
	}

	public Estabelecimento buscarPorCodigo(String codigo) {
		return estabelecimentoRepository.findByCodigo(codigo);
	}
	
	public List<EstabelecimentoResumidoDto> buscarEstabelecimentoPorCodigoOuNomeFantasia(String busca, String ordenacao) {
	    Pageable limit = PageRequest.of(0, 15, Sort.by(ordenacao));
	    List<EstabelecimentoResumidoDto> response = new ArrayList<>();

		List<Estabelecimento> estabelecimentos = null;

		if(busca == null || busca.isEmpty()) {
			Page<Estabelecimento> estabelecimentoPage = estabelecimentoRepository.findAll(limit);
			estabelecimentos = estabelecimentoPage.getContent();
		}
		else {
			estabelecimentos = estabelecimentoRepository.findByCodigoContainingIgnoreCaseOrNomeFantasiaContainingIgnoreCaseOrderByNomeFantasiaAsc(busca, busca, limit);
		}

		List<EstabelecimentoResumidoDto> estabelecimentoResumidoDtos = EstabelecimentoResumidoDto.construir(estabelecimentos);
        return estabelecimentoResumidoDtos;
    }

	public List<Estabelecimento> listarEstabelecimentosDoUsuarioLogadoPorCodigoOuNome(String codigoOuNome) {
		Pageable pageable = PageRequest.of(0, 1000, Sort.by("codigo"));
		Usuario usuario = usuarioService.getUsuarioLogado();
		Estabelecimento estabelecimento = estabelecimentoRepository.findByCodigo(usuario.getEstabelecimento());

		if(estabelecimento.getMatriz()) {
			if (codigoOuNome.isEmpty()) return estabelecimentoRepository.findAll(pageable).getContent();
			return estabelecimentoRepository.findByCodigoContainingIgnoreCaseOrNomeFantasiaContainingIgnoreCaseOrderByNomeFantasiaAsc(codigoOuNome, codigoOuNome, pageable);
		}else {
			return estabelecimentoRepository.findByCodigoOrNomeFantasiaContainingIgnoreCaseAndCodigoRegionalOrderByNomeFantasiaAsc(codigoOuNome, codigoOuNome, estabelecimento.getCodigoRegional(), pageable);
		}
	}
	public List<EstabelecimentoResumidoDto> listarEstResumidoDtoDoUsuarioLogado(String codigoOuNome) {
	    List<Estabelecimento> estabelecimentos = listarEstabelecimentosDoUsuarioLogadoPorCodigoOuNome(codigoOuNome);
		List<EstabelecimentoResumidoDto> estabelecimentoResumidoDtos = EstabelecimentoResumidoDto.construir(estabelecimentos);
		return estabelecimentoResumidoDtos;
	}
	
	public Page<Estabelecimento> pesquisarPorCodigoOuNomeFantasiaAtivo(String codigoOuNomeFantasia, Pageable pageable) {
		return estabelecimentoRepository.findAll(codigoOuNomeFantasia, pageable, ATIVO);
	}

	public void salvar(List<EstabelecimentoDto> estabelecimentosDTO) {
		for(EstabelecimentoDto estabelecimentoDto : estabelecimentosDTO) {
			cadastrarOuAtualizar(estabelecimentoDto);
		}
	}

	public Estabelecimento update(EstabelecimentoDto estabelecimentoDto) {
		return cadastrarOuAtualizar(estabelecimentoDto);
	}

	public void update(List<EstabelecimentoDto> estabelecimentosDTO) {
		for(EstabelecimentoDto estabelecimentoDto : estabelecimentosDTO) {
			cadastrarOuAtualizar(estabelecimentoDto);
		}
	}

	public Estabelecimento buscarPorId(Long id) {
		Estabelecimento obj = estabelecimentoRepository.findById(id).orElse(null);

		if(obj == null) {
			throw new ObjectNotFoundException("O estabelecimento não foi encontrado! ID: " + id + ", Tipo: " + Estabelecimento.class.getName());
		}

		return obj;
	}

	public List<Estabelecimento> buscarAtivoSemVinculoComSafraComposta(TipoSafraEnum tipoSafra, Long idTipoProduto) {
		return estabelecimentoRepository.buscarAtivoSemVinculoComSafraComposta(tipoSafra.name(), idTipoProduto);
	}
	
	public Estabelecimento cadastrar(Estabelecimento estabelecimento) {
		try {
			return estabelecimentoRepository.save(estabelecimento);
		}catch(ConstraintViolationException e) {
			throw new ObjectNotFoundException("Estabelecimento já cadastrado.", e);
		}
	}
	
	public Estabelecimento cadastrarOuAtualizarIntegration(EstabelecimentoDto objDto) { 
		if(objDto.getCodigo() == null) throw new ObjectDefaultException("Necessario informa o código do estabelecimento.");
		Estabelecimento estabelecimento = estabelecimentoRepository.findByCodigo(objDto.getCodigo());
		if(estabelecimento == null) estabelecimento = new Estabelecimento();
		BeanUtils.copyProperties(objDto, estabelecimento);
		
		if(objDto.getAtivo() != null && !objDto.getAtivo() && estabelecimento.getDataInativacao() == null) {
			estabelecimento.setDataInativacao(new Date());
		}else if(objDto.getAtivo() != null && objDto.getAtivo() && estabelecimento.getDataInativacao() != null) {
			estabelecimento.setDataInativacao(null);
		}
		
		if(Strings.isEmpty(objDto.getCodigoRegional())) {
			estabelecimento.setCodigoRegional(estabelecimento.getCodigo());
		}
		
		estabelecimento.setStatusIntegracao(StatusIntegracao.INTEGRADO);
		estabelecimento.setDataIntegracao(new Date());
		return estabelecimentoRepository.save(estabelecimento);
	}

	public Estabelecimento cadastrarOuAtualizar(EstabelecimentoDto objDto) { 
		if(objDto.getCodigo() == null) throw new ObjectDefaultException("Necessario informa o código do estabelecimento.");
		Estabelecimento estabelecimento = estabelecimentoRepository.findByCodigo(objDto.getCodigo());
		if(estabelecimento == null) estabelecimento = new Estabelecimento();
		BeanUtils.copyProperties(objDto, estabelecimento);
		
		if(objDto.getAtivo() != null && !objDto.getAtivo() && estabelecimento.getDataInativacao() == null) {
			estabelecimento.setDataInativacao(new Date());
		}else if(objDto.getAtivo() != null && objDto.getAtivo() && estabelecimento.getDataInativacao() != null) {
			estabelecimento.setDataInativacao(null);
		}
		
		if(Strings.isEmpty(objDto.getCodigoRegional())) {
			estabelecimento.setCodigoRegional(estabelecimento.getCodigo());
		}
		
		return estabelecimentoRepository.save(estabelecimento);
	}

	public Page<EstabelecimentoSimplesDto> buscarEstabelecimentosPorIdSafraCompostaComPaginacao(Long idSafraComposta, Pageable pageable) {
		Page<Estabelecimento> estabelecimentoPage = estabelecimentoRepository.buscarEstabelecimentosPorIdSafraComposta(idSafraComposta, pageable);
		List<EstabelecimentoSimplesDto> estabelecimentoSimplesDtos = estabelecimentoPage.getContent()
				.stream().map(estabelecimento -> {
					return EstabelecimentoSimplesDto.construir(estabelecimento);
				}).toList();

		return new PageImpl(estabelecimentoSimplesDtos, pageable, estabelecimentoPage.getTotalElements());
	}

	public Estabelecimento ativarEstabelecimento(Long id) {
		Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElse(null);
		
		estabelecimento.setDataInativacao(null);
		estabelecimento.setDataAtualizacao(new Date());
				
		log.info("Ativando estabelecimento...");
		return estabelecimentoRepository.save(estabelecimento);
	}

	public Estabelecimento inativarEstabelecimento(Long id) {
		Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElse(null);
		
		estabelecimento.setDataInativacao(new Date());
		log.info("Inativando estabelecimento...");
		return estabelecimentoRepository.save(estabelecimento);
	}

	public Page<EstabelecimentoAllDto> findAll(Pageable pageable, EstabelecimentoFilter filter, Situacao situacao) {
		
		log.info("Listando todos os Estabelecimentos");
		Page<Estabelecimento> estabelecimentoPage = estabelecimentoRepository.findAll(pageable, filter, situacao);
		List<EstabelecimentoAllDto> estabelecimentoDtos = EstabelecimentoAllDto.construir(estabelecimentoPage.getContent());
		return new PageImpl<EstabelecimentoAllDto>(estabelecimentoDtos, pageable, estabelecimentoPage.getTotalElements());
	}
	
	public Estabelecimento converterEstabelecimentoSimplesDtoComValidacao(EstabelecimentoSimplesDto estabelecimentoDto) {
		if(Strings.isEmpty(estabelecimentoDto.getCodigo())) {
			throw new ObjectDefaultException("Necessário informar o código do estabelecimento");
		}

		Estabelecimento estabelecimento = buscarPorCodigo(estabelecimentoDto.getCodigo());

		if(estabelecimento == null) {
			throw new ObjectDefaultException("Não foi encontrado o estabelecimento com o código " + estabelecimentoDto.getCodigo());
		}

		return estabelecimento;
	}

	public List<Estabelecimento> converterEstabelecimentoSimplesDtoComValidacao(List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		List<Estabelecimento> estabelecimentos = new ArrayList<>();
		if(CollectionUtils.isEmpty(estabelecimentoDtos)) return estabelecimentos;

		for(EstabelecimentoSimplesDto estabelecimentoDto : estabelecimentoDtos) {
			Estabelecimento estabelecimento = converterEstabelecimentoSimplesDtoComValidacao(estabelecimentoDto);
			estabelecimentos.add(estabelecimento);
		}

		return estabelecimentos;
	}

	public Estabelecimento validarEntradaProducaoPorUsuarioVsEstabelecimento(String codigoEstabelecimento, Boolean entradaManual) {
		ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(codigoEstabelecimento);
		
		if(parametroEstabelecimento == null) {
			throw new ObjectNotFoundException("Não foi encontrado parâmetro para o estabelecimento \"" + codigoEstabelecimento + "\".");
		}
		
		if(entradaManual && !parametroEstabelecimento.getLogEntradaSemTik()) {
			throw new ObjectDefaultException("O estabelecimento \"" + codigoEstabelecimento + "\" não está parametrizado para entrada manual.");
		}
		
		if(!parametroEstabelecimento.getLogSilo()) {
			throw new ObjectDefaultException("Para criar uma entrada de produção, o estabelecimento \"" + codigoEstabelecimento + "\" deve estar parametrizado como \"silo\".");
		}
		
		ParametrosUsuarioEstabelecimento parametroUsuarioEstabelecimento = parametroUsuarioEstabelecimentoService.buscarPorUsuarioAutenticadoECodigoEstabelecimento(codigoEstabelecimento);
		
		if(parametroUsuarioEstabelecimento.getRe() == null || !parametroUsuarioEstabelecimento.getRe()) {
			throw new ObjectDefaultException("O usuário não possui permissão para entrada de RE.");
		}
		
		return parametroEstabelecimento.getEstabelecimento();
	}

	//Busca Estabelecimentos tipo Silo por Parametro e Natureza Operação 
	public List<EstabelecimentoAllDto> buscarEstabelecimentoPorParametroEstabelecimentoAndNaturezaOperacaoAndSilo() {
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.buscarEstabelecimentoPorParametroEstabelecimentoAndNaturezaOperacaoAndSilo();
		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		for(Estabelecimento estabelecimento: estabelecimentos) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}		
		return estabelecimentosList;
	}
	
	//Busca Estabelecimentos tipo Silo por Parametro 
	public List<EstabelecimentoAllDto> buscarEstabelecimentoPorParametroEstabelecimentoAndSilo() {
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.buscarEstabelecimentoPorParametroEstabelecimentoAndSilo();
		
		List<EstabelecimentoAllDto> estabelecimentosList = new ArrayList<>();
		for(Estabelecimento estabelecimento: estabelecimentos) {
			EstabelecimentoAllDto estabelecimentoAllDto = new EstabelecimentoAllDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoAllDto);
			estabelecimentosList.add(estabelecimentoAllDto);
		}		
		return estabelecimentosList;
	}
	
	public Page<Estabelecimento> buscarPorUsuarioAutenticadoVsEstabelecimentoComPermissaReEstabelecimentoTipoSilo(
			String filtro, Pageable pageable) {
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		return estabelecimentoRepository.findAll(
				EstabelecimentoSpecs.usernameAtivoEquals(usuario.getUsername())
				.and(EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filtro))
				.and(EstabelecimentoSpecs.siloEquals(true))
				.and(EstabelecimentoSpecs.reEquals(true))
				, pageable);
	}	
		
	public List<Estabelecimento> buscarEstabelecimentosDisponiveisSilodoUsuario(Long idUsuario){
		return estabelecimentoRepository.buscarEstabelecimentosDisponiveisSilodoUsuario(idUsuario);
	}

	public Page<Estabelecimento> buscarPorUsuarioAutenticadoComHierarquiaEstabelecimentoQueSejaSilo(String filtro,
			Pageable pageable) {
		Usuario usuario = usuarioService.getUsuarioLogado();
		return estabelecimentoRepository.findAll(
				EstabelecimentoSpecs.hierarquiaEstabelecimento(usuario.getEstabelecimento(), usuario.getRegional())
				.and(EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filtro))
				.and(EstabelecimentoSpecs.siloEquals(true))
				.and(EstabelecimentoSpecs.doSituacao(ATIVO))
				, pageable);
	}

	public List<String> buscarCodigosPorUsuarioLogadoAutenticadoComHierarquiaEstabelecimento() {
		Usuario usuario = usuarioService.getUsuarioLogado();
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.findAll (
				EstabelecimentoSpecs.hierarquiaEstabelecimento(usuario.getEstabelecimento(), usuario.getRegional())
				.and(EstabelecimentoSpecs.doSituacao(ATIVO)));
		
		List<String> codigos = new ArrayList<>();
		if(estabelecimentos != null) {
			for (Estabelecimento estabelecimento : estabelecimentos) {
				codigos.add(estabelecimento.getCodigo());
			}
		}
		
		return codigos;
	}
	
	public List<EstabelecimentoNomeCodigoDto> filtrarEstabelecimentoSilo(String filtro) {
		Usuario usuario = usuarioService.getUsuarioLogado();
		List<Estabelecimento> estabelecimentos = new ArrayList<>();
		if(usuario.getRegional().equals("001")) {
			estabelecimentos = estabelecimentoRepository.findAll(
					EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filtro)
					.and(EstabelecimentoSpecs.siloEquals(true)),
					Sort.by(Sort.Direction.ASC, "codigoRegional"));
		}else {
			estabelecimentos =  estabelecimentoRepository.findAll(
					EstabelecimentoSpecs.usernameAtivoEquals(usuario.getUsername())
					.and(EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filtro))
					.and(EstabelecimentoSpecs.siloEquals(true)),
					Sort.by(Sort.Direction.ASC, "codigoRegional"));
		}
		
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: estabelecimentos) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;
	}
	
	public List<EstabelecimentoNomeCodigoDto> findBySiloNotNaturezaOperacao() {
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: estabelecimentoRepository.findBySiloNotNaturezaOperacao()) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;		
		
	}
	
	public List<EstabelecimentoNomeCodigoDto> findByNaturezaTributariaSilo() {
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: estabelecimentoRepository.findByNaturezaTributariaSilo()) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;		
		
	}
	
	public List<EstabelecimentoNomeCodigoDto> estabelecimentoUbsFilter(EstabelecimentoFilter filter) {
		List<Estabelecimento> lista = estabelecimentoRepository.findAll(
				EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filter.getPesquisar())
				.and(EstabelecimentoSpecs.ubsEquals(filter.getLogUbs()))
				.and(EstabelecimentoSpecs.doSituacao(Situacao.ATIVO))
			
		);
		if(lista.isEmpty()) {
			throw new ObjectDefaultException("Não encontramos estabelecimentos ubs com codigo ou nome: " + filter);
		}	
		
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: lista) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;		
		
	}
	
	public List<EstabelecimentoNomeCodigoDto> findBySiloNotItemAvariado(String tipoValidacao, Long idGrupo) {	
		log.info("Listando estabelecimentos pelo tipoValidacao: {} e grupoProduto: {}", tipoValidacao, idGrupo);
		List<Estabelecimento> estabelecimentos = estabelecimentoRepository.findBySiloNotItemAvariado(tipoValidacao, idGrupo);
		
		if(estabelecimentos.isEmpty()) {
			throw new ObjectDefaultException("Todos os estabelecimento encontram-se vinculados a este grupo de produto");
		}
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: estabelecimentos) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;		
		
	}
	
	public List<EstabelecimentoNomeCodigoDto> estabelecimentoSiloFilter(EstabelecimentoFilter filter) {
		List<Estabelecimento> lista = estabelecimentoRepository.findAll(
				EstabelecimentoSpecs.codigoOuNomeFantasiaLike(filter.getPesquisar())
				.and(EstabelecimentoSpecs.siloEquals(filter.getLogSilo()))
				.and(EstabelecimentoSpecs.doSituacao(Situacao.ATIVO)),
				Sort.by(Sort.Direction.ASC, "codigo")
			
		);
		
		if(lista.isEmpty()) {
			throw new ObjectDefaultException("Não encontramos estabelecimentos ubs com codigo ou nome: " + filter);
		}	
		
		List<EstabelecimentoNomeCodigoDto> respose = new ArrayList<>();
		for(Estabelecimento item: lista) {
			EstabelecimentoNomeCodigoDto estabelecimento = new EstabelecimentoNomeCodigoDto();
			BeanUtils.copyProperties(item, estabelecimento);
			respose.add(estabelecimento);
		}
		return respose;			
	}
}

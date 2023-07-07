package br.coop.integrada.api.pa.domain.service.rependente;

import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Entrada_RE;
import static br.coop.integrada.api.pa.domain.enums.StatusPesagem.AGUARDANDO_RE;
import static br.coop.integrada.api.pa.domain.enums.StatusPesagem.GERANDO_RE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.gerarRe.TipoEntradaEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaSecagemEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.StatusMovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioRequest;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioResponse;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteDesmembramento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteJavaDto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteParametrosEstabelecimento;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteTipoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RependentePesagemDto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.externo.NumeroReDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoGmoDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.UsuarioEstabelecimentoFuncaoDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemModalFilter;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoPesoLiquidoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRecepcaoResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoRendaLiquidaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoValorSecagemResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.ProdutoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.RePendenteSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.VerificarTaxasReResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento.RePendenteDesmembramentoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.DocumentoPesagemValidation;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.EntradaReValidation;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.LoteValidation;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.NotaFiscalPjValidation;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.ProdutoPadraoValidation;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.ImovelProdutorRep;
import br.coop.integrada.api.pa.domain.repository.ImovelRep;
import br.coop.integrada.api.pa.domain.repository.UsuarioRep;
import br.coop.integrada.api.pa.domain.repository.gmo.TipoGmoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.PesagemRep;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorRep;
import br.coop.integrada.api.pa.domain.repository.rependente.RePendenteRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.movimentoDiario.MovimentoDiarioService;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.parametros.ItemAvariadoDetalheService;
import br.coop.integrada.api.pa.domain.service.parametros.ParametroUsuarioEstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.parametros.TaxaService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.service.produto.TipoGmoService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import br.coop.integrada.api.pa.domain.service.semente.SementeClasseService;
import br.coop.integrada.api.pa.domain.service.tratamentoTransacaoRe.TratamentoTransacaoReService;
import br.coop.integrada.api.pa.domain.validation.CadproValidation;
import br.coop.integrada.api.pa.domain.validation.re.pendente.RePendenteValidation;
import br.coop.integrada.api.pa.domain.validation.rec.entrega.RecEntregaValidation;

@Service
public class RePendenteService {
	
	private static final Logger log = LoggerFactory.getLogger(RePendenteService.class);
	
	@Autowired
	private RePendenteRep repenRep;
	
	@Autowired
	private RePendenteItemService rePendenteItemService;
	
	@Autowired
	private RePendenteDesmembramentoService rePendenteDesmembramentoService;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
	private GrupoProdutoService grupoProdutoService;
	
	@Autowired
	private ProdutorService produtorService;
	
	@Autowired
	private UsuarioRep usuarioRep;
	
	@Autowired
	private ProdutorRep produtorRep;	

	@Autowired 
	private TipoGmoRep tipoGmoRep;
	
	@Autowired
	private PesagemRep pesagemRep;
	
	@Autowired
	private ImovelRep imovelRep; 
	
	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	@Autowired
	private ImovelProdutorRep imovelProdutorRep;
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	
	@Autowired
	private ParametroEstabelecimentoRep parametroEstabelecimentoRep;

    @Autowired
    private RePendenteValidation rePendenteValidation;
    
    @Autowired
    private ProdutoRep produtoRep;
    
    @Autowired
    private ParametroEstabelecimentoService parametroEstabelecimentoService;
    
    @Autowired
    private PesagemService pesagemService;
    
    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private TaxaService taxaService;
    
    @Autowired
    private TipoGmoService tipoGmoService;
    
    @Autowired
    private ImovelService imovelService;
    
    @Autowired
    private SementeClasseService sementeClasseService;
    
    @Lazy
    @Autowired
    private RecEntregaService recEntregaService;
    
    @Autowired
    private MovimentoDiarioService movimentoDiarioService;
    
    @Autowired
	ParametroUsuarioEstabelecimentoService paramUsuarioEstabelecimentoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ItemAvariadoDetalheService itemAvariadoDetalheService;
	
	@Autowired
	private RecEntregaValidation recEntregaValidation;
	
	@Autowired
	private GerarReService gerarReService;
	
	@Autowired
	private EntradaReService entradaReService;
	
	@Autowired
	private TratamentoTransacaoReService tratamentoTransacaoReService;
	
	@Autowired
	private CadproValidation cadproValidation;
	
    @Transactional
	public RePendente salvar(RePendenteDto obj) {
    	RePendente rePendente = new RePendente();
		RePendente rePendenteAtual = buscarPorCodigoEstabelecimentoEPlacaECodigoGrupoProdutoECodigoProdutor(
				obj.getCodigoEstabelecimento(),
				obj.getPlaca(),
				obj.getCodigoGrupoProduto(),
				obj.getCodigoProdutor(),
				obj.getNomeProd());
		
		if(rePendenteAtual != null) {
			BeanUtils.copyProperties(rePendenteAtual, rePendente);
			
			if(obj.getCadastro() == null || obj.getCadastro()) {
				String dados = "(";
				dados += "Código Estabelecimento: " + rePendenteAtual.getEstabelecimento().getCodigo(); 
				dados += ", Placa: " + rePendenteAtual.getPlaca(); 
				dados += ", Código Grupo Produto: " + rePendenteAtual.getGrupoProduto().getFmCodigo(); 
				dados += ", Código Produtor: " + rePendenteAtual.getProdutor().getCodProdutor();
				
				if(obj.getNomeProd() != null) {
					dados += "Nome Produtor: " + obj.getNomeProd();
				}
				
				dados += ")";
				
				throw new ObjectDefaultException("Já existe uma entrada de produção registrada com os mesmos dados informados. " + dados);
			}
		}
		
		BeanUtils.copyProperties(obj, rePendente);
		
		// VERIFICAR SE OS CÓDIGOS INFORMADOS EXISTEM E VINCULAR NA RE PENDENTE
		if(rePendente.getEstabelecimento() == null) {
			String codigoEstabelecimento = obj.getCodigoEstabelecimento();
			Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);
			
			if(estabelecimento == null) {
				throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código \"" + codigoEstabelecimento + "\"");
			}
			
			rePendente.setEstabelecimento(estabelecimento);
		}
		
		if(rePendente.getGrupoProduto() == null) {
			String codigoGrupoProduto = obj.getCodigoGrupoProduto();
			GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoGrupoProduto);
			rePendente.setGrupoProduto(grupoProduto);
		}
		
		if(rePendente.getProdutor() == null) {
			String codigoProdutor = obj.getCodigoProdutor();
			Produtor produtor = produtorService.buscarPorCodigoProdutor(codigoProdutor);
			
			if(produtor.getCooperativa() && Strings.isEmpty(obj.getNomeProd())) {
				throw new ObjectDefaultException("Campo {nomeProd} obrigatório");
			}
			
			rePendente.setProdutor(produtor);
		}
		
		TipoGmo tipoGmo = null;
		if(Strings.isNotEmpty(obj.getTipoGmo())) {
			tipoGmo = tipoGmoService.buscarPorTipoGmo(obj.getTipoGmo());
		}
		rePendente.setTipoGmo(tipoGmo);
		
		Imovel imovel = null;
		if(obj.getMatriculaImovel()!= null && rePendente.getProdutor() != null) {
			Long matriculaImovel = obj.getMatriculaImovel();
			String codigoProdutor = rePendente.getProdutor().getCodProdutor();
			imovel = imovelService.buscarImovelAtivoPorMatriculaImovelECodigoProdutor(matriculaImovel, codigoProdutor);
		}
		rePendente.setImovel(imovel);
		
		if(obj.getClasse() != null) {
			SementeClasse sementeClasse = sementeClasseService.buscarPorCodigo(obj.getClasse());
			
			if(sementeClasse == null) {
				throw new ObjectNotFoundException("Não foi encontrado classe com o código " + obj.getClass());
			}
			
			rePendente.setClasse(sementeClasse);
		}
		//-------------------------------------------------------------------------------------------------------------
		
		if(rePendente.getDtEntrada() == null) {
			throw new ObjectDefaultException("O campo \"Data Entrada\" é obrigatório.");
		}
		
		Calendar dataEntrada = Calendar.getInstance();
		dataEntrada.setTime(rePendente.getDtEntrada());
		Integer safra = dataEntrada.get(Calendar.YEAR);
		Long idEstabelecimento = rePendente.getEstabelecimento().getId();
		Long idGrupoProduto = rePendente.getGrupoProduto().getId();
		
		try {
			taxaService.buscarTaxaProducaoEstabelecimento(safra, idEstabelecimento, idGrupoProduto);
		}catch (Exception e) {
			throw new ObjectNotFoundException("Taxa de Produção por Estabelecimento não encontrada, favor entrar em contato com SIO.\n" + e.getMessage());
		}
		
		if(CollectionUtils.isEmpty(obj.getItens()) == false){
			List<String> mensagens = rePendenteValidation.validar(rePendente, true);
			if(!mensagens.isEmpty()) {
				String mensagem = String.join("\n", mensagens);
				throw new ObjectDefaultException(mensagem.trim());
			}
		}
		
		rePendente = repenRep.save(rePendente);
		
		List<RePendenteItemDto> itemDtos = obj.getItens();
		rePendenteItemService.validarEIncluirDadosConformeParametrizacao(rePendente, itemDtos);
		
		List<RePendenteDesmembramentoDto> desmembramentoDtos = obj.getDesmembramentos();
		rePendenteDesmembramentoService.validarDesmembramentos(rePendente, itemDtos, desmembramentoDtos);
		
		List<RePendenteItem> itens = rePendenteItemService.converterDto(rePendente, itemDtos);
		rePendente.getItens().clear();
		rePendente.getItens().addAll(itens);
		
		List<RePendenteDesmembramento> desmembramentos = rePendenteDesmembramentoService.converterDto(rePendente, desmembramentoDtos);
		rePendente.getDesmembramentos().clear();
		rePendente.getDesmembramentos().addAll(desmembramentos);
		
		if(CollectionUtils.isEmpty(desmembramentos)) {
			cadproValidation.validarRePendente(rePendente);
		}
		else {
			cadproValidation.validarDesmembramento(desmembramentos);
		}
		
		if(obj.getEntradaManual() == false) {
    		String codigoEstabelecimento = rePendente.getEstabelecimento().getCodigo();
    		Integer nroPesagem = rePendente.getNrDocPes();
			
			Pesagem pesagem = pesagemService.buscarPesagemEstabelecimentoSafraNrDocumento(codigoEstabelecimento, safra, nroPesagem);
			
    		pesagem.setStatus(GERANDO_RE);
    		pesagemRep.save(pesagem);
		}
		
		return rePendente;
	}
	
	public RePendente buscarPorCodigoEstabelecimentoEPlacaECodigoGrupoProdutoECodigoProdutor(
			String codigoEstabelecimento, String placa, String codigoGrupoProduto, String codigoProdutor, String nomeProd) {
		return repenRep.findByEstabelecimentoCodigoIgnoreCaseAndPlacaIgnoreCaseAndGrupoProdutoFmCodigoIgnoreCaseAndProdutorCodProdutorIgnoreCaseAndNomeProdIgnoreCase(
				codigoEstabelecimento, placa, codigoGrupoProduto, codigoProdutor, nomeProd).orElse(null);
	}
	
	public RePendente findById(Long id) {
		return repenRep.getReferenceById(id);
	}
	
	public Page<RePendenteSimplesDto> listarPorCodigoEstabelecimento(String codigoEstabelecimento, Pageable pageable) {
		Page<RePendente> rePendentePage = repenRep.findByEstabelecimentoCodigo(codigoEstabelecimento, pageable);
		List<RePendenteSimplesDto> rePendenteSimlesDtos = RePendenteSimplesDto.construir(rePendentePage.getContent());
		
		return new PageImpl<>(rePendenteSimlesDtos, pageable, rePendentePage.getTotalElements());
    }

    public List<RePendenteEstabelecimentoDto> getEstabelecimentos(String busca){
        Pageable pageable = PageRequest.of(0, 5, Sort.by("nomeFantasia"));
        Usuario usuario = usuarioRep.findByUsernameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName());
        Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(usuario.getEstabelecimento());

        List<Estabelecimento> lista = new ArrayList<>();
        if(estabelecimento.getMatriz()) {
            lista = estabelecimentoRep.findByCodigoContainingIgnoreCaseOrNomeFantasiaContainingIgnoreCaseOrderByNomeFantasiaAsc(busca, busca, pageable);
        }else {
            lista = estabelecimentoRep.findByCodigoOrNomeFantasiaContainingIgnoreCaseAndCodigoRegionalOrderByNomeFantasiaAsc(busca, busca, estabelecimento.getCodigoRegional(), pageable);
        }

        List<RePendenteEstabelecimentoDto> response = new ArrayList<>();
        for(Estabelecimento item: lista) {
            RePendenteEstabelecimentoDto estabelecimentoDto = new  RePendenteEstabelecimentoDto();
            BeanUtils.copyProperties(item, estabelecimentoDto);
            response.add(estabelecimentoDto);
        }		
        return response;
    }

    public List<RePendenteGrupoProdutoDto> getGruposProdutos(String dados) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("descricao"));

        List<RePendenteGrupoProdutoDto> response = new ArrayList<>();
        for(GrupoProduto item: grupoProdutoRep.findByFmCodigoContainingOrDescricaoContainingIgnoreCase(dados, dados, pageable)) {
            RePendenteGrupoProdutoDto grupoProdutoDto = new RePendenteGrupoProdutoDto();
            BeanUtils.copyProperties(item, grupoProdutoDto);
            response.add(grupoProdutoDto);
        }

        return response;
    }

    public List<ProdutorSimplesDto> getProdutor(String dados){
        Pageable pageable = PageRequest.of(0, 5);    
        List<ProdutorSimplesDto> response =  new ArrayList<>();
        for(Produtor item: produtorRep.findByCodProdutorContainingIgnoreCaseOrNomeContainingIgnoreCaseOrderByNomeAsc(dados, dados, pageable)) {
            ProdutorSimplesDto produtorDto = new  ProdutorSimplesDto();
            BeanUtils.copyProperties(item, produtorDto);
            response.add(produtorDto);
        }
        return response;
    }

    public List<ImovelSimplesDto> getImovel(Long id){ 
        List<ImovelSimplesDto> response = new ArrayList<>();
        Produtor produtor = produtorService.findById(id);
        
        if(produtor != null) {
	        for(ImovelProdutor item: imovelProdutorRep.findByCodProdutorAndDataInativacaoNullAndTransferenciaIsFalseOrderByImovel(produtor.getCodProdutor())){
	            ImovelSimplesDto imovelDto = new ImovelSimplesDto();
	            BeanUtils.copyProperties(item.getImovel(), imovelDto);
	            response.add(imovelDto);
	        }
        }
        return response;
    }

    public RePendenteParametrosEstabelecimento getParametros(Long id){
        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findByEstabelecimentoId(id);
        RePendenteParametrosEstabelecimento parametrosDto = new RePendenteParametrosEstabelecimento();

        BeanUtils.copyProperties(parametroEstabelecimento, parametrosDto);
        return parametrosDto;
    }

    public void deletar(Long id) {
    	RePendente rePendente = repenRep.findById(id)
    			.orElseThrow(() -> new ObjectDefaultException("Não foi encontrado RE Pendente com o ID informado."));

    	if(rePendente.getEntradaManual() == false) {
    		String codigoEstabelecimento = rePendente.getEstabelecimento().getCodigo();
    		Integer nroPesagem = rePendente.getNrDocPes();
    		
    		Calendar dataEntrada = Calendar.getInstance();
    		dataEntrada.setTime(rePendente.getDtEntrada());
    		Integer safra = dataEntrada.get(Calendar.YEAR);
    		
    		Pesagem pesagem = pesagemRep.findByNroDocPesagemAndCodEstabelecimentoAndSafra(nroPesagem, codigoEstabelecimento, safra);
    		
    		if(pesagem == null) {
    			throw new ObjectDefaultException("Não foi encontrado o ticket de pesagem vinculado a entrada de produção. Parâmetros: número pesagem " + nroPesagem + ", código de estabelecimento " + codigoEstabelecimento + " e safra " + safra + ".");
    		}
    		
			pesagem.setStatus(AGUARDANDO_RE);
    		
    		pesagemRep.save(pesagem);
    	}    	
        repenRep.delete(rePendente);
    }

    public List<String> validarRePendente(String codigoEstabelecimento, String placa, String codigoGrupoProdutor, String codigoProdutor) {
        RePendente rePendente = repenRep.findByCodigoEstabelecimentoAndPlacaAndCodigoGrupoProdutoAndCodigoProdutor(codigoEstabelecimento, placa, codigoGrupoProdutor, codigoProdutor);

        if(rePendente == null) {
            return Arrays.asList("Não foi encontrado re pendente!");
        }

        return rePendenteValidation.validar(rePendente, true);
    }

    public Page<RependentePesagemDto> getTicketPesagens(String codigo, Pageable pageable) {
        Page<Pesagem> pesagem = pesagemRep.findByCodEstabelecimentoAndDataInativacaoNullOrderByDataCadastroDesc(codigo, pageable);		
        List<RependentePesagemDto> response = new ArrayList<>();

        for(Pesagem item: pesagem.getContent()) {
            RependentePesagemDto pesagemDto = new RependentePesagemDto();			
            BeanUtils.copyProperties(item, pesagemDto);
            response.add(pesagemDto);

        }
        return new PageImpl<>(
                response,
                pageable,
                pesagem.getTotalElements()
        );
	}
    
    public Page<RependentePesagemDto> getModalTicketPesagens(Pageable pageable, PesagemModalFilter filter) {  
    	System.out.println("Filtrando pesagem ( Estabelecimento: "+filter.getEstabelecimento() + ");" );
		
		Page<Pesagem> pesagem = pesagemRep.buscarPesagensPendentesModel(pageable, filter);		
        List<RependentePesagemDto> response = new ArrayList<>();

        for(Pesagem item: pesagem.getContent()) {
            RependentePesagemDto pesagemDto = new RependentePesagemDto();			
            BeanUtils.copyProperties(item, pesagemDto);
            response.add(pesagemDto);

        }
        return new PageImpl<>(
                response,
                pageable,
                pesagem.getTotalElements()
        );
    }
	
	public RePendenteJavaDto getPesagensNrDocumento(Integer documento) {
		Pesagem pesagem = pesagemRep.findByNroDocPesagem(documento);				
		RePendenteJavaDto response = new RePendenteJavaDto();

		Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(pesagem.getCodEstabelecimento());
		if(estabelecimento != null) {
			RePendenteEstabelecimentoDto estabelecimentoDto = new  RePendenteEstabelecimentoDto();
			BeanUtils.copyProperties(estabelecimento, estabelecimentoDto);		
			response.setEstabelecimento(estabelecimentoDto);
		}
		
		Produto produto = produtoRep.findByCodItemIgnoreCase(pesagem.getCodProduto());
		if(produto != null) {
			GrupoProduto grupoProduto = grupoProdutoRep.findByFmCodigo(produto.getGrupoProduto().getFmCodigo());
			RePendenteGrupoProdutoDto grupoProdutoDto = new RePendenteGrupoProdutoDto();
			BeanUtils.copyProperties(grupoProduto, grupoProdutoDto);
			response.setGrupoProduto(grupoProdutoDto);
		}
		
		Produtor produtor = produtorRep.findByCodProdutor(pesagem.getCodProdutor().toString());
		if(produtor != null) {
			ProdutorSimplesDto produtorDto = new ProdutorSimplesDto();
			BeanUtils.copyProperties(produtor, produtorDto);
			response.setProdutor(produtorDto);
		}
		
		Imovel imovel = imovelRep.findByMatricula(pesagem.getCodImovel());
		if(imovel != null) {
			ImovelSimplesDto imovelDto = new ImovelSimplesDto();
			BeanUtils.copyProperties(imovel, imovelDto);
			response.setImovel(imovelDto);
		}
		
		TipoGmo tipoGmo = tipoGmoRep.findByTipoGmoIgnoreCase(pesagem.getTipoGmo());
		if(tipoGmo != null) {
			RePendenteTipoGmo tipoGmoDto = new RePendenteTipoGmo();
			BeanUtils.copyProperties(tipoGmo, tipoGmoDto);
			response.setTipoGmo(tipoGmoDto);
		}
		
		response.setId(pesagem.getId());
		response.setPlaca(pesagem.getPlaca());
		response.setHrEntrada(pesagem.getHoraEntrada());
		response.setDtEntrada(pesagem.getDataEntrada());
		response.setMatricula(pesagem.getMatricula());
		response.setNrNfProd(Long.parseLong(pesagem.getNfProdutor()));
		response.setNrDocPes(pesagem.getNroDocPesagem());
		response.setPesoBruto(pesagem.getPesoEntrada());		
		response.setMotorista(pesagem.getMotorista());
		response.setTulha(pesagem.getCodigoMoega());
		response.setObservacoes(pesagem.getObservacao());
		response.setProdPadr(pesagem.getProdPadr());
		response.setDescarUnid(pesagem.getDescarUnid());
		
		
		if(pesagem.getPesoSaida() != null) response.setTaraVeiculo(pesagem.getPesoSaida());
		if(pesagem.getPesoLiquido() != null) response.setPesoLiquido(pesagem.getPesoLiquido());
		if(pesagem.getOrdemCampo() != null) response.setNrOrdCampo(Integer.parseInt(pesagem.getOrdemCampo()));
		if(pesagem.getLaudoVistoria() != null) response.setNrLaudo(Integer.parseInt(pesagem.getLaudoVistoria()));
	
		pesagemRep.save(pesagem);
		
		return response;
	}

	public ProdutoPesagemDto buscarProdutoPesagemPorCodigoEstabelecimentoSafraNroDocPesagem(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem) {
		// BUSCAR OS PARÂMETROS DO ESTABELECIMENTO
		ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(codigoEstabelecimento);
		
		// CASO O ESTABELECIMENTO NÃO TENHA PARÂMETRO
		if(parametroEstabelecimento == null) {
			throw new ObjectNotFoundException("Não foi encontrado parâmetro para o estabelecimento \"" + codigoEstabelecimento + "\".");
		}
		// CASO O PARÂMETRO ESTEJA INATIVO
		else if(!parametroEstabelecimento.getAtivo()) {
			throw new ObjectDefaultException("O parâmetro do estabelecimento \"" + codigoEstabelecimento + "\" está inativo.");
		}
		
		// VERIFICAR SE O ESTABELECIMENTO ESTÁ PARAMETRIZADO PARA INTEGRAR RE
		if(!parametroEstabelecimento.getIntegraRe()) {
			throw new ObjectDefaultException("O estabelecimento não está parametrizado para \"Integrar RE\".");
		}
		
		// BUSCAR AS INFORMAÇÕES DE PESAGEM
		Pesagem pesagem = pesagemService.buscarPorCodigoEstabelecimentoSafraDocPesagem(codigoEstabelecimento, safra, nroDocPesagem);
		
		// CASO NÃO TENHA REGISTRO DE PESAGEM
		if(pesagem == null || !pesagem.getAtivo()) {
			throw new ObjectNotFoundException("Não foi encontrado registro de pesagem com os parâmetros estabelecimento \"" + codigoEstabelecimento + "\", safra \"" + safra + "\" e número documento pesagem \"" + nroDocPesagem + "\".");
		}
		
		// BUSCAR AS INFORMAÇÕES DO PRODUTO E GRUPO PRODUTO
		Produto produto = null;
		GrupoProduto grupoProduto = null;
		String codigoProduto = pesagem.getCodProduto();
		
		if(codigoProduto != null) {
			// CASO O CÓDIGO DO PRODUTO VENHA COM A SIGLA "AF" SERÁ REMOVIDA
			codigoProduto = codigoProduto.replaceAll("AF", "");
			
			try {
				produto = produtoService.buscarProdutoAtivoPorCodItem(codigoProduto);
			}
			catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		// OBTER INFORMAÇÕES DO GRUPO DE PRODUTO
		if(produto != null) grupoProduto = produto.getGrupoProduto();
		
		return ProdutoPesagemDto.construir(produto, grupoProduto, pesagem);
	}

	public VerificarTaxasReResponseDto verificarTaxasRe(VerificarTaxasReDto input) {
		try {
						
			VerificarTaxasReResponseDto responseDto = new VerificarTaxasReResponseDto(null);
			
			//---------------------------------------------
			//Análise Comum a Todas as Opções
			//---------------------------------------------
			Produtor produtor = produtorService.buscarPorCodigoProdutor(input.getCodProdutor());
			
			//--------------------------------------------
			// Verificar se RE tem retenção de Royalties
			//--------------------------------------------
						
			
			GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(input.getFmCodigo());
			GrupoProdutoDto grupoProdutoDto = grupoProdutoService.buscarGrupoProdutoDto(grupoProduto.getId());
			
			
			if(produtor.getParticipanteBayer() || produtor.getCooperativa()) {
				responseDto.setRetemDPI(false);
			}
			else
			{
							
			
				if(grupoProduto.isLogTransgenico()) {
						
					TipoGmo tipoGmo = null;
					
					for(GrupoProdutoGmoDto objtipoGmo : grupoProdutoDto.getListGrupoProdutoGmo() ){
						if(objtipoGmo.getTipoGmo().getId().equals(input.getGmo())) {
							tipoGmo = tipoGmoService.buscarTipoGmoPorId( objtipoGmo.getTipoGmo().getId() );
						}
					}
				
					if(tipoGmo == null) {
						//Se NÃO encontrar GMO
						responseDto.setRetemDPI(false);
					}
					else {
						//Se encontrar GMO
						if( tipoGmo.getPerDeclarada().compareTo(BigDecimal.ZERO) == 0  && tipoGmo.getPerTestada().compareTo(BigDecimal.ZERO) == 0) {
							responseDto.setRetemDPI(false);
						}
						
						//Se maior que zero
						if( tipoGmo.getPerDeclarada().compareTo(BigDecimal.ZERO) == 1  && tipoGmo.getPerTestada().compareTo(BigDecimal.ZERO) == 1) {
							responseDto.setRetemDPI(true);
							
							if(input.getDeclaradaTestada().equals("Declarada")){
								responseDto.setPercentualDpi( tipoGmo.getPerDeclarada() ); 
							}
							else if(input.getDeclaradaTestada().equals("Testada")) {
								responseDto.setPercentualDpi( tipoGmo.getPerTestada() ); 
							}
							
							
							responseDto.setObservacaoRE(tipoGmo.getObsRomaneio());
						}
					}
					
				}
				else {
					responseDto.setRetemDPI(false);
				}
			}
				
			//--------------------------------------------
			// Verificar se tem cobrança de sub produto
			//--------------------------------------------
			
			if(!grupoProduto.isTbm()) {
				responseDto.setSubProduto(false);
			}
			
			if(grupoProduto.isTbm() && input.getTbm().compareTo(BigDecimal.ZERO) <= 0) {
				responseDto.setSubProduto(false);
			}
			
			if(input.getTbm() != null && input.getTbm().compareTo(BigDecimal.ZERO) > 0) {
				String grupoProdutoSub = grupoProduto.getFmCodigoSub();
				String produtoSub = grupoProduto.getItSubCoop();
				
				if(Strings.isEmpty(grupoProdutoSub) || Strings.isEmpty(produtoSub)) {
					throw new ObjectDefaultException("Não existe informação de sub produto no cadastro do grupo de produto \"" + input.getFmCodigo() + "\".");
				}
				
				responseDto.setSubProduto(true);
				responseDto.setGrupoProdutoSub(grupoProdutoSub);
				responseDto.setProdutoSub(produtoSub);
			}

			//------------------------------------------------------------------
			// Verificar se tem cobrança de Taxa Secagem / Limpeza por produto
			//------------------------------------------------------------------
			if(produtor.getCooperativa()) {
				responseDto.setSecagem(false);
			}
			else
			{
				if(input.getProdutoPadronizado()) {
					responseDto.setSecagem(false);
				}
				else
				{
					
					Taxa taxa = taxaService.buscarTaxaProducaoEstabelecimento(input.getSafra(), input.getCodEstabelecimento(), grupoProduto.getId());
					
					if(!taxa.getCobraSecagem()) {
						responseDto.setSecagem(false);
					}
					else
					{
						if( taxa.getTipoCobraSecagemNa() == TipoCobrancaEnum.FIXACAO) {
							responseDto.setSecagem(false);
						}
						else if( taxa.getTipoCobraSecagemNa() == TipoCobrancaEnum.ENTRADA) {
						
							responseDto.setSecagem(true);
							
							if(taxa.getTipoCobrancaSecagemPor() == TipoCobrancaSecagemEnum.EM_QUILOS) {
								responseDto.setCobranca("RE");
								responseDto.setProduto(grupoProduto.getItTaxaCoop());
							}
							else if(taxa.getTipoCobrancaSecagemPor() == TipoCobrancaSecagemEnum.VALOR) {
								responseDto.setCobranca("Renda");
								responseDto.setProduto("");
							}
						
						}
					
					}
				}
			}
			
			//------------------------------------------------------
			//Verificar se tem cobrança de Recepção
			//-------------------------------------------------------
			
			if(produtor.getCooperativa()) {
				responseDto.setSecagem(false);
			}
			else
			{
				Taxa taxa = taxaService.buscarTaxaProducaoEstabelecimento(input.getSafra(), input.getCodEstabelecimento(), grupoProduto.getId());
								
				if(!taxa.getCobraRecepcao()) {
					responseDto.setRecepcao(false);
				}
				else {
					if( taxa.getTipoCobraRecepcaoPor() == TipoCobrancaEnum.FIXACAO) {
						responseDto.setRecepcao(false);
					}
					else if( taxa.getTipoCobraRecepcaoPor() == TipoCobrancaEnum.ENTRADA) {
						
						if(!input.getDescargaUnidade())
						{
							responseDto.setRecepcao(true);
							responseDto.setValorRecepcao(taxa.getValorTaxaRecepcao());
						}
						else
						{
							responseDto.setRecepcao(false);
						}
						
					}
				}
				
			}
			
			responseDto.setMensagem("Cálculo realizado com sucesso.");
			return responseDto;
		}
		catch (Exception e) {
			throw e;
		}
		
	}
	
	public CalculoValorSecagemResponseDto calcularQtdSecagem(BigDecimal pesoLiquido, BigDecimal percentualDescontoImpureza, BigDecimal valorTaxaLimpeza, BigDecimal precoPonto) {
		//Não premitir parâmetros nulos
		if(percentualDescontoImpureza == null || pesoLiquido == null ||
				precoPonto == null || valorTaxaLimpeza == null) {
			throw new ObjectDefaultException("Os parâmetros informados não podem ser nulos.");
		}
		
		//Não permite que o Preço Ponto seja igual a Zero pois ele será o divisor em um dos cálculos abaixo
		if(precoPonto.equals(BigDecimal.ZERO)){
			throw new ObjectDefaultException("O parâmetro 'Preço Ponto' não pode ser zero.");
		}
		
		
		try {
			/*
			 Funcionalidade:
				- Qt.impureza = Peso Liquido * (percentual de desconto impureza / 100)
				- Vl.Taxa = valor da taxa de limpeza / 60
				- Vl.Secagem = (Peso Liquido – qt.impureza) * vl-taxa
				- Qt.Secagem = Vl.Secagem / Preço Ponto
			 */

			BigDecimal quantidadeImpureza = BigDecimal.ZERO;
			if(BigDecimal.ZERO.equals(percentualDescontoImpureza) == false) {
				quantidadeImpureza = percentualDescontoImpureza.divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN);
				quantidadeImpureza = pesoLiquido.multiply(quantidadeImpureza).setScale(9, RoundingMode.HALF_EVEN);
			}

			BigDecimal valorTaxa = BigDecimal.ZERO;
			if(BigDecimal.ZERO.equals(valorTaxaLimpeza) == false) {
					valorTaxa = valorTaxaLimpeza.divide(new BigDecimal(60), 9, RoundingMode.HALF_EVEN);
			}

			BigDecimal valorSecagem = BigDecimal.ZERO;
			if(BigDecimal.ZERO.equals(valorTaxa) == false) {
				valorSecagem = pesoLiquido.subtract(quantidadeImpureza);
				valorSecagem = valorSecagem.multiply(valorTaxa).setScale(9, RoundingMode.HALF_EVEN);
			}

			BigDecimal quantidadeSecagem = BigDecimal.ZERO;
			if(BigDecimal.ZERO.equals(valorSecagem) == false) {
				quantidadeSecagem = valorSecagem.divide(precoPonto, 0, RoundingMode.HALF_EVEN);
			}

			return new CalculoValorSecagemResponseDto(
					valorSecagem.setScale(0, RoundingMode.HALF_EVEN), 
					quantidadeSecagem, 
					"Cálculo realizado com sucesso.");
		}
		catch(Exception e) {
			throw new ObjectDefaultException("O cálculo não pode ser realizado. " + e.getMessage());
		}
	}
	
	public CalculoRecepcaoResponseDto calcularRecepcao(BigDecimal quantidade, BigDecimal valorPonto, BigDecimal valorRecepcao) {
		
		//Não premitir parâmetros nulos
		if(quantidade == null || valorPonto == null || valorRecepcao == null) {
			throw new ObjectDefaultException("Os parâmetros informados não podem ser nulos.");
		}

		/*
		 * Funcionalidade:
		 * Valor recepção = (quantidade * valor recepção)-retornar com 9 casas decimais
		 * Quantidade recepção = (valor recepção / valor ponto)–retornar valor inteiro arredondamento padrão25,2 = 25 25,5 = 26
		 */
		
		try {
			BigDecimal recepcaoValor = (quantidade.multiply( valorRecepcao ));
			BigDecimal recepcaoQuantidade = recepcaoValor.divide(valorPonto, 0, RoundingMode.HALF_EVEN);
			
			return new CalculoRecepcaoResponseDto(recepcaoValor, recepcaoQuantidade, "Cálculo realizado com sucesso.");
		}
		catch (Exception e) {
			throw new ObjectDefaultException("O cálculo não pode ser realizado. " + e.getMessage());
		}
	}
	
	public CalculoRendaLiquidaResponseDto calcularRendaLiquida(CalculoRendaLiquidaDto input) {
		try {
			
			//Não premitir parâmetros nulos
			/*if(input.getCalculaSecagem() == null || 
					input.getPercDescChuvadoAvariado() == null ||
					input.getPercDescImpureza() == null ||
					input.getPercDescUmidade() == null ||
					input.getPercTBM() == null ||
					input.getPesoLiquido() == null ||
					input.getQuantidadeSecagem() == null ||
					input.getQuantidadeRecepcao() == null ||
					input.getTaxaSecagem() == null) {
				return ResponseEntity.badRequest().body(new CalculoRendaLiquidaResponseDto("Os parâmetros informados não podem ser nulos."));
			}*/
			
			if(input.getCalculaSecagem() == false) {
				
				BigDecimal baseCalculo = input.getPesoLiquido();
				BigDecimal qtdImpureza = baseCalculo.multiply((input.getPercDescImpureza().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN)));
				
				baseCalculo = baseCalculo.subtract(qtdImpureza);
				BigDecimal qtdUmidade = baseCalculo.multiply((input.getPercDescUmidade().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN)));
				
				baseCalculo = baseCalculo.subtract(qtdUmidade);
				BigDecimal qtdChuvadoAvariado = baseCalculo.multiply((input.getPercDescChuvadoAvariado().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN)));
  			    
				baseCalculo = baseCalculo.subtract(qtdChuvadoAvariado);
				BigDecimal qtdTbm = baseCalculo.multiply((input.getPercTBM().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN)));
				BigDecimal qtdSecagem = input.getQuantidadeSecagem();
				BigDecimal qtdRendaLiquida = input.getPesoLiquido();
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdImpureza);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdUmidade);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdChuvadoAvariado);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdTbm);
				qtdRendaLiquida = qtdRendaLiquida.subtract(input.getQuantidadeRecepcao());
				qtdRendaLiquida = qtdRendaLiquida.subtract(input.getQuantidadeSecagem());
				
				CalculoRendaLiquidaResponseDto responseDto = new CalculoRendaLiquidaResponseDto("Cálculo realizado com sucesso.");
				responseDto.setQtdChuvaAvariado(qtdChuvadoAvariado.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdImpureza(qtdImpureza.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdRendaLiquida(qtdRendaLiquida.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdSecagem(qtdSecagem.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdTBM(qtdTbm.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdUmidade(qtdUmidade.setScale(0, RoundingMode.HALF_EVEN));
								
				return responseDto;
			}
			else {
				
				//  Base Calculo = Peso Líquido
				BigDecimal baseCalculo = input.getPesoLiquido();
				
				
				//  Qt.Impureza = Base Calculo * (%Desc.Impureza / 100)
				BigDecimal qtdImpureza = baseCalculo.multiply(input.getPercDescImpureza().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN));
				
				//  Base Calculo = Base Calculo –Qt.Impureza
				baseCalculo = baseCalculo.subtract(qtdImpureza);
				
				// Qt.Umidade = Base Calculo * (%Desc.Umidade / 100)
				BigDecimal qtdUmidade = baseCalculo.multiply(input.getPercDescUmidade().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN));
				
				//  Base Calculo = Base Calculo –Qt.Umidade
				baseCalculo = baseCalculo.subtract(qtdUmidade);
				
				//  Qt.Chuvado/Avariado = Base Calculo * (%Desc.Chuvado/Avariado / 100)
				BigDecimal qtdChuvadoAvariado = baseCalculo.multiply(input.getPercDescChuvadoAvariado().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN));
				
				//  Base Calculo = Base Calculo –Qt.chuvado/avariado
				baseCalculo = baseCalculo.subtract(qtdChuvadoAvariado);
				
				//  Qt.Secagem= Base Calculo * (taxa secagem / 100)
				BigDecimal qtdSecagem = baseCalculo.multiply(input.getTaxaSecagem().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN));
				
				//  Base Calculo = Case Calculo –Qt.secagem
				baseCalculo = baseCalculo.subtract(qtdSecagem);
				
				//  Qt.Tbm = Base Calculo * (%TBM/ 100)
				BigDecimal qtdTbm = baseCalculo.multiply(input.getPercTBM().divide(new BigDecimal(100), 9, RoundingMode.HALF_EVEN));
				
				//  Qt.Renda Liquida = Peso Liquido –qt.impureza –qt.umidade –qt.chuvado/avariado –qt tbm –quantidade recepção –quantidade secagem
				BigDecimal qtdRendaLiquida = input.getPesoLiquido();
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdImpureza);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdUmidade);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdChuvadoAvariado);
				qtdRendaLiquida = qtdRendaLiquida.subtract(qtdTbm);
				qtdRendaLiquida = qtdRendaLiquida.subtract(input.getQuantidadeRecepcao());
				qtdRendaLiquida = qtdRendaLiquida.subtract( qtdSecagem);
				
				CalculoRendaLiquidaResponseDto responseDto = new CalculoRendaLiquidaResponseDto("Cálculo realizado com sucesso.");
				responseDto.setQtdChuvaAvariado(qtdChuvadoAvariado.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdImpureza(qtdImpureza.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdRendaLiquida(qtdRendaLiquida.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdSecagem(qtdSecagem.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdTBM(qtdTbm.setScale(0, RoundingMode.HALF_EVEN));
				responseDto.setQtdUmidade(qtdUmidade.setScale(0, RoundingMode.HALF_EVEN));
				
				return responseDto;
			}
		}
		catch (Exception e) {
			throw new ObjectDefaultException("O cálculo não pode ser realizado. " + e.getMessage());
		}
	}
	
	//Objetivo: À partir de uma renda líquida, encontrar o peso liquido.
	public BigDecimal calcularPesoLiquidoAtual(CalculoPesoLiquidoDto input) {
		
		/*
		   	 Base Calculo = Renda Liquida + Qt.tbm + Qt Recepcao + QT Secagem
			 Base Calculo = (Base Calculo / (100 - %Desc.Impureza)) * 100
			 Base Calculo = (Base Calculo / (100 - %Desc.Umidade)) * 100
			 Base Calculo = (Base Calculo / (100 - %Desc.Chuvado/Avariado)) * 100
		 */
		BigDecimal baseCalculo = new BigDecimal(0);
		
		if(!input.getTipoTbm()){
			baseCalculo = input.getRendaLiquida();
		}
		else {
			baseCalculo= input.getRendaLiquida().add(input.getQtdTbm()).add(input.getQtdRecepcao()).add(input.getQtdSecagem());
		}
		
		BigDecimal aux = BigDecimal.ZERO;
		
		aux = new BigDecimal(100).subtract( input.getPercDescImpureza() );
		baseCalculo = baseCalculo.divide( aux, 2, RoundingMode.HALF_EVEN ).multiply( new BigDecimal(100) );
		
		aux = new BigDecimal(100).subtract( input.getPercDescUmidade() );
		baseCalculo = baseCalculo.divide( aux, 2, RoundingMode.HALF_EVEN ).multiply( new BigDecimal(100) );
		
		aux = new BigDecimal(100).subtract( input.getPercDescChvadoAvariado() );
		baseCalculo = baseCalculo.divide( aux, 2, RoundingMode.HALF_EVEN ).multiply( new BigDecimal(100) );
		
		if(!input.getTipoTbm()){
			aux = new BigDecimal(100).subtract( input.getPercTbm() );
			baseCalculo = baseCalculo.divide( aux, 2, RoundingMode.HALF_EVEN ).multiply( new BigDecimal(100) );
		}
		
		return baseCalculo;
	}

//	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<NumeroReDto> atualizarRe(Long id, Boolean permitirDataMovimentoAbertoMenorQueDataAtual) {
		RePendente rePendente = repenRep.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado RE Pendente com o ID informado."));
		
		List<String> mensagens = rePendenteValidation.validar(rePendente, false);
		if(!mensagens.isEmpty()) {
			String mensagem = String.join("\n", mensagens);
			throw new ObjectDefaultException(mensagem.trim());
		}
		
		List<RePendenteItemDto> itemDtos = RePendenteItemDto.construir(rePendente.getItens());
		rePendenteItemService.validarEIncluirDadosConformeParametrizacao(rePendente, itemDtos);
		List<RePendenteItem> itens = rePendenteItemService.converterDto(rePendente, itemDtos);
		rePendente.getItens().clear();
		rePendente.getItens().addAll(itens);
		
		if(CollectionUtils.isEmpty(rePendente.getItens())) {
			throw new ObjectDefaultException("A RE Pendente não possui item vinculado.");
		}
		
		String codigoEstabelecimento = rePendente.getEstabelecimento().getCodigo();
		Integer nroPesagem = rePendente.getNrDocPes();
		
		Calendar dataEntrada = Calendar.getInstance();
		dataEntrada.setTime(rePendente.getDtEntrada());
		Integer safra = dataEntrada.get(Calendar.YEAR);
		
		Pesagem pesagem = null;
		if(!rePendente.getEntradaManual()) {
			pesagem = pesagemService.buscarPesagemEstabelecimentoSafraNrDocumento(codigoEstabelecimento, safra, nroPesagem);
		}
		
		recEntregaService.validaSeExisteDocumentoPesagemNaRecEntrega(codigoEstabelecimento, safra, Long.parseLong(nroPesagem.toString()));
		
		Date dataMovimentoAberto = parametroEstabelecimentoService.buscarDataMovimento(codigoEstabelecimento);
		LocalDate dataAtual = LocalDate.now();
        LocalDate dataMovimentoAbertoAux = dataMovimentoAberto.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(dataMovimentoAbertoAux.isAfter(dataAtual)) {
        	throw new ObjectDefaultException("Data de emissão da RE não pode ser maior que a data atual.");
        }
        
        LocalDate dataEntradaAux = rePendente.getDtEntrada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(dataEntradaAux.isAfter(dataMovimentoAbertoAux)) {
        	throw new ObjectDefaultException("Data de recebimento não pode ser maior que a data de entrada.");
        }
        
        if(!permitirDataMovimentoAbertoMenorQueDataAtual && !dataMovimentoAbertoAux.isEqual(dataAtual)) {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	throw new ObjectDefaultException("Data do movimento aberto é dia \"" + sdf.format(dataMovimentoAberto) + "\" é diferente da data atual.");
        }
        
        rePendente.setDtEmissaoOri(dataMovimentoAberto);
        RecEntregaValidation.validarNotaPropria(rePendente);
        
        MovimentoDiarioRequest movimentoDiarioRequest = new MovimentoDiarioRequest();
        movimentoDiarioRequest.setCodEstab(codigoEstabelecimento);
        movimentoDiarioRequest.setDataMov(dataMovimentoAberto);
        MovimentoDiarioResponse movimentoDiarioResponse = movimentoDiarioService.movimentoDiarioValid(movimentoDiarioRequest);
        if(movimentoDiarioResponse.isStatus() == false) {
        	throw new ObjectDefaultException(movimentoDiarioResponse.getMessage());
        }
		
		RePendenteItem rePendenteItem = rePendente.getItens().get(0);
		
		Boolean produtorEhPj = rePendente.getProdutor().getNatureza().equalsIgnoreCase("PJ");
		Boolean produtorEmiteNf = rePendente.getProdutor().getEmiteNota();
		Boolean rendaLiquidaEhMaiorQueQtdTotalNota = rePendenteItem.getRendaLiquida().compareTo(rePendente.getPjQtTotNota()) > 0;
		if(produtorEhPj && produtorEmiteNf && rendaLiquidaEhMaiorQueQtdTotalNota) {
			throw new ObjectDefaultException("A quantidade da nota fiscal do produtor não pode ser inferior à renda líquida \"" + rePendenteItem.getRendaLiquida() + "\".");
		}
		
		if(rePendenteItem.getDesmembramento()) {
			itemDtos = RePendenteItemDto.construir(rePendente.getItens());
			List<RePendenteDesmembramentoDto> desmembramentoDtos = RePendenteDesmembramentoDto.construir(rePendente.getDesmembramentos());
			rePendenteDesmembramentoService.validarDesmembramentos(rePendente, itemDtos, desmembramentoDtos);
		}
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		UsuarioEstabelecimentoFuncaoDto usuarioEstabelecimentoFuncaoDto = paramUsuarioEstabelecimentoService.findParametroByUsuarioEstabelecimentoFuncao(codigoEstabelecimento, usuario.getCodUsuario(), Entrada_RE);
		if(usuarioEstabelecimentoFuncaoDto.isPermite() == false) {
			throw new ObjectDefaultException(usuarioEstabelecimentoFuncaoDto.getMessage());
		}
		
		String codigoGrupoProduto = rePendente.getGrupoProduto().getFmCodigo();
		BigDecimal percentual = rePendenteItem.getChuvadoAvariado();
		ItemAvariadoDetalhe itemAvariadoDetalhe = null;
		
		if(rePendenteItem.getPhEntrada() != null && rePendenteItem.getPhEntrada().compareTo(BigInteger.ZERO) > 0) {
			try {
				itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoPh(codigoGrupoProduto, codigoEstabelecimento, percentual, rePendenteItem.getPhEntrada(), rePendenteItem.getFnt());
			}
			catch (Exception e) {
				log.info(e.getMessage());
			}
			
			if(itemAvariadoDetalhe != null && !rePendenteItem.getProduto().getCodItem().equalsIgnoreCase(itemAvariadoDetalhe.getProduto().getCodItem()) 
					&& !rePendenteItem.getReferencia().equalsIgnoreCase(itemAvariadoDetalhe.getProdutoReferencia().getCodRef())) {
				throw new ObjectDefaultException("Produto Inválido, deve ser utilizado o item \"" + itemAvariadoDetalhe.getProduto() + "\".");
			}
		}
		else {
			// PRIMEIRO FAZ A BUSCA POR CHUVADO/AVARIADO PASSANDO O PERCENTUAL
			try {
				itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoChuvadoAvaraido(codigoGrupoProduto, codigoEstabelecimento, percentual);
			}
			catch (Exception e) {
				log.info(e.getMessage());
			}
			
			if(itemAvariadoDetalhe != null && !rePendenteItem.getProduto().getCodItem().equalsIgnoreCase(itemAvariadoDetalhe.getProduto().getCodItem()) 
					&& !rePendenteItem.getReferencia().equalsIgnoreCase(itemAvariadoDetalhe.getProdutoReferencia().getCodRef())) {
				throw new ObjectDefaultException("Produto Inválido, para esse Chuvado/Avariado deverá ser utilizado o item \"" + itemAvariadoDetalhe.getProduto() + "\".");
			}
			
			// CASO NÃO ENCONTRE POR PRODUTO, EH REALIZADO OUTRA BUSCA POR PRODUTO
			if(itemAvariadoDetalhe == null) {
				try {
					itemAvariadoDetalhe = itemAvariadoDetalheService.buscarPorValidacaoProduto(codigoGrupoProduto, codigoEstabelecimento);
				}
				catch (Exception e) {
					log.info(e.getMessage());
				}
				
				if(itemAvariadoDetalhe != null && rePendenteItem.getProduto().equals(itemAvariadoDetalhe.getProduto())) {
					throw new ObjectDefaultException("Produto Inválido, deverá ser utilizado o produto \"" + itemAvariadoDetalhe.getProduto() + "\" para esse Grupo de Produto.");
				}
			}
		}
		
		
		// ----------------------------------------------------------
		// DESENVOLVER AS VALIDAÇÕES UTILIZANDO AS APIs GENERICAS
		// ----------------------------------------------------------

		// -----------------------
		// NF PJ
		// -----------------------
		produtorEhPj = rePendente.getProdutor().getNatureza() != null && rePendente.getProdutor().getNatureza().equalsIgnoreCase("PJ");
		Boolean produtorEmiteNota = rePendente.getProdutor().getEmiteNota(); 
		
		EntradaReValidation entradaReValidation = new EntradaReValidation();
		entradaReValidation.setDocumentosDePesagem(new ArrayList());
		entradaReValidation.setNotasFiscaisPj(new ArrayList());	
		entradaReValidation.setProdutos(new ArrayList());
		entradaReValidation.setLotes(new ArrayList());	
		
		Boolean validarNfPj = false;
		
		if(produtorEhPj && produtorEmiteNota) {	
			NotaFiscalPjValidation notaFiscalPjValidation = new NotaFiscalPjValidation();
			notaFiscalPjValidation.setNrNotaFiscal(rePendente.getPjNroNota());
			notaFiscalPjValidation.setSerie(rePendente.getPjSerie());
			notaFiscalPjValidation.setNatOperacao(rePendente.getPjNatOper());
			notaFiscalPjValidation.setCodProdutor(rePendente.getProdutor().getCodProdutor());				
			entradaReValidation.getNotasFiscaisPj().add(notaFiscalPjValidation);
			validarNfPj = true;
		}
		
		// -----------------------
		// DOC PESAGEM
		// -----------------------
		DocumentoPesagemValidation pesagemValidation = new DocumentoPesagemValidation();
		pesagemValidation.setCodEstabel(rePendente.getEstabelecimento().getCodigo());
		pesagemValidation.setSafra(safra);
		pesagemValidation.setNrDocPes(rePendente.getNrDocPes());
		entradaReValidation.getDocumentosDePesagem().add(pesagemValidation);
		Boolean validarPesagem = true;
				
		// -----------------------
		// LOTE
		// -----------------------		
		Boolean validarLote = false;
		if(rePendente.getGrupoProduto().isLote()) {
			
			if(Strings.isEmpty(rePendenteItem.getLote())) {
				throw new ObjectDefaultException("Campo lote obrigatório.");
			}
			
			LoteValidation loteValidation = new LoteValidation();
			loteValidation.setCodItem(rePendenteItem.getProduto().getCodItem());
			loteValidation.setLote(rePendenteItem.getLote());
			loteValidation.setCodEstabel(rePendente.getEstabelecimento().getCodigo());
			entradaReValidation.getLotes().add(loteValidation);
			
			validarLote = true;
		}
		
		// -----------------------
		// PROD PADRONIZADO
		// -----------------------	
		Boolean validarProduto = false;
		if(rePendente.getProdPadr()) {
			ProdutoPadraoValidation produtoPadraoValidation = new ProdutoPadraoValidation();
			produtoPadraoValidation.setCodProdutor(rePendente.getProdutor().getCodProdutor());
			produtoPadraoValidation.setFmCodigo(rePendente.getGrupoProduto().getFmCodigo());
			produtoPadraoValidation.setSafra(safra);
			produtoPadraoValidation.setUmidade(rePendenteItem.getUmidade());
			produtoPadraoValidation.setImpureza(rePendenteItem.getImpureza());
			produtoPadraoValidation.setChuvAvar(rePendenteItem.getChuvadoAvariado());
			entradaReValidation.getProdutos().add(produtoPadraoValidation); 
			validarProduto = true;
		}
		
		
		entradaReValidation = entradaReService.validarEntradaProducao(entradaReValidation);
		if(entradaReValidation.isSucessFull(validarNfPj, validarPesagem, validarProduto, validarLote)) {
			System.out.printf("Validações entrada RE (NF PJ: %b, Pesagem: %b, Produto Padronizado: %b, Lote: %b, Sucesso: Sim ", 
					validarNfPj, validarPesagem, validarProduto, validarLote);
		}
		
		// ----------------------------------------------------------
		// INICIO DO DESENVOLVIMENTO ATUALIZAÇÃO RE PARTE 2
		// ----------------------------------------------------------
		
		RecEntrega recEntregaAux = new RecEntrega();
		recEntregaAux.setLogTemSaldo(true);
		recEntregaAux.setLogNfe(false);
		recEntregaAux.setNatEntArmaz("");
		recEntregaAux.setSerie("");
		recEntregaAux.setNrNotaFis("");
		recEntregaAux.setNfPj(false);
		recEntregaAux.setNrReOpcao(0);
		recEntregaAux.setCodUnidParc(0);
		recEntregaAux.setCodEstOffLine("");
		recEntregaAux.setImpresso(false);
		recEntregaAux.setCodSit(30);
		recEntregaAux.setNrAutTransf(0l);
		recEntregaAux.setUbsLogLiberado(false);
		recEntregaAux.setUbsLogRequisitado(false);
		recEntregaAux.setUbsDataRequisicao(null);
		recEntregaAux.setUbsHoraRequisicao("");
		recEntregaAux.setNrBcqs(0L);
		recEntregaAux.setReDevolvido(false);
		recEntregaAux.setNrOrdProd(0l);
		recEntregaAux.setNrReOrigDfx(0);
		recEntregaAux.setMotivoBloqueio("");
		recEntregaAux.setNotifPortal(true);
		recEntregaAux.setNrReParcialDpi(0l);
		recEntregaAux.setSafra(safra);
		recEntregaAux.setDtEmissao(rePendente.getDtEmissaoOri());
		recEntregaAux.setPlaca(rePendente.getPlaca());
		recEntregaAux.setFmCodigo(rePendente.getGrupoProduto().getFmCodigo());
		recEntregaAux.setDtEntrada(rePendente.getDtEntrada());
		recEntregaAux.setHrEntrada(rePendente.getHrEntrada());
		
		if(pesagem != null && pesagem.getHoraSaida() != null) {
			recEntregaAux.setHrSaida(pesagem.getHoraSaida());
		}
		
		recEntregaAux.setNrDocPes(rePendente.getNrDocPes());
		recEntregaAux.setMotorista(rePendente.getMotorista());
		recEntregaAux.setTulha(rePendente.getTulha());
		recEntregaAux.setNrOrdCampo(rePendente.getNrOrdCampo());
		recEntregaAux.setNrLaudo(rePendente.getNrLaudo());
		recEntregaAux.setNrContSem(rePendente.getNrContSem());
		
		if(rePendente.getClasse() != null) {
			recEntregaAux.setClasse(rePendente.getClasse().getCodigo().toString());
		}
		
		recEntregaAux.setTipoRr(rePendente.getTipoRr());
		recEntregaAux.setTipoGmo(rePendente.getTipoGmo() != null ? rePendente.getTipoGmo().getTipoGmo() : null);
		recEntregaAux.setProdPadr(rePendente.getProdPadr());
		recEntregaAux.setDescarUnid(rePendente.getDescarUnid());
		recEntregaAux.setSafraCompos(rePendente.getSafraCompos());
		recEntregaAux.setNomeSafraCompos(rePendente.getNomeSafraCompos());
		recEntregaAux.setCodEstabel(rePendente.getEstabelecimento().getCodigo());
		recEntregaAux.setTipoRendaCafe(rePendente.getTipoRendaCfe());
		recEntregaAux.setEntradaManual(rePendente.getEntradaManual());
		recEntregaAux.setLogTxSpRetida(rePendente.getLogTxSpRetida());
		recEntregaAux.setNrReTxSpRetida(rePendente.getNrReTxSpRetida());
		recEntregaAux.setQtdDpi(rePendente.getQtDepi());
		recEntregaAux.setPerDpi(rePendente.getPerDepi());
		recEntregaAux.setPjNroNota(rePendente.getPjNroNota());
		recEntregaAux.setPjSerie(rePendente.getPjSerie());
		recEntregaAux.setPjDtEmissao(rePendente.getPjDtEmissao());
		recEntregaAux.setPjVlTotNota(rePendente.getPjVlTotNota());
		recEntregaAux.setPjQtTotNota(rePendente.getPjQtTotNota());
		
		if(rePendente.getPjChaveAcesso() != null && rePendente.getPjChaveAcesso().trim().length() == 44) {
			recEntregaAux.setPjChaveAcesso(rePendente.getPjChaveAcesso());
		}
		else {
			recEntregaAux.setPjChaveAcesso("");
		}
		
		recEntregaAux.setPjLogNotaPropria(rePendente.getPjLogNotaPropria());
		recEntregaAux.setPjNatOper(rePendente.getPjNatOper());
		recEntregaAux.setPendenciasFiscais(false);
		
		for(RePendenteItem item : rePendente.getItens()) {
			RecEntregaItem recEntregaItemAux = new RecEntregaItem();
			recEntregaItemAux.setSeqItemDocum(10);
			recEntregaItemAux.setCodEstabel(rePendente.getEstabelecimento().getCodigo());
			recEntregaItemAux.setCodProduto(item.getProduto().getCodItem());
			recEntregaItemAux.setCodRefer(item.getReferencia());
			recEntregaItemAux.setPhEntrada(item.getPhEntrada());
			recEntregaItemAux.setPhCorrigido(item.getPhCorrigido());
			recEntregaItemAux.setImpureza(item.getImpureza());
			recEntregaItemAux.setPerDescImpur(item.getPercentualDescontoImpureza());
			recEntregaItemAux.setUmidade(item.getUmidade());
			recEntregaItemAux.setPerDescUmid(item.getPercentualDescontoUmidade());
			recEntregaItemAux.setChuvAvar(item.getChuvadoAvariado());
			recEntregaItemAux.setPerDescChuv(item.getPercentualDescontoChuvadoAvariado());
			recEntregaItemAux.setTbm(item.getTbm());
			recEntregaItemAux.setTipo(item.getTipo());
			recEntregaItemAux.setBebida(item.getBebida());
			recEntregaItemAux.setCafeEscolha(item.getQtdCafeEscolha());
			recEntregaItemAux.setDefeitos(item.getDefeitos());
			recEntregaItemAux.setNormal(item.getQualidadeProdutoNormal());
			recEntregaItemAux.setSementeira(item.getQualidadeProdutoSementeira());
			recEntregaItemAux.setTerra(item.getQualidadeProdutoTerra());
			recEntregaItemAux.setVagem(item.getQualidadeProdutoVagem());
			recEntregaItemAux.setLote(item.getLote());
			recEntregaItemAux.setTabTxSecKg(item.getTabelaUmidadeTaxaSecagemKg());
			recEntregaItemAux.setTabTxSecVl(item.getTabelaUmidadeTaxaSecagemValor());
			recEntregaItemAux.setCafermTipo(item.getCafeRmTipo());
			recEntregaItemAux.setCafermBebida(item.getCafeRmBebida());
			recEntregaItemAux.setCafermCafeEscolha(item.getCafeRmCafeEscolha());
			recEntregaItemAux.setCafermRendaLiquida(item.getCafeRmRendaLiquida());
			recEntregaItemAux.setCafermDefeitos(item.getCafeRmDefeito());
			recEntregaItemAux.setCafermCodRefer(item.getCafeRmCodigoReferencia());
			recEntregaItemAux.setCafermRendascLiq(item.getCafeRmRenda());
			recEntregaItemAux.setCafermRendascEsc(item.getCafeRmRendaEscolha());
			recEntregaItemAux.setCafermRendascTot(item.getCafeRmRendaTotal());
			recEntregaItemAux.setPerPen14Acima(item.getPercentualPeneira14Acima());
			recEntregaItemAux.setPercBandinha(item.getPercentualBandinha());
			recEntregaItemAux.setTabVlRecepcao(item.getTabelaValorRecepcao());
			recEntregaItemAux.setRendaLiquida(item.getRendaLiquida());
			recEntregaItemAux.setVlPonto(item.getValorPonto());
			recEntregaItemAux.setVlFiscal(item.getValorFiscal());
			recEntregaItemAux.setFnt(item.getFnt());
			recEntregaItemAux.setDensidade(item.getDensidade());
			
			recEntregaAux.getItens().add(recEntregaItemAux);
		}
		
		List<RecEntrega> recEntregas = new ArrayList<>();
		if(rePendenteItem.getDesmembramento()) {
			recEntregas = converterRePendenteParaRecEntregaComDesmembramento(recEntregaAux, rePendente, rePendenteItem);
		}
		else {
			RecEntrega recEntrega = new RecEntrega();
			BeanUtils.copyProperties(recEntregaAux, recEntrega);
			
			Long sequencia = 10l;
			recEntrega.setSequencia(sequencia);
			
			recEntrega.setCodEmitente(rePendente.getProdutor().getCodProdutor());
			recEntrega.setNomeProd(rePendente.getNomeProd());
			recEntrega.setMatricula(rePendente.getImovel().getMatricula());
			recEntrega.setNrNfProd(rePendente.getNrNfProd());
			recEntrega.setSerNfProd(rePendente.getSerNfProd());
			recEntrega.setDtNfProd(rePendente.getDtNfProd());
			
			TipoGmo tipoGmo = rePendente.getTipoGmo();
			if(tipoGmo != null && tipoGmo.getVlKit() != null && BigDecimal.ZERO.compareTo(tipoGmo.getVlKit()) != 0) {
				recEntrega.setComCobranca(true);
				recEntrega.setKitCobrado(false);
			}
			else {
				recEntrega.setComCobranca(false);
				recEntrega.setKitCobrado(false);
			}
			
			recEntrega.setEntradaRr("ORI");
			recEntrega.setPesoBruto(rePendente.getPesoBruto());
			recEntrega.setTaraVeiculo(rePendente.getTaraVeiculo());
			recEntrega.setTaraSacaria(rePendente.getTaraSacaria());
			recEntrega.setPesoLiquido(rePendente.getPesoLiquido());
			recEntrega.setCodSit(30);
			
			String observacoes = "";
			if(rePendente.getObservacoes() != null) {
				observacoes = rePendente.getObservacoes();
			}
			
			if(rePendenteItem.getObservacaoDpi() != null) {
				observacoes += "\n";
				observacoes += rePendenteItem.getObservacaoDpi();
			}
			
			recEntrega.setObservacoes(observacoes);
			
			List<RecEntregaItem> recEntregaItens = recEntrega.getItens().stream().map(recEntregaItem -> {
				recEntregaItem.setRecEntrega(recEntrega);
				recEntregaItem.setPesoLiquido(recEntrega.getPesoLiquido());
				return recEntregaItem;
			}).toList();
			
			recEntrega.setItens(recEntregaItens);
			
			recEntregas.add(recEntrega);
		}
		
		recEntregas = ajustarPeso(rePendente, recEntregas);
		
		mensagens = recEntregaValidation.validar(recEntregas);
		if(!CollectionUtils.isEmpty(mensagens)) {
			String mensagem =  String.join("\n", mensagens);
			throw new ObjectDefaultException(mensagem);
		}
		
		try {
			if(rePendenteItem.getIndiceSecagemLimpeza() != null && rePendenteItem.getIndiceSecagemLimpeza() 
					&& rePendenteItem.getTipoCobrancaSecagem() != null && rePendenteItem.getTipoCobrancaSecagem().equalsIgnoreCase("RE")) {				
				recEntregas = recEntregaService.gerarReCobrancaTaxa(recEntregas, rePendenteItem);
			}
			else if(rePendenteItem.getIndiceDpi() != null && rePendenteItem.getIndiceDpi()) {
				recEntregas = recEntregaService.gerarEntradaDpi(recEntregas, rePendenteItem);
			}
			else if(rePendenteItem.getTeraSubProduto() != null && rePendenteItem.getTeraSubProduto()) {
				recEntregas = recEntregaService.gerarEntradaSubProduto(recEntregas, rePendenteItem);
			}
			else {
				recEntregas = recEntregaService.gerarEntradasNormais(recEntregas, rePendenteItem);
			}
			
			cadproValidation.validarRecEntrega(recEntregas);
			recEntregas = gerarReService.gerarRe(TipoEntradaEnum.ENTRADA, recEntregas);
			
			for(RecEntrega recEntrega : recEntregas) {
				tratamentoTransacaoReService.salvarTratamentoTransacaoRe(
						recEntrega.getCodEstabel(),
						recEntrega.getId(),
						recEntrega.getNrRe(),
						new Date(),
						MovimentoReEnum.CRIACAO_RE,
						StatusMovimentoReEnum.OK,
						null);
			}
			
			repenRep.delete(rePendente);
			
			List<NumeroReDto> numerosReDto = new ArrayList<NumeroReDto>();
			List<Long> idsRecEntrega = new ArrayList<Long>();
			for(RecEntrega recEntrega : recEntregas) {
				NumeroReDto numeroReDto = NumeroReDto.construir(recEntrega.getCodEstabel(), recEntrega.getId(), null);
				numerosReDto.add(numeroReDto);
				idsRecEntrega.add(recEntrega.getId());
				tratamentoTransacaoReService.salvarTratamentoTransacaoRe(
						recEntrega.getCodEstabel(),
						recEntrega.getId(),
						recEntrega.getNrRe(),
						new Date(),
						MovimentoReEnum.SOLICITADO_NRO_RE,
						StatusMovimentoReEnum.OK, 
						null);
			}
			
			recEntregaService.atualizarCampoCodSit(MovimentoReEnum.SOLICITADO_NRO_RE, idsRecEntrega);

			try {
				numerosReDto = recEntregaService.gerarPayloadIntegracaoReComErp(numerosReDto);
				for(NumeroReDto numeroReDto : numerosReDto) {
					tratamentoTransacaoReService.salvarTratamentoTransacaoRe(
							numeroReDto.getCodEstabel(),
							numeroReDto.getIdGenesis(),
							numeroReDto.getNroDocto(),
							new Date(),
							MovimentoReEnum.ENVIO_RE_DATASUL,
							StatusMovimentoReEnum.OK,
							null);
				}
			
				return numerosReDto;
			}
			catch (Exception e) {
				for(RecEntrega recEntrega : recEntregas) {
					tratamentoTransacaoReService.salvarTratamentoTransacaoRe(
							recEntrega.getCodEstabel(),
							recEntrega.getId(),
							recEntrega.getNrRe(),
							new Date(),
							MovimentoReEnum.ENVIO_RE_DATASUL,
							StatusMovimentoReEnum.FALHA,
							e.getMessage());
				}
				
				throw e;
			}
		} catch (CloneNotSupportedException e) {
			throw new ObjectDefaultException("Falha ao clonar o objecto Rec Entrega.");
		}
	}
	
	public List<RecEntrega> converterRePendenteParaRecEntregaComDesmembramento(RecEntrega recEntregaAux, RePendente rePendente, RePendenteItem rePendenteItem) {
		if(CollectionUtils.isEmpty(rePendente.getDesmembramentos())) throw new ObjectDefaultException("Não foi informado os desmembramentos.");
		
		List<RecEntrega> recEntregas = new ArrayList<RecEntrega>();
		Long sequencia = 0l;
		for(RePendenteDesmembramento desmembramento : rePendente.getDesmembramentos()) {
			RecEntrega recEntrega = new RecEntrega();
			BeanUtils.copyProperties(recEntregaAux, recEntrega);
			recEntrega.setItens(new ArrayList<RecEntregaItem>());
			
			for(RecEntregaItem recEntregaItemAux : recEntregaAux.getItens()) {
				RecEntregaItem recEntregaItem = new RecEntregaItem();
				BeanUtils.copyProperties(recEntregaItemAux, recEntregaItem);
				recEntregaItem.setRecEntrega(recEntrega);
				recEntrega.getItens().add(recEntregaItem);
			}
			
			sequencia += 10l;
			recEntrega.setSequencia(sequencia);
			
			recEntrega.setCodEmitente(desmembramento.getProdutorFavorecido().getCodProdutor());
			recEntrega.setNomeProd(desmembramento.getProdutorFavorecidoNome());
			recEntrega.setMatricula(desmembramento.getImovel().getMatricula());
			recEntrega.setNrNfProd(desmembramento.getNfProdutor());
			recEntrega.setSerNfProd(desmembramento.getSerieNfProdutor());
			recEntrega.setDtNfProd(desmembramento.getDataNfProdutor());
			recEntrega.setComCobranca(desmembramento.isCobraKit());
			recEntrega.setKitCobrado(false);
			recEntrega.setEntradaRr(sequencia == 10 ? "ORI" : "DES");
			recEntrega.setCodSit(30);
			
			String observacoes = "";
			if(rePendente.getObservacoes() != null) {
				observacoes = rePendente.getObservacoes();
			}
			
			if(rePendenteItem.getObservacaoDpi() != null) {
				observacoes += "\n";
				observacoes += rePendenteItem.getObservacaoDpi();
			}
			
			recEntrega.setObservacoes(observacoes);
			
			UnidadeDesmembramento unidadeDesmembramento = rePendenteItem.getUnidadeDesmembramento();
			
			switch (rePendenteItem.getTipoDesmembramento()) {
			case PESO_LIQUIDO:
				
				if(UnidadeDesmembramento.QUILOS.equals(unidadeDesmembramento)) {
					recEntrega.setPesoLiquido(desmembramento.getQuantidadeQuilos().setScale(3, RoundingMode.HALF_UP));
				}
				else if(UnidadeDesmembramento.PERCENTUAL.equals(unidadeDesmembramento)) {
					BigDecimal percentualDesmembramento = desmembramento.getPercentual().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
					BigDecimal pesoLiquido = rePendente.getPesoLiquido().multiply(percentualDesmembramento);
					recEntrega.setPesoLiquido(pesoLiquido.setScale(3, RoundingMode.HALF_UP));
				}
				else if(UnidadeDesmembramento.SACAS.equals(unidadeDesmembramento)) {
					BigDecimal qtdSacas = desmembramento.getQuantidadeSacas();
					BigDecimal quilosPorSacas = new BigDecimal(rePendente.getGrupoProduto().getKgSc().toString()); 
					BigDecimal pesoLiquido = qtdSacas.multiply(quilosPorSacas);
					recEntrega.setPesoLiquido(pesoLiquido.setScale(3, RoundingMode.HALF_UP));
				}
				break;

			case RENDA_LIQUIDA:
				BigDecimal rendaLiquida = BigDecimal.ZERO;
				
				if(UnidadeDesmembramento.QUILOS.equals(unidadeDesmembramento)) {
					rendaLiquida = desmembramento.getQuantidadeQuilos().setScale(3, RoundingMode.HALF_UP);
				}
				else if(UnidadeDesmembramento.PERCENTUAL.equals(unidadeDesmembramento)) {
					BigDecimal percentualDesmembramento = desmembramento.getPercentual().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
					rendaLiquida = rePendenteItem.getRendaLiquida().multiply(percentualDesmembramento);
				}
				else if(UnidadeDesmembramento.SACAS.equals(unidadeDesmembramento)) {
					BigDecimal qtdSacas = desmembramento.getQuantidadeSacas();
					BigDecimal quilosPorSacas = new BigDecimal(rePendente.getGrupoProduto().getKgSc().toString()); 
					rendaLiquida = qtdSacas.multiply(quilosPorSacas);
				}
				
				CalculoPesoLiquidoDto calculoPesoLiquidoDto = new CalculoPesoLiquidoDto();
				calculoPesoLiquidoDto.setRendaLiquida(rendaLiquida.setScale(3, RoundingMode.HALF_UP));
				calculoPesoLiquidoDto.setPercDescImpureza(rePendenteItem.getPercentualDescontoImpureza());
				calculoPesoLiquidoDto.setPercDescUmidade(rePendenteItem.getPercentualDescontoUmidade());
				calculoPesoLiquidoDto.setPercDescChvadoAvariado(rePendenteItem.getPercentualDescontoChuvadoAvariado());
//				calculoPesoLiquidoDto.setQtdRecepcao(rePendenteItem.getQtdRecepcao());
//				calculoPesoLiquidoDto.setQtdSecagem(rePendenteItem.getQtdSecagem());
				calculoPesoLiquidoDto.setQtdTbm(rePendenteItem.getQtdTbm());
				calculoPesoLiquidoDto.setPercTbm(rePendenteItem.getTbm());
				calculoPesoLiquidoDto.setTipoTbm(false);

				BigDecimal baseCalculo = calcularPesoLiquidoAtual(calculoPesoLiquidoDto);

				recEntrega.setPesoLiquido(baseCalculo);
				break;
			}
			
			recEntregas.add(recEntrega);
		}
		
		return recEntregas;
	}
	
	private List<RecEntrega> ajustarPeso(RePendente rePendente, List<RecEntrega> recEntregas) {
		BigDecimal pesoLiquido = recEntregas.stream().map(x -> x.getPesoLiquido()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		if(rePendente.getPesoLiquido().compareTo(pesoLiquido) == -1) {
			BigDecimal diferencaEntrePesoLiquido = pesoLiquido.subtract(rePendente.getPesoLiquido());
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal pesoLiquidoNovo = recEntrega.getPesoLiquido().subtract(diferencaEntrePesoLiquido);
			recEntrega.setPesoLiquido(pesoLiquidoNovo);
		}
		else if(rePendente.getPesoLiquido().compareTo(pesoLiquido) == 1) {
			BigDecimal diferencaEntrePesoLiquido = rePendente.getPesoLiquido().subtract(pesoLiquido);
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal pesoLiquidoNovo = recEntrega.getPesoLiquido().add(diferencaEntrePesoLiquido);
			recEntrega.setPesoLiquido(pesoLiquidoNovo);
		}
		
		recEntregas = recEntregas.stream().map(recEntrega -> {
			BigDecimal cemPorCento = new BigDecimal("100");
			BigDecimal percentualAux = recEntrega.getPesoLiquido().multiply(cemPorCento);
			percentualAux = percentualAux.divide(rePendente.getPesoLiquido(), 2, RoundingMode.HALF_EVEN); // percentual-aux = (Lista.peso_liquido * 100) / re_pen.peso_liquido (2 casas decimais)
			
			BigDecimal taraVeiculo = rePendente.getTaraVeiculo().multiply(percentualAux);
			taraVeiculo = taraVeiculo.divide(cemPorCento, 0, RoundingMode.UP); // re_pen.tara_veiculo * percentual-aux / 100 (arredondar ficar como inteiro))
			recEntrega.setTaraVeiculo(taraVeiculo);
			
			BigDecimal taraSacaria = rePendente.getTaraSacaria().multiply(percentualAux);
			taraSacaria = taraSacaria.divide(cemPorCento, 0, RoundingMode.UP); // Lista.tara_sacaria = re_pen.tara_sacaria * percentual-aux / 100 (arredondar ficar como inteiro)
			recEntrega.setTaraSacaria(taraSacaria);
			
			BigDecimal pesoBruto = recEntrega.getPesoLiquido().add(taraVeiculo).add(taraSacaria); // Lista.peso_Bruto = Lista.peso_liquido + lista.tara_veiculo + lista.tara_sacaria
			recEntrega.setPesoBruto(pesoBruto);
			
			return recEntrega;
		}).toList();
		
		/* ====================== */
		/* TARA VEICULO           */
		/* ====================== */
		System.out.println("=============================================");
		BigDecimal totalTaraVeiculo = recEntregas.stream().map(x -> x.getTaraVeiculo()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("RE PEN TARA VEICULO: " + rePendente.getTaraVeiculo());
		System.out.println("TOTAL TARA VEICULO: " + totalTaraVeiculo);
		
		
		if(rePendente.getTaraVeiculo().compareTo(totalTaraVeiculo) == -1) {
			BigDecimal diferenca = totalTaraVeiculo.subtract(rePendente.getTaraVeiculo());
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal taraVeiculoNovo = recEntrega.getTaraVeiculo().subtract(diferenca);
			recEntrega.setTaraVeiculo(taraVeiculoNovo);
		}
		else if(rePendente.getTaraVeiculo().compareTo(totalTaraVeiculo) == 1) {
			BigDecimal diferenca = rePendente.getTaraVeiculo().subtract(totalTaraVeiculo);
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal taraVeiculoNovo = recEntrega.getTaraVeiculo().add(diferenca);
			recEntrega.setTaraVeiculo(taraVeiculoNovo);
		}
		
		totalTaraVeiculo = recEntregas.stream().map(x -> x.getTaraVeiculo()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("TOTAL TARA VEICULO: " + totalTaraVeiculo);
		
		/* ====================== */
		/* TARA SACARIA           */
		/* ====================== */
		System.out.println("=============================================");
		BigDecimal totalTaraSacaria = recEntregas.stream().map(x -> x.getTaraSacaria()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("RE PEN TARA SACARIA: " + rePendente.getTaraSacaria());
		System.out.println("TOTAL TARA SACARIA: " + totalTaraSacaria);
		
		if(rePendente.getTaraSacaria().compareTo(totalTaraSacaria) == -1) {
			BigDecimal diferenca = totalTaraSacaria.subtract(rePendente.getTaraSacaria());
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal taraSacariaNovo = recEntrega.getTaraSacaria().subtract(diferenca);
			recEntrega.setTaraSacaria(taraSacariaNovo);
		}
		else if(rePendente.getTaraSacaria().compareTo(totalTaraSacaria) == 1) {
			BigDecimal diferenca = rePendente.getTaraSacaria().subtract(totalTaraSacaria);
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal taraSacariaNovo = recEntrega.getTaraSacaria().add(diferenca);
			recEntrega.setTaraVeiculo(taraSacariaNovo);
		}
		
		totalTaraSacaria = recEntregas.stream().map(x -> x.getTaraSacaria()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("TOTAL TARA SACARIA: " + totalTaraSacaria);
		
		/* ====================== */
		/* PESO BRUTO             */
		/* ====================== */
		System.out.println("=============================================");
		BigDecimal totalPesoBruto = recEntregas.stream().map(x -> x.getPesoBruto()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("RE PEN PESO BRUTO: " + rePendente.getPesoBruto());
		System.out.println("TOTAL PESO BRUTO: " + totalPesoBruto);
		
		if(rePendente.getPesoBruto().compareTo(totalPesoBruto) == -1) {
			BigDecimal diferenca = totalPesoBruto.subtract(rePendente.getPesoBruto());
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal pesoBrutoNovo = recEntrega.getPesoBruto().subtract(diferenca);
			recEntrega.setPesoBruto(pesoBrutoNovo);
		}
		else if(rePendente.getPesoBruto().compareTo(totalPesoBruto) == 1) {
			BigDecimal diferenca = rePendente.getPesoBruto().subtract(totalPesoBruto);
			RecEntrega recEntrega = recEntregas.get(0);
			
			BigDecimal pesoBrutoNovo = recEntrega.getPesoBruto().add(diferenca);
			recEntrega.setPesoBruto(pesoBrutoNovo);
		}
		
		totalPesoBruto = recEntregas.stream().map(x -> x.getPesoBruto()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("TOTAL PESO BRUTO: " + totalPesoBruto);
		
		/* ======================================================== */
		/* PESO BRUTO - TARA VEICULO - TARA SACARIA == PESO LIQUIDO */
		/* ======================================================== */
		return recEntregas.stream().map(recEntrega -> {
			BigDecimal pesoLiquidoAux = recEntrega.getPesoBruto().subtract(recEntrega.getTaraVeiculo()).subtract(recEntrega.getTaraSacaria());
			
			System.out.println("COMPARANDO PESO LIQUIDO " + recEntrega.getCodEmitente());
			
			if(recEntrega.getPesoLiquido().compareTo(pesoLiquidoAux) == -1) {
				BigDecimal diferenca = pesoLiquidoAux.subtract(recEntrega.getPesoLiquido());
				
				System.out.println("Peso liqudio diferente (Maior): " + diferenca);
				
				BigDecimal taraSacariaNovo = recEntrega.getTaraSacaria().subtract(diferenca);
				recEntrega.setTaraSacaria(taraSacariaNovo);
			}
			else if(recEntrega.getPesoLiquido().compareTo(pesoLiquidoAux) == 1) {
				BigDecimal diferenca = recEntrega.getPesoLiquido().subtract(pesoLiquidoAux);
				
				System.out.println("Peso liqudio diferente (Menor): " + diferenca);
				
				BigDecimal taraSacariaNovo = recEntrega.getTaraSacaria().add(diferenca);
				recEntrega.setTaraSacaria(taraSacariaNovo);
			}
			
			List<RecEntregaItem> recEntregaItens = recEntrega.getItens().stream().map(recEntregaItem -> {
				recEntregaItem.setPesoLiquido(recEntrega.getPesoLiquido());
				return recEntregaItem;
			}).toList();
			
			recEntrega.setItens(recEntregaItens);
			return recEntrega;
		}).toList();
	}
}

package br.coop.integrada.api.pa.domain.service.imovel;

import static br.coop.integrada.api.pa.domain.spec.ImovelSpecs.daMatriculaOuNomeLike;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.cadpro.CadPro;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImoveisProdutorFilter;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.CadProResponse;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorImovelDto;
import br.coop.integrada.api.pa.domain.repository.ImovelProdutorRep;
import br.coop.integrada.api.pa.domain.repository.ImovelRep;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.CadProCliente;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.ImovelSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import br.coop.integrada.api.pa.domain.spec.imovel.ImovelProdutorSpecs;

@Service
public class ImovelService {
	private static final Logger logger = LoggerFactory.getLogger(ImovelService.class);

	@Autowired
	private ImovelRep imovelRep;

	@Autowired
	private ProdutorRep produtorRep;

	@Autowired
	private ImovelProdutorRep imovelProdutorRep;

	@Autowired
	private ProdutorService produtorService;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private CadProCliente cadProCliente;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Imovel salvarImovel(ImovelDto imovelDto) {
		logger.info("Salvando imóvel...");

//		if (imovelDto.getListaProdutor() == null || imovelDto.getListaProdutor().isEmpty()) {
//			throw new ObjectDefaultException("Favor informar pelo menos um produtor!");
//		}

		Imovel imovel = new Imovel();

		if (imovelDto.getMatricula() != null) {
			imovel = imovelRep.findByMatricula(imovelDto.getMatricula());
		}

		BeanUtils.copyProperties(imovelDto, imovel);
		imovel = imovelRep.save(imovel);

//		if (!imovelProdutorRep.findByImovel(imovel).isEmpty()) {
//			imovelProdutorRep.deleteByImovel(imovel);
//		}

//		List<ImovelProdutor> imovelProdutorList = new ArrayList<>();

//		for (ImovelProdutorDto item : imovelDto.getListaProdutor()) {
//			ImovelProdutor imovelProdutor = new ImovelProdutor();
//			imovelProdutor.setImovel(imovel);
//			imovelProdutor.setCadpro(item.getCadpro());
//			imovelProdutor.setCpfCnpj(item.getCpfCnpj());
//			imovelProdutor.setTransferencia(item.getTransferencia());
//			imovelProdutor.setBaixado(item.getBaixado());
//			imovelProdutor.setCodProdutor(item.getCodProdutor());
//			imovelProdutor.setDataIntegracao(new Date());
//			// Produtor produtor =
//			// produtorService.buscarPorCodigoProdutor(item.getCodProdutor());
//			// imovelProdutor.setProdutor(produtor);
//
//			imovelProdutorList.add(imovelProdutor);
//		}

//		imovelProdutorRep.saveAll(imovelProdutorList);

		return imovel;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Imovel integrationSaveAll(ImovelDto imovelDto) {
		logger.info("Salvando imóvel matrícula: " + imovelDto.getMatricula());

		if (imovelDto.getMatricula() == null) {
			throw new ObjectDefaultException("O campo {matricula} é obrigatório!");
		}

		Imovel imovel = imovelRep.findByMatricula(imovelDto.getMatricula());

		if (imovel == null) {
			imovel = new Imovel();
		}

		BeanUtils.copyProperties(imovelDto, imovel);
		imovel.setDataInativacao(imovelDto.getAtivo() ? null : new Date());

		imovel = imovelRep.save(imovel);

		return imovel;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ImovelProdutor integrationSaveAll(ImovelProdutorDto item) {		

		if (item == null || item.getCodProdutor() == null) {
			throw new ObjectDefaultException("Campo {codProdutor} obrigatório");
		}
		
		logger.info("Salvando imóvel produtor matrícula: " + item.getMatricula() + ", produtor: "+ item.getCodProdutor());
		ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelMatriculaAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(item.getMatricula(), item.getCodProdutor());
		
		if(imovelProdutor == null) {
			imovelProdutor = new ImovelProdutor();
		}
		
		Imovel imovel = imovelRep.findByMatricula(item.getMatricula());
		
		if(imovel == null) {
			throw new ObjectDefaultException("Imóvel não encontrado");
		}
		
		imovelProdutor.setImovel(imovel);
		imovelProdutor.setCadpro(item.getCadpro());
		imovelProdutor.setCpfCnpj(item.getCpfCnpj());
		imovelProdutor.setTransferencia(item.getTransferencia());
		imovelProdutor.setBaixado(item.getBaixado());
		imovelProdutor.setCodProdutor(item.getCodProdutor());
		imovelProdutor.setDataIntegracao(new Date());
		imovelProdutor.setIdUnico(item.getIdUnico());
		imovelProdutor.setStatusIntegracao(StatusIntegracao.INTEGRADO);
		imovelProdutor.setDataInativacao(imovel.getDataInativacao());

		if (imovelProdutor.getAtivo() == null || imovelProdutor.getAtivo()) {
			imovelProdutor.setDataInativacao(null);
		} else {
			imovelProdutor.setDataInativacao(new Date());
		}
		
		return imovelProdutorRep.save(imovelProdutor);
	
		
	}

	public void salvarImovelList(List<Imovel> imovelList) {
		logger.info("Salvando Lista de imóveis...");
		for (Imovel imovel : imovelList) {
			logger.info("Salvando novo registro imóvel...");
			imovelRep.save(imovel);
		}
	}

	public Page<Imovel> buscarImovelPage(Pageable pageable) {
		logger.info("Buscando imóveis páginados...");
		Page<Imovel> imoveis = imovelRep.findByDataInativacaoNull(pageable);
		if (imoveis == null || imoveis.isEmpty())
			throw new NullPointerException("Não existem imoveis cadastrados.");
		return imoveis;
	}

	public List<Imovel> buscarImovelList() {
		logger.info("Buscando imóveis lista...");
		List<Imovel> imoveis = imovelRep.findAll();
		if (imoveis == null || imoveis.isEmpty())
			throw new NullPointerException("Não existem imoveis cadastrados.");
		return imoveis;
	}

	public Page<ImovelProdutor> buscarImovelPorProdutor(String codProdutor, Pageable pageable) {
		Page<ImovelProdutor> imoveis = imovelProdutorRep.findByCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(codProdutor, pageable);
		if (Objects.isNull(imoveis))
			throw new ObjectNotFoundException("Imoveis não encontrado para esse produtor.");
		return imoveis;
	}

	public Page<ImovelProdutor> buscarTodosAtivos(Pageable pageable) {
		Page<ImovelProdutor> imoveis = imovelProdutorRep.findByDataInativacaoNullAndTransferenciaIsFalseOrderByDataCadastroDesc(pageable);
		if (imoveis == null)
			throw new NullPointerException("Não existem imoveis cadastrados.");
		return imoveis;
	}

	public Imovel buscarImovelUnicoId(Long idImovel) {
		logger.info("Buscando imóveil unico por id...");
		Imovel imovel = imovelRep.findById(idImovel).orElse(null);

		if (imovel == null) {
			throw new ObjectNotFoundException("Não foi encontrado imóvel com o id " + idImovel);
		}

		return imovel;
	}

    public Imovel inativarImovelPorId(Long idImovel){
        logger.info("Inativando imóveil por id...");
        Imovel imovel = imovelRep.getReferenceById(idImovel);
        if(imovel == null) throw new NullPointerException("Imovel não encontrado.");
        imovel.setDataInativacao(new Date());
        return imovelRep.save(imovel);
    }

    public Imovel ativarImovelPorId(Long idImovel){
        logger.info("Ativando imóveil por id...");
        Imovel imovel = imovelRep.getReferenceById(idImovel);
        if(imovel == null) throw new NullPointerException("Imovel não encontrado.");
        imovel.setDataInativacao(null);
        imovel.setDataAtualizacao(new Date());
        return imovelRep.save(imovel);
    }

    public void deletarImovelPorId(Long idImovel){
        logger.info("Excluindo imóveil por id...");
        Imovel imovel = imovelRep.getReferenceById(idImovel);
        if(imovel == null) throw new NullPointerException("Imovel não encontrado.");
        imovelRep.deleteById(idImovel);
    }

    public List<ImovelProdutor> buscarPorCodigoProdutor(String codigoProdutor) {
        List<ImovelProdutor> imovelProdutores = imovelProdutorRep.buscarPorProdutor(codigoProdutor);
        return imovelProdutores;
    }
    
    public Imovel buscarImovelAtivoPorMatriculaImovelECodigoProdutor(Long matriculaImovel, String codigoProdutor) {
    	ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelMatriculaAndCodProdutorAndDataInativacaoIsNull(matriculaImovel, codigoProdutor);
    	if(imovelProdutor == null) {
    		throw new ObjectNotFoundException("Não foi encontrado imóvel com os parâmetros informado. (Matricula do imóvel: " + matriculaImovel + " | Código do produtor: " + codigoProdutor + ")");
    	}
    	
    	Imovel imovel = imovelProdutor.getImovel();
    	if(!imovel.getAtivo()) {
    		throw new ObjectDefaultException("O imóvel \"" + matriculaImovel + "\" está inativo.");
    	}
    	
    	return imovel;
    }
    
    public CadProResponse getCadProProdutorAndImovel(String codigoProdutor, Long matriculaImovel) {
    	Imovel imovel = imovelRep.findByMatricula(matriculaImovel);    	
    	if(imovel == null) {
    		throw new ObjectNotFoundException("Imóvel não encontado.");
    	}
    	
    	Produtor produtor = produtorRep.findByCodProdutor(codigoProdutor);
    	if(produtor == null) {
    		throw new ObjectNotFoundException("Produtor não encontado.");
    	}
    	ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(imovel, produtor.getCodProdutor());
    	if(imovelProdutor == null) {
    		throw new ObjectNotFoundException("Imóvel não cadastrado para o produtor.");
    	}
    	CadProResponse cadProResponse = new CadProResponse();
    	cadProResponse.setCadpro(imovelProdutor.getCadpro());
		return cadProResponse;
    }
    
    public CadProResponse getCadProProdutorCodigoProdutorAndImovelMatricula(String codigoProdutor, Long matriculaImovel) {
    	Imovel imovel = imovelRep.findByMatricula(matriculaImovel);    	
    	if(imovel == null) {
    		throw new ObjectNotFoundException("Imóvel não encontado: "+ matriculaImovel);
    	}
    	
    	Produtor produtor = produtorRep.findByCodProdutor(codigoProdutor);
    	if(produtor == null) {
    		throw new ObjectNotFoundException("Produtor não encontado: "+ codigoProdutor);
    	}
    	ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(imovel, produtor.getCodProdutor());
    	if(imovelProdutor == null) {
    		throw new ObjectNotFoundException("Imóvel ("+matriculaImovel+") não cadastrado para o produtor ("+codigoProdutor+").");
    	}
    	CadProResponse cadProResponse = new CadProResponse();
    	cadProResponse.setCadpro(imovelProdutor.getCadpro());
		return cadProResponse;
    }

	public Page<ImovelProdutor> pesquisarPorCodigoProdutorEMatriculaOuDescricaoSemSerTransferencia(String codigoProdutor, String filtro, Situacao situacao, Pageable pageable) { 
		Page<ImovelProdutor> imovelProdutorPage = imovelProdutorRep.findAll( 
				ImovelProdutorSpecs.codProdutorEquals(codigoProdutor) 
				.and(ImovelProdutorSpecs.imovelMatriculaOuNomeLike(filtro)) 
				.and(ImovelProdutorSpecs.daSituacao(situacao))
				.and(ImovelProdutorSpecs.transferenciaEquals(false)),
				pageable); 
		return imovelProdutorPage; 
	} 
	
	public Imovel buscarPorMatricula(Long matricula)  {
		Imovel imovel = imovelRep.findByMatricula(matricula); 
		return imovel; 
	}

	public void validarImovelEntrada(Long matriculaImovel, String codigoProdutor, String codigoEstabelecimento) {
		Imovel imovel = imovelRep.findByMatricula(matriculaImovel);
		
		if (imovel == null) {
			throw new ObjectNotFoundException("Não foi encontrado imóvel com a matricula " + matriculaImovel + "!");
		}

		if (imovel.getBloqueado()) {
			throw new ObjectDefaultException("O imóvel ( "+imovel.getMatriculaNome()+" ) está bloqueado para recebimento!");
		}

		Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);
		if (estabelecimento == null) {
			throw new ObjectNotFoundException(
					"Não foi encontrado estabelecimento com o código " + codigoEstabelecimento + "!");
		}

		if (!imovel.getEstado().equals(estabelecimento.getEstado())) {
			System.out.println(imovel.getEstado() + estabelecimento.getEstado());
			throw new ObjectDefaultException(
					"A entrada tem que ocorrer no mesmo estado, o imóvel ( " +imovel.getMatriculaNome()+ " ) está no estado \""
							+ imovel.getEstado() + "\" e o estabelecimento está no estado \""
							+ estabelecimento.getEstado() + "\"!");
		} else if (imovel.getTipo()) {
			throw new ObjectDefaultException("O imóvel \"" + imovel.getDescricao()
					+ "\" é do tipo urbano, não é possível dar entrada nesse imóvel!");
		}

		Produtor produtor = produtorService.buscarPorCodigoProdutor(codigoProdutor);
		if (produtor == null) {
			throw new ObjectNotFoundException("Não foi encontrado produtor com o código " + codigoProdutor + "!");
		}

		ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(imovel, produtor.getCodProdutor());
		if (imovelProdutor == null) {
			throw new ObjectNotFoundException("Imóvel \"" + imovel.getDescricao() + "\" não vinculado ao produtor \""
					+ produtor.getNome() + "\"!");
		} else if (imovelProdutor.getTransferencia()) {
			throw new ObjectDefaultException(
					"Imóvel \"" + imovel.getDescricao().toUpperCase() + "\" não pertence ao produtor \""
							+ produtor.getNome().toUpperCase() + "\", apenas foi utilizado para transferência!");
		} else if (imovelProdutor.getBaixado()) {
			throw new ObjectDefaultException("Não é possível utilizar o imóvel \"" + imovel.getDescricao().toUpperCase()
					+ "\", imóvel marcado como vendido ou fim de arrendamento para o produtor!");
		} else if (!imovelProdutor.getAtivo()) {
			throw new ObjectDefaultException("O produtor não está ativo nessa propriedade, não é possível prosseguir!");
		}
	}
	
	public String buscarCadProPor(String codProdutor, Long matriculaImovel) {
		logger.info("INICIANDO A BUSCA E VALIDAÇÃO DE CADPRO DO PRODUTOR " + codProdutor + " e do imóvel " + matriculaImovel);
		
		Produtor produtor = produtorService.buscarPorCodigoProdutor(codProdutor);
		ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelMatriculaAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(matriculaImovel, codProdutor);
		if(imovelProdutor == null) throw new ObjectNotFoundException("Não foi encontrado o vinculo do imóvel " + matriculaImovel + " com o produtor " + codProdutor);
		Imovel imovel = imovelProdutor.getImovel();
		
		String cadProImovel = null;
		Boolean produtorNaoEhCooperativa = produtor.getCooperativa() == false;
		Boolean produtorEhPf = produtor.getNatureza().equalsIgnoreCase("PF");
		Boolean imovelEhDoParana = false;
		
		if(Strings.isNotEmpty(imovel.getEstado())) {
			imovelEhDoParana = imovel.getEstado().equals("PR");
		}
		else {
			throw new ObjectDefaultException("É obrigatório cadastrar o estado do imovel " + imovel.getMatriculaNome());
		}
		
		if (produtorNaoEhCooperativa && imovelEhDoParana && produtorEhPf) {
			String cadPro = imovelProdutor.getCadpro() != null ? imovelProdutor.getCadpro().toString() : "";
			
			if(Strings.isEmpty(cadPro)) {
				throw new ObjectDefaultException("O imóvel("+imovel.getMatriculaNome()+") do produtor ("+produtor.getCodNome()+") não possui CADPRO, portanto, não é possível efetivar a entrada.");
			}
			
			
			String ufImovel = imovel.getEstado().toUpperCase();
			List<CadPro> cadPros = cadProCliente.consultarCadpro(cadPro, ufImovel);
			Boolean cadProAtivo = false;

			for (CadPro item : cadPros) {
				if (!item.getBaixado()) {
					cadProAtivo = true;
					cadProImovel = cadPro;
				}
			}

			if (!cadProAtivo) {
				throw new ObjectDefaultException("CAD/PRO do imóvel " + imovel.getMatricula() + " não é valido!");
			}
		}
		
		return cadProImovel;
	}

	public CadProResponse getCadProProdutorCpfAndImovelMatricula(String cpf, Long matriculaImovel) {
		Imovel imovel = imovelRep.findByMatricula(matriculaImovel);
		if (imovel == null) {
			throw new ObjectNotFoundException("Imóvel não encontado.");
		}

		Produtor produtor = produtorRep.findByCpfCnpj(cpf);
		if (produtor == null) {
			throw new ObjectNotFoundException("Produtor não encontado.");
		}
		ImovelProdutor imovelProdutor = imovelProdutorRep.findByImovelAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(imovel, produtor.getCodProdutor());
		if (imovelProdutor == null) {
			throw new ObjectNotFoundException("Imóvel não cadastrado para o produtor.");
		}
		CadProResponse cadProResponse = new CadProResponse();
		cadProResponse.setCadpro(imovelProdutor.getCadpro());
		return cadProResponse;
	}

	public Page<Imovel> getImovelFilter(ImoveisProdutorFilter filter, Pageable pageable) {	
		logger.info("Filtrando produtor: {} imovel: {}  status: {}", filter.getCodProdutor(), filter.getMatriculaNome(), filter.getStatus());
		
		Page<Imovel> imovies = imovelRep.findAll(
				ImovelSpecs.doCodProdutor(filter.getCodProdutor())
				.and(daMatriculaOuNomeLike(filter.getMatriculaNome()))
				.and(ImovelSpecs.doStatus(filter.getStatus())), pageable);	
		
		System.out.println(pageable.getPageNumber());	
		
		return imovies;	
	}
	
	public List<ProdutorImovelDto> getProdutorByImovel(Long matricula) {		
		if(matricula == null) {
			logger.info("Matricula inválida!");
			throw new ObjectNotFoundException("Matricula inválida.");
		}
		List<ImovelProdutor> imovelProdutor = imovelProdutorRep.findAll(ImovelProdutorSpecs.matriculaEquals(matricula));
		if(imovelProdutor.isEmpty()) {
			logger.info("Imóvel não possui produtor vinculado.");
			throw new ObjectNotFoundException("Imóvel não possui produtor vinculado.");
		}
		
		logger.info("Buscando produtores do imóvel {}. Aguarde!", matricula);
		List<ProdutorImovelDto> produtores = new ArrayList<>();
		for(ImovelProdutor item: imovelProdutor) {
			logger.info("Buscando produtor do código: {}.", item.getCodProdutor());
			Produtor produtor = produtorRep.findByCodProdutor(item.getCodProdutor());
			ProdutorImovelDto produtorImovelDto = new ProdutorImovelDto();			
			if(produtor != null) {
				logger.info("Produtor encontrado, carregando dados.");
				produtorImovelDto.setCadpro(item.getCadpro());
				produtorImovelDto.setBaixado(item.getBaixado());
				produtorImovelDto.setAtivo(item.getAtivo());
				produtorImovelDto.setMatriculaNome(item.getImovel().getMatriculaNome());
			}else {
				logger.info("Produtor código: {} não cadastrado.", item.getCodProdutor());
				produtor = new Produtor();
				produtor.setCpfCnpj("Não cadastrado");
				produtor.setNome("Produtor não cadastrado");
				produtor.setCodProdutor(item.getCodProdutor());
				
			}	
			
			BeanUtils.copyProperties(produtor, produtorImovelDto);
			produtores.add(produtorImovelDto);
		}
		return produtores;		
	}
}

package br.coop.integrada.api.pa.domain.service;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.PRODUTOR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.modelDto.produtor.VerificaProdutorResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtorFilter.ProdutorFilter;
import br.coop.integrada.api.pa.domain.modelDto.produtorFilter.ProdutorFilterResponse;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorRep;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.ProdutorSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Service
public class ProdutorService {
    private static final Logger logger = LoggerFactory.getLogger(ImovelService.class);
    private static final String BUSCA_DE_PRODUTORES = "Busca de produtores por filtro, ";

    @Autowired
    private ProdutorRep produtorRep;
    
    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @Lazy
    @Autowired
    private ParticipanteBayerService participanteBayerService;

    public List<Produtor> buscarProdutor(String dados){ 
        logger.info("Buscando produtor por codigo, nome, cpf");
        Pageable max = PageRequest.of(0, 5);        
        List<Produtor> produtor =  buscar(dados, max);      
        if(produtor.isEmpty()) throw new NullPointerException("Produtor não encontrado"); 
        return produtor;
    }
    
    public Page<Produtor> buscarTodosPage(Pageable pageable, String nome){
        logger.info("Buscando todos os produtores paginação...");
        Page<Produtor> produtor = produtorRep.findAll(pageable, nome);
        if(produtor.isEmpty()) throw new NullPointerException("Produtor não encontrado"); 
        return produtor;
    }
    
    public List<Produtor> buscarTodosLista(){
        logger.info("Listando todos os produtores...");
        List<Produtor> produtor = produtorRep.findByDataInativacaoNull();
        if(produtor.isEmpty()) throw new NullPointerException("Produtor não encontrado"); 
        return produtor;
    }
    
    public Produtor salvar(Produtor produtor) throws ParseException {   
        logger.info("Salvando um novo produtor...");
                
        if(produtor.getNomeAbreviado() == null || produtor.getNomeAbreviado().isEmpty()) {
            produtor.setNomeAbreviado(abreviarNome(produtor.getNome()));
        }

        List<ParticipanteBayer> participantesBayer = participanteBayerService.buscarPorCnpj(produtor.getCpfCnpj());
        if(participantesBayer != null && !participantesBayer.isEmpty()) {
            produtor.setParticipanteBayer(true);
        }else {
        	produtor.setParticipanteBayer(false);
        }

        return produtorRep.save(produtor);
    }
    
    public void salvarList(List<Produtor> produtor) {
        logger.info("Salvando uma lista de novos produtores...");
        for(Produtor unico: produtor) {
            unico.setNomeAbreviado(abreviarNome(unico.getNome()));
            produtorRep.save(unico);
        }        
    }
    
    public void inativar(Long id) {
        logger.info("Inativar produtor por id...");
        Produtor produtor = produtorRep.getReferenceById(id);
        if(produtor == null) throw new NullPointerException("Produtor não encontrado.");
        produtor.setDataInativacao(new Date());
        produtor.setDataAtualizacao(new Date());
        produtor.setAtivo(null);
        produtorRep.save(produtor);
    }
    
    public void ativar(Long id) {
        logger.info("Ativar produtor por id...");
        Produtor produtor = produtorRep.getReferenceById(id);
        if(produtor == null) throw new NullPointerException("Produtor não encontrado.");
        produtor.setDataInativacao(null);
        produtor.setDataAtualizacao(new Date());
        produtor.setAtivo(true);
        produtorRep.save(produtor);
    }
    
    public void deletar(Long id) {
        logger.info("Excluir produtor por id...");
        Produtor produtor = produtorRep.getReferenceById(id);
        if(produtor == null) throw new NullPointerException("Produtor não encontrado.");
        produtorRep.delete(produtor);
    }
    
    public String abreviarNome(String nome) {  
        if(nome == null) {
            return null;
        }
        String[] nomeCompleto = nome.split(" ");
        String primeiro = nomeCompleto[0];
        String meio = " ";
        for (int i = 1; i < nomeCompleto.length - 1; i++) 
        {
            if (!nomeCompleto[i].toLowerCase().equals("de") && !nomeCompleto[i].toLowerCase().equals("da") && !nomeCompleto[i].toLowerCase().equals("do") && !nomeCompleto[i].toLowerCase().equals("das") && !nomeCompleto[i].toLowerCase().equals("dos"))
            {
                 meio += nomeCompleto[i].substring(0, 1);
                 meio += ". ";
            }
        }

        String ultimo = nomeCompleto[nomeCompleto.length-1];        
        String nomeAbreviado = primeiro + meio + ultimo;        
        return nomeAbreviado.toString();
    }   
    
    public List<Produtor> buscar(String dados, Pageable max){
        List<Produtor> produtores = new ArrayList<>(); 
        if(produtores.isEmpty()) produtores = produtorRep.findByNomeContainingIgnoreCaseOrCpfCnpjContainingIgnoreCaseOrNomeAbreviadoContainingIgnoreCaseOrderByNomeAsc(dados, dados, dados, max);
        return produtores;
    }    

    public Produtor findByCodProdutor(String codigo) {
        return produtorRep.findByCodProdutor(codigo);
    }   

    public Produtor findById(Long id) {
        return produtorRep.findById(id).get();
    }
    
    public Produtor findByCpfCnpj(String cpfCnpj) {
        return produtorRep.findByCpfCnpj(cpfCnpj);
    }

    public Produtor buscarPorCodigoProdutor(String codigoProdutor) {
        logger.info("Buscando produtor por código do produtor " + codigoProdutor);
        Produtor produtor = produtorRep.findByCodProdutor(codigoProdutor);

        if(produtor == null) {
            throw new ObjectNotFoundException("Não foi encontrado produtor com o código " + codigoProdutor);
        }

        return produtor;
    }

    @Transactional
    public void setParticipanteBayer(String cnpj, Boolean parceiro, String movimento) {
        logger.info("Buscando Produtor por CPF ou CNPJ");

        Produtor produtor = produtorRep.findByCpfCnpjIgnoreCaseAndDataInativacaoIsNull(cnpj);

        if(produtor != null) {
            logger.info("Alterando produtor para Participante Bayer igual a " + parceiro);

            produtor.setParticipanteBayer(parceiro);
            historicoGenericoService.salvar(produtor.getId(), PRODUTOR, "Participante Bayer", movimento);
        }
    }

	public List<Produtor> pesquisarPorCodigoOuNomeOuCpfCnpj(String filtro, Situacao situacao, Pageable pageable) {
		Page<Produtor> produtorPage = produtorRep.findAll(
				ProdutorSpecs.doCodigoOuNomeOuCpfCnpj(filtro)
				.and(ProdutorSpecs.doSituacao(situacao)),
				pageable);
		return produtorPage.getContent();
	}

	public Optional<Produtor> buscarPorCodigoQueNaoEmiteNf(String codProdutor, Situacao situacao) {
		return produtorRep.findOne(
				ProdutorSpecs.doCodigo(codProdutor)
				.and(ProdutorSpecs.doSituacao(situacao))
				.and(ProdutorSpecs.emiteNotaEquals(false)));
	}
	
	public VerificaProdutorResponseDto verificarProdutor(String codProdutor) {
		Produtor produtor = this.buscarPorCodigoProdutor(codProdutor);
		
		if(produtor == null) {
			throw new ObjectNotFoundException("Produtor não encontrado com o código: " + codProdutor);
		}
		
		return new VerificaProdutorResponseDto(produtor, "Busca realizada com sucesso.");
	}
	
	public VerificaProdutorResponseDto verificarProdutorPorCodigo(String codigoProdutor) {
		Produtor produtor = findByCodProdutor(codigoProdutor);
		
		if(produtor == null) {
			throw new ObjectNotFoundException("Não foi encontrado produtor com o código: " + codigoProdutor);
		}
		
		return new VerificaProdutorResponseDto(produtor, "Busca realizada com sucesso.");
	}
	
	public Page<Produtor> listarProdutoresComFiltros(Pageable pageable, ProdutorFilter filter) {
		return produtorRep.findAll(
				ProdutorSpecs.doNome(filter.getNome())
				.and(ProdutorSpecs.codProdutorLike(filter.getCodProdutor()))
				.and(ProdutorSpecs.doCpfCnpj(filter.getCpfCnpj()))
				.and(ProdutorSpecs.emiteNotaEquals(filter.getEmiteNotaFiscal()))
                .and(ProdutorSpecs.doSituacao(filter.getSituacao())), pageable);
	}
	
	public ProdutorFilterResponse findByCodigoFilter(String codProdutor) {
        logger.info("Buscando produtor por código: {}. aguarde!", codProdutor);
        Produtor produtor = produtorRep.findByCodProdutor(codProdutor);
       
        if(produtor == null) {
        	logger.info("Produtor não encontrado!", codProdutor);
            throw new ObjectNotFoundException("Não foi encontrado produtor com o código " + codProdutor);
        } 
        ProdutorFilterResponse response = new ProdutorFilterResponse();
        BeanUtils.copyProperties(produtor, response);
        return response;
    }
	
	public void remove(Produtor produtor) {
		produtorRep.delete(produtor);
	}
	
	
	
	
}
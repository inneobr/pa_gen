package br.coop.integrada.api.pa.domain.service.parametroEstabelecimento;

import static br.coop.integrada.api.pa.domain.spec.ParametroEstabelecimentoSpecs.codigoEstabelecimentoEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.HistoricoParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaResponseDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.HistoricoParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.spec.ParametroEstabelecimentoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class ParametroEstabelecimentoService {
    private static final Logger logger = LoggerFactory.getLogger(ParametroEstabelecimentoService.class);
    
    @Autowired
    private HistoricoParametroEstabelecimentoRep historicoParametroEstabelecimentoRep;
    
    @Autowired
    private ParametroEstabelecimentoRep parametroEstabelecimentoRep;
    
    @Autowired
    private EstabelecimentoRep estabelecimentoRep;
    
    public String dataMovimentacaoAberto(String codigo) {
    	ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findByEstabelecimentoCodigo(codigo);
    	Date dataMovimentacaoAberto = parametroEstabelecimento.getDtMovtoAberto();
    	if(dataMovimentacaoAberto == null) throw new NullPointerException("Estabelecimento não possui data de movimentação em aberto.");    	
    	
    	SimpleDateFormat formatador = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
    	String dataMovimentacao = formatador.format(dataMovimentacaoAberto);    	
		return dataMovimentacao;    	
    }

    public ParametroEstabelecimento cadastrar(ParametroEstabelecimentoDto objDto) {
        if(Strings.isEmpty(objDto.getCodigo()) ) {
            throw new ObjectNotFoundException("Campo {codigo} obrigatório!");
        }

        String codigoEstabelecimento = objDto.getCodigo();
        Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(codigoEstabelecimento);

        if(estabelecimento == null) {
            throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código " + codigoEstabelecimento);
        }

        return cadastrar(objDto, estabelecimento);
    }

    private ParametroEstabelecimento cadastrar(ParametroEstabelecimentoDto objDto, Estabelecimento estabelecimento) {
        logger.info("Salvando novo parametro de estabelecimentos...");

        var parametroEstabelecimento = new ParametroEstabelecimento();
        BeanUtils.copyProperties(objDto, parametroEstabelecimento);

        parametroEstabelecimento.setId(null);
        parametroEstabelecimento.setEstabelecimento(estabelecimento);
        parametroEstabelecimento.setNomeEst(estabelecimento.getNomeFantasia());
    
        if(parametroEstabelecimento.getLogDesfazerFixacao() == null) {
            parametroEstabelecimento.setLogDesfazerFixacao(false);
        }

        if(Strings.isEmpty(parametroEstabelecimento.getDesMotCancFixacao())) {
            parametroEstabelecimento.setDesMotCancFixacao("Nota cancelada nos termos do ART. 11, I, ANEXO III, do RICMS-PR, decreto Nro. 7.871, de 29.9.2017");
        }

        return parametroEstabelecimentoRep.save(parametroEstabelecimento);
    }
    
    public ParametroEstabelecimento atualizar(ParametroEstabelecimentoDto objDto) {
        logger.info("Editando dados parametro de estabelecimentos... ubs: {}", objDto.getLogUbs() );

        if(objDto.getId() == null) {
            throw new ObjectNotFoundException("Necessário informar o ID para alterar o parâmetro do estabelecimento!");
        }

        ParametroEstabelecimento parametroEstabelecimentoAtual = parametroEstabelecimentoRep.findById(objDto.getId()).orElse(null);   

        if(parametroEstabelecimentoAtual == null) {
            throw new ObjectNotFoundException("Não foi encontrado parâmetro de estabelecimento com o ID" + objDto.getId());
        }

        if(objDto.getCodigo() == null) {
            throw new ObjectNotFoundException("Necessário informar o código do estabelecimento!");
        }

        Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(objDto.getCodigo());
      
        if(estabelecimento == null) {
            throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código: " + objDto.getCodigo());
        }

        objDto.setId(parametroEstabelecimentoAtual.getId()); 
       
        ParametroEstabelecimento parametroEstabelecimentoNovo = new ParametroEstabelecimento();
        BeanUtils.copyProperties(parametroEstabelecimentoAtual, parametroEstabelecimentoNovo);
        BeanUtils.copyProperties(objDto, parametroEstabelecimentoNovo);
        parametroEstabelecimentoNovo.setLogUbs(objDto.getLogUbs());
        parametroEstabelecimentoNovo.setEstabelecimento(estabelecimento);
        parametroEstabelecimentoNovo.setNomeEst(estabelecimento.getNomeFantasia());

        return atualizar(parametroEstabelecimentoAtual, parametroEstabelecimentoNovo);
    }

    private ParametroEstabelecimento atualizar(ParametroEstabelecimento objAtual, ParametroEstabelecimento objNovo) {
        List<HistoricoParametroEstabelecimento> historicos = null;

        try {
            historicos = CompararObjetos.compararParametros(objAtual, objNovo);
            System.out.println(historicos);
        }
        catch (Exception e) {
            throw new ObjectDefaultException("Falha ao comparar os campos antigos com os novos");
        }
        
        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.save(objNovo);

        if(historicos != null && !historicos.isEmpty()) {
            for(HistoricoParametroEstabelecimento historicoParametro : historicos) {
                historicoParametroEstabelecimentoRep.save(historicoParametro);
            }
        }

        return parametroEstabelecimento;
    }

    public ParametroEstabelecimento cadastrarOuAtualizar(ParametroEstabelecimentoDto objDto) {
    	if(Strings.isEmpty(objDto.getCodigo()) ) {
            throw new ObjectNotFoundException("Campo {codigo} obrigatório!");
        }

        String codigoEstabelecimento = objDto.getCodigo();
        Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(codigoEstabelecimento);

        if(estabelecimento == null) {
            throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código: " + codigoEstabelecimento);
        }

        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findByEstabelecimento(estabelecimento);

        if(parametroEstabelecimento == null) {
            return cadastrar(objDto, estabelecimento);
        }

        objDto.setId(parametroEstabelecimento.getId());
        ParametroEstabelecimento parametroEstabelecimentoNovo = new ParametroEstabelecimento();
        BeanUtils.copyProperties(parametroEstabelecimento, parametroEstabelecimentoNovo);
        BeanUtils.copyProperties(objDto, parametroEstabelecimentoNovo);


        parametroEstabelecimentoNovo.setDataIntegracao(new Date());
        parametroEstabelecimentoNovo.setStatusIntegracao(StatusIntegracao.INTEGRADO);
        return atualizar(parametroEstabelecimento, parametroEstabelecimentoNovo);
    }
    
    public Page<ParametroEstabelecimentoDto> buscarParametroEstabelecimentoPaginacao(Pageable pageable){
        logger.info("Buscando parametros de estabelecimentos...");        
        Page<ParametroEstabelecimento> parametroPage = parametroEstabelecimentoRep.findByDataInativacaoNull(pageable);
        List<ParametroEstabelecimentoDto> parametroList = ParametroEstabelecimentoDto.construir(parametroPage.getContent());
        Page<ParametroEstabelecimentoDto> pagina = new PageImpl<ParametroEstabelecimentoDto>(parametroList, pageable, parametroPage.getTotalElements());
        return pagina;
    }
    
    public Page<ParametroEstabelecimentoDto> buscarParametroEstabelecimentoPaginacao(Pageable pageable, String filtro, Situacao situacao){
        logger.info("Buscando parametros de estabelecimentos...");        

        Page<ParametroEstabelecimento> parametroPage;
        if(StringUtils.hasText(filtro) && filtro.matches("[0-9]+"))
        {
	        parametroPage = parametroEstabelecimentoRep.findAll(
	                ParametroEstabelecimentoSpecs.doNomeFantasia(filtro)
	                		.or(ParametroEstabelecimentoSpecs.doCodigo(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoEmitente(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoImovel(filtro))
	                        .and(ParametroEstabelecimentoSpecs.doSituacao(situacao)),
	                        pageable
	        );
        }
        else {
        	parametroPage = parametroEstabelecimentoRep.findAll(
	                ParametroEstabelecimentoSpecs.doNomeFantasia(filtro)
	                		.or(ParametroEstabelecimentoSpecs.doCodigo(filtro))
	                		.or(ParametroEstabelecimentoSpecs.doCodigoEmitente(filtro))
	                        .and(ParametroEstabelecimentoSpecs.doSituacao(situacao)),
	                pageable
	        );
        }
        
        List<ParametroEstabelecimentoDto> parametroList = ParametroEstabelecimentoDto.construir(parametroPage.getContent());
        return new PageImpl<ParametroEstabelecimentoDto>(
        		parametroList, 
        		pageable, 
        		parametroPage.getTotalElements());
    }
    
    public List<ParametroEstabelecimentoDto> listar(){
        logger.info("Buscando parametros de estabelecimentos...");        
        List<ParametroEstabelecimento> parametroDados = parametroEstabelecimentoRep.findByDataInativacaoNullOrderByDataCadastroDesc();
        return ParametroEstabelecimentoDto.construir(parametroDados);
    }
    
    public ParametroEstabelecimentoDto buscarPorId(Long id){
        logger.info("Buscando parametros de estabelecimento unico...");        
        ParametroEstabelecimento parametroDados = parametroEstabelecimentoRep.findById(id).orElse(null);
        if(parametroDados == null) {
            throw new ObjectNotFoundException("Não foi encontrado parâmetro de estabelecimento com o id " + id + "!");
        }        
        return ParametroEstabelecimentoDto.construir(parametroDados);
    }
    
    public Page<ParametroEstabelecimentoDto> buscarParametroEstabelecimentoPorEstabelecimento(Long idEstabelecimento, Pageable pageable){
        logger.info("Buscando parametros de estabelecimentos..."); 
        Estabelecimento estabelecimento = estabelecimentoRep.getReferenceById(idEstabelecimento);
        Page<ParametroEstabelecimento> parametroPage = parametroEstabelecimentoRep.findByEstabelecimentoAndDataInativacaoNull(estabelecimento, pageable);
        List<ParametroEstabelecimentoDto> parametroList = ParametroEstabelecimentoDto.construir(parametroPage.getContent());
        Page<ParametroEstabelecimentoDto> pagina = new PageImpl<ParametroEstabelecimentoDto>(parametroList, pageable, parametroPage.getTotalElements());
        return pagina;
    }

	public ParametroEstabelecimento buscarPorCodigoEstabelecimento(String codigoEstabelecimento) {
		return parametroEstabelecimentoRep.findOne(codigoEstabelecimentoEquals(codigoEstabelecimento)).orElse(null);
	}
    
    public ParametroEstabelecimento inativar(Long id){
        logger.info("Inativando parametros de estabelecimento unico...");

        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findById(id).orElse(null);
        if(parametroEstabelecimento == null) {
            throw new ObjectNotFoundException("Não foi encontrado parâmetro de estabelecimento com o id " + id + "!");
        }

        parametroEstabelecimento.setDataInativacao(new Date());
        parametroEstabelecimentoRep.save(parametroEstabelecimento);
        return parametroEstabelecimento;
    }
    
    public ParametroEstabelecimento ativar(Long id){
        logger.info("Ativando parametros de estabelecimento unico...");

        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findById(id).orElse(null);
        if(parametroEstabelecimento == null) {
            throw new ObjectNotFoundException("Não foi encontrado parâmetro de estabelecimento com o id " + id + "!");
        }

        parametroEstabelecimento.setDataInativacao(null);
        parametroEstabelecimento.setDataAtualizacao(new Date());
        parametroEstabelecimentoRep.save(parametroEstabelecimento);
        return parametroEstabelecimento;
    }
    
    public List<HistoricoParametroEstabelecimento> BuscarHistoricoParametroEstabelecimentoList(Long idEstabelecimento){
        logger.info("Listando historico de parametros de estabelecimento...");
        Estabelecimento estabelecimento = estabelecimentoRep.getReferenceById(idEstabelecimento);
        List<HistoricoParametroEstabelecimento> historicoParametroEstabelecimento = historicoParametroEstabelecimentoRep.findByEstabelecimento(estabelecimento);        
        return historicoParametroEstabelecimento;
    }
    
    public Page<HistoricoParametroEstabelecimento> BuscarHistoricoParametroEstabelecimentoPage(String codEstabelecimento, Pageable pageable){
        logger.info("Historico de parametros de estabelecimento...");
        Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(codEstabelecimento);
        return historicoParametroEstabelecimentoRep.findByEstabelecimentoAndDataInativacaoNull(estabelecimento, pageable);
    }

	public ValidaEntradaCooperativaResponseDto validarEntradaNomeCooperativa(ValidaEntradaCooperativaDto input) {
		
		ValidaEntradaCooperativaResponseDto output = new ValidaEntradaCooperativaResponseDto(null);
		
		Estabelecimento estabelecimento = estabelecimentoRep.findByIdOrCodigo(null, input.getCodEstabelecimento());
		
		if(estabelecimento == null) {
			throw new ObjectDefaultException("Não foi possível encontrar um estabelecimento com o código: " + input.getCodEstabelecimento());
		}
		
		ParametroEstabelecimento paramEstab = parametroEstabelecimentoRep.findByEstabelecimento(estabelecimento);
		
		if(paramEstab == null) {
			throw new ObjectDefaultException("Não foi possível encontrar um parâmetro de estabelecimento cadastrado para o estabelecimento consultado com o código: " + input.getCodEstabelecimento());
		}
		
		
		if(paramEstab.getPermitirEntradaCooperativa()) {
			output.setPermitido(true);
		}
		else {
			output.setPermitido(false);
			output.setMensagemPermitido("Não é possível dar entrada em nome da cooperativa. Favor validar");
		}
		
		//O cód.de cliente deve ser igual ao cód.Produtor enviado via parâmetro
		if( paramEstab.getCodEmitente() == null || paramEstab.getCodEmitente().isBlank() ) {
			throw new ObjectDefaultException("O campo Código do Cliente não está preenchido no parâmetro de estabelecimento, para o estabelecimento consultado com o código: " + input.getCodEstabelecimento());
		}
		
		if ( paramEstab.getCodEmitente().equals(input.getCodProdutor()) ) {
			output.setCodProd("Igual");
		}
		else {
			output.setCodProd("Diferente");
			output.setMensagemCodProd("Código de Produtor, diferente do parâmetro, entrada dever ser no código: " + paramEstab.getCodEmitente());
		}
		
		//O cód.do imóvel deve ser igual ao cód.Imóvel enviado via parâmetros
		if(paramEstab.getCodImovel() == null) {
			throw new ObjectDefaultException("O campo Código do Imóvel não está preenchido no parâmetro de estabelecimento, para o estabelecimento consultado com o código: " + input.getCodEstabelecimento());
		}
		
		if( paramEstab.getCodImovel().compareTo(input.getCodImovel()) == 0 ) {
			output.setCodImovel("Igual");
		}
		else {
			output.setCodImovel("Diferente");
			output.setMensagemCodImovel("Código do imóvel, diferente do parâmetro, imóvel deve ser o código: " + paramEstab.getCodImovel());
		}
			
		
		return output;
	}
	
	public Date buscarDataMovimento(String codigoEstabelecimento) {
    	ParametroEstabelecimento paramEstabelecimento = parametroEstabelecimentoRep.findOne(codigoEstabelecimentoEquals(codigoEstabelecimento))
    			.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado parâmetro para o estabelecimento " + codigoEstabelecimento));
    	
    	if(paramEstabelecimento.getDtMovtoAberto() == null) {
    		throw new ObjectDefaultException("Não tem movimento em aberto para o estabelecimento, favor entrar em contato com Suporte TI.");
    	}
    	
    	return paramEstabelecimento.getDtMovtoAberto();
	}
	
	public void delete(ParametroEstabelecimento parametroEstabelecimento) {
		parametroEstabelecimentoRep.delete(parametroEstabelecimento);
	}
	
	public void save(ParametroEstabelecimento parametroEstabelecimento) {
		parametroEstabelecimentoRep.save(parametroEstabelecimento);
	}
	
	
}
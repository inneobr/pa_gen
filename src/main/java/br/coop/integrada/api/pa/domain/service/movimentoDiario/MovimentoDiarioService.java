package br.coop.integrada.api.pa.domain.service.movimentoDiario;

import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.GENESIS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.DataUtil;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioFiltro;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioRequest;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioResponse;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.movimentoDiario.MovimentoDiarioDto;
import br.coop.integrada.api.pa.domain.repository.movimentoDiario.MovimentoDiarioRep;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import br.coop.integrada.api.pa.domain.spec.MovimentoDiarioSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class MovimentoDiarioService {
	
	private static final Logger logger = LoggerFactory.getLogger(MovimentoDiarioService.class);
	private final MovimentoDiarioRep movimentoDiarioRep;
	
	@Autowired
    private IntegrationService integrationService;
	
	@Autowired
    private EstabelecimentoService estabelecimentoService;
	
	@Autowired
    private ParametroEstabelecimentoService parametroEstabelecimentoService;
	
	public MovimentoDiario findByIdUnico(String idUnico) {
		return movimentoDiarioRep.findByIdUnico(idUnico);
	}
	
	public Page<MovimentoDiario> findByEstabelecimentoAndDataMovimento(Pageable pageable, MovimentoDiarioFiltro filter){	
		Page<MovimentoDiario> movimentoDiario = null;
		
		if(filter.getCodEstabelecimento() != null) {
			movimentoDiario = movimentoDiarioRep.findByCodEstabelAndDtMovtoBetween(
				filter.getCodEstabelecimento(), 
				filter.getDataInicio(),
				filter.getDataFinal(), 
				pageable);
			
		} else {
			
			List<String> codigos = estabelecimentoService.buscarCodigosPorUsuarioLogadoAutenticadoComHierarquiaEstabelecimento();
			if(codigos != null && !codigos.isEmpty()) {
				movimentoDiario = movimentoDiarioRep.findByCodEstabelInAndDtMovtoBetween(
					codigos, 
					filter.getDataInicio(),
					filter.getDataFinal(), 
					pageable);
			}
		}
		
		if(movimentoDiario.isEmpty()) {
			throw new ObjectNotFoundException("Movimento diário não encontrado.");
		}
		
		List<MovimentoDiario> response = new ArrayList<>();
		for(MovimentoDiario item: movimentoDiario.getContent()) {
			System.out.println("cont");
			ParametroEstabelecimento parametro = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(item.getCodEstabel());
			item.setParamEstabDtMovto(parametro.getDtMovtoAberto());	
			MovimentoDiario movimentoDiarioResponse = new MovimentoDiario();
			BeanUtils.copyProperties(item, movimentoDiarioResponse);
			response.add(movimentoDiarioResponse);
		}
		
		return new PageImpl<>(
			response, pageable, movimentoDiario.getTotalElements()
		);
		
	}	

	public MovimentoDiario findByCodEstabelAndDtMovto(String codEstabel, Date dtMovto) {
		return movimentoDiarioRep.findByCodEstabelAndDtMovto(codEstabel, dtMovto);
	}
	
	public Page<MovimentoDiario> findByCodEstabel(String codEstabel, Pageable pageble){
		return movimentoDiarioRep.findByCodEstabel(codEstabel, pageble);
	}
	
	public MovimentoDiario cadastrarOuAtualizar(MovimentoDiarioDto objDto) {	
		
		if(Strings.isEmpty(objDto.getIdUnico())) {
			throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
		}

		MovimentoDiario movimentoDiarioAtual = null;
		if(objDto.getOperacao() == null) {
			throw new ObjectNotFoundException("Campo {operacao} obrigatório");
		}else {
			if(objDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
				movimentoDiarioAtual = movimentoDiarioRep.findByIdUnico(objDto.getIdUnico());
				if(movimentoDiarioAtual != null) {
					movimentoDiarioRep.delete(movimentoDiarioAtual);
					return movimentoDiarioAtual;
				}else{
					movimentoDiarioAtual = new MovimentoDiario();
					BeanUtils.copyProperties(objDto, movimentoDiarioAtual);
					return movimentoDiarioAtual;
				}
			}
		}
		
		if(Strings.isEmpty(objDto.getCodEstabel())) {
			throw new ObjectNotFoundException("Campo {codEstabel} obrigatório");
		}
		
		if(objDto.getDtMovto() == null) {
			throw new ObjectNotFoundException("Campo {dtMovto} obrigatório");
		}
		
		if(objDto.getMovtoFech() == null) {
			throw new ObjectNotFoundException("Campo {movtoFech} obrigatório");
		}
		
		movimentoDiarioAtual = movimentoDiarioRep.findByIdUnico(objDto.getIdUnico());
				
		if(movimentoDiarioAtual == null) {
			movimentoDiarioAtual = new MovimentoDiario();
		}
		
		BeanUtils.copyProperties(objDto, movimentoDiarioAtual);
		
		IntegracaoPagina pagina = null; 
		try {
			pagina = integrationService.buscarPorPagina(PaginaEnum.MOVIMENTO_DIARIO);
		}catch(Exception e) {
			
		}
    	if(pagina != null && GENESIS.equals(pagina.getOrigenEnum())) {
    		movimentoDiarioAtual.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    	}else{
    		movimentoDiarioAtual.setStatusIntegracao(StatusIntegracao.INTEGRADO);
    		movimentoDiarioAtual.setDataIntegracao(new Date());
    	}
		return movimentoDiarioRep.save(movimentoDiarioAtual);		
	}
	
	public MovimentoDiario buscarPorCodigoEstabelecimentoEDataMovimento(String codigoEstabelecimento, Date dataMovimento) {
		List<MovimentoDiario> movimentos = movimentoDiarioRep.findAll(
				MovimentoDiarioSpecs.codEstabelEquals(codigoEstabelecimento)
				.and(MovimentoDiarioSpecs.dtMovtoEquals(dataMovimento)) 
				.and(MovimentoDiarioSpecs.situacaoEquals(Situacao.ATIVO)));
		
		if(CollectionUtils.isEmpty(movimentos)) return null;
		return movimentos.get(0);
	}
	
	private List<MovimentoDiario> buscarOsUltimos4DiasDeMovimentoAberto(String codigoEstabelecimento, Date dataMovimento) {
		Date dataInicial = DataUtil.adicionarOuRemoverDias(dataMovimento, -6);
		Date dataFinal = DataUtil.adicionarOuRemoverDias(dataMovimento, -1);
		return movimentoDiarioRep.findAll(
				MovimentoDiarioSpecs.codEstabelEquals(codigoEstabelecimento)
				.and(MovimentoDiarioSpecs.dtMovtoBetween(dataInicial, dataFinal))
				.and(MovimentoDiarioSpecs.movtoFechEquals(false))
				.and(MovimentoDiarioSpecs.situacaoEquals(Situacao.ATIVO)));
	}
	
	public MovimentoDiarioResponse movimentoDiarioValid(MovimentoDiarioRequest request){
		MovimentoDiario movimentoDiario = buscarPorCodigoEstabelecimentoEDataMovimento(request.getCodEstab(), request.getDataMov());
		MovimentoDiarioResponse response = new MovimentoDiarioResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
		
		logger.info("Request: buscando movimento diario.");

		if(movimentoDiario == null) {
			response.setStatus(false);
			response.setMessage("O dia " + sdf.format(request.getDataMov()) + " não foi aberto para o estabelecimento " + request.getCodEstab() + "!");
			return response;
		}		
		else if(movimentoDiario.getMovtoFech()) {
			response.setStatus(false);
			response.setMessage("O dia " + sdf.format(movimentoDiario.getDtMovto()) + " já foi fechado. Não é possível fazer movimentações nesse dia.");
			return response;
		}

		if(DataUtil.diaUtil(request.getDataMov())) {
			List<MovimentoDiario> ultimosMovimentos = buscarOsUltimos4DiasDeMovimentoAberto(request.getCodEstab(), request.getDataMov());
			
			if(CollectionUtils.isEmpty(ultimosMovimentos) == false) {
				List<String> movimentosAberto = ultimosMovimentos.stream().map(movimento -> {
					return sdf.format(movimento.getDtMovto());
				}).toList();
				
				String mensagem = String.join("\", \"", movimentosAberto);
				
				if(movimentosAberto.size() > 1) {
					Integer posicaoUltimaVirgula = mensagem.lastIndexOf(",");
					if(posicaoUltimaVirgula > 0) {
						mensagem = mensagem.substring(0, posicaoUltimaVirgula) + " e" + mensagem.substring(posicaoUltimaVirgula + 1);
					}
					
					mensagem = "O estabelecimento " + request.getCodEstab() + " está com os movimentos dos dias \"" + mensagem + "\" em aberto.";
				}
				else {
					mensagem = "O estabelecimento " + request.getCodEstab() + " está com o movimento do dia \"" + mensagem + "\" em aberto.";
				}

				response.setStatus(false);
				response.setMessage(mensagem + " É necessário abrir o movimento do dia para prosseguir.");
				return response;
			}
		}
		
		response.setStatus(true);
		response.setMessage(sdf.format(movimentoDiario.getDtMovto()));
		return response;
	}
}

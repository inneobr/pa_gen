package br.coop.integrada.api.pa.domain.service.tratamentoTransacaoRe;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.StatusMovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoReMov;
import br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe.TransacaoReMovRep;
import br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe.TransacaoReRep;
import br.coop.integrada.api.pa.domain.service.externo.ErpExtService;
import br.coop.integrada.api.pa.domain.service.nfRemessa.NfRemessaService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import br.coop.integrada.api.pa.domain.service.rependente.EntradaReService;
import br.coop.integrada.api.pa.domain.spec.TransacaoReSpecs;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;

@Service
public class TratamentoTransacaoReService {
	
	private static final Logger logger = LoggerFactory.getLogger(TratamentoTransacaoReService.class);
	
	@Autowired
	private TransacaoReRep transacaoReRep;
	
	@Autowired
	private TransacaoReMovRep transacaoReMovRep;
	
	@Lazy
	@Autowired
	private RecEntregaService recEntregaService;
	
//	@Autowired
//	private ErpExtService erpExtService;
	
	@Autowired
	private EntradaReService entradaReService;
	
	@Autowired
	private NfRemessaService nfRemessaService;
	
	
	
	public void salvarTratamentoTransacaoRe(
			String codEstabel,
			Long idRe,
			Long nrRe,
			Date dataHoraMovimento,
			MovimentoReEnum movimento,
			StatusMovimentoReEnum statusMovimento,
			String mensagem) 
	{
		
		logger.info("Iniciando Tratamento Transação RE...");
		
		TransacaoReMov transacaoReMov = transacaoReMovRep.getByCodEstabelAndIdReAndMovimento(codEstabel, idRe, movimento);
		
		if(transacaoReMov != null) {
			
			transacaoReMov.setDataAtualizacao(new Date());
			transacaoReMov.setDataHoraMovimento(dataHoraMovimento); //Atualiza a dataHome
			transacaoReMov.setMovimento(movimento); //Atualiza o Movimento
			transacaoReMov.setStatusMovimento(statusMovimento); //Atualiza o Status
			transacaoReMov.setMensagem(mensagem);			
		}
		else{
		
			//transacaoReMov é nula não existe no banco de dados.
			transacaoReMov = new TransacaoReMov();
			
			transacaoReMov.setCodEstabel(codEstabel);
			transacaoReMov.setIdRe(idRe);
			transacaoReMov.setNrRe(nrRe);
			transacaoReMov.setDataHoraMovimento(dataHoraMovimento);
			transacaoReMov.setMovimento(movimento);
			transacaoReMov.setStatusMovimento(statusMovimento);
			transacaoReMov.setMensagem(mensagem);
		}
		
		
		TransacaoRe transacaoReBD = transacaoReRep.findByCodEstabelAndIdRe(codEstabel, idRe);
		
		if(transacaoReBD == null) {
			
			TransacaoRe transacaoRe = new TransacaoRe();
			
			transacaoRe.setNrRe(nrRe);
			transacaoRe.setCodEstabel(codEstabel);
			transacaoRe.setIdRe(idRe);
			transacaoRe.setDataUltimoMovimento(dataHoraMovimento);
			transacaoRe.setMovimentoAtual(movimento);
			transacaoRe.setStatusMovimentoAtual(statusMovimento);
			
			transacaoReMov.setTransacaoRe(transacaoRe);
			
			transacaoRe.getTransacaoReMov().add(transacaoReMov);
			
			logger.info("Salvando nova Transação...");
			transacaoReRep.save(transacaoRe);
			
		}
		else {
			
			transacaoReBD.setNrRe(nrRe); 
			//transacaoReBD.setDataAtualizacao(new Date());
			
			transacaoReBD.setDataUltimoMovimento(dataHoraMovimento);
			
			if( movimento.getOrdem() > transacaoReBD.getMovimentoAtual().getOrdem() ) {
				
				transacaoReBD.setMovimentoAtual(movimento);
				transacaoReBD.setStatusMovimentoAtual(statusMovimento);
				
			}
			
			transacaoReMov.setTransacaoRe(transacaoReBD);
			transacaoReBD.getTransacaoReMov().add(transacaoReMov);
			
			logger.info("Atualizando nova Transação...");
			transacaoReRep.save(transacaoReBD);
			
		}
		
	}
	
	public Page<TransacaoRe> buscarTransacoes(Pageable pageable, String filter) {
		logger.info("Listando as transações RE...");

		Page<TransacaoRe> transacaoRe = transacaoReRep.findAll(
				TransacaoReSpecs.doEstabelecimento(filter),
				pageable);
		
		return transacaoRe;
		
	}

	public Page<TransacaoReMov> buscarTransacoesMovimento(Long idRe, Pageable pageable) {
		logger.info("Listando os movimentos de transações RE...");
		return transacaoReMovRep.findByIdRe(idRe, pageable);
	}

	public void reenviar(Long idTransacaoReMov) throws Exception {
		try {
			logger.info("Iniciando o reenvio da transação id:" + idTransacaoReMov);
			
			TransacaoReMov mov = transacaoReMovRep.findById(idTransacaoReMov).orElse(null);
			
			if(mov == null)
				throw new ObjectNotFoundException("Não foi encontrado o registro de movimento com o id: " + idTransacaoReMov);
						
			switch (mov.getMovimento()) {
				case ABERTO:
					//Não possui ação
					break;
				case CRIACAO_RE:
					//Não possui ação
					break;
				case SOLICITADO_NRO_RE:

					solicitarNroRe(mov.getId());
					
					break;
					
				case ENVIO_RE_DATASUL:
					
					solicitarReenvioEnvioDataSul(mov.getIdRe());
					
					RecEntrega recEntrega = recEntregaService.findById( mov.getIdRe() );
					
					//Altera o Movimento para status ok
					salvarTratamentoTransacaoRe(
							recEntrega.getCodEstabel(), 
							recEntrega.getId(),
							recEntrega.getNrRe(),
							new Date(),
							MovimentoReEnum.ENVIO_RE_DATASUL,
							StatusMovimentoReEnum.OK,
							null);
					
					break;
					
				case SOLICITADO_NF_REMESSA: 
					
					solicitarReenvioNfRemessa( mov.getIdRe() );
					
					//Altera o Movimento para status ok
					salvarTratamentoTransacaoRe(
							mov.getCodEstabel(), 
							mov.getIdRe(),
							mov.getNrRe(),
							new Date(),
							MovimentoReEnum.SOLICITADO_NF_REMESSA,
							StatusMovimentoReEnum.OK,
							null);
					
					
					break;
					
				default:
					throw new IllegalArgumentException("Unexpected value: " + mov.getMovimento() );
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void solicitarReenvioEnvioDataSul(Long idRe){
		
		RecEntrega recEntrega = recEntregaService.findById(idRe);
		
		if(recEntrega == null)
			throw new ObjectNotFoundException("Não foi encontrado o registro recEntrega com o id: " + idRe);
		
		recEntregaService.gerarPayloadReenvioIntegracaoReComErp(recEntrega.getCodEstabel(), recEntrega.getId());
	}
	
	public void solicitarReenvioNfRemessa(Long idRe) {
		
		RecEntrega recEntrega = recEntregaService.findById(idRe);
		
		if(recEntrega == null)
			throw new ObjectNotFoundException("Não foi encontrado o registro recEntrega com o id: " + idRe);
		
		NfRemessa nfRemessa = nfRemessaService.buscarPorRecEntrega(recEntrega.getCodEstabel(), recEntrega.getId());
		
		if(nfRemessa == null)
			throw new ObjectNotFoundException("Não foi encontrado o registro NfRemessa com o CodEstabel: " + recEntrega.getCodEstabel() + " e com o IdRecEntrega: " + recEntrega.getId());
		
		
		entradaReService.solicitarNfErp( nfRemessa.getId() );
	}
	
	public void solicitarNroRe(Long idRe) {
		/*
		 * Comentado 
		 * Vamos aguardar um caso ocorrer para decidir como realizar este processo de Solicitar Numero do RE
		 * 
		 */
		
		
//		RecEntrega recEntrega = recEntregaService.findById(idRe);
//		
//		List<NumeroReDto> numerosReDto = new ArrayList<NumeroReDto>();
//		NumeroReDto numeroReDto = NumeroReDto.construir(recEntrega.getCodEstabel(), recEntrega.getId(), null);
//		numerosReDto.add(numeroReDto);
//		
//		List<NumeroReDto> response = erpExtService.buscarNumeroRe(numerosReDto);
//		
//		if(response != null) {
//			
//			RecEntregaIntegrationDto integrationDto = new RecEntregaIntegrationDto();
//			integrationDto.setRecEntrega(new ArrayList<>());
//			
//			for (NumeroReDto numeroReDto2 : response) {
//				if(numeroReDto2.getNroDocto() != null) {
//
//					Long nrRe = numeroReDto2.getNroDocto();
//					logger.info("Atualizando a RecEntrega: "+ numeroReDto2.getIdGenesis() + " com o Nr RE: "+ nrRe);
//					
//					recEntrega.setNrRe(nrRe);
//					
//					RecEntregaDto recEntregaDto = RecEntregaDto.construir(recEntrega);
//					
//					recEntregaService.salvar(recEntregaDto);
//					
//				}
//			}
//		}
		
	}
	
}

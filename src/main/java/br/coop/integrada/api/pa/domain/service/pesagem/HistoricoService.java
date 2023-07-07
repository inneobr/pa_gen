package br.coop.integrada.api.pa.domain.service.pesagem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import br.coop.integrada.api.pa.domain.model.Historico;
import br.coop.integrada.api.pa.domain.repository.parametros.HistoricoRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class HistoricoService {
	private static final Logger logger = LoggerFactory.getLogger(PesagemService.class);
	
	@Autowired
	private HistoricoRep historicoRep;
	
	public Historico salvarHistorico(Historico historico) {
		logger.info("Salvando histórico pesagem...");
		return historicoRep.save(historico);
	}	
	
	public Page<Historico> buscarTodos(Pageable pageable) {
		logger.info("Listando todos histórico pesagem...");
		return historicoRep.findAll(pageable);
	}
	
	public Page<Historico> buscarPorData(String dataInicioString, String dataFimString, Pageable pageable) throws ParseException {
		
		SimpleDateFormat formatarData = new SimpleDateFormat("dd-MM-yyyy");
        Date dataInicio = formatarData.parse(dataInicioString);
        Date dataFim = formatarData.parse(dataFimString);
        
		logger.info("Listando todos histórico pesagem por data...");
		return historicoRep.findByDataCadastroAfterAndDataCadastroBeforeOrderByDataCadastroDesc(dataInicio, dataFim,  pageable);
	}

	public Page<Historico> buscarPorCodEstabSafraENrDocPesagem(String codEstabelecimento, Integer safra, Integer nroDocPesagem, Pageable pageable) throws ParseException {
		logger.info("Listar histórico por Safra e Número do documento de pesagem...");
		return historicoRep.findByCodEstabelecimentoAndSafraAndNroDocPesagemOrderByDataCadastroDesc(codEstabelecimento, safra, nroDocPesagem,  pageable);
	}

}

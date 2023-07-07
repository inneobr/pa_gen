package br.coop.integrada.api.pa.domain.service.baixaCredito;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovDtoList;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoMovRequestDto;
import br.coop.integrada.api.pa.domain.repository.baixaCredito.BaixaCreditoMovRep;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class BaixaCreditoMovService {
	private static final Logger logger = LoggerFactory.getLogger(BaixaCreditoService.class);
	
	private final BaixaCreditoMovRep baixaCreditoMovRep;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public BaixaCreditoMov criarBaixaCreditoMovimento(BaixaCreditoMov obj) {
		List<String> mensagens = new ArrayList<String>(); 
		
		if(obj.getCodEstabel() == null) mensagens.add("CodEstabel");
		if(obj.getIdRe() == null) mensagens.add("IdRe");
		if(obj.getTransacao() == null) mensagens.add("Transacao");
		if(obj.getObservacao() == null) mensagens.add("Observacao");
		
		if(!CollectionUtils.isEmpty(mensagens)) {
			String mensagem = String.join("\", \"", mensagens);
			
			if(mensagens.size() > 1) {
				throw new ObjectDefaultException("É obrigatório informar os parâmetros (\"" + mensagem + "\") para criar um movimento da baixa de crédito.");
			}
			
			throw new ObjectDefaultException("É obrigatório informar o parâmetro (\"" + mensagem + "\") para criar um movimento da baixa de crédito.");
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	    Date date = new Date();
	    
	    obj.setDtMovto(date);
	    obj.setHrMovto(formatter.format(date));
	    obj.setCodUsuario(usuarioService.getUsuarioLogado().getUsername());
	    
	    BaixaCreditoMov baixaCreditoMov = baixaCreditoMovRep.save(obj);
//	    baixaCreditoMov.setIdMovtoBxaCred("GEN" + baixaCreditoMov.getId());
	    
	    return baixaCreditoMov;
	}
	
	public BaixaCreditoMov save(BaixaCreditoMov obj) {
		if(obj.getCodEstabel() == null) {
			throw new ObjectNotFoundException("Código estabelecimento é obrigatório..");
		}
		if(obj.getCodUsuario() == null) {
			throw new ObjectNotFoundException("Código usuário é obrigatório..");
		}	
		BaixaCreditoMov baixaCreditoMov = baixaCreditoMovRep.findByCodEstabelAndNrRe(obj.getCodEstabel(), obj.getNrRe());
		if(baixaCreditoMov == null) {
			baixaCreditoMov = obj;
			logger.info("Cadastrando nova baixa de crédito movimentação.");
		}else {
			Long id = baixaCreditoMov.getId();
			BeanUtils.copyProperties(obj, baixaCreditoMov);
			baixaCreditoMov.setId(id);
			logger.info("Atualizando baixa de crédito movimentação.");
		}
		return baixaCreditoMovRep.save(baixaCreditoMov);
	}
	
	public void saveAll(BaixaCreditoMovDtoList baixaCreditoMovList) {
		for(BaixaCreditoMovRequestDto itemDto:  baixaCreditoMovList.getBaixaCreditoMov()) {
			BaixaCreditoMov baixaCreditoMov = new BaixaCreditoMov();
			BeanUtils.copyProperties(itemDto, baixaCreditoMov);
			save(baixaCreditoMov);
		}				
	}
	
	public BaixaCreditoMov findByCodEstabAndNrRe(String codEstab, Long nrRe) {
		BaixaCreditoMov baixaCredito = baixaCreditoMovRep.findByCodEstabelAndNrRe(codEstab, nrRe);	
		logger.info("Buscando baixa de crédito movimentação por estabelecimento.");
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito movimento não encontrada.");
		}
		return baixaCredito;
	}
	
	public BaixaCreditoMov findByCodEstabAndIdRe(String codEstab, Long idRe) {
		BaixaCreditoMov baixaCredito = baixaCreditoMovRep.findByCodEstabelAndIdRe(codEstab, idRe);	
		logger.info("Buscando baixa de crédito movimentação por estabelecimento.");
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito movimento não encontrada.");
		}
		return baixaCredito;
	}
	
	public Page<BaixaCreditoMov> findByNrRe(Integer nrRe, Pageable pageable) {
		Page<BaixaCreditoMov> baixaCredito = baixaCreditoMovRep.findByNrRe(nrRe, pageable);	
		logger.info("Buscando baixa de crédito movimentação por número de re.");
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito movimento não encontrada.");
		}
		return baixaCredito;
	}
	
	public Page<BaixaCreditoMov> findByCodUsuario(String codUsuario, Pageable pageable) {
		Page<BaixaCreditoMov> baixaCredito = baixaCreditoMovRep.findByCodUsuario(codUsuario, pageable);
		logger.info("Buscando baixa de crédito movimentação por codigo do emitente.");
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito movimento não encontrada.");
		}
		return baixaCredito;
	}
	
	public BaixaCreditoMov salvar(BaixaCreditoMov baixaCreditoMov) {
		return baixaCreditoMovRep.save(baixaCreditoMov);
	}
	
	public BaixaCreditoMov movimentoBaixaCredito(BaixaCreditoMov obj) {
		BaixaCreditoMov baixaCreditoMov = baixaCreditoMovRep.save(obj);
		baixaCreditoMov.setIdMovtoBxaCred("GEN"+baixaCreditoMov.getId());
		baixaCreditoMovRep.save(baixaCreditoMov);
		return baixaCreditoMov;
	}
	public BaixaCreditoMov findByIdMovtoBxaCredAndCodEstabel(String idMovtoBxaCred, String codEstabel) {
		return baixaCreditoMovRep.findByIdMovtoBxaCredAndCodEstabel(idMovtoBxaCred, codEstabel);
	}
}

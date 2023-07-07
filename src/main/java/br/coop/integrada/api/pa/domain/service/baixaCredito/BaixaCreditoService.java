package br.coop.integrada.api.pa.domain.service.baixaCredito;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.modelDto.baixaCredito.BaixaCreditoDtoList;
import br.coop.integrada.api.pa.domain.repository.baixaCredito.BaixaCreditoRep;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class BaixaCreditoService {
	private static final Logger logger = LoggerFactory.getLogger(BaixaCreditoService.class);
	private final BaixaCreditoRep baixaCreditoRep;
	
	public BaixaCredito salvalOuAtualizar(BaixaCredito baixaCreditoRequest) {
		BaixaCredito baixaCredito = baixaCreditoRep.findByCodEstabelAndNrRe(baixaCreditoRequest.getCodEstabel(), baixaCreditoRequest.getNrRe());
		if(baixaCredito == null) {
			logger.info("Cadastrando nova baixa de créditos");
			baixaCredito = new BaixaCredito();
		}
		else {
			baixaCreditoRequest.setId(baixaCredito.getId());
		}
		
		BeanUtils.copyProperties(baixaCreditoRequest, baixaCredito);		
		return baixaCreditoRep.save(baixaCredito);		
	}
	
	public BaixaCredito findByCodEstabAndNrRe(String codEstab, Long nrRe) {
		BaixaCredito baixaCredito = baixaCreditoRep.findByCodEstabelAndNrRe(codEstab, nrRe);	
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito não encontrada.");
		}
		return baixaCredito;
	}
	
	public BaixaCredito findByCodEstabAndIdRe(String codEstab, Long idRe) {
		BaixaCredito baixaCredito = baixaCreditoRep.findByCodEstabelAndIdRe(codEstab, idRe);	
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito não encontrada.");
		}
		return baixaCredito;
	}
	
	public Page<BaixaCredito> findByNrRe(Integer nrRe, Pageable pageable) {
		Page<BaixaCredito> baixaCredito = baixaCreditoRep.findByNrRe(nrRe, pageable);	
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito não encontrada.");
		}
		return baixaCredito;
	}
	
	public Page<BaixaCredito> findByCodEmitente(Integer codEstabe, Pageable pageable) {
		Page<BaixaCredito> baixaCredito = baixaCreditoRep.findByCodEmitente(codEstabe, pageable);
		if(baixaCredito == null) {
			throw new ObjectNotFoundException("Baixa de credito não encontrada.");
		}
		return baixaCredito;
	}
	
	public BaixaCredito salvar(BaixaCredito baixaCredito) {
		return baixaCreditoRep.save(baixaCredito);
	}
	
}

package br.coop.integrada.api.pa.domain.service.movimentoPesagem;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;
import br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem.MovtoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem.MovtoPesagemDtoList;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.movimentoPesagem.MovtoPesagemRep;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class MovtoPesagemService {
	private final MovtoPesagemRep movtoPesagemRep;
	private final EstabelecimentoRep estabelecimentoRep;
	
	public MovtoPesagem save(MovtoPesagemDto objRequest) {
		if(objRequest.getCodEstabel() == null) {
			throw new ObjectNotFoundException("Código do estabelecimento é obrigatório.");
		}
		
		Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(objRequest.getCodEstabel());
		if(estabelecimento == null) {
			throw new ObjectNotFoundException("Código do estabelecimento inválido.");
		}
		
		MovtoPesagem baixaCreditoMov = new MovtoPesagem();
		BeanUtils.copyProperties(objRequest, baixaCreditoMov);		
		baixaCreditoMov.setEstabelecimento(estabelecimento);
		return movtoPesagemRep.save(baixaCreditoMov);		
	}
	
	public MovtoPesagem findByIdMovtoPesagem(String idMovtoPesagem) {
		return movtoPesagemRep.findByIdMovtoPesagem(idMovtoPesagem);		
	}
	
	public Page<MovtoPesagem> findByNroDocPesagem(Integer nroDocPesagem, Pageable pageable) {
		return movtoPesagemRep.findByNroDocPesagem(nroDocPesagem, pageable);		
	}
	
	public Page<MovtoPesagem> findByNroMovto(Integer nroMovto, Pageable pageable) {
		return movtoPesagemRep.findByNroMovto(nroMovto, pageable);		
	}
	
	public Page<MovtoPesagem> findByDtMovto(Date dtMovto, Pageable pageable) {		
		return movtoPesagemRep.findByDtMovto(dtMovto, pageable);		
	}
	
	public Page<MovtoPesagem> findByNroDocto(String nroDocto, Pageable pageable) {
		return movtoPesagemRep.findByNroDocto(nroDocto, pageable);		
	}
	
	public Page<MovtoPesagem> findByCodEmitente(Integer codEmitente, Pageable pageable) {
		return movtoPesagemRep.findByCodEmitente(codEmitente, pageable);		
	}
	
	public Page<MovtoPesagem> findByNrRe(Integer nrRe, Pageable pageable) {
		return movtoPesagemRep.findByNrRe(nrRe, pageable);		
	}
}

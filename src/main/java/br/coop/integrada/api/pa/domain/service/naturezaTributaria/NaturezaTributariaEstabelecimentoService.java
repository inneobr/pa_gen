package br.coop.integrada.api.pa.domain.service.naturezaTributaria;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;

import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;

import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;

import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaEstabelecimento;
import br.coop.integrada.api.pa.domain.repository.naturezaTributaria.NaturezaTributariaEstabelecimentoRep;

@Service
public class NaturezaTributariaEstabelecimentoService {
	private static final Logger logger = LoggerFactory.getLogger(NaturezaTributariaService.class);
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	
	@Autowired
	private NaturezaTributariaEstabelecimentoRep naturezaTributariaEstabelecimentoRep;
	
	public void cadastrar(NaturezaTributaria naturezaTributaria, List<EstabelecimentoNomeCodigoDto> estabelecimentosDto) {				
		for(EstabelecimentoNomeCodigoDto estabelecimento: estabelecimentosDto) {
			cadastrarNaturezaTributariaEstabelecimento(naturezaTributaria, estabelecimento);			
		}
	}
	
	@SuppressWarnings("null")
	public void cadastrarNaturezaTributariaEstabelecimento (NaturezaTributaria naturezaTributaria, EstabelecimentoNomeCodigoDto estabelecimentoDto) {
		if(estabelecimentoDto.getCodigo() == null) {
			logger.info("Campo operação  { Codigo } é obrigatório");
			throw new ObjectNotFoundException("Campo { Codigo } é obrigatório");
		}
		
		Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(estabelecimentoDto.getCodigo());
		if(estabelecimento == null) {
			logger.info("Estabelecimento não encontrado: " + estabelecimento.getCodigo());
			throw new ObjectNotFoundException("Estabelecimento não encontrado: " + estabelecimentoDto.getCodigo());
		}
		
		NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimentoEncontrado = naturezaTributariaEstabelecimentoRep.findByNaturezaTributariaCodigoAndEstabelecimentoCodigo(naturezaTributaria.getCodigo(), estabelecimento.getCodigo());
		if(naturezaTributariaEstabelecimentoEncontrado == null) {		
			NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento = new NaturezaTributariaEstabelecimento();
			naturezaTributariaEstabelecimento.setNaturezaTributaria(naturezaTributaria);
			naturezaTributariaEstabelecimento.setEstabelecimento(estabelecimento);
			naturezaTributariaEstabelecimento.setStatusIntegracao(naturezaTributaria.getStatusIntegracao());
			if(naturezaTributaria.getEstabelecimentos() == null) {
				naturezaTributaria.setEstabelecimentos(new ArrayList<>());
			}
			naturezaTributaria.getEstabelecimentos().add(naturezaTributariaEstabelecimento);
		}
	}
	
	public Page<NaturezaTributariaEstabelecimento> findEstabelecimentosByNaturezaTribitaria(Long idNatureza, Pageable pageable){
		Page<NaturezaTributariaEstabelecimento> naturezaTributariaEstabelecimento = naturezaTributariaEstabelecimentoRep.findByNaturezaTributariaId(idNatureza, pageable);
		if(naturezaTributariaEstabelecimento.isEmpty()) {
			throw new ObjectNotFoundException("Não encontramos estabelecimentos para natureza tributaria: " + idNatureza);
		}
		return naturezaTributariaEstabelecimento;
	}
	
	public void integrar(NaturezaTributaria naturezaTributaria, List<EstabelecimentoNomeCodigoDto> estabelecimentos) {				
		for(EstabelecimentoNomeCodigoDto estabelecimento: estabelecimentos) {
			atualizarEstabelecimento(naturezaTributaria, estabelecimento);			
		}
	}
	
	public void atualizarEstabelecimento(NaturezaTributaria naturezaTributaria, EstabelecimentoNomeCodigoDto estabelecimentoDto) {
		NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimentoEncontrado = buscarEstabelecimentoCadastrado(naturezaTributaria, estabelecimentoDto);
		if(estabelecimentoDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
			if(naturezaTributaria.getGrupoProduto() != null) {
				if(naturezaTributariaEstabelecimentoEncontrado != null) {
					naturezaTributaria.getEstabelecimentos().remove(naturezaTributariaEstabelecimentoEncontrado);
				}
			}
		}else {
			if(naturezaTributariaEstabelecimentoEncontrado == null) {
				if(estabelecimentoDto.getCodigo() == null) {
					logger.info("Campo { codigo } é obrigatório");
					throw new ObjectNotFoundException("Campo  { codigo } é obrigatório");
				}
				Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(estabelecimentoDto.getCodigo());
				if(estabelecimento == null) {
					logger.info("Estabelecimento não encontrado: " + estabelecimentoDto.getCodigo());
					throw new ObjectNotFoundException("Estabelecimento não encontrado: " + estabelecimentoDto.getCodigo());
				}
				
				NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento = new NaturezaTributariaEstabelecimento();
				naturezaTributariaEstabelecimento.setStatusIntegracao(naturezaTributaria.getStatusIntegracao());
				naturezaTributariaEstabelecimento.setIdRegistro(estabelecimentoDto.getIdRegistro());
				naturezaTributariaEstabelecimento.setNaturezaTributaria(naturezaTributaria);
				naturezaTributariaEstabelecimento.setEstabelecimento(estabelecimento);
				naturezaTributariaEstabelecimento.setDataIntegracao(new Date());
				if(naturezaTributaria.getEstabelecimentos() == null) {
					naturezaTributaria.setEstabelecimentos(new ArrayList<>());
				}
				naturezaTributaria.getEstabelecimentos().add(naturezaTributariaEstabelecimento);
			}
		}
	}
	
	public NaturezaTributariaEstabelecimento buscarEstabelecimentoCadastrado(NaturezaTributaria naturezaTributaria, EstabelecimentoNomeCodigoDto estabelecimentoDto) {
		NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimentoEncontrada = null;
		if(naturezaTributaria.getEstabelecimentos() != null) {			
			for(NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento : naturezaTributaria.getEstabelecimentos()) {
				if(naturezaTributariaEstabelecimento.getIdRegistro().equals(estabelecimentoDto.getIdRegistro())) {
					naturezaTributariaEstabelecimentoEncontrada = naturezaTributariaEstabelecimento;
					break;
				}						
			}
		}
		return naturezaTributariaEstabelecimentoEncontrada;
	}	
}

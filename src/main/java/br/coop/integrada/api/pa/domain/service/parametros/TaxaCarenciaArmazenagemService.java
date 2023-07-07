package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.TAXA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaCarenciaArmazenagemDto;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaCarenciaArmazenagemRep;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;

@Service
public class TaxaCarenciaArmazenagemService {
	
	@Autowired
	private TaxaCarenciaArmazenagemRep taxaCarenciaArmazenagemRep;
	
	@Autowired
	private IntegrationService integrationService;
	
	public TaxaCarenciaArmazenagem salvar(TaxaCarenciaArmazenagem obj) {
		return taxaCarenciaArmazenagemRep.save(obj);
	}
	
	public List<TaxaCarenciaArmazenagem> salvar(Taxa taxa, List<TaxaCarenciaArmazenagemDto> objDtos) {
		if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
		List<TaxaCarenciaArmazenagem> taxaCarenciaArmazenagems = converterDto(taxa, objDtos);
		return taxaCarenciaArmazenagemRep.saveAll(taxaCarenciaArmazenagems);
	}
	
	public TaxaCarenciaArmazenagem buscarPorId(Long id) {
		return taxaCarenciaArmazenagemRep.findById(id).orElse(null);
	}
	
	public List<TaxaCarenciaArmazenagem> buscarPorIdTaxa(Long taxaId) {
		return taxaCarenciaArmazenagemRep.findByTaxaIdAndDataInativacaoIsNull(taxaId);
	}
	
	public List<TaxaCarenciaArmazenagem> getCarenciaArmazenagemParaInativar(Long idTaxa, List<TaxaCarenciaArmazenagemDto> carenciaArmazenagemDtos) {
		if(CollectionUtils.isEmpty(carenciaArmazenagemDtos)) return Collections.emptyList();
		
		List<TaxaCarenciaArmazenagem> carenciaArmazenagens = buscarPorIdTaxa(idTaxa);

		return carenciaArmazenagens.stream()
				.filter(carenciaArmazenagem -> {
					TaxaCarenciaArmazenagemDto carenciaArmazenagemDto = carenciaArmazenagemDtos.stream()
							.filter(detalheDto -> { return carenciaArmazenagem.getId().equals(detalheDto.getId()); })
							.findFirst().orElse(null);
					return carenciaArmazenagemDto == null;
				})
				.toList();
	}

    public TaxaCarenciaArmazenagem converterDto(Taxa taxa, TaxaCarenciaArmazenagemDto objDto, StatusIntegracao statusIntegracao) {
    	TaxaCarenciaArmazenagem taxaCarenciaArmazenagem = new TaxaCarenciaArmazenagem();
    	TaxaCarenciaArmazenagem taxaCarenciaArmazenagemAtual = null;
    	
    	if(objDto.getId() != null) {
    		taxaCarenciaArmazenagemAtual = taxaCarenciaArmazenagemRep.findById(objDto.getId()).orElse(null);
    	}
    	
    	if(taxaCarenciaArmazenagemAtual != null) {
    		BeanUtils.copyProperties(taxaCarenciaArmazenagemAtual, taxaCarenciaArmazenagem);
    	}
    	
    	BeanUtils.copyProperties(objDto, taxaCarenciaArmazenagem);
    	taxaCarenciaArmazenagem.setTaxa(taxa);
    	taxaCarenciaArmazenagem.setDataInativacao(null);
    	
    	if(CompararObjetos.isPossuiAlteracao(taxaCarenciaArmazenagemAtual, taxaCarenciaArmazenagem)) {
    		taxaCarenciaArmazenagem.setDataIntegracao(null);
    		taxaCarenciaArmazenagem.setStatusIntegracao(statusIntegracao);
		}
    	
        return taxaCarenciaArmazenagem;
    }

    public List<TaxaCarenciaArmazenagem> converterDto(Taxa taxa, List<TaxaCarenciaArmazenagemDto> objDtos) {
    	if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
    	
    	StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
    	
        return objDtos.stream().map(taxaCarenciaArmazenagemDto -> {
            return converterDto(taxa, taxaCarenciaArmazenagemDto, statusIntegracao);
        }).toList();
    }

	public void inativar(List<TaxaCarenciaArmazenagem> carenciaArmazenagens) {
		if(CollectionUtils.isEmpty(carenciaArmazenagens)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
		List<TaxaCarenciaArmazenagem> carenciaArmazenagensParaInativar = carenciaArmazenagens.stream()
				.map(carenciaArmazenagem -> {
					carenciaArmazenagem.setDataInativacao(new Date());
					carenciaArmazenagem.setDataIntegracao(null);
					carenciaArmazenagem.setStatusIntegracao(statusIntegracao);
					return carenciaArmazenagem;
				}).toList();
		taxaCarenciaArmazenagemRep.saveAll(carenciaArmazenagensParaInativar);
	}

	public List<TaxaCarenciaArmazenagem> buscarComStatus(Long idTaxa, StatusIntegracao status) {
		List<TaxaCarenciaArmazenagem> taxaxCarenciaArmazenagemList = taxaCarenciaArmazenagemRep.findByTaxaIdAndStatusIntegracao(idTaxa, status);
		
		if(CollectionUtils.isEmpty(taxaxCarenciaArmazenagemList)) return Collections.emptyList();
		
		return taxaxCarenciaArmazenagemList.stream().map(taxaCarenciaArmazenagem -> {
			var obj = new TaxaCarenciaArmazenagem();
			BeanUtils.copyProperties(taxaCarenciaArmazenagem, obj);
			return obj;
		}).toList();
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<TaxaCarenciaArmazenagem> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<TaxaCarenciaArmazenagem> carenciaArmazenagens = objs.stream().map(taxaCarenciaArmazenagem -> {
			taxaCarenciaArmazenagem.setDataIntegracao(null);
			taxaCarenciaArmazenagem.setStatusIntegracao(status);
			return taxaCarenciaArmazenagem;
		}).toList();
		taxaCarenciaArmazenagemRep.saveAllAndFlush(carenciaArmazenagens);
	}
	
	public Page<TaxaCarenciaArmazenagem> findByTaxaIdAndDataInativacaoIsNull(Long idTaxa, Pageable pageable) {		
		return taxaCarenciaArmazenagemRep.findByTaxaIdAndDataInativacaoIsNull(idTaxa, pageable);
		
	}
}

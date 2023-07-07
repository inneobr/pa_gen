package br.coop.integrada.api.pa.domain.service.semente.campoSemente;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeCampoFilter;
import br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente.CampoDto;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoRep;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.LaudoCampo.CampoSementeSpec;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class CampoService {
	private static final Logger logger = LoggerFactory.getLogger(ImovelService.class);
	
	@Autowired
	private SementeCampoRep sementeCampoRep;	
	 
	public List<CampoDto> getCampoSemente(SementeCampoFilter filter){
	   	logger.info("Buscando campo semente: {}", filter);
	   	List<SementeCampo> sementeCampo = sementeCampoRep.findAll(
	   			CampoSementeSpec.doEstabelecimento(filter.getCodEstab())
	   			.and(CampoSementeSpec.daSafra(filter.getSafra()))
	   			.and(CampoSementeSpec.doGrupo(filter.getFmCodigo()))
	   			.and(CampoSementeSpec.daSituacao(Situacao.ATIVO))
	   	);	   	
	   	return CampoDto.construir(sementeCampo);
	}
	
	public Page<CampoDto> getCampoSementePagina(SementeCampoFilter filter, Pageable pageable){
	   	logger.info("Buscando campo semente: {}", filter);
	   	Page<SementeCampo> campoPagina = sementeCampoRep.findAll(
	   			CampoSementeSpec.doEstabelecimento(filter.getCodEstab())
	   			.and(CampoSementeSpec.daSafra(filter.getSafra()))
	   			.and(CampoSementeSpec.doGrupo(filter.getFmCodigo()))
	   			.and(CampoSementeSpec.daSituacao(Situacao.ATIVO)),
	   			pageable
	   	);
	   	List<CampoDto> campoSemente = CampoDto.builder(campoPagina);
	   	
	    return new PageImpl<>(
	    		campoSemente, pageable, campoPagina.getTotalElements()
		);	
	}
	
	
}

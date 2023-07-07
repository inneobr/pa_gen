package br.coop.integrada.api.pa.domain.service.semente.campoSemente;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente.CampoProdutorFilter;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeLaudoInspecaoRep;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.LaudoCampo.CampoLaudoSpec;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class CampoLaudoService {	
	private static final Logger logger = LoggerFactory.getLogger(ImovelService.class);

	@Autowired
    private SementeLaudoInspecaoRep campoLaudoRep;
	
	public List<SementeLaudoInspecao> getCampoLaudo(CampoProdutorFilter filter) {
		logger.info("Buscando campo laudo: {}", filter);
        return campoLaudoRep.findAll(CampoLaudoSpec.daSafra(filter.getSafra())
        		.and(CampoLaudoSpec.doGrupo(filter.getFmCodigo()))
        		.and(CampoLaudoSpec.daClasse(filter.getCodClasse()))
        		.and(CampoLaudoSpec.daOrdemCampo(filter.getOrdemCampo()))
        		.and(CampoLaudoSpec.doEstabelecimento(filter.getCodEstab()))
        		.and(CampoLaudoSpec.daSituacao(Situacao.ATIVO))
        );
    }

}

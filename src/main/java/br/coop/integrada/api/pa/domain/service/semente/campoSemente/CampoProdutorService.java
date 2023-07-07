package br.coop.integrada.api.pa.domain.service.semente.campoSemente;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.spec.campoSemente.CampoProdutorSpec;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente.CampoProdutorFilter;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoProdutorRep;

@Service @RequiredArgsConstructor
public class CampoProdutorService {	
	private static final Logger logger = LoggerFactory.getLogger(ImovelService.class);
	
	@Autowired
	private SementeCampoProdutorRep campoProdutorRep;
	
	public List<SementeCampoProdutor> getCampoProdutor(CampoProdutorFilter filter) {
		logger.info("Buscando campo produtor: {}", filter);
        return campoProdutorRep.findAll(
        		CampoProdutorSpec.daSafra(filter.getSafra())
        		.and(CampoProdutorSpec.doGrupo(filter.getFmCodigo()))
        		.and(CampoProdutorSpec.daClasse(filter.getCodClasse()))
        		.and(CampoProdutorSpec.daOrdemCampo(filter.getOrdemCampo()))
        		.and(CampoProdutorSpec.doEstabelecimento(filter.getCodEstab()))
        		.and(CampoProdutorSpec.daSituacao(Situacao.ATIVO))
        );
    }

}

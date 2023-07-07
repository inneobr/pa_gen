package br.coop.integrada.api.pa.domain.service.imovel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.repository.ImovelProdutorRep;
import br.coop.integrada.api.pa.domain.spec.imovel.ImovelProdutorSpecs;

@Service
public class ImovelProdutorService {
	private static final Logger log = LoggerFactory.getLogger(ImovelProdutorService.class);

	@Autowired
	private ImovelProdutorRep imovelProdutorRep;
	
	public ImovelProdutor buscarPorMatriculaECodProdutor(Long matricula, String codProdutor) {
		return imovelProdutorRep.findOne(
				ImovelProdutorSpecs.codProdutorEquals(codProdutor)
				.and(ImovelProdutorSpecs.matriculaEquals(matricula)))
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado o vínculo do imóvel com a matrícula \"" + matricula + "\" com o código do produtor \"" + codProdutor + "\"."));
	}
}

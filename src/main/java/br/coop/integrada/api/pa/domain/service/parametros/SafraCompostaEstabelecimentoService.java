package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.SAFRA_COMPOSTA;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraCompostaEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.repository.parametros.SafraCompostaEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;

@Service
public class SafraCompostaEstabelecimentoService {
	
	@Autowired
	private SafraCompostaEstabelecimentoRep safraCompostaEstabelecimentoRep;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
	private IntegrationService integrationService;

	public List<SafraCompostaEstabelecimento> salvar(SafraComposta safraComposta, List<EstabelecimentoSimplesDto> objDtos) {
		if(CollectionUtils.isEmpty(objDtos)) return Collections.emptyList();
		List<SafraCompostaEstabelecimento> safraCompostaEstabelecimentos = converterDto(safraComposta, objDtos);
		return safraCompostaEstabelecimentoRep.saveAll(safraCompostaEstabelecimentos);
	}

	public SafraCompostaEstabelecimento salvar(SafraCompostaEstabelecimento obj) {
		return safraCompostaEstabelecimentoRep.save(obj);
	}
	
	public List<SafraCompostaEstabelecimento> buscarPorSafraCompostaId(Long safraCompostaId) {
		return safraCompostaEstabelecimentoRep.findBySafraCompostaIdAndDataInativacaoIsNull(safraCompostaId);
	}
	
	public SafraCompostaEstabelecimento buscarPorSafraCompostaIdECodigoEstabelecimento(Long safraCompostaId, String codigoEstabelecimento) {
		return safraCompostaEstabelecimentoRep.findBySafraCompostaIdAndEstabelecimentoCodigo(safraCompostaId, codigoEstabelecimento);
	}

	public List<SafraCompostaEstabelecimento> buscarPorIdSafraCompostaComStatus(Long idSafraComposta, StatusIntegracao status) {
		return safraCompostaEstabelecimentoRep.findBySafraCompostaIdAndStatusIntegracao(idSafraComposta, status);
	}
	
	public List<SafraCompostaEstabelecimento> getEstabelecimentoParaInativar(Long safraCompostaId, List<EstabelecimentoSimplesDto> objDtos) {
		if(CollectionUtils.isEmpty(objDtos)) return Collections.emptyList();
		
		List<SafraCompostaEstabelecimento> safraCompostaEstabelecimentos = buscarPorSafraCompostaId(safraCompostaId);

		return safraCompostaEstabelecimentos.stream()
				.filter(safraCompostaEstabelecimento -> {
					EstabelecimentoSimplesDto estabelecimentoSimplesDto = objDtos.stream()
							.filter(item -> {
								return safraCompostaEstabelecimento.getCodigoEstabelecimento().equals(item.getCodigo()); 
							})
							.findFirst().orElse(null);
					return estabelecimentoSimplesDto == null;
				})
				.toList();
	}
	
	public SafraCompostaEstabelecimento converterDto(SafraComposta safraComposta, EstabelecimentoSimplesDto objDto, StatusIntegracao statusIntegracao) {
		SafraCompostaEstabelecimento safraCompostaEstabelecimento = new SafraCompostaEstabelecimento();
		SafraCompostaEstabelecimento safraCompostaEstabelecimentoAtual = buscarPorSafraCompostaIdECodigoEstabelecimento(safraComposta.getId(), objDto.getCodigo());
		
		if(safraCompostaEstabelecimentoAtual != null) {
			BeanUtils.copyProperties(safraCompostaEstabelecimentoAtual, safraCompostaEstabelecimento);
		}
		
		Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(objDto.getCodigo());
		
		if(estabelecimento == null) {
			throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código \"" + objDto.getCodigo() + "\"");
		}
		
        safraCompostaEstabelecimento.setSafraComposta(safraComposta);
        safraCompostaEstabelecimento.setEstabelecimento(estabelecimento);
        safraCompostaEstabelecimento.setDataInativacao(null);
        
        if(safraCompostaEstabelecimentoAtual == null || safraCompostaEstabelecimentoAtual.getDataInativacao() != safraCompostaEstabelecimento.getDataInativacao()) {
        	safraCompostaEstabelecimento.setDataIntegracao(null);
            safraCompostaEstabelecimento.setStatusIntegracao(statusIntegracao);
        }
        
        return safraCompostaEstabelecimento;
	}

	public List<SafraCompostaEstabelecimento> converterDto(SafraComposta safraComposta, List<EstabelecimentoSimplesDto> objDtos) {
        if(objDtos == null) return Collections.emptyList();

        StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(SAFRA_COMPOSTA);
        
        return objDtos.stream().map(estabelecimentoSimplesDto -> {
            return converterDto(safraComposta, estabelecimentoSimplesDto, statusIntegracao);
        }).toList();
    }

	public void inativar(List<SafraCompostaEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(SAFRA_COMPOSTA);
		
		List<SafraCompostaEstabelecimento> safraCompostaEstabelecimentos = objs.stream().map(safraCompostaEstabelecimento -> {
			safraCompostaEstabelecimento.setDataInativacao(new Date());
			safraCompostaEstabelecimento.setStatusIntegracao(statusIntegracao);
			return safraCompostaEstabelecimento;
		}).toList();
		
		safraCompostaEstabelecimentoRep.saveAll(safraCompostaEstabelecimentos);
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<SafraCompostaEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<SafraCompostaEstabelecimento> estabelecimentos = objs.stream().map(safraCompostaEstabelecimento -> {
			safraCompostaEstabelecimento.setDataIntegracao(null);
			safraCompostaEstabelecimento.setStatusIntegracao(status);
			return safraCompostaEstabelecimento;
		}).toList();
		safraCompostaEstabelecimentoRep.saveAll(estabelecimentos);
	}
}

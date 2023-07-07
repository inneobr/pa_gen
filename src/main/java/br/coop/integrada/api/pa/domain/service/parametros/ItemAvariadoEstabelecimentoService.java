package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.ITEM_AVARIADO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.repository.parametros.ItemAvariadoEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;

@Service
public class ItemAvariadoEstabelecimentoService {

	@Autowired
	private ItemAvariadoEstabelecimentoRep itemAvariadoEstabelecimentoRep;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private IntegrationService integrationService;

	public List<ItemAvariadoEstabelecimento> salvar(ItemAvariado itemAvariado, List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		if(CollectionUtils.isEmpty(estabelecimentoDtos)) return new ArrayList<>();
		List<ItemAvariadoEstabelecimento> estabelecimentos = converterDto(itemAvariado, estabelecimentoDtos);
		return itemAvariadoEstabelecimentoRep.saveAll(estabelecimentos);
	}

	public ItemAvariadoEstabelecimento salvar(ItemAvariadoEstabelecimento obj) {
		return itemAvariadoEstabelecimentoRep.save(obj);
	}

	public List<ItemAvariadoEstabelecimento> buscarPorIdItemAvariadoAtivos(Long idItemAvariado) {
		return itemAvariadoEstabelecimentoRep.findByItemAvariadoIdAndDataInativacaoIsNull(idItemAvariado);
	}

	public ItemAvariadoEstabelecimento buscarPor(Long idItemAvariado, String codigoEstabelecimento) {
		return itemAvariadoEstabelecimentoRep.findByItemAvariadoIdAndEstabelecimentoCodigo(idItemAvariado, codigoEstabelecimento);
	}

	public ItemAvariadoEstabelecimento converterDto(ItemAvariado itemAvariado, EstabelecimentoSimplesDto estabelecimentoDto, StatusIntegracao statusIntegracao) {
		if(itemAvariado == null || itemAvariado.getId() == null) {
			throw new ObjectDefaultException("Necessário informar o ID do item avariado");
		}

		if(estabelecimentoDto == null || Strings.isEmpty(estabelecimentoDto.getCodigo())) {
			throw new ObjectDefaultException("Necessário informar o código do estabelecimento");
		}

		ItemAvariadoEstabelecimento itemAvariadoEstabelecimento = new ItemAvariadoEstabelecimento();
		ItemAvariadoEstabelecimento itemAvariadoEstabelecimentoAtual = buscarPor(itemAvariado.getId(), estabelecimentoDto.getCodigo());

		if(itemAvariadoEstabelecimentoAtual != null) {
			BeanUtils.copyProperties(itemAvariadoEstabelecimentoAtual, itemAvariadoEstabelecimento);
		}
		
		Estabelecimento estabelecimento = estabelecimentoService.converterEstabelecimentoSimplesDtoComValidacao(estabelecimentoDto);
		itemAvariadoEstabelecimento.setEstabelecimento(estabelecimento);
		itemAvariadoEstabelecimento.setItemAvariado(itemAvariado);
		itemAvariadoEstabelecimento.setDataInativacao(null);
		
		if(itemAvariadoEstabelecimentoAtual == null || itemAvariadoEstabelecimentoAtual.getDataInativacao() != itemAvariadoEstabelecimento.getDataInativacao()) {
			itemAvariadoEstabelecimento.setDataIntegracao(null);
			itemAvariadoEstabelecimento.setStatusIntegracao(statusIntegracao);
		}

		return itemAvariadoEstabelecimento;
	}

	public List<ItemAvariadoEstabelecimento> converterDto(ItemAvariado itemAvariado, List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		if(CollectionUtils.isEmpty(estabelecimentoDtos)) return new ArrayList<>();

		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(ITEM_AVARIADO);

		return estabelecimentoDtos.stream().map(estabelecimentoDto -> {
			return converterDto(itemAvariado, estabelecimentoDto, statusIntegracao);
		}).toList();
	}

	public List<ItemAvariadoEstabelecimento> getEstabelecimentosParaInativar(Long idItemAvariado, List<EstabelecimentoSimplesDto> estabelecimentoDtos) {
		if(CollectionUtils.isEmpty(estabelecimentoDtos)) return Collections.emptyList();
		List<ItemAvariadoEstabelecimento> itemAvariadoEstabelecimentos = buscarPorIdItemAvariadoAtivos(idItemAvariado);

		return itemAvariadoEstabelecimentos.stream()
				.filter(itemAvariadoEstabelecimento -> {
					EstabelecimentoSimplesDto estabelecimentoSimplesDto = estabelecimentoDtos.stream()
							.filter(estabelecimentoDto -> {
								String codigoEstabelecimentoAtual = itemAvariadoEstabelecimento.getEstabelecimento().getCodigo();
								return codigoEstabelecimentoAtual.equalsIgnoreCase(estabelecimentoDto.getCodigo());
							})
							.findFirst().orElse(null);
					return estabelecimentoSimplesDto == null;
				})
				.toList();
	}

	public void inativar(List<ItemAvariadoEstabelecimento> estabelecimentos) {
		if(CollectionUtils.isEmpty(estabelecimentos)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(ITEM_AVARIADO);
		List<ItemAvariadoEstabelecimento> estabelecimentosParaInativar = estabelecimentos.stream()
				.map(itemAvariadoEstabelecimento -> {
					itemAvariadoEstabelecimento.setDataInativacao(new Date());
					itemAvariadoEstabelecimento.setStatusIntegracao(statusIntegracao);
					return itemAvariadoEstabelecimento;
				}).toList();
		itemAvariadoEstabelecimentoRep.saveAll(estabelecimentosParaInativar);
	}

	public List<ItemAvariadoEstabelecimento> buscarPorItemAvariadoComStatus(ItemAvariado itemAvariado, StatusIntegracao status) {
		return itemAvariadoEstabelecimentoRep.findByItemAvariadoAndStatusIntegracaoAndDataInativacaoIsNull(itemAvariado, status);
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<ItemAvariadoEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<ItemAvariadoEstabelecimento> estabelecimentos = objs.stream().map(estabelecimento -> {
			estabelecimento.setDataIntegracao(null);
			estabelecimento.setStatusIntegracao(status);
			return estabelecimento;
		}).toList();
		itemAvariadoEstabelecimentoRep.saveAll(estabelecimentos);
	}
	
	public Page<ItemAvariadoEstabelecimento> findByItemAvariadoId(Long id, Pageable pageable) {
		return itemAvariadoEstabelecimentoRep.findByItemAvariadoIdOrderByEstabelecimentoAsc(id, pageable);
	}
	
	public void updateStatus(StatusIntegracao status, Long idItemAvariado) {
		itemAvariadoEstabelecimentoRep.updateStatus(status, idItemAvariado);
	}
	
	public void deleteEstabelecimentos(Long idItemAvariado) {
		itemAvariadoEstabelecimentoRep.deleteEstabelecimentos(idItemAvariado);
	}
}

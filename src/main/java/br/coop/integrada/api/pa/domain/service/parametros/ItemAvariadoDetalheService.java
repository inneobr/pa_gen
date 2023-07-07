package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.ITEM_AVARIADO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ItemAvariadoDetalheDto;
import br.coop.integrada.api.pa.domain.repository.parametros.ItemAvariadoDetalheRep;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoReferenciaService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.spec.ItemAvariadoDetalheSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Service
@Transactional
public class ItemAvariadoDetalheService {

	@Autowired
	private ItemAvariadoDetalheRep itemAvariadoDetalheRep;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoReferenciaService referenciaService;

	@Autowired
	private IntegrationService integrationService;

	public List<ItemAvariadoDetalhe> salvar(ItemAvariado itemAvariado, List<ItemAvariadoDetalheDto> detalheDtos) {
		if(CollectionUtils.isEmpty(detalheDtos)) return new ArrayList<>();
		validarDuplicidade(detalheDtos);
		List<ItemAvariadoDetalhe> detalhes = converterDto(itemAvariado, detalheDtos);
		return itemAvariadoDetalheRep.saveAll(detalhes);
	}

	private void validarDuplicidade(List<ItemAvariadoDetalheDto> detalheDtos) {
		for(ItemAvariadoDetalheDto detalheDto : detalheDtos) {
			List<ItemAvariadoDetalheDto> _detalheDtos = detalheDtos.stream().filter(_detalheDto -> {
				Boolean percentualInicialEhIgual = _detalheDto.getPercentualInicial().compareTo(detalheDto.getPercentualInicial()) == 0;
				Boolean percentualFinalEhIgual = _detalheDto.getPercentualFinal().compareTo(detalheDto.getPercentualFinal()) == 0;
				Boolean phInicialEhIgual = _detalheDto.getPhInicial().compareTo(detalheDto.getPhInicial()) == 0;
				Boolean phFinalEhIgual = _detalheDto.getPhFinal().compareTo(detalheDto.getPhFinal()) == 0;
				Boolean fntInicialEhIgual = _detalheDto.getFntInicial().compareTo(detalheDto.getFntInicial()) == 0;
				Boolean fntFinalEhIgual = _detalheDto.getFntFinal().compareTo(detalheDto.getFntFinal()) == 0;
				return percentualInicialEhIgual && percentualFinalEhIgual && phInicialEhIgual && phFinalEhIgual && fntInicialEhIgual && fntFinalEhIgual;
			}).toList();
			
			if(_detalheDtos.size() > 1) {
				StringBuilder mensagem = new StringBuilder();
				mensagem.append("Não é permitido lançar item duplicado (");
				
				mensagem.append("Chu/Ava Inicial: " + detalheDto.getPercentualInicial());
				mensagem.append(", Chuv/Ava Final: " + detalheDto.getPercentualFinal());
				
				if(detalheDto.getPhInicial().compareTo(BigDecimal.ZERO) > 0 || detalheDto.getPhFinal().compareTo(BigDecimal.ZERO) > 0) {
					mensagem.append(", PH Inicial: " + detalheDto.getPhInicial());
					mensagem.append(", PH Final: " + detalheDto.getPhFinal());
				}
				
				if(detalheDto.getFntInicial().compareTo(BigDecimal.ZERO) > 0 || detalheDto.getFntFinal().compareTo(BigDecimal.ZERO) > 0) {
					mensagem.append(", FNT Inicial: " + detalheDto.getFntInicial());
					mensagem.append(", FNT Final: " + detalheDto.getFntFinal());
				}
				
				mensagem.append(")");
				
				throw new ObjectDefaultException(mensagem.toString());
			}
		}
	}

	public ItemAvariadoDetalhe salvar(ItemAvariadoDetalhe obj) {
		return itemAvariadoDetalheRep.save(obj);
	}

	public List<ItemAvariadoDetalhe> buscarPorStatusIntegracao(StatusIntegracao statusIntegracao) {
		return itemAvariadoDetalheRep.findByStatusIntegracao(statusIntegracao);
	}

	public List<ItemAvariadoDetalhe> buscarPorItemAvariado(ItemAvariado itemAvariado) {
		return itemAvariadoDetalheRep.findByItemAvariado(itemAvariado);
	}

	public List<ItemAvariadoDetalhe> buscarPorIdItemAvariado(Long idItemAvariado) {
		return itemAvariadoDetalheRep.findByItemAvariadoIdAndDataInativacaoIsNull(idItemAvariado);
	}

	public ItemAvariadoDetalhe buscarPor(Long itemAvariadoId, String codigoProduto,
			BigDecimal percentualInicial, BigDecimal percentualFinal, BigDecimal phInicial, BigDecimal phFinal,
			BigDecimal fntInicial, BigDecimal fntFinal) {
		
		List<ItemAvariadoDetalhe> detalhes = itemAvariadoDetalheRep.findAll(ItemAvariadoDetalheSpecs.idItemAvariadoEquals(itemAvariadoId)
				.and(ItemAvariadoDetalheSpecs.codigoProdutoEquals(codigoProduto))
				.and(ItemAvariadoDetalheSpecs.percentualInicialEquals(percentualInicial))
				.and(ItemAvariadoDetalheSpecs.percentualFinalEquals(percentualFinal))
				.and(ItemAvariadoDetalheSpecs.phInicialEquals(phInicial))
				.and(ItemAvariadoDetalheSpecs.phFinalEquals(phFinal))
				.and(ItemAvariadoDetalheSpecs.fntInicialEquals(fntInicial))
				.and(ItemAvariadoDetalheSpecs.fntFinalEquals(fntFinal))
				.and(ItemAvariadoDetalheSpecs.situacaoEquals(Situacao.ATIVO)));
		
		if(CollectionUtils.isEmpty(detalhes)) return null;
		return detalhes.get(0);
		
	}

	public List<ItemAvariadoDetalhe> getDetalhesParaInativar(Long idItemAvariado, List<ItemAvariadoDetalheDto> detalheDtos) {
		if(CollectionUtils.isEmpty(detalheDtos)) return Collections.emptyList();
		List<ItemAvariadoDetalhe> detalhesAtual = buscarPorIdItemAvariado(idItemAvariado);

		return detalhesAtual.stream()
				.filter(detalheAtual -> {
					ItemAvariadoDetalheDto itemAvariadoDetalheDto = detalheDtos.stream()
							.filter(detalheDto -> {
									Boolean percentualInicialEhIgual = detalheAtual.getPercentualInicial().compareTo(detalheDto.getPercentualInicial()) == 0;
									Boolean percentualFinalEhIgual = detalheAtual.getPercentualFinal().compareTo(detalheDto.getPercentualFinal()) == 0;
									Boolean phInicialEhIgual = detalheAtual.getPhInicial().compareTo(detalheDto.getPhInicial()) == 0;
									Boolean phFinalEhIgual = detalheAtual.getPhFinal().compareTo(detalheDto.getPhFinal()) == 0;
									Boolean fntInicialEhIgual = detalheAtual.getFntInicial().compareTo(detalheDto.getFntInicial()) == 0;
									Boolean fntFinalEhIgual = detalheAtual.getFntFinal().compareTo(detalheDto.getFntFinal()) == 0;
									return percentualInicialEhIgual && percentualFinalEhIgual && phInicialEhIgual && phFinalEhIgual && fntInicialEhIgual && fntFinalEhIgual;
								})
							.findFirst().orElse(null);
					return itemAvariadoDetalheDto == null;
				})
				.toList();
	}

	public void inativar(List<ItemAvariadoDetalhe> detalhes) {
		if(CollectionUtils.isEmpty(detalhes)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(ITEM_AVARIADO);
		List<ItemAvariadoDetalhe> detalhesParaInativar = detalhes.stream()
				.map(detalheAtual -> {
					detalheAtual.setDataInativacao(new Date());
					detalheAtual.setStatusIntegracao(statusIntegracao);
					return detalheAtual;
				}).toList();
		itemAvariadoDetalheRep.saveAll(detalhesParaInativar);
	}

	public ItemAvariadoDetalhe converterDto(ItemAvariado itemAvariado, ItemAvariadoDetalheDto detalheDto, StatusIntegracao statusIntegracao) {
		if(detalheDto.getGrupoProduto() == null || Strings.isEmpty(detalheDto.getGrupoProduto().getFmCodigo())) { 
			throw new ObjectDefaultException("Necessário informar o código do produto");
		}

		if(detalheDto.getProduto() == null || Strings.isEmpty(detalheDto.getProduto().getCodigo())) { 
			throw new ObjectDefaultException("Necessário informar o código do produto");
		}

		ItemAvariadoDetalhe itemAvariadoDetalhe = new ItemAvariadoDetalhe();
		String codigoProduto = detalheDto.getProduto().getCodigo();
		ItemAvariadoDetalhe itemAvariadoDetalheAtual = buscarPor(
				itemAvariado.getId(), codigoProduto,
				detalheDto.getPercentualInicial(), detalheDto.getPercentualFinal(),
				detalheDto.getPhInicial(), detalheDto.getPhFinal(),
				detalheDto.getFntInicial(), detalheDto.getFntFinal());

		if(itemAvariadoDetalheAtual != null) {
			BeanUtils.copyProperties(itemAvariadoDetalheAtual, itemAvariadoDetalhe);
		}

		BeanUtils.copyProperties(detalheDto, itemAvariadoDetalhe);
		itemAvariadoDetalhe.setItemAvariado(itemAvariado);
		itemAvariadoDetalhe.setDataInativacao(null);

		Produto produto = produtoService.buscarProdutoAtivoPorCodItem(detalheDto.getProduto().getCodigo());
		itemAvariadoDetalhe.setProduto(produto);
		
		if(detalheDto.getProdutoReferencia() != null && Strings.isNotEmpty(detalheDto.getProdutoReferencia().getCodRef())) { 
			ProdutoReferencia produtoReferencia = referenciaService.buscarPorCodigoReferencia(detalheDto.getProdutoReferencia().getCodRef());
			itemAvariadoDetalhe.setProdutoReferencia(produtoReferencia);
		}

		if(CompararObjetos.isPossuiAlteracao(itemAvariadoDetalheAtual, itemAvariadoDetalhe)) {
			itemAvariadoDetalhe.setDataIntegracao(null);
			itemAvariadoDetalhe.setStatusIntegracao(statusIntegracao);
		}

		return itemAvariadoDetalhe;
	}

	public List<ItemAvariadoDetalhe> converterDto(ItemAvariado itemAvariado, List<ItemAvariadoDetalheDto> detalheDtos) {
		if(CollectionUtils.isEmpty(detalheDtos)) return new ArrayList<>();

		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(ITEM_AVARIADO);

		return detalheDtos.stream().map(detalheDto -> {
			return converterDto(itemAvariado, detalheDto, statusIntegracao);
		}).toList();
	}

	public List<ItemAvariadoDetalhe> buscarPorItemAvariadoComStatus(ItemAvariado itemAvariado, StatusIntegracao status) {
		return itemAvariadoDetalheRep.findByItemAvariadoAndStatusIntegracaoAndDataInativacaoIsNull(itemAvariado, status);
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<ItemAvariadoDetalhe> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<ItemAvariadoDetalhe> detalhes = objs.stream().map(detalhe -> {
			detalhe.setDataIntegracao(null);
			detalhe.setStatusIntegracao(status);
			return detalhe;
		}).toList();
		itemAvariadoDetalheRep.saveAll(detalhes);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ItemAvariadoDetalhe buscarPorValidacaoProduto(String codigoGrupoProduto, String codigoEstabelecimento) {
		List<ItemAvariadoDetalhe> detalhes = itemAvariadoDetalheRep.findAll(
				ItemAvariadoDetalheSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(ItemAvariadoDetalheSpecs.codigoEstabelecimentoEquals(codigoEstabelecimento))
				.and(ItemAvariadoDetalheSpecs.campoValidacaoEquals(ItemAvariadoValidacaoEnum.PRODUTO)));
		
		if(detalhes == null || detalhes.isEmpty()) {
    		throw new ObjectNotFoundException("Não foi encontrado \"Item Avariado\" com os parâmetros informados!");
    	}
		
		return detalhes.get(0);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ItemAvariadoDetalhe buscarPorValidacaoChuvadoAvaraido(String codigoGrupoProduto, String codigoEstabelecimento, BigDecimal percentual) {
		List<ItemAvariadoDetalhe> detalhes = itemAvariadoDetalheRep.findAll(
				ItemAvariadoDetalheSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(ItemAvariadoDetalheSpecs.codigoEstabelecimentoEquals(codigoEstabelecimento))
				.and(ItemAvariadoDetalheSpecs.campoValidacaoEquals(ItemAvariadoValidacaoEnum.CHUVADO_AVARIADO))
				.and(ItemAvariadoDetalheSpecs.percentualRange(percentual)));
		
		if(detalhes == null || detalhes.isEmpty()) {
    		throw new ObjectNotFoundException("Não foi encontrado \"Item Avariado\" com os parâmetros informados!");
    	}
		
		return detalhes.get(0);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ItemAvariadoDetalhe buscarPorValidacaoPh(String codigoGrupoProduto, String codigoEstabelecimento, BigDecimal percentual, BigInteger ph, BigDecimal fnt) {
		List<ItemAvariadoDetalhe> detalhes = itemAvariadoDetalheRep.findAll(
				ItemAvariadoDetalheSpecs.codigoGrupoProdutoEquals(codigoGrupoProduto)
				.and(ItemAvariadoDetalheSpecs.codigoEstabelecimentoEquals(codigoEstabelecimento))
				.and(ItemAvariadoDetalheSpecs.campoValidacaoEquals(ItemAvariadoValidacaoEnum.PH))
				.and(ItemAvariadoDetalheSpecs.phRange(ph))
				.and(ItemAvariadoDetalheSpecs.fntInicialLessThanOrEqualTo(fnt)));
		
		if(detalhes == null || detalhes.isEmpty()) {
    		throw new ObjectNotFoundException("Não foi encontrado \"Item Avariado\" com os parâmetros informados!");
    	}
		
		// BUSCAR O REGISTRO QUE POSSUI O PERCENTUAL INFORMADO POR PARAMETRO NO RANGE (ENTRE O PERCENTUAL INICIAL E FINAL)
		ItemAvariadoDetalhe detalhePercentual = detalhes.stream().filter(detalhe -> {
			return percentual.compareTo(detalhe.getPercentualInicial()) >= 0
					&& percentual.compareTo(detalhe.getPercentualFinal()) <= 0;
		}).findFirst().orElse(null);
		
		
		// BUSCAR O REGISTRO QUE POSSUI O FNT INFORMADO NO PARAMETRO DE ACORDO COM O RANGE (ENTRE O FNT INICIAL E FNT FINAL)
		// E PERCENTUAL INFORMADO NO PARAMETRO MENOR OU IGUAL AO PERCENTUAL FINAL DO DETALHE
		ItemAvariadoDetalhe detalheFnt = detalhes.stream().filter(detalhe -> {
			return fnt.compareTo(detalhe.getFntInicial()) >= 0
					&& fnt.compareTo(detalhe.getFntFinal()) <= 0
					&& percentual.compareTo(detalhe.getPercentualFinal()) <= 0;
		}).findFirst().orElse(null);
		
		if(detalhePercentual != null && (fnt.compareTo(detalhePercentual.getFntFinal()) >= 0 || detalheFnt == null)) {
			return detalhePercentual;
		}
		
		return detalheFnt;
	}
	
	public void updateStatus(StatusIntegracao status, Long idItemAvariado) {
		itemAvariadoDetalheRep.updateStatus(status, idItemAvariado);
	}
	
	public void deleteDetalhes(Long idItemAvariado) {
		itemAvariadoDetalheRep.deleteDetalhes(idItemAvariado);
	}
}

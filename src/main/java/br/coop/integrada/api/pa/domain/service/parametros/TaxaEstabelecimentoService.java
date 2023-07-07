package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.TAXA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;

@Service
@Transactional
public class TaxaEstabelecimentoService {

	@Autowired
	private TaxaEstabelecimentoRep taxaEstabelecimentoRep;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
	private IntegrationService integrationService;

	public TaxaEstabelecimento salvar(TaxaEstabelecimento taxaEstabelecimento) {
		return taxaEstabelecimentoRep.save(taxaEstabelecimento);
	}

	public List<TaxaEstabelecimento> salvar(Taxa taxa, List<TaxaEstabelecimentoDto> estabelecimentoDtos) {
		List<TaxaEstabelecimento> taxaEstabelecimentos = converterDto(taxa, estabelecimentoDtos);
		return taxaEstabelecimentoRep.saveAll(taxaEstabelecimentos);
	}
	
	public TaxaEstabelecimento buscarPorIdTaxaECodigoEstabelecimento(Long idTaxaGrupoProduto, String codigoEstabelecimento) {
		return taxaEstabelecimentoRep.findByTaxaIdAndEstabelecimentoCodigo(idTaxaGrupoProduto, codigoEstabelecimento);
	}
	
	public List<TaxaEstabelecimento> buscarPorIdTaxa(Long taxaGrupoProdutoId) {
		return taxaEstabelecimentoRep.findByTaxaIdAndDataInativacaoIsNull(taxaGrupoProdutoId);
	}
	
	public TaxaEstabelecimento converterDto(Taxa taxa, TaxaEstabelecimentoDto objDto, StatusIntegracao statusIntegracao) {
		String codigoEstabelecimento = objDto.getCodigo();
		TaxaEstabelecimento taxaEstabelecimento = new TaxaEstabelecimento();
		TaxaEstabelecimento taxaEstabelecimentoAtual = buscarPorIdTaxaECodigoEstabelecimento(taxa.getId(), codigoEstabelecimento);
		
		if(taxaEstabelecimentoAtual != null) {
			BeanUtils.copyProperties(taxaEstabelecimentoAtual, taxaEstabelecimento);
		}

		Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);
		if(estabelecimento == null) {
			throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código " + codigoEstabelecimento);
		}
		
		taxaEstabelecimento.setEstabelecimento(estabelecimento);
		taxaEstabelecimento.setTaxa(taxa);
		taxaEstabelecimento.setDataInativacao(null);
		
		if(taxaEstabelecimentoAtual == null || taxaEstabelecimentoAtual.getDataInativacao() != taxaEstabelecimento.getDataInativacao()) {
			taxaEstabelecimento.setDataIntegracao(null);
			taxaEstabelecimento.setStatusIntegracao(statusIntegracao);
		}
		
		return taxaEstabelecimento;
	}

	public List<TaxaEstabelecimento> converterDto(Taxa taxa, List<TaxaEstabelecimentoDto> objDtos) {
		if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
		
		return objDtos.stream().map(estabelecimentoSimplesDto -> {
			return converterDto(taxa, estabelecimentoSimplesDto, statusIntegracao);
		}).toList();
	}

	public List<TaxaEstabelecimento> buscarComStatus(Long idTaxaGrupoProduto, StatusIntegracao status) {
		List<TaxaEstabelecimento> taxaEstabelecimentoList = taxaEstabelecimentoRep.findByTaxaIdAndStatusIntegracao(idTaxaGrupoProduto, status);
		if(CollectionUtils.isEmpty(taxaEstabelecimentoList)) return Collections.emptyList();
		return taxaEstabelecimentoList.stream().map(taxaEstabelecimento -> {
			var obj = new TaxaEstabelecimento();
			BeanUtils.copyProperties(taxaEstabelecimento, obj);
			return obj; 
		}).toList();
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<TaxaEstabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<TaxaEstabelecimento> estabelecimentos = objs.stream().map(taxaEstabelecimento -> {
			taxaEstabelecimento.setDataIntegracao(null);
			taxaEstabelecimento.setStatusIntegracao(status);
			return taxaEstabelecimento;
		}).toList();
		taxaEstabelecimentoRep.saveAll(estabelecimentos);
	}

	public List<TaxaEstabelecimento> getEstabelecimentoParaInativar(Long idTaxaGrupoProduto, List<TaxaEstabelecimentoDto> taxaGrupoProdutoDtos) {
		if(CollectionUtils.isEmpty(taxaGrupoProdutoDtos)) return Collections.emptyList();
		
		List<TaxaEstabelecimento> taxaEstabelecimentoList = buscarPorIdTaxa(idTaxaGrupoProduto);

		return taxaEstabelecimentoList.stream().filter(taxaEstabelecimento -> {
			TaxaEstabelecimentoDto taxaEstabelecimentoDto = taxaGrupoProdutoDtos.stream()
					.filter(item -> { return taxaEstabelecimento.getCodigo().equals(item.getCodigo()); })
					.findFirst().orElse(null);
			return taxaEstabelecimentoDto == null;
		}).toList();
	}

	public void inativar(List<TaxaEstabelecimento> taxaEstabelecimentos) {
		if(CollectionUtils.isEmpty(taxaEstabelecimentos)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
		List<TaxaEstabelecimento> taxaEstabelecimentosParaInativar = taxaEstabelecimentos.stream()
				.map(taxaEstabelecimento -> {
					taxaEstabelecimento.setDataInativacao(new Date());
					taxaEstabelecimento.setDataIntegracao(null);
					taxaEstabelecimento.setStatusIntegracao(statusIntegracao);
					return taxaEstabelecimento;
				}).toList();
		taxaEstabelecimentoRep.saveAll(taxaEstabelecimentosParaInativar);
	}
	
	public Page<TaxaEstabelecimento> findByTaxaIdAndDataInativacaoIsNullOrderByEstabelecimentoCodigo(Long id, Pageable pageable){
		return taxaEstabelecimentoRep.findByTaxaIdAndDataInativacaoIsNullOrderByEstabelecimentoCodigo(id,  pageable);
	}
}

package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.TAXA;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaGrupoProdutoRep;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;

@Service
@Transactional
public class TaxaGrupoProdutoService {
	
	@Autowired
	private TaxaGrupoProdutoRep taxaGrupoProdutoRep;
		
	@Autowired
	private GrupoProdutoService grupoProdutoService;
	
	@Autowired
	private IntegrationService integrationService;

	public TaxaGrupoProduto salvar(TaxaGrupoProduto taxaGrupoProduto) {
		return taxaGrupoProdutoRep.save(taxaGrupoProduto);
	}

	public TaxaGrupoProduto salvar(Taxa taxa, TaxaGrupoProdutoDto objDto, StatusIntegracao statusIntegracao) {
		TaxaGrupoProduto taxaGrupoProduto = converterDto(taxa, objDto, statusIntegracao);
		taxaGrupoProduto = taxaGrupoProdutoRep.save(taxaGrupoProduto);
	
		return taxaGrupoProduto;
	}
	
	public List<TaxaGrupoProduto> salvar(Taxa taxa, List<TaxaGrupoProdutoDto> objDtos) {
		if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
		
		return objDtos.stream().map(taxaGrupoProdutoDto -> {
			return salvar(taxa, taxaGrupoProdutoDto, statusIntegracao);
		}).toList();
	}
	
	public List<TaxaGrupoProduto> buscarPorIdTaxa(Long idTaxa) {
		return taxaGrupoProdutoRep.findByTaxaIdAndDataInativacaoIsNull(idTaxa);
	}
	
	public TaxaGrupoProduto buscarPorIdTaxaECodigoGrupoProduto(Long idTaxa, String codigoGrupoProduto) {
		return taxaGrupoProdutoRep.findByTaxaIdAndGrupoProdutoFmCodigo(idTaxa, codigoGrupoProduto);
	}
	
	public TaxaGrupoProduto converterDto(Taxa taxa, TaxaGrupoProdutoDto objDto, StatusIntegracao statusIntegracao) {
		if(Strings.isEmpty(objDto.getFmCodigo())) {
			throw new ObjectDefaultException("Necessário informar o código do grupo de produto \"fmCodigo\"");
		}
		
		TaxaGrupoProduto taxaGrupoProduto = new TaxaGrupoProduto();
		TaxaGrupoProduto taxaGrupoProdutoAtual = buscarPorIdTaxaECodigoGrupoProduto(taxa.getId(), objDto.getFmCodigo());
		
		if(taxaGrupoProdutoAtual != null) {
			BeanUtils.copyProperties(taxaGrupoProdutoAtual, taxaGrupoProduto);
		}
		
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(objDto.getFmCodigo());
		if(grupoProduto == null) {
			throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o código \"" + objDto.getFmCodigo() + "\"");
		}
		
		taxaGrupoProduto.setGrupoProduto(grupoProduto);
		taxaGrupoProduto.setTaxa(taxa);
		taxaGrupoProduto.setDataInativacao(null);
		taxaGrupoProduto.setDataIntegracao(null);
		taxaGrupoProduto.setStatusIntegracao(statusIntegracao);
        
        return taxaGrupoProduto;
	}

	public List<TaxaGrupoProduto> buscarComStatus(Long idTaxa, StatusIntegracao status) {
		List<TaxaGrupoProduto> taxaGrupoProdutoList = taxaGrupoProdutoRep.findByTaxaIdAndStatusIntegracao(idTaxa, status);
		
		return taxaGrupoProdutoList;
		
		//List<TaxaEstabelecimento> estabelecimentos = taxaEstabelecimentoService.buscarComStatus(obj.getId(), status);
		/*if(CollectionUtils.isEmpty(taxaGrupoProdutoList)) return Collections.emptyList();
		return taxaGrupoProdutoList.stream().map(taxaGrupoProduto -> {
			var obj = new TaxaGrupoProduto();
    		BeanUtils.copyProperties(taxaGrupoProduto, obj);
			List<TaxaEstabelecimento> estabelecimentos = taxaEstabelecimentoService.buscarComStatus(obj.getId(), status);
			//obj.setEstabelecimentos(estabelecimentos);
			return obj;
		}).toList();*/
	}

	public void alterarStatusIntegracao(StatusIntegracao status, List<TaxaGrupoProduto> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		
		List<TaxaGrupoProduto> grupoProdutos = objs.stream().map(taxaGrupoProduto -> {
			//taxaEstabelecimentoService.alterarStatusIntegracao(status, taxaGrupoProduto.getEstabelecimentos());
			taxaGrupoProduto.setDataIntegracao(null);
			taxaGrupoProduto.setStatusIntegracao(status);
			return taxaGrupoProduto;
		}).toList();
		
		taxaGrupoProdutoRep.saveAllAndFlush(grupoProdutos);
	}

	public List<TaxaGrupoProduto> getGrupoProdutoParaInativar(Long idTaxa, List<TaxaGrupoProdutoDto> grupoProdutoDtos) {
		if(CollectionUtils.isEmpty(grupoProdutoDtos)) return Collections.emptyList();
		
		List<TaxaGrupoProduto> taxaGrupoProdutoList = buscarPorIdTaxa(idTaxa);

		return taxaGrupoProdutoList.stream().filter(taxaGrupoProduto -> {
			TaxaGrupoProdutoDto taxaGrupoProdutoDto = grupoProdutoDtos.stream()
					.filter(item -> { return taxaGrupoProduto.getCodigoGrupoProduto().equals(item.getFmCodigo()); })
					.findFirst().orElse(null);
			return taxaGrupoProdutoDto == null;
		}).toList();
	}

	public void inativar(List<TaxaGrupoProduto> taxaGrupoProdutos) {
		if(CollectionUtils.isEmpty(taxaGrupoProdutos)) return;
		
		StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
		List<TaxaGrupoProduto> taxaGrupoProdutosParaInativar = new ArrayList<TaxaGrupoProduto>();
		for(TaxaGrupoProduto item : taxaGrupoProdutos) {
			item.setDataInativacao(new Date());
			item.setDataIntegracao(null);
			item.setStatusIntegracao(statusIntegracao);
			
			//taxaEstabelecimentoService.inativar(item.getEstabelecimentos());
			
			taxaGrupoProdutosParaInativar.add(item);
		}
		taxaGrupoProdutoRep.saveAll(taxaGrupoProdutosParaInativar);
	}
	
	public Page<TaxaGrupoProduto> findByTaxaIdAndDataInativacaoIsNull(Long id, Pageable pageable){
		return taxaGrupoProdutoRep.findByTaxaIdAndDataInativacaoIsNull(id, pageable);
	}
}

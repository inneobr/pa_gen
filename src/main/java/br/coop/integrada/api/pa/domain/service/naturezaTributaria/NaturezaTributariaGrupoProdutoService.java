package br.coop.integrada.api.pa.domain.service.naturezaTributaria;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaGrupoProduto;
import br.coop.integrada.api.pa.domain.repository.naturezaTributaria.NaturezaTributariaGrupoProdutoRep;

@Service
public class NaturezaTributariaGrupoProdutoService {
	private static final Logger logger = LoggerFactory.getLogger(NaturezaTributariaService.class);
	
	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	@Autowired
	private NaturezaTributariaGrupoProdutoRep naturezaTributariaGrupoProdutoRep;
	

	public void cadastrar(NaturezaTributaria naturezaTributaria, List<GrupoProdutoResumidoDto> grupoProdutoDto) {				
		for(GrupoProdutoResumidoDto grupoProduto: grupoProdutoDto) {
			cadastrarGrupoProduto(naturezaTributaria, grupoProduto);			
		}
	}
	
	public void cadastrarGrupoProduto (NaturezaTributaria naturezaTributaria, GrupoProdutoResumidoDto grupoProdutoDto) {
		if(grupoProdutoDto.getFmCodigo() == null) {
			logger.info("Campo operação  { fmCodigo } é obrigatório");
			throw new ObjectNotFoundException("Campo { fmCodigo } é obrigatório");
		}
		
		GrupoProduto grupoProduto = grupoProdutoRep.findByFmCodigo(grupoProdutoDto.getFmCodigo());
		if(grupoProduto == null) {
			logger.info("Grupo Produto não encontrado: " + grupoProdutoDto.getFmCodigo());
			throw new ObjectNotFoundException("Grupo Produto não encontrado: " + grupoProdutoDto.getFmCodigo());
		}
		
		NaturezaTributariaGrupoProduto naturezaTributariaGrupoProdutoEncontrada = naturezaTributariaGrupoProdutoRep.findByNaturezaTributariaIdAndGrupoProdutoFmCodigo(naturezaTributaria.getId(),grupoProdutoDto.getFmCodigo());
		if(naturezaTributariaGrupoProdutoEncontrada == null) {		
			NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto = new NaturezaTributariaGrupoProduto();
			naturezaTributariaGrupoProduto.setNaturezaTributaria(naturezaTributaria);
			naturezaTributariaGrupoProduto.setGrupoProduto(grupoProduto);
			naturezaTributariaGrupoProduto.setStatusIntegracao(naturezaTributaria.getStatusIntegracao());
			if(naturezaTributaria.getGrupoProduto() == null) {
				naturezaTributaria.setGrupoProduto(new ArrayList<>());
			}
			naturezaTributaria.getGrupoProduto().add(naturezaTributariaGrupoProduto);
		}
	}
	
	public Page<NaturezaTributariaGrupoProduto> findGrupoProdutoByNaturezaTribitaria(Long idNatureza, Pageable pageable){
		Page<NaturezaTributariaGrupoProduto> naturezaTributariaGrupoProduto = naturezaTributariaGrupoProdutoRep.findByNaturezaTributariaId(idNatureza, pageable);
		if(naturezaTributariaGrupoProduto.isEmpty()) {
			throw new ObjectNotFoundException("Não encontramos grupoProduto para naturezaTributaria: " + idNatureza);
		}
		return naturezaTributariaGrupoProduto;
	}	
	
	public void integrar(NaturezaTributaria naturezaTributaria, List<GrupoProdutoResumidoDto> grupoProdutoDto) {				
		for(GrupoProdutoResumidoDto grupoProduto: grupoProdutoDto) {
			atualizarGrupoProduto(naturezaTributaria, grupoProduto);			
		}
	}
	
	public void atualizarGrupoProduto(NaturezaTributaria naturezaTributaria, GrupoProdutoResumidoDto grupoProdutoDto) {
		NaturezaTributariaGrupoProduto naturezaTributariaGrupoProdutoEncontrada = buscarGrupoProdutoCadastrado(naturezaTributaria, grupoProdutoDto);
		if(grupoProdutoDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
			if(naturezaTributaria.getGrupoProduto() != null) {
				if(naturezaTributariaGrupoProdutoEncontrada != null) {
					naturezaTributaria.getGrupoProduto().remove(naturezaTributariaGrupoProdutoEncontrada);
				}
			}
		}else {
			if(naturezaTributariaGrupoProdutoEncontrada == null) {
				if(grupoProdutoDto.getFmCodigo() == null) {
					logger.info("Campo operação  { fmCodigo } é obrigatório");
					throw new ObjectNotFoundException("Campo { fmCodigo } é obrigatório");
				}
				GrupoProduto grupoProduto = grupoProdutoRep.findByFmCodigo(grupoProdutoDto.getFmCodigo());
				if(grupoProduto == null) {
					logger.info("Grupo Produto não encontrado: " + grupoProdutoDto.getFmCodigo());
					throw new ObjectNotFoundException("Grupo Produto não encontrado: " + grupoProdutoDto.getFmCodigo());
				}
				
				NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto = new NaturezaTributariaGrupoProduto();
				naturezaTributariaGrupoProduto.setNaturezaTributaria(naturezaTributaria);
				naturezaTributariaGrupoProduto.setIdRegistro(grupoProdutoDto.getIdRegistro());
				naturezaTributariaGrupoProduto.setGrupoProduto(grupoProduto);
				naturezaTributariaGrupoProduto.setStatusIntegracao(naturezaTributaria.getStatusIntegracao());
				naturezaTributariaGrupoProduto.setDataIntegracao(new Date());
				if(naturezaTributaria.getGrupoProduto() == null) {
					naturezaTributaria.setGrupoProduto(new ArrayList<>());
				}
				naturezaTributaria.getGrupoProduto().add(naturezaTributariaGrupoProduto);
			}
		}
	}
	
	public NaturezaTributariaGrupoProduto buscarGrupoProdutoCadastrado(NaturezaTributaria naturezaTributaria, GrupoProdutoResumidoDto grupoProdutoDto) {
		NaturezaTributariaGrupoProduto naturezaTributariaGrupoProdutoEncontrada = null;
		if(naturezaTributaria.getGrupoProduto() != null) {			
			for(NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto : naturezaTributaria.getGrupoProduto()) {
				if(naturezaTributariaGrupoProduto.getIdRegistro().equals(grupoProdutoDto.getIdRegistro())) {
					naturezaTributariaGrupoProdutoEncontrada = naturezaTributariaGrupoProduto;
					break;
				}						
			}
		}
		return naturezaTributariaGrupoProdutoEncontrada;
	}
}

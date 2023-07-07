package br.coop.integrada.api.pa.domain.service.naturezaTributaria;

import java.util.List;
import org.slf4j.Logger;

import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.GENESIS;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import org.springframework.beans.factory.annotation.Autowired;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.spec.NaturezaTriburatiaSpecs;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;

import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;

import br.coop.integrada.api.pa.domain.repository.naturezaTributaria.NaturezaTributariaRep;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturezaTributariaDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaGrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.NaturesaTributariaFilter;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaEstabelecimento;

@Service @RequiredArgsConstructor @Transactional
public class NaturezaTributariaService {
	private static final Logger logger = LoggerFactory.getLogger(NaturezaTributariaService.class);	
		
	@Autowired
    private IntegrationService integrationService;

	@Autowired
	private NaturezaTributariaRep naturezaTributariaRep;
	
	@Autowired
	private HistoricoGenericoService historicoGenericoService;

	@Autowired
	private NaturezaTributariaGrupoProdutoService naturezaTributariaGrupoProdutoService;
	
	@Autowired
	private NaturezaTributariaEstabelecimentoService naturezaTributariaEstabelecimentoService;
	
	public List<NaturezaTributariaDto> findAll(){	
		logger.info("Listando naturezas tributarias.");
		List<NaturezaTributaria> naturezaTributarias = naturezaTributariaRep.findAll();
		
		List<NaturezaTributariaDto> lista = new ArrayList<>();
		for(NaturezaTributaria naturezaTributaria : naturezaTributarias) {
			NaturezaTributariaDto naturezaTributariaDto = new NaturezaTributariaDto();
			BeanUtils.copyProperties(naturezaTributaria, naturezaTributariaDto);
			lista.add(naturezaTributariaDto);
		}
		return lista;
	}
	
	public Page<NaturezaTributariaDto> findAllPage(NaturesaTributariaFilter filter, Pageable pageable){	
		logger.info("Listando naturezas tributarias com paginação.");
		Page<NaturezaTributaria> naturezaTributarias = naturezaTributariaRep.findAll(
				NaturezaTriburatiaSpecs.doCodigoDescricao(filter.getPesquisar()),
				pageable
		);
		if(naturezaTributarias == null) {
			throw new ObjectNotFoundException("Não encontramos registros para filtro: ");
		}
		
		List<NaturezaTributariaDto> lista = new ArrayList<>();
		for(NaturezaTributaria naturezaTributaria : naturezaTributarias) {
			NaturezaTributariaDto naturezaTributariaDto = new NaturezaTributariaDto();
			BeanUtils.copyProperties(naturezaTributaria, naturezaTributariaDto);
			
			List<EstabelecimentoNomeCodigoDto> estabelecimentoResponse = new ArrayList<>();
			for(NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento: naturezaTributaria.getEstabelecimentos()) {
				EstabelecimentoNomeCodigoDto estabelecimentoDto = new EstabelecimentoNomeCodigoDto();
				Estabelecimento estabelecimento = naturezaTributariaEstabelecimento.getEstabelecimento();
				BeanUtils.copyProperties(estabelecimento,  estabelecimentoDto);
				estabelecimentoResponse.add(estabelecimentoDto);
			}	
			naturezaTributariaDto.setEstabelecimentos(estabelecimentoResponse);
			
			List<GrupoProdutoResumidoDto> grupoProdutoResponse = new ArrayList<>();
			for(NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto: naturezaTributaria.getGrupoProduto()) {
				GrupoProdutoResumidoDto grupoProdutoResumidoDto = new GrupoProdutoResumidoDto();
				GrupoProduto grupoProduto = naturezaTributariaGrupoProduto.getGrupoProduto();
				BeanUtils.copyProperties(grupoProduto, grupoProdutoResumidoDto);
				grupoProdutoResponse.add(grupoProdutoResumidoDto);
			}	
			naturezaTributariaDto.setGrupoProdutos(grupoProdutoResponse);			
			lista.add(naturezaTributariaDto);
		}
		return new PageImpl<>(
				lista, pageable, naturezaTributarias.getTotalElements()
		);
	}
	
	public NaturezaTributariaDto findById(Long id){	
		logger.info("Buscando natureza tributaria id: {}", id);
		NaturezaTributaria naturezaTributaria = naturezaTributariaRep.getReferenceById(id);
		if(naturezaTributaria == null) {
			throw new ObjectNotFoundException("Não encontramos registros para o id: " + id);
		}
		
		NaturezaTributariaDto naturezaTributariaDto = new NaturezaTributariaDto();
		BeanUtils.copyProperties(naturezaTributaria, naturezaTributariaDto);
		
		List<EstabelecimentoNomeCodigoDto> estabelecimentoResponse = new ArrayList<>();
		for(NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento: naturezaTributaria.getEstabelecimentos()) {
			EstabelecimentoNomeCodigoDto estabelecimentoDto = new EstabelecimentoNomeCodigoDto();
			Estabelecimento estabelecimento = naturezaTributariaEstabelecimento.getEstabelecimento();
			BeanUtils.copyProperties(estabelecimento,  estabelecimentoDto);
			estabelecimentoResponse.add(estabelecimentoDto);
		}	
		naturezaTributariaDto.setEstabelecimentos(estabelecimentoResponse);
		
		List<GrupoProdutoResumidoDto> grupoProdutoResponse = new ArrayList<>();
		for(NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto: naturezaTributaria.getGrupoProduto()) {
			GrupoProdutoResumidoDto grupoProdutoResumidoDto = new GrupoProdutoResumidoDto();
			GrupoProduto grupoProduto = naturezaTributariaGrupoProduto.getGrupoProduto();
			BeanUtils.copyProperties(grupoProduto, grupoProdutoResumidoDto);
			grupoProdutoResponse.add(grupoProdutoResumidoDto);
		}	
		naturezaTributariaDto.setGrupoProdutos(grupoProdutoResponse);
		return naturezaTributariaDto;
	}

	public NaturezaTributaria findByGrupoProdutoAndEstabelecimento(String fmCodigo, String codigo){	
		logger.info("Buscando natureza tributaria por estabelecimento: {} and grupoProduto: {}", codigo, fmCodigo);
		NaturezaTributaria naturezaTributaria = naturezaTributariaRep.findByGrupoProdutoGrupoProdutoFmCodigoAndEstabelecimentosEstabelecimentoCodigo(fmCodigo, codigo);
		return naturezaTributaria;
	}
	
	public void deleteById(Long id){	
		logger.info("Deletando natureza tributaria id: {}", id);
		naturezaTributariaRep.deleteById(id);
	}
	
	public Page<GrupoProdutoResumidoDto> findGrupoProdutoByNaturezaTributaria(Long idNatureza, Pageable pageable){
		 Page<NaturezaTributariaGrupoProduto> naturezaTributariaGrupoProdutos = naturezaTributariaGrupoProdutoService.findGrupoProdutoByNaturezaTribitaria(idNatureza, pageable);
		 
		 List<GrupoProdutoResumidoDto> grupoProdutos = new ArrayList<>();
		 for(NaturezaTributariaGrupoProduto naturezaTributariaGrupoProduto: naturezaTributariaGrupoProdutos.getContent()) {
			 GrupoProdutoResumidoDto grupoProdutoDto = new GrupoProdutoResumidoDto();
			 BeanUtils.copyProperties(naturezaTributariaGrupoProduto.getGrupoProduto(), grupoProdutoDto);
			 grupoProdutos.add(grupoProdutoDto);
		 }
		 return new PageImpl<>(
				 grupoProdutos, pageable, naturezaTributariaGrupoProdutos.getTotalElements()
		);		 
	}
	
	public Page<EstabelecimentoNomeCodigoDto> findEstabelecimentosByNaturezaTributaria(Long idNatureza, Pageable pageable){
		 Page<NaturezaTributariaEstabelecimento> naturezaTributariaEstabelecimentos = naturezaTributariaEstabelecimentoService.findEstabelecimentosByNaturezaTribitaria(idNatureza, pageable);
		 
		 List<EstabelecimentoNomeCodigoDto> estabelecimentos = new ArrayList<>();
		 for(NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento: naturezaTributariaEstabelecimentos.getContent()) {
			 EstabelecimentoNomeCodigoDto estabelecimentoDto = new EstabelecimentoNomeCodigoDto();
			 BeanUtils.copyProperties(naturezaTributariaEstabelecimento.getEstabelecimento(), estabelecimentoDto);
			 estabelecimentos.add(estabelecimentoDto);
		 }
		 return new PageImpl<>(
			estabelecimentos, pageable, naturezaTributariaEstabelecimentos.getTotalElements()
		);		 
	}
	
	public NaturezaTributaria cadastrar(NaturezaTributariaDto naturezaTributariaDto){		
		logger.info("Cadastrando naturezas tributarias");
		if(naturezaTributariaDto.getCodigo() == null) {
			logger.info("Campo { codigo } é obrigatório.");
			throw new ObjectNotFoundException("Código é um campo chave { codigo } não pode ser vazio.");			
		}
		NaturezaTributaria naturezaTributaria = null;		
		if(naturezaTributariaDto.getId() != null) {
			naturezaTributaria = naturezaTributariaRep.getReferenceById(naturezaTributariaDto.getId());
		}
		
		if(naturezaTributaria == null) {
			naturezaTributaria = new NaturezaTributaria();
		}
		
		naturezaTributariaDto.setId(naturezaTributaria.getId());
		BeanUtils.copyProperties(naturezaTributariaDto, naturezaTributaria);	
		
		if(naturezaTributaria.getGrupoProduto() != null && !naturezaTributaria.getGrupoProduto().isEmpty()) {
			naturezaTributaria.getGrupoProduto().clear();
		}
		
		if(naturezaTributaria.getEstabelecimentos()!= null && !naturezaTributaria.getEstabelecimentos().isEmpty()) {
			naturezaTributaria.getEstabelecimentos().clear();
		}
		
		naturezaTributaria.setDataIntegracao(null);		
		naturezaTributaria.setStatusIntegracao(StatusIntegracao.INTEGRAR);	
		naturezaTributaria = naturezaTributariaRep.save(naturezaTributaria);		
	
		if(naturezaTributariaDto.getGrupoProdutos() != null && !naturezaTributariaDto.getGrupoProdutos().isEmpty()) {			
			naturezaTributariaGrupoProdutoService.cadastrar(naturezaTributaria, naturezaTributariaDto.getGrupoProdutos());
		}
		
		
		if(naturezaTributariaDto.getEstabelecimentos() != null && !naturezaTributariaDto.getEstabelecimentos().isEmpty()) {		
			naturezaTributariaEstabelecimentoService.cadastrar(naturezaTributaria, naturezaTributariaDto.getEstabelecimentos());
		}
		
		historicoGenericoService.salvar(naturezaTributaria.getId(), PaginaEnum.NATUREZA_TRIBURARIA, "Cadastro de nova Natureza Tributária", naturezaTributaria.getDescricao());			
		return naturezaTributaria;
	}
	
	public void integration(NaturezaTributariaDto naturezaDto){	
		logger.info("Integrando naturezas tributarias");
		if(naturezaDto.getCodigo() == null) {
			logger.info("Campo { codigo } é obrigatório.");
			throw new ObjectNotFoundException("Código é um campo chave { codigo } não pode ser vazio.");			
		}
		
		if(naturezaDto.getOperacao() == null) {	
			logger.info("Campo operacao { WRITE ou DELETE } é obrigatório.");
			throw new ObjectNotFoundException("Campo operação  { WRITE / DELETE } é obrigatório");
		}
		
		NaturezaTributaria naturezaTributaria = naturezaTributariaRep.findByCodigo(naturezaDto.getCodigo());
		if(naturezaTributaria == null) {
			naturezaTributaria = new NaturezaTributaria();
		}
				
		if(naturezaDto.getOperacao().equals(IntegrationOperacaoEnum.DELETE)) {
			if(naturezaTributaria.getId() == null) {
				return;
			}
			
			try {
				naturezaTributariaRep.deleteById(naturezaTributaria.getId());
			}catch (Exception e) {
				if(naturezaTributaria.getDataInativacao() == null) {
					naturezaTributaria.setDataInativacao(new Date());
				}
				naturezaTributariaRep.save(naturezaTributaria);	
			}
		}else {
			naturezaDto.setId(naturezaTributaria.getId());
			BeanUtils.copyProperties(naturezaDto, naturezaTributaria);	
		
			try {
				IntegracaoPagina integracaoOrigem =  integrationService.buscarPorPagina(PaginaEnum.NATUREZA_TRIBURARIA);
				if(integracaoOrigem != null && GENESIS.equals(integracaoOrigem.getOrigenEnum())) {
					naturezaTributaria.setStatusIntegracao(StatusIntegracao.INTEGRADO);
					naturezaTributaria.setDataIntegracao(new Date());
				}else {
					naturezaTributaria.setStatusIntegracao(StatusIntegracao.INTEGRAR);
				}
			}catch (Exception e) {
				naturezaTributaria.setStatusIntegracao(StatusIntegracao.INTEGRAR);
			}				

			if(naturezaDto.getGrupoProdutos() != null && !naturezaDto.getGrupoProdutos().isEmpty()) {
				naturezaTributariaGrupoProdutoService.integrar(naturezaTributaria, naturezaDto.getGrupoProdutos());
			}
			
			if(naturezaDto.getEstabelecimentos() != null && !naturezaDto.getEstabelecimentos().isEmpty()) {
				naturezaTributariaEstabelecimentoService.integrar(naturezaTributaria, naturezaDto.getEstabelecimentos());
			}
			
			naturezaTributaria = naturezaTributariaRep.save(naturezaTributaria);
			historicoGenericoService.salvar(naturezaTributaria.getId(), PaginaEnum.NATUREZA_TRIBURARIA, "Cadastro ou Atualização de Natureza Tributária realizado com sucesso", naturezaTributaria.getDescricao());
			
		}
	}	
}

package br.coop.integrada.api.pa.domain.service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoBuscaDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoDto;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoFilter;
import br.coop.integrada.api.pa.domain.modelDto.preco.PrecoSaveDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.PrecoRep;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.spec.PrecoSpecs;

import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrecoService {
	private static final Logger logger = LoggerFactory.getLogger(PrecoService.class);
	
	@Autowired
	private PrecoRep precoRep;
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	@Autowired
	private ProdutoRep produtoRep;
	@Autowired
	private GrupoProdutoRep grupoProdutoRep;
	
	public Preco atualizar(Preco preco) {
		return precoRep.save(preco);
	}
	
	public Preco salvar(PrecoSaveDto objDto) {
		List<String> mensagens = new ArrayList<>();

		if(Strings.isEmpty(objDto.getCodigoEstabelecimento())) mensagens.add("Código do estabelecimento");
		if(Strings.isEmpty(objDto.getCodigoProduto()))  mensagens.add("Código do produto");

		if(!mensagens.isEmpty()) {
			String campos = String.join(" \", \"", mensagens);
			throw new ObjectDefaultException("Necessário informar o(s) campo(s) chave(s) \"" + campos + "\"!");
		}
		
		if(objDto.getPrecoFiscal() == null || objDto.getPrecoFiscal().doubleValue() < 0) {
			throw new ObjectDefaultException("Campo {precoFiscal} inválido!");
		}
		
		if(objDto.getPrecoFechamento() == null || objDto.getPrecoFechamento().doubleValue() < 0) {
			throw new ObjectDefaultException("Campo {precoFechamento} inválido!");
		}
		
		if(objDto.getPrecoFechamentoCoco() == null || objDto.getPrecoFechamentoCoco().doubleValue() < 0) {
			throw new ObjectDefaultException("Campo {precoFechamentoCoco} inválido!");
		}
		
		if(objDto.getHoraValidade() == "18::0") {
			objDto.setHoraValidade("18:00");
		}
		
		Preco preco = null;
		if(Strings.isEmpty(objDto.getIdUnico() ) ) {	
			preco = precoRep.findByCodigoEstabelecimentoAndCodigoProdutoAndCodigoReferencia(objDto.getCodigoEstabelecimento(), objDto.getCodigoProduto(), objDto.getCodigoReferencia());
		}else {
			preco = precoRep.findByIdUnico(objDto.getIdUnico());
		}

		if(preco == null) {
			preco = new Preco();
		}

		BeanUtils.copyProperties(objDto, preco);
		preco.setDataIntegracao(new Date());
		preco.setStatusIntegracao(StatusIntegracao.INTEGRADO);
		
		if(preco.getHoraValidade() == "18::0") {
			preco.setHoraValidade("18:00");
		}

		return precoRep.save(preco);
	}
	
	/*
	public void horaValidadeCorrecao() {
		for(Preco precos: precoRep.findByHoraValidade("18::0")) {
			System.out.println(precos.getId() + " " + precos.getHoraValidade());
			precos.setHoraValidade("18:00");
			precos = precoRep.save(precos);
			System.out.println("Hora corrigida: " + precos.getHoraValidade());
		}
	}*/
	
	public Page<PrecoDto> buscar(Long idEstabelecimento, PrecoFilter filter, Pageable pageable) {
		Estabelecimento estab = estabelecimentoRep.getReferenceById(idEstabelecimento);
		
		Page<Preco> pages;
		
		if(estab.getMatriz() != null && estab.getMatriz()){
			pages = precoRep.findAll(pageable);
		}else {
			pages = precoRep.findByCodigoEstabelecimento(estab.getCodigo(), pageable);
		}
		
		List<PrecoDto> listaPrecos = new ArrayList<>();
		for(Preco preco: pages.getContent()) {
			PrecoDto precoDto = new PrecoDto();
			BeanUtils.copyProperties(preco, precoDto);			
			listaPrecos.add(precoDto);
		}		
		
		return new PageImpl<>(
				listaPrecos,
                pageable,
                pages.getTotalElements()
        );
	}
	
	
	public Page<PrecoDto> buscarRegional(PrecoFilter filter, Pageable pageable) {

		Page<Preco> pages = precoRep.findAll(
//				PrecoSpecs.doProduto(filter.getDescricao())
//						.or(PrecoSpecs.doProdutoCodItem(filter.getDescricao()))
//						.and(PrecoSpecs.doCodigoRegional(filter.getCodigoRegional()))
//						.and(PrecoSpecs.doCodigoEstabelecimento(filter.getCodigoEstabelecimento()))
//						.and(PrecoSpecs.doPrecoValido()),
				PrecoSpecs.doProdutoCodItem(filter.getDescricao())
				.and(PrecoSpecs.doCodigoEstabelecimento(filter.getCodigoEstabelecimento()))
				.and(PrecoSpecs.doPrecoValido()),
				pageable
		);
		
		List<PrecoDto> listaPrecos = PrecoDto.construir(pages.getContent());
		
		return new PageImpl<>(
				listaPrecos,
                pageable,
                pages.getTotalElements()
        );
	}

	public Page<PrecoDto> buscarPrecoPorEstabelecimentoEOuGrupoEOuProduto(PrecoFilter filter, Pageable pageable) {	
		Page<Preco> precos = precoRep.findAll(
				PrecoSpecs.doCodigoEstabelecimento(filter.getCodigoEstabelecimento())
				.and(PrecoSpecs.doGrupoProduto(filter.getIdGrupoProduto()))
				.and(PrecoSpecs.doProdutoCodItem(filter.getCodigoProduto())), pageable);	
		
		List<PrecoDto> tabelaPrecos = PrecoDto.construir(precos.getContent());
		
		if (!tabelaPrecos.isEmpty()) {
			Map<String, String> map = new HashedMap();
			for(PrecoDto item: tabelaPrecos) {
				if(map.containsKey(item.getCodigoProduto())) {
					item.setDescricaoProduto(map.get(item.getCodigoProduto()));
				}else {
					Produto produto = produtoRep.findByCodItem(item.getCodigoProduto());
					if(produto != null) {
						map.put(produto.getCodItem(), produto.getDescItem());
						item.setDescricaoProduto(produto.getDescItem());
					}
				}
				
			}
		}
		
		return new PageImpl<>(
				tabelaPrecos,
				pageable,
				precos.getTotalElements()
		);
	}
	
	public PrecoBuscaDto buscarPor(String codigoEstabelecimento, String codigoProduto, String codigoReferencia, Boolean precoCoco) {
		logger.info("Buscando preço valido por código estabelecimento, código produto, código referência e preço coco Sim/Não.");
		
		Preco preco = precoRep.findByPrecoValido(codigoEstabelecimento, codigoProduto, codigoReferencia).orElse(null);
		
		if(preco == null) {
			throw new ObjectNotFoundException("Não foi encontrado Preço valido com os parâmetros informados.");
		}
		
		return PrecoBuscaDto.construir(preco, precoCoco); 
	}
	
	public Preco findByIdUnico(String idUnico) {
		return precoRep.findByIdUnico(idUnico);
	}
	
	public void remove(Preco preco) {
		precoRep.delete(preco);
	}
}

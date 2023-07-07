package br.coop.integrada.api.pa.domain.service.produto;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ReferencaFilterDto;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoReferenciaRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import br.coop.integrada.api.pa.domain.spec.produtos.ReferenciaSpec;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class ProdutoReferenciaService {
	private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);	
	private final ProdutoReferenciaRep produtoReferenciaRep;
	private final ProdutoRep produtoRep;
	
	public ProdutoReferencia salvar(ProdutoReferencia produtoReferencia) {
		logger.info("Salvando nova referência de produto...");
		return produtoReferenciaRep.save(produtoReferencia);
	}
	
	public List<ProdutoReferencia> buscarPorProdutoReferencia(String referencia) {
		logger.info("Buscando referência produtos ativas...");
		Pageable limit = PageRequest.of(0, 500);
		List<ProdutoReferencia> produtoReferencia = new ArrayList<>();

		if(referencia != null && !referencia.isEmpty()) {
			produtoReferencia = produtoReferenciaRep.findByCodRefContainingIgnoreCaseAndDataInativacaoNullOrderByCodRefAsc(referencia, limit);
		}
		else {
			produtoReferencia = produtoReferenciaRep.findByDataInativacaoNull(limit).getContent();
		}

		return produtoReferencia;
	}

	public ProdutoReferencia buscarPorCodigoReferencia(String codRef) {
		
		if(codRef.isBlank())
			return null;
		
		ProdutoReferencia obj = produtoReferenciaRep.findByCodRefAndDataInativacaoNull(codRef.trim());

		if(obj == null) {
			throw new NullPointerException("O produto referência não foi encontrado! CodRef: " + codRef + ", Tipo: " + ProdutoReferencia.class.getName());
		}
		return obj;
	}

	public ProdutoReferencia buscarPorCodigoReferenciaSemValidacao(String codRef) {
		return produtoReferenciaRep.findByCodRefIgnoreCase(codRef);
	}
	
	public Page<ProdutoReferencia> buscarTodasProdutoReferencia(Pageable pageable, String filtro) {
		logger.info("Listando todas referência produtos...");
		return produtoReferenciaRep.findAll(pageable, filtro);
	}
	
	public ProdutoReferencia atualizar(ProdutoReferenciaDto objDto) {
		logger.info("Atualizando Produto Referência...");

		if(objDto.getId() == null) {
			throw new ObjectDefaultException("Necessário informar o id para atualizar a referência de produto!");
		}

		ProdutoReferencia produtoReferenciaAtual = produtoReferenciaRep.getReferenceById(objDto.getId());

		if(produtoReferenciaAtual == null) {
			throw new ObjectNotFoundException("Não foi encontrado referência de produto com o ID " + objDto.getId());
		}

		ProdutoReferencia produtoReferenciaNovo = new ProdutoReferencia();
		BeanUtils.copyProperties(produtoReferenciaAtual, produtoReferenciaNovo);
		BeanUtils.copyProperties(objDto, produtoReferenciaNovo);

		produtoReferenciaNovo.setDataAtualizacao(new Date());

		if(objDto.getAtivo() == false) {
			produtoReferenciaNovo.setDataInativacao(new Date());
		}
		else {
			produtoReferenciaNovo.setDataInativacao(null);
		}
		return salvar(produtoReferenciaNovo);
	}
	
	
	
	
	public ProdutoReferencia buscarProdutoReferenciaPorId(Long id) {
		logger.info("Buscando id referência produto...");

		ProdutoReferencia produtoReferencia = produtoReferenciaRep.findById(id).orElse(null);

		if(produtoReferencia == null) {
			throw new ObjectNotFoundException("Não foi encontrado referência de produto com o ID " + id);
		}

		return produtoReferencia;
	}
	
	public String ativarProdutoReferencia(String codRef) {
		ProdutoReferencia produtoReferencia  = produtoReferenciaRep.findByCodRefIgnoreCase(codRef);
		logger.info("Inativando referência de produto...");
		produtoReferencia.setDataInativacao(null);	
		produtoReferencia.setDataAtualizacao(new Date());
		produtoReferencia  = produtoReferenciaRep.save(produtoReferencia);
		if(produtoReferencia.getAtivo()) {
			return "Referência de produto ativada com sucesso!";
		}
		return "Não foi possivel ativar a Referência de produto:" + produtoReferencia.getCodRef();
	}	
	
	public ProdutoReferencia inativarProdutoReferencia(String codRef) {
		ProdutoReferencia produtoReferencia  = produtoReferenciaRep.findByCodRefIgnoreCase(codRef);
		List<Produto> produtos = produtoRep.getByprodutosReferencia(codRef);
		if(!produtos.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(Produto produto: produtos) {
				sb.append(" ( " +produto.getDescItem() + " ), ");
			}
			throw new ObjectNotFoundException("Referência vinculada ao produto: " + sb.toString() + " remova o vinculo antes de continuar." );
		}
		
		logger.info("Inativando referência de produto...");
		produtoReferencia.setDataInativacao(new Date());
		produtoReferencia.setDataAtualizacao(new Date());
		return produtoReferenciaRep.save(produtoReferencia);
	}
	
	public List<ReferencaFilterDto> findByProduto(Long id){
		List<ReferencaFilterDto> response = new ArrayList<>();
		for(ProdutoReferencia item: produtoReferenciaRep.findByProduto(id)){
			ReferencaFilterDto referencia = new ReferencaFilterDto();
			BeanUtils.copyProperties(item, referencia);
			response.add(referencia);
		}
		return response;
	}
	
	public Page<ProdutoReferencia> getReferencias(ProdutoReferenciaFilter filter, Pageable pageable) {		
		if(filter.getSituacao() == null) filter.setSituacao(Situacao.ATIVO);
		logger.info("Buscando refêrencia de produto: {}", filter.getSituacao());
		Page<ProdutoReferencia> produtoReferencia = produtoReferenciaRep.findAll(
				ReferenciaSpec.situacaoEquals(filter.getSituacao())
				.and(ReferenciaSpec.doCodRef(filter.getReferencia())), pageable);
		if(produtoReferencia.isEmpty()) {
			throw new ObjectNotFoundException("Não encontamos referências " + filter.getSituacao().toString().toLowerCase());
		}
		return produtoReferencia;
		
	}
}

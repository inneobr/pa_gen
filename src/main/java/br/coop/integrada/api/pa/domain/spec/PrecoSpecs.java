package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.model.produto.Produto;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.time.LocalDate;
import java.util.List;

public class PrecoSpecs {
	
	public static Specification<Preco> doProdutoCodItem(String codigoProduto){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codigoProduto)) {
				return builder.equal(builder.upper(root.get("codigoProduto")), codigoProduto.toUpperCase());
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<Preco> doGrupoProduto(Long idGrupoProduto){
		return (root, query, builder) -> {
            if(idGrupoProduto != null) { 
            	Subquery<Produto> queryProduto = query.subquery(Produto.class);
            	Root<Produto> rootProduto = queryProduto.from(Produto.class);
            	queryProduto.select(rootProduto.get("codItem"))
            		.where(
            			builder.equal(rootProduto.get("grupoProduto"), idGrupoProduto),
            			builder.isNull(rootProduto.get("dataInativacao"))
            		);
            	return builder.and(root.get("codigoProduto").in(queryProduto));
            }
            return builder.and(new Predicate[0]);
        };	
	}
	
	public static Specification<Preco> doCodigoEstabelecimento(String codigo){
        return (root, query, builder) -> {
        	if(StringUtils.hasText(codigo)) {
                return builder.equal(builder.upper(root.get("codigoEstabelecimento")), codigo.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<Preco> doReferencia(String codigoReferencia){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codigoReferencia)) {
				return builder.equal(builder.upper(root.get("codigoReferencia")), codigoReferencia.toUpperCase());
			}
			return builder.isNull(root.get("codigoReferencia"));
		};
	}
	
	public static Specification<Preco> doPrecoValido(){
        return (root, query, builder) -> {        	
        	return builder.greaterThanOrEqualTo(root.get("dataValidade"), LocalDate.now());
        };
	}

	public static Specification<Preco> doDataInativacaoIsNull() {
		return (root, query, builder) -> {
			return builder.isNull(root.get("dataInativacao"));
		};	
	}
	
}

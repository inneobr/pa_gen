package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ProdutoSpecs {
	
	public static Specification<Produto> codigoOuDescricaoLike(String codigoOuDescricao){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoOuDescricao)) {
            	Predicate predicateFieldCodigo = builder.like(builder.upper(root.get("codItem")), "%" + codigoOuDescricao.toUpperCase() + "%");
            	Predicate predicateFieldDescricao = builder.like(builder.upper(root.get("descItem")), "%" + codigoOuDescricao.toUpperCase() + "%");
                return builder.or(predicateFieldCodigo, predicateFieldDescricao);
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produto> codigoGrupoProdutoEquals(String codigoGrupoProduto){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoGrupoProduto)) {
                query.distinct(true);
                Join<GrupoProduto, Produto> grupoProduto = root.join("grupoProduto");
                return builder.equal(
                		builder.upper(grupoProduto.get("fmCodigo")),
                		codigoGrupoProduto.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produto> doGrupo(Long idGrupo){
        return (root, query, builder) -> {
            if(idGrupo != null) {
                query.distinct(true);
                Join<GrupoProduto, Produto> grupoProduto = root.join("grupoProduto");
                return builder.equal(grupoProduto.get("id"), idGrupo);
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produto> situacaoEquals(Situacao situacao){
        return (root, query, builder) -> {
            if(situacao != null) {
                if(situacao.equals(Situacao.ATIVO)) {
                    return builder.isNull(root.get("dataInativacao"));
                }
                else if(situacao.equals(Situacao.INATIVO)) {
                    return builder.isNotNull(root.get("dataInativacao"));
                }
            }
            return builder.and(new Predicate[0]);
        };
    }
}

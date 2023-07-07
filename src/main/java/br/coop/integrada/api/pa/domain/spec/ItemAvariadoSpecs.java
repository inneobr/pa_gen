package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public class ItemAvariadoSpecs {

    public static Specification<ItemAvariado> doDescricao(String descricao){
        return (root, query, builder) -> {
            if(StringUtils.hasText(descricao)) {
                return builder.like(
                        builder.upper(root.get("descricao")),
                        "%" + descricao.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ItemAvariado> doIdGrupoProduto(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<ItemAvariadoDetalhe, ItemAvariado> detalhes = root.join("detalhes");
                Join<Produto, ItemAvariadoDetalhe> produto = detalhes.join("produto");
                Join<GrupoProduto, ItemAvariadoDetalhe> grupoProduto = produto.join("grupoProduto");
                return builder.equal(grupoProduto.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ItemAvariado> doIdProduto(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<ItemAvariadoDetalhe, ItemAvariado> detalhes = root.join("detalhes");
                Join<Produto, ItemAvariadoDetalhe> produto = detalhes.join("produto");
                return builder.equal(produto.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ItemAvariado> doIdEstabelecimento(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<ItemAvariadoEstabelecimento, ItemAvariado> itemAvariadoEstabelecimentos = root.join("estabelecimentos");
                Join<Estabelecimento, ItemAvariadoEstabelecimento> estabelecimentos = itemAvariadoEstabelecimentos.join("estabelecimento");
                return builder.equal(estabelecimentos.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ItemAvariado> doSituacao(Situacao situacao){
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

	public static Specification<ItemAvariado> doCodigoGrupoProduto(String codigoGrupoProduto) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoGrupoProduto)) {
            	query.distinct(true);
                Join<ItemAvariadoDetalhe, ItemAvariado> detalhe = root.join("detalhes");
                Join<Produto, ItemAvariadoDetalhe> produto = detalhe.join("produto");
                Join<GrupoProduto, Produto> grupoProduto = produto.join("grupoProduto");
                
                return builder.equal(
                		builder.upper(grupoProduto.get("fmCodigo")),
                		codigoGrupoProduto.toUpperCase()
            		);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ItemAvariado> doCodigoEstabelecimento(String codigoEstabelecimento) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
            	query.distinct(true);
                Join<ItemAvariadoEstabelecimento, ItemAvariado> itemAvariadoEstabelecimento = root.join("estabelecimentos");
                Join<Estabelecimento, ItemAvariadoEstabelecimento> estabelecimento = itemAvariadoEstabelecimento.join("estabelecimento");
                
                return builder.equal(
                		builder.upper(estabelecimento.get("codigo")),
                		codigoEstabelecimento.toUpperCase()
            		);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ItemAvariado> doCampoValidacao(ItemAvariadoValidacaoEnum campoValidacao) {
        return (root, query, builder) -> {
            if(campoValidacao != null) {
            	query.distinct(true);
                
                return builder.equal(
                		root.get("campoValidacao"),
                		campoValidacao
            		);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ItemAvariado> doPercentualEntreInicialEFinal(BigDecimal percentual) {
        return (root, query, builder) -> {
        	if(percentual != null) {
        		query.distinct(true);
        		Join<ItemAvariadoDetalhe, ItemAvariado> detalhe = root.join("detalhes");
        		return builder.and(
        				builder.greaterThanOrEqualTo(detalhe.get("percentualFinal"), percentual),
        				builder.lessThanOrEqualTo(detalhe.get("percentualInicial"), percentual)
        				);
        	}
            return builder.and(new Predicate[0]);
        };
	}
}

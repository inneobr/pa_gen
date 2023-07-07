package br.coop.integrada.api.pa.domain.spec;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ItemAvariadoDetalheSpecs {

	public static Specification<ItemAvariadoDetalhe> codigoGrupoProdutoEquals(String codigoGrupoProduto) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoGrupoProduto)) {
            	query.distinct(true);
                Join<Produto, ItemAvariadoDetalhe> produto = root.join("produto");
                Join<GrupoProduto, Produto> grupoProduto = produto.join("grupoProduto");
                
                return builder.equal(
                		builder.upper(grupoProduto.get("fmCodigo")),
                		codigoGrupoProduto.toUpperCase()
            		);
            }
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> codigoEstabelecimentoEquals(String codigoEstabelecimento) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
            	query.distinct(true);
                Join<ItemAvariado, ItemAvariadoDetalhe> itemAvariado = root.join("itemAvariado");
                Join<ItemAvariadoEstabelecimento, ItemAvariado> itemAvariadoEstabelecimento = itemAvariado.join("estabelecimentos");
                Join<Estabelecimento, ItemAvariado> estabelecimento = itemAvariadoEstabelecimento.join("estabelecimento");
                
                return builder.equal(
                		builder.upper(estabelecimento.get("codigo")),
                		codigoEstabelecimento.toUpperCase()
            		);
            }
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> campoValidacaoEquals(ItemAvariadoValidacaoEnum campoValidacao) {
        return (root, query, builder) -> {
            if(campoValidacao != null) {
            	query.distinct(true);
                Join<ItemAvariado, ItemAvariadoDetalhe> itemAvariado = root.join("itemAvariado");
                
                return builder.equal(
                		itemAvariado.get("campoValidacao"),
                		campoValidacao
            		);
            }
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> percentualRange(BigDecimal percentual) {
        return (root, query, builder) -> {
        	if(percentual != null) {
        		return builder.and(
        				builder.greaterThanOrEqualTo(builder.literal(percentual), root.get("percentualFinal")),
        				builder.lessThanOrEqualTo(builder.literal(percentual), root.get("percentualInicial"))
        				);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> phRange(BigInteger ph) {
        return (root, query, builder) -> {
        	if(ph != null) {
        		return builder.and(
        				builder.greaterThanOrEqualTo(builder.literal(ph), root.get("phInicial")),
        				builder.lessThanOrEqualTo(builder.literal(ph), root.get("phFinal"))
        				);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> fntInicialLessThanOrEqualTo(BigDecimal fnt) {
        return (root, query, builder) -> {
        	if(fnt != null) {
        		return builder.lessThanOrEqualTo(root.get("fntInicial"), fnt);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> idItemAvariadoEquals(Long itemAvariadoId) {
        return (root, query, builder) -> {
        	if(itemAvariadoId != null) {
                Join<ItemAvariado, ItemAvariadoDetalhe> itemAvariado = root.join("itemAvariado");
        		return builder.equal(itemAvariado.get("id"), itemAvariadoId);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> codigoProdutoEquals(String codigoProduto) {
        return (root, query, builder) -> {
        	if(codigoProduto != null) {
                Join<Produto, ItemAvariadoDetalhe> produto = root.join("produto");
        		return builder.equal(produto.get("codItem"), codigoProduto);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> percentualInicialEquals(BigDecimal percentualInicial) {
        return (root, query, builder) -> {
        	if(percentualInicial != null) {
        		return builder.equal(root.get("percentualInicial"), percentualInicial);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> percentualFinalEquals(BigDecimal percentualFinal) {
        return (root, query, builder) -> {
        	if(percentualFinal != null) {
        		return builder.equal(root.get("percentualFinal"), percentualFinal);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> phInicialEquals(BigDecimal phInicial) {
        return (root, query, builder) -> {
        	if(phInicial != null) {
        		return builder.equal(root.get("phInicial"), phInicial);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> phFinalEquals(BigDecimal phFinal) {
        return (root, query, builder) -> {
        	if(phFinal != null) {
        		return builder.equal(root.get("phFinal"), phFinal);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> fntInicialEquals(BigDecimal fntInicial) {
        return (root, query, builder) -> {
        	if(fntInicial != null) {
        		return builder.equal(root.get("fntInicial"), fntInicial);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> fntFinalEquals(BigDecimal fntFinal) {
        return (root, query, builder) -> {
        	if(fntFinal != null) {
        		return builder.equal(root.get("fntFinal"), fntFinal);
        	}
            return null;
        };
	}

	public static Specification<ItemAvariadoDetalhe> situacaoEquals(Situacao situacao){
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

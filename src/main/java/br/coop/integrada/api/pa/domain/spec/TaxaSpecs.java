package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public class TaxaSpecs {

	public static Specification<Taxa> doDescricao(String descricao){
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
	
    public static Specification<Taxa> doSafra(Integer safra){
        return (root, query, builder) -> {
            if(safra != null) {
                return builder.equal(root.get("safra"), safra);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Taxa> doIdGrupoProduto(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<TaxaGrupoProduto, Taxa> taxaGrupoProdutos = root.join("grupoProdutos");
                Join<GrupoProduto, TaxaGrupoProduto> grupoProduto = taxaGrupoProdutos.join("grupoProduto");
                return builder.equal(grupoProduto.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Taxa> doIdEstabelecimento(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<TaxaEstabelecimento, Taxa> taxaEstabelecimentos = root.join("estabelecimentos");
                Join<Estabelecimento, TaxaEstabelecimento> estabelecimento = taxaEstabelecimentos.join("estabelecimento");
                return builder.equal(estabelecimento.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Taxa> doSituacao(Situacao situacao){
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

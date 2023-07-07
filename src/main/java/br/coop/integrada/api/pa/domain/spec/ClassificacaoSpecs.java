package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public class ClassificacaoSpecs {

    public static Specification<Classificacao> doDescricao(String descricao){
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

    public static Specification<Classificacao> doSafra(Integer safra){
        return (root, query, builder) -> {
            if(safra != null) {
                query.distinct(true);
                Join<ClassificacaoSafra, Classificacao> safras = root.join("safras");
                return builder.equal(safras.get("safra"), safra);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Classificacao> doIdTipoClassificacao(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<TipoClassificacao, Classificacao> tipoClassificacao = root.join("tipoClassificacao");
                return builder.equal(tipoClassificacao.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Classificacao> doIdGrupoProduto(Long id){
        return (root, query, builder) -> {
            if(id != null) {
                query.distinct(true);
                Join<ClassificacaoGrupoProduto, Classificacao> classificacaoGrupoProdutos = root.join("grupoProdutos");
                Join<GrupoProduto, ClassificacaoGrupoProduto> grupoProdutos = classificacaoGrupoProdutos.join("grupoProduto");
                return builder.equal(grupoProdutos.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<Classificacao> doSituacao(Situacao situacao){
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

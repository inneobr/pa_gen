package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraCompostaEstabelecimento;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public class SafraCompostaSpecs {

    public static Specification<SafraComposta> doNomeSafra(String nomeSafra){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nomeSafra)) {
                return builder.like(
                        builder.upper(root.get("nomeSafra")),
                        "%" + nomeSafra.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<SafraComposta> doIdEstabelecimento(Long idEstabelecimento){
        return (root, query, builder) -> {
            if(idEstabelecimento != null) {
                query.distinct(true);
                Join<SafraCompostaEstabelecimento, SafraComposta> safrasCompostaEstabelecimentos = root.join("estabelecimentos");
                Join<Estabelecimento, SafraCompostaEstabelecimento> estabelecimentos = safrasCompostaEstabelecimentos.join("estabelecimento");
                return builder.equal(estabelecimentos.get("id"), idEstabelecimento);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<SafraComposta> doSituacao(Situacao situacao){
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

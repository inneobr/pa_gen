package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ParticipanteBayerSpecs {

    public static Specification<ParticipanteBayer> doCnpj(String cnpj){
        return (root, query, builder) -> {
            if(StringUtils.hasText(cnpj)) {
                return builder.like(
                        builder.upper(root.get("cnpj")),
                        "%" + cnpj.toUpperCase() + "%"
                        );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ParticipanteBayer> doNome(String nome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nome)) {
                return builder.like(
                        builder.upper(root.get("nome")),
                        "%" + nome.toUpperCase() + "%"
                        );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ParticipanteBayer> doSituacao(Situacao situacao){
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

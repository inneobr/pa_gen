package br.coop.integrada.api.pa.domain.spec;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;

public class UnidadeFederacaoSpecs {

    public static Specification<UnidadeFederacao> doEstado(String estado){
        return (root, query, builder) -> {
            if(StringUtils.hasText(estado)) {
                return builder.like(
                        builder.upper(root.get("estado")),
                        "%" + estado.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<UnidadeFederacao> doEstadoNome(String estadoNome){
        return (root, query, builder) -> {
            if(estadoNome != null) {
                return builder.like(
                    builder.upper(root.get("estadoNome")),
                    "%" + estadoNome.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<UnidadeFederacao> doCodigoIbge(String codigoIbge){
        return (root, query, builder) -> {
            if(codigoIbge != null) {
                return builder.like(
                        builder.upper(root.get("codigoIbge")),
                        "%" + codigoIbge.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<UnidadeFederacao> doSituacao(Situacao situacao){
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

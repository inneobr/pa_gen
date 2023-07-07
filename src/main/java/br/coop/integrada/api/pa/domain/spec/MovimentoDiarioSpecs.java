package br.coop.integrada.api.pa.domain.spec;

import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class MovimentoDiarioSpecs {
	
	 public static Specification<MovimentoDiario> codEstabelEquals(String codEstabel){
	        return (root, query, builder) -> {
	            if(StringUtils.hasText(codEstabel)) {
	                return builder.equal(builder.upper(root.get("codEstabel")), codEstabel);
	            }
	            return builder.and(new Predicate[0]);
	        };
	    }

	public static Specification<MovimentoDiario> dtMovtoBetween(Date dataInicial, Date dataFinal) {
        return (root, query, builder) -> {
            if(dataInicial != null && dataFinal != null) {
                return builder.between(root.get("dtMovto"), dataInicial, dataFinal);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<MovimentoDiario> dtMovtoEquals(Date dtMovto) {
        return (root, query, builder) -> {
            if(dtMovto != null) {
                return builder.equal(root.get("dtMovto"), dtMovto);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<MovimentoDiario> movtoFechEquals(Boolean movtoFech) {
        return (root, query, builder) -> {
            if(movtoFech != null) {
                return builder.equal(root.get("movtoFech"), movtoFech);
            }
            return builder.and(new Predicate[0]);
        };
	}

    public static Specification<MovimentoDiario> situacaoEquals(Situacao situacao){
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

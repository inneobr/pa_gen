package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;


public class TipoClassificacaoSpecs {
	
	
	public static Specification<TipoClassificacao> doTipoClassificacao(String tipo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(tipo)) {
                return builder.like(
                        builder.upper(root.get("tipoClassificacao")), "%" + tipo.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }

	public static Specification<TipoClassificacao> doSituacao(Situacao situacao){
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

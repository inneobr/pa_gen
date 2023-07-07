package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ProdutoReferenciaSpecs {
	
	public static Specification<ProdutoReferencia> doCodRef(String codRef){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codRef)) {
                return builder.like(
                        builder.upper(root.get("codRef")),
                        "%" + codRef.toUpperCase() + "%"
                );
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

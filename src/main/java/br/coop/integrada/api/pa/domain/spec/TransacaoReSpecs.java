package br.coop.integrada.api.pa.domain.spec;


import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;


public class TransacaoReSpecs {
	
	public static Specification<TransacaoRe> doEstabelecimento(String codEstabelecimento){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codEstabelecimento)) {
                return builder.equal(
                        builder.upper(root.get("codEstabel")),
                        codEstabelecimento.toUpperCase()
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	

	
}

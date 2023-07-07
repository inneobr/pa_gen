package br.coop.integrada.api.pa.domain.spec;


import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;

public class NaturezaTriburatiaSpecs {
	
	public static Specification<NaturezaTributaria> doCodigoDescricao(String pesquisa) {
		return (root, query, builder) -> {
            if(pesquisa != null) {            	
            	if(pesquisa.matches("^\\d+$")) {
            		return builder.equal(root.get("codigo"), Integer.parseInt(pesquisa));
            	}else { 
            		return builder.like(builder.upper(root.get("descricao")), "%" + pesquisa.toUpperCase() + "%" );
            	}
            }
            return builder.and(new Predicate[0]);
        };
	}
}

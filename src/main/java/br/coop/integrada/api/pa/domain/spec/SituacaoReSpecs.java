package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;


public class SituacaoReSpecs {
		
	public static Specification<SituacaoRe> codigoOuDescricaoLike(String codigoOuDescricao){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoOuDescricao)) {
            	Long codigo = null;
            	
            	if(codigoOuDescricao.matches("^\\d+$")) {
            		codigo = Long.parseLong(codigoOuDescricao);
            	}
            	
            	Predicate predicateFieldCodigo = builder.equal(root.get("codigo"), codigo);
            	Predicate predicateFieldDescricao = builder.like(builder.upper(root.get("descricao")), "%" + codigoOuDescricao.toUpperCase() + "%");
            	
            	return builder.or(predicateFieldCodigo, predicateFieldDescricao);
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<SituacaoRe> doSituacao(Situacao situacao){
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

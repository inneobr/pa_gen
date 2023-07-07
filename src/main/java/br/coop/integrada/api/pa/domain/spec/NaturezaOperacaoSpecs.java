package br.coop.integrada.api.pa.domain.spec;


import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class NaturezaOperacaoSpecs {
	
	public static Specification<NaturezaOperacao> doDescricao(String descricao){
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

	public static Specification<NaturezaOperacao> doCodGrupo(String codGrupo){
        return (root, query, builder) -> {
            if(codGrupo != null) {
            	try {
            		int codGrupoInt = Integer.parseInt(codGrupo);
            		return builder.equal(root.get("codGrupo"), codGrupoInt);
            	}
            	catch (NumberFormatException e) {
            		return null;
				}
                
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	
	public static Specification<NaturezaOperacao> doFilter(String filtro){
		return (root, query, builder) -> {
			if(filtro != null) {
				Predicate predicateFielddescricao = builder.like(builder.upper(root.get("descricao")), "%"+ filtro.toUpperCase() +"%");
				return builder.or(predicateFielddescricao);
			}
			return builder.and(new Predicate[0]);			
		};
	}
    

    
    public static Specification<NaturezaOperacao> doSituacao(Situacao situacao){
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

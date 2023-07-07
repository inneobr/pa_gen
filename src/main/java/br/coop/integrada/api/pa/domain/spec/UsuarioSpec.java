package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class UsuarioSpec {
	
	public static Specification<Usuario> doCodUsuario(String codUsuario){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codUsuario)) {
                return builder.like(
                        builder.upper(root.get("codUsuario")), "%" + codUsuario.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	 public static Specification<Usuario> doFilter(String filtro){
		return (root, query, builder) -> {
			if(filtro != null) {		
				Predicate predicateFieldNome = builder.like(builder.upper(root.get("nome")), "%"+ filtro.toUpperCase() +"%");
				Predicate predicateFieldUsername = builder.like(builder.upper(root.get("username")), "%"+ filtro.toUpperCase() +"%");
				Predicate predicateFieldMatricula = builder.like(builder.upper(root.get("matricula")), "%"+ filtro.toUpperCase() +"%");
				
				return builder.or(predicateFieldNome, predicateFieldUsername,  predicateFieldMatricula);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	 
	 public static Specification<Usuario> daSituacao(Situacao situacao){
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

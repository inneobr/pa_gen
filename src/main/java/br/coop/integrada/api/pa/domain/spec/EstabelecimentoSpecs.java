package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;


public class EstabelecimentoSpecs {
	
	
	public static Specification<Estabelecimento> doCodigo(String codigo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigo)) {
                return builder.like(
                        builder.upper(root.get("codigo")), "%" + codigo.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doCodigoRegional(String codigoRegional){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoRegional)) {
                return builder.like(
                        builder.upper(root.get("codigoRegional")), "%" + codigoRegional.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> isRegional(){
        return (root, query, builder) -> {
        	return builder.equal(root.get("codigo"), root.get("codigoRegional"));
        };
    }
	
	
	public static Specification<Estabelecimento> doRazaoSocial(String razaoSocial){
        return (root, query, builder) -> {
            if(StringUtils.hasText(razaoSocial)) {
                return builder.like(
                        builder.upper(root.get("razaoSocial")), "%" + razaoSocial.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	
	public static Specification<Estabelecimento> doNomeFantasia(String nomeFantasia){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nomeFantasia)) {
                return builder.like(
                        builder.upper(root.get("nomeFantasia")), "%" + nomeFantasia.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doEmail(String email){
        return (root, query, builder) -> {
            if(StringUtils.hasText(email)) {
                return builder.like(
                        builder.upper(root.get("email")), "%" + email.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doTelefone(String telefone){
        return (root, query, builder) -> {
            if(StringUtils.hasText(telefone)) {
                return builder.like(
                        builder.upper(root.get("telefone")), "%" + telefone.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doCnpj(String cnpj){
        return (root, query, builder) -> {
            if(StringUtils.hasText(cnpj)) {
                return builder.like(
                        builder.upper(root.get("cnpj")), "%" + cnpj.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doCidade(String cidade){
        return (root, query, builder) -> {
            if(StringUtils.hasText(cidade)) {
                return builder.like(
                        builder.upper(root.get("cidade")), "%" + cidade.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Estabelecimento> doEstado(String estado){
        return (root, query, builder) -> {
            if(StringUtils.hasText(estado)) {
                return builder.like(
                        builder.upper(root.get("estado")), "%" + estado.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	
	
	public static Specification<Estabelecimento> doSituacao(Situacao situacao){
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

	public static Specification<Estabelecimento> codigoOuNomeFantasiaLike(String codigoOuNomeFantasia) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoOuNomeFantasia)) {
            	Predicate predicateFieldCodigo = builder.equal(builder.upper(root.get("codigo")), codigoOuNomeFantasia.toUpperCase());
            	Predicate predicateFieldNomeFantasia = builder.like(builder.upper(root.get("nomeFantasia")), "%" + codigoOuNomeFantasia.toUpperCase() + "%");
                return builder.or(predicateFieldCodigo, predicateFieldNomeFantasia);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<Estabelecimento> usernameAtivoEquals(String username) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(username)) {
            	query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Estabelecimento> parametrosUsuarios = root.join("parametrosUsuarios");
                Join<Usuario, ParametrosUsuarioEstabelecimento> usuario = parametrosUsuarios.join("usuario");
                
                Predicate predicateFieldUsername = builder.equal(builder.upper(usuario.get("username")), username.toUpperCase()); 
                Predicate predicateUsuarioAtivo = builder.isNull(usuario.get("dataInativacao"));
            	
                return builder.and(predicateFieldUsername, predicateUsuarioAtivo);
            }
            return null;
        };
	}

	public static Specification<Estabelecimento> siloEquals(Boolean silo) {
        return (root, query, builder) -> {
            if(silo != null) {
            	query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> parametrosEstabelecimento = root.join("parametrosEstabelecimento");
                
                Predicate predicateFieldSilo = builder.equal(parametrosEstabelecimento.get("logSilo"), silo); 
                Predicate predicateParametroEstabelecimentoAtivo = builder.isNull(parametrosEstabelecimento.get("dataInativacao"));
            	
                return builder.and(predicateFieldSilo, predicateParametroEstabelecimentoAtivo);
            }
            return null;
        };
	}
	
	public static Specification<Estabelecimento> ubsEquals(Boolean logUbs) {
        return (root, query, builder) -> {
            if(logUbs != null) {
            	query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> parametrosEstabelecimento = root.join("parametrosEstabelecimento");
                
                Predicate predicateFieldSilo = builder.equal(parametrosEstabelecimento.get("logUbs"), logUbs); 
                Predicate predicateParametroEstabelecimentoAtivo = builder.isNull(parametrosEstabelecimento.get("dataInativacao"));
            	
                return builder.and(predicateFieldSilo, predicateParametroEstabelecimentoAtivo);
            }
            return null;
        };
	}
	
	public static Specification<Estabelecimento> reEquals(Boolean re) {
        return (root, query, builder) -> {
            if(re != null) {
            	query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Estabelecimento> parametrosUsuario = root.join("parametrosUsuarios");
                
                Predicate predicateFieldRe = builder.equal(parametrosUsuario.get("re"), re); 
                Predicate predicateParametroUsuarioAtivo = builder.isNull(parametrosUsuario.get("dataInativacao"));
            	
                return builder.and(predicateFieldRe, predicateParametroUsuarioAtivo);
            }
            return null;
        };
	}

	public static Specification<Estabelecimento> hierarquiaEstabelecimento(String codigoEstabelecimento, String codigoRegional) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
            	query.distinct(true);
            	
            	// RETORNA TODOS OS ESTABELECIMENTO
            	Subquery<Estabelecimento> queryEstabelecimentoMatriz = query.subquery(Estabelecimento.class);
            	Root<Estabelecimento> rootEstabelecimentoMatriz = queryEstabelecimentoMatriz.from(Estabelecimento.class);
            	queryEstabelecimentoMatriz.select(rootEstabelecimentoMatriz.get("matriz"))
            		.where(
            			builder.equal(rootEstabelecimentoMatriz.get("codigo"), codigoEstabelecimento),
            			builder.equal(rootEstabelecimentoMatriz.get("codigoRegional"), codigoRegional),
            			builder.equal(rootEstabelecimentoMatriz.get("matriz"), true),
            			builder.isNull(rootEstabelecimentoMatriz.get("dataInativacao"))
            		);
            	
            	Predicate predicateIsMatriz = builder.equal(queryEstabelecimentoMatriz, true);
            	
            	// RETORNA TODOS OS ESTABELECIMENTO REFERENTE A REGIONAL
            	Predicate predicateEstabelecimentos = builder.equal(root.get("codigoRegional"), codigoRegional);
            	
                return builder.or(predicateIsMatriz, predicateEstabelecimentos);
            }
            return null;
        };
	}
	
	public static Specification<Estabelecimento> parametroSiloEquals(Boolean silo) {
		 return (root, query, builder) -> {
	            if(silo != null) {
	            	query.distinct(true);
	            	Join<ParametroEstabelecimento, Estabelecimento> parametroEstabelecimento = root.join("parametrosEstabelecimento");
	            	 return builder.equal(parametroEstabelecimento.get("logSilo"), silo);
	            }
	            return null;
	     };
	}
}

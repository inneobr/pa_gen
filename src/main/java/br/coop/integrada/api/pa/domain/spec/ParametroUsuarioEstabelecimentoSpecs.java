package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ParametroUsuarioEstabelecimentoSpecs {

	public static Specification<ParametrosUsuarioEstabelecimento> usernameEquals(String username) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(username)) {
                query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Usuario> usuario = root.join("usuario");
                return builder.equal(builder.upper(usuario.get("username")), username.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<ParametrosUsuarioEstabelecimento> doCodUsuarioEquals(String codUsuario) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codUsuario)) {
                query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Usuario> usuario = root.join("usuario");
                return builder.equal(builder.upper(usuario.get("codUsuario")), codUsuario.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<ParametrosUsuarioEstabelecimento> codigoOuNomeFantasiaEstabelecimentoLike(String codigoOuNomeFantasia) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigoOuNomeFantasia)) {
                query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                
                Predicate predicateFieldCodigo = builder.like(builder.upper(estabelecimento.get("codigo")), "%" + codigoOuNomeFantasia.toUpperCase() + "%");
                Predicate predicateFieldNomeFantasia = builder.like(builder.upper(estabelecimento.get("nomeFantasia")), "%" + codigoOuNomeFantasia.toUpperCase() + "%");
                
                return builder.or(predicateFieldCodigo, predicateFieldNomeFantasia);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ParametrosUsuarioEstabelecimento> codigoEstabelecimentoEquals(String codigoEstabelecimento) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
                query.distinct(true);
                Join<ParametrosUsuarioEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                return builder.equal(builder.upper(estabelecimento.get("codigo")), codigoEstabelecimento.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<ParametrosUsuarioEstabelecimento> reEquals(Boolean re) {
		return (root, query, builder) -> {
            if(re != null) {
                return builder.equal(root.get("re"), re);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ParametrosUsuarioEstabelecimento> doSituacao(Situacao situacao) {
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

package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ProdutorSpecs {
	
	public static Specification<Produtor> doNome(String nome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nome)) {
                return builder.like(
                        builder.upper(root.get("nome")),
                        "%" + nome.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produtor> doCodigo(String codigo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigo)) {
                return builder.equal(
                        builder.upper(root.get("codProdutor")),
                        codigo
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produtor> codProdutorLike(String codProdutor){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codProdutor)) {
                return builder.like(
                        builder.upper(root.get("codProdutor")),
                        codProdutor.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produtor> doCpfCnpj(String cpfCnpj){
        return (root, query, builder) -> {
            if(StringUtils.hasText(cpfCnpj)) {
                return builder.like(
                        builder.upper(root.get("cpfCnpj")),
                        "%" + cpfCnpj + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Produtor> doCodigoOuNomeOuCpfCnpj(String descricao){
		return (root, query, builder) -> {
			if(StringUtils.hasText(descricao)) {
				Predicate predicateFieldNome = builder.like(builder.upper(root.get("nome")), "%" + descricao.toUpperCase() + "%");
				Predicate predicateFieldCodigo = builder.like(builder.upper(root.get("codProdutor")), descricao.toUpperCase() + "%");
				Predicate predicateFieldCpfCnpj = builder.like(builder.upper(root.get("cpfCnpj")), descricao.toUpperCase());
				return builder.or(predicateFieldNome, predicateFieldCodigo, predicateFieldCpfCnpj);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<Produtor> emiteNotaEquals(Boolean emiteNota) {
        return (root, query, builder) -> {
            if(emiteNota != null) {
                return builder.equal(root.get("emiteNota"), emiteNota);
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<Produtor> doSituacao(Situacao situacao){
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

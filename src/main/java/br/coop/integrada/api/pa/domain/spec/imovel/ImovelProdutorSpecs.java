package br.coop.integrada.api.pa.domain.spec.imovel;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ImovelProdutorSpecs {

    public static Specification<ImovelProdutor> imovelMatriculaOuNomeLike(String matriculaOuNome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(matriculaOuNome)) {
            	Long matricula = null;
            	
            	if(matriculaOuNome.matches("^\\d+$")) {
            		matricula = Long.parseLong(matriculaOuNome);
            	}
            	
            	query.distinct(true);
                Join<Imovel, ImovelProdutor> imovel = root.join("imovel");
            	Predicate predicateFieldMatricula = builder.equal(imovel.get("matricula"), matricula);
            	Predicate predicateFieldDescricao = builder.like(builder.upper(imovel.get("nome")), "%" + matriculaOuNome.toUpperCase() + "%");
            	
            	return builder.or(predicateFieldMatricula, predicateFieldDescricao);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<ImovelProdutor>codProdutorEquals(String codProdutor){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codProdutor)) {
            	return builder.equal(root.get("codProdutor"), codProdutor);
            }
            return builder.and(new Predicate[0]);
        };
    }
    
    public static Specification<ImovelProdutor> doNome(String nome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nome)) {
            	return builder.equal(root.get("nome"), nome);
            }
            return builder.and(new Predicate[0]);
        };
    }

	public static Specification<ImovelProdutor> matriculaEquals(Long matricula) {
		return (root, query, builder) -> {
            if(matricula != null) {
            	query.distinct(true);
                Join<Imovel, ImovelProdutor> imovel = root.join("imovel");
            	return builder.equal(imovel.get("matricula"), matricula);
            }
            return builder.and(new Predicate[0]);
        };
	}

    public static Specification<ImovelProdutor> daSituacao(Situacao situacao){
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

	public static Specification<ImovelProdutor> transferenciaEquals(Boolean transferencia) {
        return (root, query, builder) -> {
            if(transferencia != null) {
            	return builder.equal(root.get("transferencia"), transferencia);
            }
            return builder.and(new Predicate[0]);
        };
	}
}

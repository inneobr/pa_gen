package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ImovelSpecs {	
	
	public static Specification<Imovel> daMatriculaOuNomeLike(String matriculaOuNome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(matriculaOuNome)) {
            	Long matricula = null;
            	
            	if(matriculaOuNome.matches("^\\d+$")) {
            		matricula = Long.parseLong(matriculaOuNome);
            	}  
            	
            	Predicate predicateFieldMatricula = builder.equal(root.get("matricula"), matricula);
            	Predicate predicateFieldNome = builder.like(builder.upper(root.get("nome")), "%" + matriculaOuNome.toUpperCase() + "%");            	
            	return builder.or(predicateFieldMatricula, predicateFieldNome);
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<Imovel> doCodProdutor(String codProdutor){
		return (root, query, builder) -> {
            if(StringUtils.hasText(codProdutor)) {          	
            	
            	query.distinct(true);
            	Join<ImovelProdutor, Imovel> imovelProdutor = root.join("imoveisProdutores");
            	return builder.equal(builder.upper(imovelProdutor.get("codProdutor")), codProdutor.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };		
	}
	
	public static Specification<Imovel> doStatus(Situacao situacao){
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

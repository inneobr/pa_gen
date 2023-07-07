package br.coop.integrada.api.pa.domain.spec.campoSemente;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class CampoProdutorSpec {
	
	public static Specification<SementeCampoProdutor> daSafra(Integer safra){
	        return (root, query, builder) -> {
	            if(safra != null) {
	                return builder.equal(root.get("safra"), safra
	            );
	        }
	        return builder.and(new Predicate[0]);
	    };
	}
	
	public static Specification<SementeCampoProdutor> daOrdemCampo(Integer ordemCampo){
	        return (root, query, builder) -> {
	            if(ordemCampo != null) {
	                return builder.equal(root.get("ordemCampo"), ordemCampo
	            );
	        }
	        return builder.and(new Predicate[0]);
	    };
	}
	
	public static Specification<SementeCampoProdutor> doEstabelecimento(String codigo){
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigo)) {          	
            	
            	query.distinct(true);
            	Join<SementeCampoProdutor, Estabelecimento> estabelecimento = root.join("estabelecimento");
            	return builder.equal(builder.upper(estabelecimento.get("codigo")), codigo.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };		
	}
	
	public static Specification<SementeCampoProdutor> doGrupo(String fmCodigo){
		return (root, query, builder) -> {
            if(StringUtils.hasText(fmCodigo)) {          	
            	
            	query.distinct(true);
            	Join<SementeCampoProdutor, GrupoProduto> grupoProduto = root.join("grupoProduto");
            	return builder.equal(builder.upper(grupoProduto.get("fmCodigo")), fmCodigo.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };		
	}
	
	public static Specification<SementeCampoProdutor> daClasse(Long codClasse){
		return (root, query, builder) -> {
            if(codClasse != null) {          	
            	
            	query.distinct(true);
            	Join<SementeCampoProdutor, SementeClasse> classe = root.join("classe");
            	return builder.equal(classe.get("codigo"), codClasse);
            }
            return builder.and(new Predicate[0]);
        };		
	}
	
	public static Specification<SementeCampoProdutor> daSituacao(Situacao situacao){
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

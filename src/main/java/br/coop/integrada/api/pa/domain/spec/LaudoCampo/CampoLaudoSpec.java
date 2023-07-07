package br.coop.integrada.api.pa.domain.spec.LaudoCampo;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;

public class CampoLaudoSpec {
	
	public static Specification<SementeLaudoInspecao> daSafra(Integer safra){
	        return (root, query, builder) -> {
	            if(safra != null) {
	                return builder.equal(root.get("safra"), safra
	            );
	        }
	        return builder.and(new Predicate[0]);
	    };
	}

	public static Specification<SementeLaudoInspecao> daOrdemCampo(Integer ordemCampo){
	        return (root, query, builder) -> {
	            if(ordemCampo != null) {
	                return builder.equal(root.get("ordemCampo"), ordemCampo
	            );
	        }
	        return builder.and(new Predicate[0]);
	    };
	}
	
	public static Specification<SementeLaudoInspecao> doEstabelecimento(String codigo){
		return (root, query, builder) -> {
	        if(StringUtils.hasText(codigo)) {          	
	        	
	        	query.distinct(true);
	        	Join<SementeLaudoInspecao, Estabelecimento> estabelecimento = root.join("estabelecimento");
	        	return builder.equal(builder.upper(estabelecimento.get("codigo")), codigo.toUpperCase());
	        }
	        return builder.and(new Predicate[0]);
	    };		
	}
	
	public static Specification<SementeLaudoInspecao> doGrupo(String fmCodigo){
		return (root, query, builder) -> {
	        if(StringUtils.hasText(fmCodigo)) {          	
	        	
	        	query.distinct(true);
	        	Join<SementeLaudoInspecao, GrupoProduto> grupoProduto = root.join("grupoProduto");
	        	return builder.equal(builder.upper(grupoProduto.get("fmCodigo")), fmCodigo.toUpperCase());
	        }
	        return builder.and(new Predicate[0]);
	    };		
	}
	
	public static Specification<SementeLaudoInspecao> daClasse(Long codClasse){
		return (root, query, builder) -> {
	        if(codClasse != null) {          	
	        	
	        	query.distinct(true);
	        	Join<SementeLaudoInspecao, SementeClasse> classe = root.join("classe");
	        	return builder.equal(classe.get("codigo"), codClasse);
	        }
	        return builder.and(new Predicate[0]);
	    };		
	}
	
	public static Specification<SementeLaudoInspecao> daSituacao(Situacao situacao){
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

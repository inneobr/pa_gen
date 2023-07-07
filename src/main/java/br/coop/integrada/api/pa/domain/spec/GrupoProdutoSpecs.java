package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class GrupoProdutoSpecs {
	
	public static Specification<GrupoProduto> doDescricao(String descricao){
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
	
	public static Specification<GrupoProduto> doFmCodigo(String fmCodigo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(fmCodigo)) {
                return builder.like(
                        builder.upper(root.get("fmCodigo")),
                        "%" + fmCodigo.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<GrupoProduto> isSemente(){
		return (root, query, builder) -> {           
          return builder.equal(root.get("semente"), true);
        };
    }
		
	public static Specification<GrupoProduto> doTipoProduto(Long id){
		return (root, query, builder) -> {
            if(id != null) { 
            	Join<TipoProduto, GrupoProduto> tipoProduto = root.join("tipoProduto");
            	return builder.equal(tipoProduto.get("id"), id);
            }
            return builder.and(new Predicate[0]);
        };		
	}
    
	
	public static Specification<GrupoProduto> doSituacao(Situacao situacao){
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
	
	public static Specification<GrupoProduto> doTipoAvariado(String tipoAvatiado){
		 return (root, query, builder) -> {
	            if(tipoAvatiado != null) {
	            	if(tipoAvatiado.equals("CHUVADO_AVARIADO")) {
	            		return builder.equal(root.get("chuvAvar"), true );
	            	}
	            	
	            	if(tipoAvatiado.equals("PH")) {
	            		return builder.equal(root.get("phEntrada"), true );	            		
	            	}	            	 
	            }
	            return builder.and(new Predicate[0]);
		 };
    }
	
	public static Specification<GrupoProduto> codigoOuDescricaoLike(String codigoOuDescricao){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codigoOuDescricao)) {		
				Predicate predicateFieldFmCodigo = builder.like(builder.upper(root.get("fmCodigo")), "%" + codigoOuDescricao.toUpperCase() + "%");
				Predicate predicateFieldDescricao = builder.like(builder.upper(root.get("descricao")), "%" + codigoOuDescricao.toUpperCase() + "%");				
				return builder.or(predicateFieldFmCodigo, predicateFieldDescricao);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<GrupoProduto> entradaReNotIguals(EntradaReEnum entradaRe) {
		return (root, query, builder) -> {
			if(entradaRe != null) {				
				return builder.notEqual(root.get("entradaRe"), entradaRe);
			}
			return builder.and(new Predicate[0]);			
		};
	}
}

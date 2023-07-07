package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class ParametroEstabelecimentoSpecs {

	public static Specification<ParametroEstabelecimento> doCodigo(String codigo) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigo)) {
                query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                return builder.like(
                        builder.upper(estabelecimento.get("codigo")), "%" + codigo.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<ParametroEstabelecimento> codigoEstabelecimentoEquals(String codigoEstabelecimento) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
                query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                return builder.equal(
                		builder.upper(estabelecimento.get("codigo")),
                		codigoEstabelecimento.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<ParametroEstabelecimento> doNomeFantasia(String nome){
        return (root, query, builder) -> {
            if(StringUtils.hasText(nome)) {
                query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                
                return builder.like(
                    builder.upper(estabelecimento.get("nomeFantasia")), "%" + nome.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<ParametroEstabelecimento> doCodigoEmitente(String codigoEmitente) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEmitente)) {
                return builder.like(
                        builder.upper(root.get("codEmitente")), "%" + codigoEmitente.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<ParametroEstabelecimento> doCodigoImovel(String codigoImovel){
        return (root, query, builder) -> {
            if( StringUtils.hasText(codigoImovel) ) {
            	return builder.equal(root.get("codImovel"), codigoImovel);
            }
            return builder.and(new Predicate[0]);
        };
    }

	public static Specification<ParametroEstabelecimento> doSituacao(Situacao situacao) {
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
	
	public static Specification<ParametroEstabelecimento> silo(Boolean isSilo){
        return (root, query, builder) -> {
                query.distinct(true);
                Join<ParametroEstabelecimento, Estabelecimento> estabelecimento = root.join("estabelecimento");
                return builder.equal(estabelecimento.get("log_silo"), isSilo);
        };
    }


}

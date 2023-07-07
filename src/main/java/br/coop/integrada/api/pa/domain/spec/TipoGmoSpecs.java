package br.coop.integrada.api.pa.domain.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class TipoGmoSpecs {
	
	
	public static Specification<TipoGmo> doTipoGmo(String tipoGmo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(tipoGmo)) {
                return builder.like(
                        builder.upper(root.get("tipoGmo")),
                        "%" + tipoGmo.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<TipoGmo> doObsRomaneio(String obsRomaneio){
        return (root, query, builder) -> {
            if(StringUtils.hasText(obsRomaneio)) {
                return builder.like(
                        builder.upper(root.get("obsRomaneio")),
                        "%" + obsRomaneio.toUpperCase() + "%"
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	public static Specification<TipoGmo> doSituacao(Situacao situacao){
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

	public static Specification<TipoGmo> codigoGrupoProdutoEquals(String codigoGrupoProduto) {
		return (root, query, builder) -> {
            if(StringUtils.hasText(codigoGrupoProduto)) {
                Join<GrupoProdutoGmo, TipoGmo> grupoProdutoGmo = root.join("grupoProdutoGmoList");
                Join<GrupoProduto, GrupoProdutoGmo> grupoProduto = grupoProdutoGmo.join("grupoProduto");
                return builder.equal(builder.upper(grupoProduto.get("fmCodigo")), codigoGrupoProduto.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}

	public static Specification<TipoGmo> tipoGmoEquals(String tipoGmo) {
        return (root, query, builder) -> {
            if(StringUtils.hasText(tipoGmo)) {
                return builder.equal(builder.upper(root.get("tipoGmo")), tipoGmo.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
	}
	
}
